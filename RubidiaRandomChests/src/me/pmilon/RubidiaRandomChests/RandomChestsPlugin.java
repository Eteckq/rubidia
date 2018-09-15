package me.pmilon.RubidiaRandomChests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Locations;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.TileEntityChest;
import net.minecraft.server.v1_13_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomChestsPlugin extends JavaPlugin{

	private static RandomChestsPlugin instance;
	private Random random = new Random();
	public static FileConfiguration chestsConfig;
	public static File chestsConfigFile;
	public static ConsoleCommandSender console;
	private final List<Location> locations = new ArrayList<Location>();
	private final List<Location> activeLocations = new ArrayList<Location>();
	
	public void onEnable(){
		instance = this;
		console = Bukkit.getConsoleSender();
		Bukkit.getPluginManager().registerEvents(new ChestListener(this), this);
		this.getCommand("chest").setExecutor(new ChestCommandExecutor());
		
		if(this.getConfig().contains("worlds")){
			for(String world : this.getConfig().getConfigurationSection("worlds").getKeys(false)){
				for(String uuid : this.getConfig().getConfigurationSection("worlds." + world).getKeys(false)){
					Location location = (Location)this.getConfig().get("worlds." + world + "." + uuid);
					if(location != null)this.getLocations().add(location);
				}
			}
		}
		
		Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
			public void run(){
				if(getLocations().size() > 0){
					if(getActiveLocations().size() != getLocations().size()){
						if(random.nextInt(1000) < 5){
							Location location = getLocations().get(random.nextInt(getLocations().size()));
							while(getActiveLocations().contains(location)){
								location = getLocations().get(random.nextInt(getLocations().size()));
							}
							if(location != null){
								Block block = location.getBlock();
								block.setType(Material.TRAPPED_CHEST);
								toLuckyChest((Chest)block.getState());
							}
						}
					}
				}
			}
		}, 0, 5*20);
	}
	
	public void onDisable(){
		for(Location location : this.getActiveLocations()){
			Block block = location.getBlock();
			if(block.getType().equals(Material.TRAPPED_CHEST)){
				((Chest)block.getState()).getBlockInventory().clear();
				block.setType(Material.AIR);
			}
		}
		
		this.getConfig().set("worlds", null);
		for(Location location : this.getLocations()){
			this.getConfig().set("worlds." + location.getWorld().getName() + "." + UUID.randomUUID().toString(), location);
		}
		this.saveConfig();
	}
	
	public void toLuckyChest(final Chest chest){
	    World nmsWorld = ((CraftWorld) chest.getWorld()).getHandle();
	    TileEntityChest teC = (TileEntityChest) nmsWorld.getTileEntity(new BlockPosition(chest.getX(), chest.getY(), chest.getZ()));
	    teC.setCustomName(new ChatMessage("Coffre chance"));

		for(int i = 0;i < chest.getBlockInventory().getSize();i++){
			if(random.nextInt(100) < 45){//45% to have an item INSIDE WHICH:
				if(random.nextInt(100) < 9){//9% to have loot -- 91% to have else
					int r = random.nextInt(4);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.ROTTEN_FLESH : r == 1 ?Material.STRING : r == 2 ? Material.BONE : Material.GUNPOWDER, random.nextInt(4)+1));
				}else if(random.nextInt(100) < 7){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.EMERALD, random.nextInt(6)+1));
				}else if(random.nextInt(100) < 5){
					int r = random.nextInt(4);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.APPLE : r == 1 ? Material.MUSHROOM_STEW : r == 2 ? Material.COOKIE : Material.PUMPKIN_PIE, random.nextInt(4)+1));
				}else if(random.nextInt(100) < 5){
					int r = random.nextInt(7);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.COOKED_CHICKEN : r == 1 ? Material.COOKED_BEEF : r == 2 ? Material.COOKED_SALMON : r == 3 ? Material.COOKED_MUTTON : r == 4 ? Material.BEEF : r == 5 ? Material.CHICKEN : r == 6 ? Material.SALMON : Material.COOKED_MUTTON, random.nextInt(4)+1));
				}else if(random.nextInt(1000) < 3){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.CARROT_ON_A_STICK, 1));
				}else if(random.nextInt(1000) < 2){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.GOLDEN_APPLE, 1));
				}
			}
		}

		getActiveLocations().add(chest.getBlock().getLocation());
		chest.setMetadata("luckyChest", new FixedMetadataValue(this, 0));
		new BukkitTask(this){
			
			@Override
			public void run(){
				if(chest.getLocation().getBlock().hasMetadata("luckyChest")){
					Core.playAnimEffect(Particle.CRIT, Locations.getCenter(chest.getLocation()).add(0,.5,0), .3F, .3F, .3F, .3F, 30);
				}else this.cancel();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,20);
	}
	
	
	public static RandomChestsPlugin getInstance(){
		return instance;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public List<Location> getActiveLocations() {
		return activeLocations;
	}
}
