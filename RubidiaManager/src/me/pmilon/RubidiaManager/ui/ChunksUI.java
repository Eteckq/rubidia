package me.pmilon.RubidiaManager.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
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

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.NChunk;
import me.pmilon.RubidiaManager.chunks.RChunk;
import me.pmilon.RubidiaWG.Flags;

public class ChunksUI extends UIHandler {

	private Location location;
	private int p = 1;
	public ChunksUI(Player p) {
		super(p);
		this.location = this.getHolder().getLocation();
		if(location.getYaw() > 225 && location.getYaw() <= 315){
			this.p = 2;
		}else if(location.getYaw() > 315 || location.getYaw() <= 45){
			this.p = 3;
		}else if(location.getYaw() > 45 && location.getYaw() <= 135){
			this.p = 4;
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 54, rp.translateString("Map of nearby chunks", "Carte des chunks"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "CHUNKS_MAP_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				int slot = e.getRawSlot();
				int i = slot / 9;
				int j = slot % 9;
				Location location = new Location(this.location.getWorld(), this.location.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )))*16, this.location.getY(), this.location.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )))*16);
				Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
				if(e.isLeftClick()){
					ProtectedCuboidRegion region = new ProtectedCuboidRegion("temp", new BlockVector(chunk.getOrigin().getX(),chunk.getOrigin().getY(),chunk.getOrigin().getZ()), new BlockVector(chunk.getDestination().getX(),chunk.getDestination().getY(),chunk.getDestination().getZ()));
					RegionManager mng = WorldGuardPlugin.inst().getRegionManager(chunk.getWorld());
					ApplicableRegionSet set = mng.getApplicableRegions(region);
					if(set.testState(null, Flags.REGEN)){
						if(!e.isShiftClick()){
							if(chunk instanceof RChunk){
								RubidiaManagerPlugin.getChunkColl().delete(chunk);
								NChunk nchunk = new NChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), true);
								ChunkColl.chunks.add(nchunk);
								
								ChunkManager manager = ChunkManager.getManager(nchunk);
								manager.save();
								
								rp.sendMessage("§2Chunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §4NoChunk §e| §2Saved §e| §4No regeneration","§2Chunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §4NoChunk §e| §2Sauvegardé §e| §4Pas de régénération");
							}else if(chunk instanceof NChunk){
								RubidiaManagerPlugin.getChunkColl().delete(chunk);
								RChunk rchunk = new RChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), true);
								ChunkColl.chunks.add(rchunk);
								
								ChunkManager manager = ChunkManager.getManager(rchunk);
								manager.save();
								
								rp.sendMessage("§4NoChunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §2Chunk §e| §2Saved §e| §2Regeneration","§4NoChunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §2Chunk §e| §2Sauvegardé §e| §2Régénération");
							}
						}else{
							String regen = chunk.isRegenable();
							if(regen.equals("true")){
								rp.sendMessage("§eRegenerating chunk...","§eRégénération du chunk...");
								ChunkManager.getManager(chunk).load();
								if(chunk instanceof RChunk){
									((RChunk)chunk).setRegenerated(true);
								}
								rp.sendMessage("§eChunk regenerated!","§eChunk régénéré");
							}else rp.sendMessage("§cCannot regenerate this chunk: " + regen,"§cRégénération impossible : " + regen);
						}
					}else rp.sendMessage("§cThis chunk is protected by a region!","§cCe chunk est protégé par une région !");
				}else{
					if(!e.isShiftClick()){
						if(chunk instanceof RChunk){
							RChunk rch = (RChunk) chunk;
							if(rch.isRegenerated()){
								ChunkManager.getManager(rch).paste();
								rch.setRegenerated(false);
								rp.sendMessage("§eLoaded last saved chunk state", "§eDernier état du chunk enregistré chargé");
							}
						}
					}else{
						rp.sendMessage("§eForce regenerating chunk...","§eRégénération forcée du chunk...");
						ChunkManager.getManager(chunk).load();
						if(chunk instanceof RChunk){
							((RChunk)chunk).setRegenerated(true);
						}
						rp.sendMessage("§eChunk regenerated!","§eChunk régénéré de force");
					}
				}
				this.getMenu().setItem(slot, this.get(i, j));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 9;j++){
				this.getMenu().setItem(9*i+j, this.get(i, j));
			}
		}
		this.getMenu().setItem(49, this.getInfos());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	
	private ItemStack getInfos(){
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList("§7" + rp.translateString("Left click: toggles regeneration feature", "Clic gauche : active ou désactive l'option de régénération"), "§7" + rp.translateString("Sneak + Left click: regenerate chunk", "Sneak + Clic gauche : régénère le chunk"), "§7" + rp.translateString("Right click: undo regeneration", "Clic droit : annule la régénération"), "§7" + rp.translateString("Sneak + right click: force regeneration", "Sneak + clic droit : force la régénération"), "§4§l" + rp.translateString("WARNING", "ATTENTION") + "§c " + rp.translateString("This option can quickly turn to a disaster!", "Cette option peut rapidement virer au désastre !")));
		infos.setItemMeta(meta);
		return infos;
	}
	private ItemStack get(int i, int j){
		Location location = new Location(this.location.getWorld(), this.location.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )))*16, this.location.getY(), this.location.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )))*16);
		Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(chunk == null){
			chunk = new RChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ(), true);
			ChunkColl.chunks.add(chunk);
		}else{
			ProtectedCuboidRegion region = new ProtectedCuboidRegion("temp", new BlockVector(chunk.getOrigin().getX(),chunk.getOrigin().getY(),chunk.getOrigin().getZ()), new BlockVector(chunk.getDestination().getX(),chunk.getDestination().getY(),chunk.getDestination().getZ()));
			RegionManager manager = WorldGuardPlugin.inst().getRegionManager(chunk.getWorld());
			ApplicableRegionSet set = manager.getApplicableRegions(region);
			if(!set.testState(null, Flags.REGEN)){
				item.setDurability((short)7);
				meta.setDisplayName("§7§l" + rp.translateString("Protected region", "Région protégée"));
			}else{
				if(chunk instanceof NChunk){
					item.setDurability((short)14);
					meta.setDisplayName("§4§lNChunk");
					lore.add("§c§o" + rp.translateString("Regeneration disabled","Régénération désactivée"));
				}else{
					if(((RChunk)chunk).isRegenerated()){
						item.setDurability((short)13);
						meta.setDisplayName("§2§lRChunk");
						lore.add("§a§o" + rp.translateString("Regeneration enabled | Regenerated chunk","Régénération activée | Chunk régénéré"));
					}else{
						item.setDurability((short)5);
						meta.setDisplayName("§6§lRChunk");
						lore.add("§e§o" + rp.translateString("Regeneration enabled | Damaged chunk","Régénération activée | Chunk endommagé"));
					}
				}
			}
			Claim claim = Claim.get(chunk.getBukkitChunk());
			if(claim != null){
				Guild guild = claim.getGuild();
				item.setDurability((short)1);
				lore.add("§d" + rp.translateString("Chunk claimed by", "Chunk revendiqué par") + " §o" + guild.getName());
				if(!guild.isActive()){
					lore.add("§c" + rp.translateString("Inactive guild", "Guilde inactive"));
				}
			}
		}

		if(i == 2 && j == 4){
			item.setDurability((short)12);
			lore.add("");
			lore.add("§7" + rp.translateString("You are here","Vous êtes ici"));
		}
		lore.add("§8(" + chunk.getX() + "," + chunk.getZ() + ")");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}
