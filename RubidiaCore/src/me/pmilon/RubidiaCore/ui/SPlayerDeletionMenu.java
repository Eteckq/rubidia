package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Configs;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SPlayerDeletionMenu extends UIHandler {

	private int id;
	public SPlayerDeletionMenu(Player p, int id) {
		super(p);
		this.id = id;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Delete character", "Supprimer un personnage"));
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	@Override
	public String getType() {
		return "SPLAYER_DELETION_MENU";
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		if(e.getCurrentItem() != null){
			e.setCancelled(true);
			int slot = e.getRawSlot();
			if(slot == 4){
			}else{
				short damage = e.getCurrentItem().getDurability();
				if(damage == 5){
					rp.getSaves()[id] = null;
					Configs.getPlayerConfig().set("players." + rp.getUniqueId() + ".saves." + id, null);
				}
				Core.uiManager.requestUI(new SPlayerSelectionMenu(this.getHolder()));
			}
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		if(!rp.isVip() && rp.getLastLoadedSPlayerId() == 3){
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
	protected boolean openWindow() {
		ItemStack ok = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
		ItemStack notok = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
		ItemMeta meta = ok.getItemMeta();
		meta.setDisplayName(rp.translateString("§2§lDELETE CHARACTER", "§2§lSUPPRIMER LE PERSONNAGE"));
		meta.setLore(Arrays.asList(rp.translateString("§aDelete character and lose all progression", "§aSupprimer le personnage et perdre toute progression")));
		ok.setItemMeta(meta);
		meta.setDisplayName(rp.translateString("§4§lKEEP CHARACTER", "§4§lCONSERVER LE PERSONNAGE"));
		meta.setLore(Arrays.asList(rp.translateString("§cKeep character and all progression", "§cGarder le personnage et conserver la progression")));
		notok.setItemMeta(meta);
		for(int i = 0;i < 4;i++){
			this.menu.setItem(i, ok);
			this.menu.setItem(8-i, notok);
		}
		
		ItemStack quest = new ItemStack(Material.BOOK, 1);
		ItemMeta qmeta = quest.getItemMeta();
		SPlayer sp = rp.getSaves()[id];
		meta.setDisplayName("§f§l" + rp.translateString(sp.getRClass().getDisplayEn(), sp.getRClass().getDisplayFr()));
		meta.setLore(Arrays.asList("§8" + rp.translateString("Level: ", "Niveau : ") + "§7§o" + sp.getRLevel(), "§8" + rp.translateString("XP: ", "XP : ") + "§7§o" + sp.getRExp(), "§8" + rp.translateString("Mastery: ", "Maîtrise : ") + "§7§o" + rp.translateString(sp.getMastery().getNameEN(), sp.getMastery().getNameFR()), "§8" + rp.translateString("Job: ", "Métier : ") + "§7§o" + ChatColor.stripColor(rp.translateString(sp.getRJob().getNameEN(), sp.getRJob().getNameFR())), "§8" + rp.translateString("Skillpoints: ", "Points de compétence : ") + "§7§o" + sp.getSkp(), "§8" + rp.translateString("Distinction points: ", "Points de distinction : ") + "§7§o" + sp.getSkd(), "§8" + rp.translateString("Strength: ", "Force : ") + "§7§o" + sp.getStrength(), "§8" + rp.translateString("Endurance: ", "Endurance : ") + "§7§o" + sp.getEndurance(), "§8" + rp.translateString("Agility: ", "Agilité : ") + "§7§o" + sp.getAgility(), "§8" + rp.translateString("Intelligence: ", "Intelligence : ") + "§7§o" + sp.getIntelligence(), "§8" + rp.translateString("Perception: ", "Perception : ") + "§7§o" + sp.getPerception(), "§8" + rp.translateString("Kills: ", "Meurtres : ") + "§7§o" + sp.getKills(), "§8" + rp.translateString("Renom: ", "Renom : ") + "§7§o" + sp.getRenom(), "", rp.translateString("§e§oDo you really want to delete this character?", "§e§oSouhaitez-vous vraiment supprimer ce personnage ?"), "", "§f§m----------------------------------------"));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		quest.setItemMeta(qmeta);
		this.menu.setItem(4, quest);
		return this.getHolder().openInventory(this.menu) != null;
	}

}
