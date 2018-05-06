package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.duels.RBooster;
import me.pmilon.RubidiaCore.duels.RBooster.RBoosterType;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RBoostersUI extends UIHandler {

	private RBooster[] boosters = new RBooster[5];
	
	public RBoostersUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, "Boosters");
	}

	@Override
	public UIType getType() {
		return UIType.BOOSTERS;
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 5;i++){
			this.getMenu().setItem(i, this.getBooster(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				e.setCancelled(true);
				int slot = e.getRawSlot();
				RBooster booster = boosters[slot];
				RBooster active = rp.getActiveBooster(booster.getBoosterType());
				if(active != null){
					active.stop();
				}else{
					if(rp.getRenom() >= booster.getBoosterType().getCost() || rp.isOp()){
						booster.start();
					}else rp.sendMessage("§cYou don't have enough renown to start this booster.","§cVous n'avez pas assez de renom pour activer ce booster.");
				}
				this.getMenu().setItem(slot, this.getBooster(slot));
			}
		}
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	public ItemStack getBooster(int i){
		RBooster booster = boosters[i];
		if(booster == null)booster = new RBooster(rp, RBoosterType.values()[i]);
		boolean active = rp.hasActiveBooster(booster.getBoosterType());
		ItemStack item = new ItemStack(Material.INK_SACK,1,(short) (active ? 10 : 8));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((active ? "§a" : "§7") + "Booster " + booster.getBoosterType().toString());
		meta.setLore(Arrays.asList("§8" + (booster.getBoosterType().getLevel() >= 0 ? "+" : "") + Utils.round(booster.getFactor()*100, 1) + "% " + rp.translateString(booster.getBoosterType().getType().getDisplayEn(), booster.getBoosterType().getType().getDisplayFr()), "§e" + booster.getBoosterType().getCost() + rp.translateString(" points of renown per minute", " points de renom par minute")));
		item.setItemMeta(meta);
		boosters[i] = booster;
		return item;
	}

}
