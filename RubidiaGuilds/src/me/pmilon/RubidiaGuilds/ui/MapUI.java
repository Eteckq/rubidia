package me.pmilon.RubidiaGuilds.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaWG.Flags;

public class MapUI extends UIHandler {

	private Chunk center;
	private int p = 1;
	public MapUI(Player p) {
		super(p);
		Location location = this.getHolder().getLocation();
		if(location.getYaw() > 225 && location.getYaw() <= 315){
			this.p = 2;
		}else if(location.getYaw() > 315 || location.getYaw() <= 45){
			this.p = 3;
		}else if(location.getYaw() > 45 && location.getYaw() <= 135){
			this.p = 4;
		}
		this.center = location.getChunk();
		this.menu = Bukkit.createInventory(this.getHolder(), 45, rp.translateString("Map of nearby claims", "Carte des territoires"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		boolean b = this.getHolder().openInventory(this.getMenu()) != null;
		for(int i = 0;i < 5;i++){
			final int j = i;
			new BukkitTask(GuildsPlugin.instance){

				@Override
				public void run() {
					print(j);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(1+2*i);
		}
		return b;
	}
	
	private void print(int i){
		for(int j = 0;j < 9;j++){
			int x = this.center.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )));
			int z = this.center.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )));
			Chunk chunk = this.center.getWorld().getChunkAt(x, z);
			Claim claim = Claim.get(chunk);
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if(claim == null){
				ProtectedCuboidRegion region = new ProtectedCuboidRegion("temp", new BlockVector(x*16,0,z*16), new BlockVector(x*16+15,255,z*16+15));
				RegionManager mng = WorldGuardPlugin.inst().getRegionManager(chunk.getWorld());
				ApplicableRegionSet set = mng.getApplicableRegions(region);
				if(set.testState(null,Flags.CLAIM)){
					item.setDurability((short)7);
					meta.setDisplayName("§f§l" + rp.translateString("Free territory", "Territoire libre"));
					item.setItemMeta(meta);
				}else{
					item.setDurability((short)1);
					meta.setDisplayName("§6§l" + rp.translateString("Protected territory", "Territoire protégé"));
					item.setItemMeta(meta);
				}
			}else{
				Guild claimGuild = claim.getGuild();
				if(claimGuild.isPeaceful()){
					item.setDurability((short)4);
					meta.setDisplayName("§6§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
					lore.add("§e§o" + rp.translateString("by","par") + " " + claimGuild.getName());
				}else{
					if(gm.hasGuild()){
						Guild guild = gm.getGuild();
						if(guild.getRelationTo(claimGuild).equals(Relation.ALLY)){
							item.setDurability((short)2);
							meta.setDisplayName("§5§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
							lore.add("§d§o" + rp.translateString("by","par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.ENEMY)){
							item.setDurability((short)14);
							meta.setDisplayName("§4§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
							lore.add("§c§o" + rp.translateString("by","par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.MEMBER)){
							item.setDurability((short)5);
							meta.setDisplayName("§2§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
							lore.add("§a§o" + rp.translateString("by","par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.NEUTRAL)){
							item.setDurability((short)11);
							meta.setDisplayName("§9§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
							lore.add("§b§o" + rp.translateString("by","par") + " " + claimGuild.getName());
						}
					}else{
						item.setDurability((short)11);
						meta.setDisplayName("§9§l" + rp.translateString("Claimed territory", "Territoire revendiqué"));
						lore.add("§b§o" + rp.translateString("by","par") + " " + claimGuild.getName());
					}
				}
			}

			if(i == 2 && j == 4){
				item.setDurability((short)12);
				lore.add("");
				lore.add("§7" + rp.translateString("You are here","Vous êtes ici"));
			}
			lore.add("§8(" + x + "," + z + ")");
			meta.setLore(lore);
			item.setItemMeta(meta);
			this.getMenu().setItem(9*i+j, item);
		}
	}

}
