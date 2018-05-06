package me.pmilon.RubidiaRandomChests;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Locations;
import me.pmilon.RubidiaCore.utils.Sounds;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

public class ChestListener implements Listener {

	public HashMap<Player, Chest> openedChests = new HashMap<Player, Chest>();
	
	private final RandomChestsPlugin plugin;
	public ChestListener(RandomChestsPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				Player player = event.getPlayer();
				RPlayer rplayer = RPlayer.get(player);
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					Block block = event.getClickedBlock();
					if(block != null){
						if(block.getType().equals(Material.TRAPPED_CHEST)){
							Chest chest = (Chest)block.getState();
							if(rplayer.isOp() && player.isSneaking()){
								if(this.getPlugin().getLocations().contains(block.getLocation())){
									if(rplayer.isOp() && player.isSneaking()){
										this.getPlugin().getLocations().remove(block.getLocation());
										rplayer.sendMessage("§aYou successfully removed this location from the lucky chests' locations!", "§aVous avez supprimé ce lieu des lieux de coffre chance.");
										event.setCancelled(true);
										
										if(this.getPlugin().getActiveLocations().contains(block.getLocation())){
											Bukkit.getScheduler().cancelTask(block.getMetadata("luckyChest").get(0).asInt());
											chest.getBlockInventory().clear();
											block.setType(Material.AIR);
											RandomChestsPlugin.getInstance().getActiveLocations().remove(block.getLocation());
										}
										return;
									}
								}else{
									this.getPlugin().getLocations().add(block.getLocation());
									block.setType(Material.AIR);
									rplayer.sendMessage("§aYou successfully added this location to the lucky chests' locations!", "§aVous avez ajouté ce lieu comme lieu de coffre chance.");
									event.setCancelled(true);
								}
							}

							if(this.getPlugin().getActiveLocations().contains(block.getLocation())){
								Sounds.playFoundTreasure(player);
								this.openedChests.put(player, chest);
								final Location origin = Locations.getCenter(chest.getLocation());
								final Vector v = new Vector(.6,0,0);
								final Vector v2 = new Vector(-.6,0,0);
								for(int i = 0;i < 60;i++){
									Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
										public void run(){
											VectorUtils.rotateAroundAxisY(v, .2);
											VectorUtils.rotateAroundAxisY(v2, .2);
											v.add(new Vector(0,.1,0));
											v2.add(new Vector(0,.1,0));
											Core.playAnimEffect(ParticleEffect.NOTE, origin.clone().add(v), 0, 0, 0, 0, 0);
											Core.playAnimEffect(ParticleEffect.NOTE, origin.clone().add(v2), 0, 0, 0, 0, 0);
										}
									}, i/2);
								}
								Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
									public void run(){
										Firework f = (Firework) origin.getWorld().spawn(origin.clone(), Firework.class);
										FireworkMeta fm = f.getFireworkMeta();
										fm.addEffect(FireworkEffect.builder()
												.flicker(false)
												.trail(true)
												.withColor(Color.AQUA)
												.withColor(Color.YELLOW)
												.withFade(Color.AQUA)
												.withFade(Color.YELLOW)
												.withFade(Color.WHITE)
												.build());
										fm.setPower(0);
										f.setFireworkMeta(fm);
									}
								}, 18);
								rplayer.sendMessage("§aYou found a lucky chest!", "§aVous avez trouvé un coffre chance !");
								RandomChestsPlugin.console.sendMessage("§6" + rplayer.getName() + " §efound a lucky chest!");
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		final Block block = event.getBlock();
		if(block != null){
			ItemStack is = player.getEquipment().getItemInMainHand();
			if(is.getType().equals(Material.TRAPPED_CHEST)){
				if(is.hasItemMeta()){
					ItemMeta meta = is.getItemMeta();
					if(meta.hasDisplayName()){
						if(meta.getDisplayName().contains("chance")){
							boolean empty = true;
							for(BlockFace face : new BlockFace[]{BlockFace.EAST,BlockFace.NORTH,BlockFace.WEST,BlockFace.SOUTH}){
								if(block.getRelative(face).getType().equals(Material.TRAPPED_CHEST)){
									empty = false;
									break;
								}
							}
							
							if(empty){
								event.setCancelled(false);
								Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
									public void run(){
										RandomChestsPlugin.getInstance().toLuckyChest((Chest) block.getState());
										rp.sendMessage("§aYou put a lucky chest, open it!", "§aVous avez posé un coffre chance, ouvrez-le !");
									}
								}, 0);
							}else{
								rp.sendMessage("§cYou cannot put a lucky chest here!", "§cVous ne pouvez poser un coffre chance ici !");
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getName().contains("chance")){
			if(this.openedChests.containsKey(player)){
				Chest chest = this.openedChests.get(player);
				if(chest.hasMetadata("luckyChest")){
					Bukkit.getScheduler().cancelTask(chest.getMetadata("luckyChest").get(0).asInt());
					event.getInventory().clear();
					chest.getBlock().setType(Material.AIR);
					chest.getBlock().removeMetadata("luckyChest", this.getPlugin());
					RandomChestsPlugin.getInstance().getActiveLocations().remove(chest.getBlock().getLocation());
					player.getWorld().playSound(chest.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
					this.openedChests.remove(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player p = event.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Block block = event.getBlock();
		if(block.hasMetadata("luckyChest") && (!rp.isOp() || !p.isSneaking()))event.setCancelled(true);
	}

	public RandomChestsPlugin getPlugin() {
		return plugin;
	}
	
}
