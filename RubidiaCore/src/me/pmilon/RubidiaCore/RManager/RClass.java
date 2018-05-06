package me.pmilon.RubidiaCore.RManager;

import org.bukkit.Material;

public enum RClass {
	
	VAGRANT(Material.WOOD_SPADE, Material.WOOD_SPADE, "Vagabond", "Vagrant", "", "§7"),
	PALADIN(Material.DIAMOND_CHESTPLATE, Material.WOOD_AXE, "Paladin", "Paladin", "§2", "§a"),
	RANGER(Material.ARROW, Material.BOW, "Ranger", "Ranger", "§9", "§b"),
	MAGE(Material.STICK, Material.WOOD_HOE, "Mage", "Mage", "§6", "§e"),
	ASSASSIN(Material.FEATHER, Material.WOOD_SWORD, "Assassin", "Assassin", "§4", "§c");
	
	private final Material display;
	private final Material baseWeapon;
	private final String displayFr;
	private final String displayEn;
	private final String darkColor;
	private final String color;
	private RClass(Material display, Material baseWeapon, String displayFr, String displayEn, String darkColor, String color){
		this.display = display;
		this.baseWeapon = baseWeapon;
		this.displayFr = displayFr;
		this.displayEn = displayEn;
		this.darkColor = darkColor;
		this.color = color;
	}
	
	public Material getDisplay(){
		return this.display;
	}
	
	public Material getBaseWeapon(){
		return this.baseWeapon;
	}

	public String getDisplayFr() {
		return displayFr;
	}

	public String getDisplayEn() {
		return displayEn;
	}

	public String getDarkColor() {
		return darkColor;
	}

	public String getColor() {
		return color;
	}
}
