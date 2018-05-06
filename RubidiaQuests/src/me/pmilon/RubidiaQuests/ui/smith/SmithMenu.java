package me.pmilon.RubidiaQuests.ui.smith;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class SmithMenu extends UIHandler {

	private int SLOT_ENHANCEMENT = 1, SLOT_PIERCING = 3;
	
	public SmithMenu(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, rp.translateString("FORGE","FORGE"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "FORGE_MENU";
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
		int slot = e.getRawSlot();
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				if(slot == this.SLOT_ENHANCEMENT)Core.uiManager.requestUI(new SmithUI(this.getHolder()));
				else if(slot == this.SLOT_PIERCING)Core.uiManager.requestUI(new PiercingUI(this.getHolder()));
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_ENHANCEMENT, this.getEnhancement());
		this.getMenu().setItem(this.SLOT_PIERCING, this.getPiercing());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getEnhancement(){
		ItemStack item = new ItemStack(Material.ANVIL,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6" + rp.translateString("Enhancement", "Renforcement"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Power up your weapon/armor and increase", "Améliorez la puissance de votre arme/armure"), "§7" + rp.translateString("its statistics by enhancing it!", "en y appliquant un renforcement !"), "§7" + rp.translateString("Pierre étoile is necessary", "Pierre étoile requise")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getPiercing(){
		ItemStack item = new ItemStack(Material.BOOK,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Piercing");
		meta.setLore(Arrays.asList("§7" + rp.translateString("Pierce your weapon/armor to place joyaux", "Percez votre arme/armure afin d'y placer des"), "§7" + rp.translateString("inside and increase your characteristics!", "joyaux et augmenter vos caractéristiques !"), "§7" + rp.translateString("Orichalque is necessary", "Orichalque requis")));
		item.setItemMeta(meta);
		return item;
	}

}
