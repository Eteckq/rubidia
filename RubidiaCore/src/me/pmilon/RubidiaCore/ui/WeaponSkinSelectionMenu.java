package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponSkinSelectionMenu extends ListMenuUIHandler<Integer> {

	private Weapon weapon;
	public WeaponSkinSelectionMenu(Player p, Weapon weapon) {
		super(p, "Weapon Skin Selection", "Sélection de skin d'arme", 5);
		this.weapon = weapon;
		List<Integer> skinIds = new ArrayList<Integer>();
		for(Weapon w : Weapons.weapons){
			if(w.getType().equals(this.getWeapon().getType())){
				skinIds.add(w.getSkinId());
			}
		}
		int amount = Weapons.getSkinAmount(this.getWeapon().getType());
		for(int i = 1;i <= amount;i++){
			if(!skinIds.contains(i) || skinIds.size() >= amount){
				this.list.add(i);
			}
		}
	}

	@Override
	protected void onOpen() {
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		if(is != null){
			int slot = e.getRawSlot();
			this.getWeapon().setSkinId(this.get(slot));
			Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), this.getWeapon()));
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(rp.translateString("§7Click a skin to choose it.", "§7Cliquez sur un skin pour le choisir."),rp.translateString("§7Right click here to erase current skin", "§7Cliquez-droit ici pour supprimer le skin actuel"),rp.translateString("§7Left click to get back", "§7Cliquez gauche pour retour")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		if(e.isLeftClick()){
			Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), this.getWeapon()));
		}else{
			this.getWeapon().setSkinId(0);
			Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), this.getWeapon()));
		}
	}

	@Override
	protected void onPageTurn() {
	}

	@Override
	public UIType getType() {
		return UIType.WEAPON_SKIN_MENU;
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected ItemStack getItem(Integer i) {
		ItemStack item = new ItemStack(this.getWeapon().getType(),1,(short)(this.getWeapon().getType().getMaxDurability()*i*Weapons.getSkinFactor(this.getWeapon().getType())+1));
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName("Skin #" + i);
		item.setItemMeta(meta);
		return item;
	}

	private Weapon getWeapon() {
		return this.weapon;
	}

}
