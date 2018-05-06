package me.pmilon.RubidiaPets.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.ritems.general.SpawnEgg;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaPets.pets.Pearl;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.utils.LevelUtils;

public class PetsUI extends ListMenuUIHandler<Pet> {

	public PetsUI(Player p) {
		super(p, "Pet list", "Liste des compagnons", 1);
		this.list.addAll(rp.getPets());
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§7Informations");
		meta.setLore(Arrays.asList("§8" + rp.translateString("Click a pet to release or catch back.", "Cliquez sur un compagnon à faire sortir ou à ranger.")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected ItemStack getItem(Pet pet) {
		ItemStack stack = new SpawnEgg(pet.getType()).toItemStack();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§f" + pet.getName());
		List<String> lore = new ArrayList<String>();
		lore.addAll(Arrays.asList("§f§m----------------------------",
				"§8" + rp.translateString("Level", "Niveau") + " §7" + pet.getLevel(),
				"§8" + rp.translateString("Experience:", "Expérience :") + " §7" + Utils.round(pet.getExp(),2) + "/" + Utils.round(LevelUtils.getPLevelTotalExp(pet.getLevel()),2) + "XP  (" + Utils.round((pet.getExp()/LevelUtils.getPLevelTotalExp(pet.getLevel())*100), 2) + "%)",
				"§8" + rp.translateString("Damages:", "Attaque :") + " §7" + Utils.round(pet.getDamages(),2),
				"§8" + rp.translateString("Health:", "Vie :") + " §7" + Utils.round(pet.getHealth(),2) + "/" + Utils.round(pet.getMaxHealth(),2),
				"§8" + rp.translateString("Attack speed:", "Vitesse d'attaque :") + " §7" + Utils.round(pet.getAttackSpeed(),2)));
		if(!pet.getActivePearls().isEmpty()){
			lore.addAll(Arrays.asList("§f§m----------------------------","",
					"§8" + rp.translateString("Active pearls", "Perles actives")));
			for(Pearl pearl : pet.getActivePearls()){
				String lvl = "";
				for(int i = 0;i < pearl.getAmplifier();i++){
					lvl += "I";
				}
				lore.add("§7" + rp.translateString("Pearl of", "Perle de") + " " + rp.translateString(pearl.getType().getDisplay()[0], pearl.getType().getDisplay()[1]) + " " + lvl + " (" + (int)(pearl.getDuration()/3600000) + "h)");
			}
			lore.addAll(Arrays.asList("","§f§m----------------------------"));
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return pet.isActive() ? Utils.setGlowingWithoutAttributes(stack) : stack;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack arg2) {
		if(e.getCurrentItem() != null){
			int slot = e.getRawSlot();
			Pet pet = this.get(slot);
			if(pet != null){
				if(pet.isActive()){
					pet.despawn();
					pet.setActive(false);
					rp.sendMessage("§cYou caught back §4" + pet.getName() + "§c.", "§cVous avez rangé §4" + pet.getName() + "§c.");
				}else{
					pet.spawn(this.getHolder());
					pet.setActive(true);
					rp.sendMessage("§aYou released §2" + pet.getName() + "§a.", "§aVous avez fait sortir §2" + pet.getName() + "§a.");
				}
				this.menu.setItem(slot, this.getItem(pet));
			}
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
	}

	@Override
	protected void onOpen() {
	}

	@Override
	protected void onPageTurn() {
	}

	@Override
	public String getType() {
		return "PETS_LIST_MENU";
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

}
