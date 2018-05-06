package me.pmilon.RubidiaCore.RManager;

import org.bukkit.Material;

public enum RJob {
	
	JOBLESS(Material.IRON_SPADE, "§fNo job", "§fAucun job"),
	MINER(Material.IRON_PICKAXE, "§aMiner", "§aMineur"),
	LUMBERMAN(Material.IRON_AXE, "§bLumberman", "§bBûcheron"),
	FARMER(Material.IRON_HOE, "§eFarmer", "§eAgriculteur"),
	HUNTER(Material.IRON_SWORD, "§cHunter", "§cChasseur"),
	ALCHEMIST(Material.BREWING_STAND_ITEM, "§dAlchemist", "§dAlchimiste");
	
	private final Material display;
	private final String nameEN;
	private final String nameFR;
	private RJob(Material display, String nameEN, String nameFR){
		this.display = display;
		this.nameEN = nameEN;
		this.nameFR = nameFR;
	}

	public Material getDisplay() {
		return display;
	}
	public String getNameEN() {
		return nameEN;
	}
	public String getNameFR() {
		return nameFR;
	}
}
