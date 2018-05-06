package me.pmilon.RubidiaQuests.ui.bank;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SafeUI extends UIHandler {

	public SafeUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 18, rp.translateString("SAFE", "COFFRE-FORT"));
	}

	@Override
	public String getType(){
		return "SAFE_MENU";
	}
	
	@Override
	protected boolean openWindow() {
		for(int slot = 0; slot < 18; slot++){
			if(rp.getBank().containsKey(slot)){
				this.getMenu().setItem(slot, rp.getBank().get(slot));
			}
		}
		if(!rp.isVip()){
			for(int slot = 9; slot < 18; slot++){
				if(this.getMenu().getItem(slot) == null){
					this.getMenu().setItem(slot, this.getVipSlot());
				}
			}
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			final int slot = e.getRawSlot();
			if(!rp.isVip()){
				if(slot > 8){
					if(!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)){
						e.setCurrentItem(this.getMenu().getItem(slot));
						new BukkitTask(Core.instance){
							public void run(){
								getMenu().setItem(slot, getVipSlot());
								rp.getBank().remove(slot);
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(0);
					}
				}
			}
			
			if(slot > 8 && e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)){
				if(rp.isVip())e.setCurrentItem(null);
				else e.setCancelled(true);
			}
			
			if(!e.getCursor().getType().equals(Material.EMERALD) && !e.getCursor().getType().equals(Material.EMERALD_BLOCK) && !e.getCursor().getType().equals(Material.AIR)){
				e.setCancelled(true);
				rp.sendMessage("§cThis is a bank, not a storage!", "§cC'est une banque, pas un coffre !");
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		this.getBalance();
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(e.isShiftClick()){
				if(!e.getCurrentItem().getType().equals(Material.EMERALD) && !e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK) && !e.getCurrentItem().getType().equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("§cThis is a bank, not a storage!", "§cC'est une banque, pas un coffre !");
				}
			}
		}
	}

	public void getBalance() {
		for(int slot = 0; slot < 18; slot++){
			ItemStack is = this.getMenu().getItem(slot);
			if(is != null){
				if(is.getType().equals(Material.EMERALD) || is.getType().equals(Material.EMERALD_BLOCK))rp.getBank().put(slot, is);
				else if(!is.getType().equals(Material.STAINED_GLASS_PANE)){
					this.getMenu().setItem(slot, new ItemStack(Material.AIR));
					this.getHolder().getInventory().addItem(is);
				}
			}else rp.getBank().put(slot, is);
		}
	}
	
	public ItemStack getVipSlot(){
		ItemStack vipSlot = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
		ItemMeta meta = vipSlot.getItemMeta();
		meta.setDisplayName(rp.translateString("§4§lVIP SLOT", "§4§lSLOT VIP"));
		meta.setLore(Arrays.asList(rp.translateString("§cBuy vip rank at the shop in order to", "§cAchetez le grade VIP pour débloquer"), rp.translateString("§cunlock this line of empty space!", "§ccette ligne d'espace supplémentaire !")));
		vipSlot.setItemMeta(meta);
		return vipSlot;
	}
	
	public void close(){
		this.close(false);
	}
	
}