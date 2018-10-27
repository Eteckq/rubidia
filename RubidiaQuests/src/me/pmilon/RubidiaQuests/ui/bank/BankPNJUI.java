package me.pmilon.RubidiaQuests.ui.bank;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.pnjs.BankPNJ;

public class BankPNJUI extends UIHandler {

	private BankPNJ pnj;
	private int SLOT_MELT = 8;
	public BankPNJUI(Player p, BankPNJ pnj) {
		super(p);
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, ("Banque"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "BANK_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick()) {
			if(e.getCurrentItem() != null){
				if(!e.getCurrentItem().getType().equals(Material.EMERALD) && !e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)){
					e.setCancelled(true);
				}else this.menu.setItem(this.SLOT_MELT, this.getMelt());
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				int slot = e.getRawSlot();
				if(slot == this.SLOT_MELT){
					e.setCancelled(true);
					int count = this.count();
					if(rp.getBank() + count > rp.getMaxBankAmount()) {
						rp.sendMessage("§cThere is not enough room for all these emeralds in your bank!", "§cIl n'y a pas assez de place pour toutes ces émeraudes dans votre banque !");
					}else {
						for(int i = 0;i < 8;i++) {
							if(!this.getMenu().getItem(i).getType().equals(Material.EMERALD) && !this.getMenu().getItem(i).equals(Material.EMERALD_BLOCK)){
								this.getHolder().getInventory().addItem(this.getMenu().getItem(i));
							}
							this.getMenu().setItem(i, new ItemStack(Material.AIR));
						}
						EconomyHandler.deposit(this.getHolder(), count);
					}
				}
			}
			this.menu.setItem(this.SLOT_MELT, this.getMelt());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		for(int i =0;i < 8;i++) {
			ItemStack item = this.getMenu().getItem(i);
			if(item != null) {
				this.getHolder().getInventory().addItem(item);
			}
		}
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_MELT, this.getMelt());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getMelt(){
		int count = this.count();
		ItemStack item = new ItemStack(Material.LAVA_BUCKET,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Fonderie"));
		meta.setLore(Arrays.asList("§7" + ("Envoyer ces matériaux à la fonderie"),
				"§7" + ("et ajouter §a" + count + " §7émeraude" + (count > 1 ? "s" : "") + " à votre banque."),
				"", "§f§l" + ("Solde actuel : §7" + rp.getBank() + (rp.getMaxBankAmount() == -1 ? "⟡" : "§8/" + rp.getMaxBankAmount()))));
		item.setItemMeta(meta);
		return item;
	}
	
	private int count() {
		int amount = 0;
		for(int i = 0;i < 8;i++) {
			ItemStack item = this.getMenu().getItem(i);
			if(item != null) {
				amount += item.getType().equals(Material.EMERALD_BLOCK) ? 9 : (item.getType().equals(Material.EMERALD) ? 1 : 0);
			}
		}
		return amount;
	}

	public BankPNJ getPnj() {
		return pnj;
	}

	public void setPnj(BankPNJ pnj) {
		this.pnj = pnj;
	}
	
}
