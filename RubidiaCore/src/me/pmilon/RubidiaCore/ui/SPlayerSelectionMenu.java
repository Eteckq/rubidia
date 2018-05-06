package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.ui.managers.UIType;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SPlayerSelectionMenu extends UIHandler{

	boolean force = true;
	public SPlayerSelectionMenu(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 36, rp.translateString("Character selection","Sélection du personnage"));
	}

	@Override
	public UIType getType() {
		return UIType.SPLAYER_SELECTION_MENU;
	}

	@Override
	protected boolean openWindow() {
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§c§l" + rp.translateString("DELETE CHARACTER", "SUPPRIMER CE PERSONNAGE"));
		item.setItemMeta(meta);
		for(int i = 0;i < 4;i++){
			ItemStack item1;
			ItemMeta meta1;
			List<String> lore = new ArrayList<String>();
			if(rp.getSaves()[i] != null){
				SPlayer sp = rp.getSaves()[i];
				item1 = new ItemStack(sp.getRClass().equals(RClass.VAGRANT) ? Material.WOOD_AXE : sp.getRClass().getBaseWeapon(), 1, (short) (sp.getRClass().equals(RClass.VAGRANT) ? Material.WOOD_AXE.getMaxDurability()*.67 : (sp.getRClass().equals(RClass.RANGER) ? Material.BOW.getMaxDurability()*.954 : sp.getRClass().getBaseWeapon().getMaxDurability()*.72)));
				meta1 = item1.getItemMeta();
				meta1.setUnbreakable(true);
				meta1.setDisplayName("§f§l" + rp.translateString(sp.getRClass().getDisplayEn(), sp.getRClass().getDisplayFr()));
				double ratio = sp.getRExp()/LevelUtils.getRLevelTotalExp(sp.getRLevel());
				lore.addAll(Arrays.asList("§8" + rp.translateString("Level ", "Niveau ") + "§7" + sp.getRLevel(), "§8" + rp.translateString("XP ", "XP ") + "§7" + sp.getRExp() + " (" + Utils.round(ratio, 2) + "%)", "§8" + rp.translateString("Mastery ", "Maîtrise ") + "§7" + rp.translateString(sp.getMastery().getNameEN(), sp.getMastery().getNameFR()), "§8" + rp.translateString("Job ", "Métier ") + "§7" + ChatColor.stripColor(rp.translateString(sp.getRJob().getNameEN(), sp.getRJob().getNameFR())), "§8" + rp.translateString("Skillpoints ", "Points de compétence ") + "§7" + sp.getSkp(), "§8" + rp.translateString("Distinction points ", "Points de distinction ") + "§7" + sp.getSkd(), "§8" + rp.translateString("Strength ", "Force ") + "§7" + sp.getStrength(), "§8" + rp.translateString("Endurance ", "Endurance ") + "§7" + sp.getEndurance(), "§8" + rp.translateString("Agility ", "Agilité ") + "§7" + sp.getAgility(), "§8" + rp.translateString("Intelligence ", "Intelligence ") + "§7" + sp.getIntelligence(), "§8" + rp.translateString("Perception ", "Perception ") + "§7" + sp.getPerception(), "§8" + rp.translateString("Kills ", "Meurtres ") + "§7" + sp.getKills(), "§8" + rp.translateString("Renom ", "Renom ") + "§7" + sp.getRenom(), "", rp.isVip() || i != 3 ? rp.translateString("§e§oClick here to select this character", "§e§oCliquez ici pour sélectionner ce personnage") : rp.translateString("§c§oYou will be able to use this character once VIP again!", "§c§oVous pourrez utiliser ce personnage une fois de nouveau VIP !"), "", "§f§m--------------------------------------"));
				meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
			}else{
				item1 = new ItemStack(Material.BOOK, 1);
				meta1 = item1.getItemMeta();
				meta1.setDisplayName("§f§l>  §7Aucun personnage§f§l  <");
				lore.addAll(Arrays.asList("",rp.isVip() || i != 3? rp.translateString("§e§oClick here to create a new character", "§e§oCliquez ici pour créer un nouveau personnage") : rp.translateString("§c§oThis slot is only available to VIP players!", "§c§oCe slot est uniquement disponible aux joueurs VIP !"), "", "§f§m----------------------------------------"));
			}
			meta1.setLore(lore);
			item1.setItemMeta(meta1);
			this.menu.setItem(10+i*2, i == rp.getLastLoadedSPlayerId() ? Utils.setGlowingWithoutAttributes(item1) : item1);
			this.menu.setItem(19+i*2, item);
		}
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if(!item.getType().equals(Material.AIR)){
				int slot = e.getRawSlot();
				if(slot < 18 && slot > 8){
					int id = (int) Math.abs((slot-10)*.5);
					if(rp.getSaves()[id] == null){
						if(id <= 2 || rp.isVip()){
							SPlayer sp = Core.rcoll.newDefaultSPlayer(id);
							rp.getSaves()[id] = sp;
						}else rp.sendMessage("§cYou must be VIP to use this character!", "§cVous devez être VIP pour utiliser ce personnage !");
					}
					if(rp.getSaves()[id] != null){
						if(id != rp.getLastLoadedSPlayerId()){
							if(id <= 2 || rp.isVip()){
								force = false;
								this.closeUI();
								rp.load(id);
							}else rp.sendMessage("§cYou must be VIP to use this character!", "§cVous devez être VIP pour utiliser ce personnage !");
						}else rp.sendMessage("§eYou are already using this character!", "§eVous utilisez déjà ce personnage !");
					}
				}else if(slot < 27 && slot > 17){
					int id = (int) Math.abs((slot-19)*.5);
					if(rp.getSaves()[id] != null){
						if(rp.getLastLoadedSPlayerId() != id){
							Core.uiManager.requestUI(new SPlayerDeletionMenu(this.getHolder(), id));
						}else rp.sendMessage("§cYou must first select another character!", "§cVous devez d'abord sélectionner un autre personnage !");
					}
				}
			}
		}
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(force && !rp.isVip() && rp.getLastLoadedSPlayerId() == 3){
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					Core.uiManager.requestUI(new SPlayerSelectionMenu(getHolder()));
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(0);
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

}
