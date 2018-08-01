package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.books.BookUtils;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class DistinctionsMenu extends UIHandler {

	private int SLOT_SKD = 0, SLOT_STR = 2, SLOT_END = 3, SLOT_AGT = 4, SLOT_INT = 5, SLOT_PER = 6, SLOT_CRC = 8;
	private boolean canClick = true;
	public DistinctionsMenu(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Distinctions menu", "Menu des distinctions"));
	}

	@Override
	public String getType() {
		return "DISTINCTIONS_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_SKD, this.getDistinctionPoints());
		this.getMenu().setItem(this.SLOT_STR, this.getStrength());
		this.getMenu().setItem(this.SLOT_END, this.getEndurance());
		this.getMenu().setItem(this.SLOT_AGT, this.getAgility());
		this.getMenu().setItem(this.SLOT_INT, this.getIntelligence());
		this.getMenu().setItem(this.SLOT_PER, this.getPerception());
		this.getMenu().setItem(this.SLOT_CRC, this.getCrc());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				e.setCancelled(true);
				int slot = e.getRawSlot();
				if(slot > 1 && slot < 7){
					if(canClick){
						canClick = false;
						new BukkitTask(Core.instance){
							public void run(){
								canClick = true;
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(6);
						int amount = 1;
						if(e.isShiftClick())amount = 5;
						if(rp.getSkillDistinctionPoints() >= amount){
							rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()-amount);
							if(slot == this.SLOT_STR){
								rp.addStrength(amount);
								this.menu.setItem(slot, this.getStrength());
							}else if(slot == this.SLOT_END){
								rp.addEndurance(amount);
								this.menu.setItem(slot, this.getEndurance());
								this.getHolder().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(rp.getMaxHealth());
								this.getHolder().setHealth(this.getHolder().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
							}else if(slot == this.SLOT_AGT){
								rp.addAgility(amount);
								this.menu.setItem(slot, this.getAgility());
							}else if(slot == this.SLOT_INT){
								rp.addIntelligence(amount);
								this.menu.setItem(slot, this.getIntelligence());
							}else if(slot == this.SLOT_PER){
								rp.addPerception(amount);
								this.menu.setItem(slot, this.getPerception());
							}
							this.menu.setItem(this.SLOT_SKD, this.getDistinctionPoints());
						}else{
							this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
							rp.sendMessage("§cYou don't have enough distinction points!", "§cVous n'avez pas assez de points de distinction !");
						}
					}
				}else if(slot == this.SLOT_CRC){
					BookUtils.openCharacteristicsBook(Core.instance, p, p.getEquipment().getItemInMainHand());
				}
			}
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private ItemStack getDistinctionPoints(){
		ItemStack item = new ItemStack(Material.BOOK, rp.getSkillDistinctionPoints() > 64 || rp.getSkillDistinctionPoints() < 1 ? 1 : rp.getSkillDistinctionPoints());
		ItemMeta meta = item.getItemMeta();
		String color = rp.getSkillDistinctionPoints() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getSkillDistinctionPoints() + color + " " + rp.translateString("distinction point", "point") + (rp.getSkillDistinctionPoints() > 1 ? "s" : "") + rp.translateString("", " de distinction"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Distinction points are gained everytime you level up.", "Les points de distinction sont gagnés à chaque niveau."), "§7" + rp.translateString("They allow you to level up one of the following distinctions.", "Ils vous permettent d'augmenter le niveau des distinctions suivantes."), "§7" + rp.translateString("Details on the effects of each distinction available on hover.", "Les détails sur les effets de chaque distinction sont disponibles au survol.")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getStrength(){
		ItemStack item = new ItemStack(Material.ROSE_RED, rp.getStrength() > 0 ? (rp.getStrength() > 64 ? 64 : rp.getStrength()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getStrength() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getStrength() + color + " " + rp.translateString("strength point", "point") + (rp.getStrength() > 1 ? "s" : "") + rp.translateString("", " de force"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Strength enhances §lmelee§7 damages you inflict", "La force augmente les dégâts de §lmêlée§7 que vous infligez"), rp.translateString("§7depending on which type of weapon you use.", "§7en fonction du type d'arme que vous utilisez."), "§4+" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("damages from melee weapon", "dégâts d'une arme de mêlée"),  "§4" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("of total damages from melee & ranged weapon", "des dégâts totaux d'une arme polyvalente"),  "§4" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE, 2) + ")" : "") + "§l §8" + rp.translateString("damages from bare hands", "dégâts à mains nues")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getEndurance(){
		ItemStack item = new ItemStack(Material.LIME_DYE, rp.getEndurance() > 0 ? (rp.getEndurance() > 64 ? 64 : rp.getEndurance()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getEndurance() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getEndurance() + color + " " + rp.translateString("endurance point", "point") + (rp.getEndurance() > 1 ? "s" : "") + rp.translateString("", " d'endurance"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Endurance increases your max health,", "L'endurance augmente votre vie maximale, vitesse"), rp.translateString("§7energy speed regen and defense.", "§7de régénération de la vigueur et défense."), "§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_MAXHEALTH, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_MAXHEALTH, 2) + ")" : "") + "§l §8" + rp.translateString("health points", "points de vie"),  "§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_NRJREGEN, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_NRJREGEN, 2) + ")" : "") + "§l §8" + rp.translateString("energy points per second", "points de vigueur par seconde"),  "§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_DEFENSE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_DEFENSE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("physical defense", "défense physique"),  "§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_ABILITY_DEF*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_ABILITY_DEF*100, 2) + "%)" : "") + "§l §8" + rp.translateString("magical defense", "défense magique")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getAgility(){
		ItemStack item = new ItemStack(Material.DANDELION_YELLOW, rp.getAgility() > 0 ? (rp.getAgility() > 64 ? 64 : rp.getAgility()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getAgility() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getAgility() + color + " " + rp.translateString("agility point", "point") + (rp.getEndurance() > 1 ? "s" : "") + rp.translateString("", " d'agilité"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Agility improves your critical strikes damages,", "L'agilité améliore vos dégâts des coups critiques,"), rp.translateString("§7your attack speed and your ranged damages.", "§7vitesse d'attaque et dégâts infligés à distance."), "§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("ranged damages", "dégâts à distance"), "§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_ATTACK_SPEED*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_ATTACK_SPEED*100, 2) + "%)" : "") + "§l §8" + rp.translateString("attack speed", "vitesse d'attaque"),  "§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES*100, 2) + "%)" : "") + "§l §8" + rp.translateString("critical strikes damages", "dégâts des coups critiques"), "§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("critical strike chance", "chance de coup critique")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getIntelligence(){
		ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE, rp.getIntelligence() > 0 ? (rp.getIntelligence() > 64 ? 64 : rp.getIntelligence()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getIntelligence() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getIntelligence() + color + " " + rp.translateString("intelligence point", "point") + (rp.getEndurance() > 1 ? "s" : "") + rp.translateString("", " d'intelligence"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Intelligence enhances your magic damages,", "L'intelligence augmente vos dégâts magiques,"), rp.translateString("§7ability damages and your max energy.", "§7dégâts des compétences et vigueur maximum."), "§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC*100, 2) + "%)" : "") + "§l §8" + rp.translateString("magic damages", "dégâts magiques"),  "§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_ABILITY_DAMAGES*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_ABILITY_DAMAGES*100, 2) + "%)" : "") + "§l §8" + rp.translateString("ability damages", "dégâts des compétences"),  "§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_MAXNRJ, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_MAXNRJ, 2) + ")" : "") + "§l §8" + rp.translateString("energy points", "points de vigueur")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPerception(){
		ItemStack item = new ItemStack(Material.MAGENTA_DYE, rp.getPerception() > 0 ? (rp.getPerception() > 64 ? 64 : rp.getPerception()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getPerception() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getPerception() + color + " " + rp.translateString("perception point", "point") + (rp.getEndurance() > 1 ? "s" : "") + rp.translateString("", " de perception"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Perception improves your block chance,", "La perception augmente votre chance de blocage,"), rp.translateString("§7rare loot chance and elytra speed.", "§7chance de butin rare et vitesse en élytres."), "§4+" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_BLOCK_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PERCEPTION_FACTOR_BLOCK_CHANCE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("block chance", "chance de blocage"),  "§4+" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_LOOT_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PERCEPTION_FACTOR_LOOT_CHANCE*100, 2) + "%)" : "") + "§l §8" + rp.translateString("rare loot chance", "chance de butin rare"),  "§4-" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_LIFT_COST*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(-" + Utils.round(Settings.PERCEPTION_FACTOR_LIFT_COST*100, 2) + "%)" : "") + "§l §8" + rp.translateString("elytra lift up cost", "coût d'élévation en élytres")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCrc(){
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(this.getHolder().getUniqueId()));
		meta.setDisplayName("§f§l" + rp.getRClass().toString());
		meta.setLore(Arrays.asList("§7" + rp.translateString("Click to obtain detailed characteristics.", "Cliquez pour obtenir vos caractéristiques détaillées."), "§7" + rp.translateString("Attack & defense values depend", "Les valeurs d'attaque et de défense"), "§7" + rp.translateString("on your current weapon and armor.", "dépendent de votre arme et armure actuelles.")));
		item.setItemMeta(meta);
		return item;
	}

}
