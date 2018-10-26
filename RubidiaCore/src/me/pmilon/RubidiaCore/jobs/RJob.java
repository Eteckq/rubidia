package me.pmilon.RubidiaCore.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum RJob {
	
	JOBLESS(Material.IRON_SHOVEL, "§fJobless", "§fChômeur", new ArrayList<Material>(), new ArrayList<String>(), new ArrayList<Material>()),
	SCULPTOR(Material.IRON_PICKAXE, "§aScupltor", "§aSculpteur", Arrays.asList(Material.REDSTONE_BLOCK, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.LAPIS_BLOCK, Material.IRON_BLOCK,
			Material.CUT_RED_SANDSTONE, Material.CUT_SANDSTONE, Material.PURPUR_PILLAR, Material.QUARTZ_PILLAR, Material.BONE_BLOCK,
			Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.BRICKS), Arrays.asList("SMOOTH_", "CHISELED_", "POLISHED_"), new ArrayList<Material>()),
	CARPENTER(Material.IRON_AXE, "§bCarpenter", "§bMenuisier", new ArrayList<Material>(), Arrays.asList("_FENCE", "_STAIRS", "_SLAB", "_TRAPDOOR",
			"_PRESSURE_PLATE", "_FENCE_GATE", "_BUTTON", "_DOOR", "_BOAT"), Arrays.asList(Material.OAK_FENCE, Material.OAK_STAIRS, Material.OAK_SLAB,
					Material.OAK_TRAPDOOR, Material.OAK_PRESSURE_PLATE, Material.OAK_FENCE_GATE, Material.OAK_BUTTON, Material.OAK_DOOR, Material.OAK_BOAT)),
	DECORATOR(Material.SHEARS, "§eDecorator", "§eDécorateur", Arrays.asList(Material.SHEARS, Material.FLOWER_POT, Material.PAINTING, Material.ITEM_FRAME),
			Arrays.asList("_WOOL", "_TERRACOTTA", "_CONCRETE_POWDER", "_BED", "_CARPET", "_STAINED_GLASS"), Arrays.asList(Material.WHITE_BED, Material.WHITE_CARPET));
	
	private final Material display;
	private final String nameEN;
	private final String nameFR;
	private final List<Material> crafts;
	private final List<String> craftFilters;
	private final List<Material> craftBypass;
	private RJob(Material display, String nameEN, String nameFR, List<Material> crafts, List<String> craftFilters, List<Material> craftBypass){
		this.display = display;
		this.nameEN = nameEN;
		this.nameFR = nameFR;
		this.crafts = crafts;
		this.craftFilters = craftFilters;
		this.craftBypass = craftBypass;
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

	public List<Material> getCrafts() {
		return crafts;
	}
	
	public boolean check(Material type) {
		if((this.crafts.contains(type) || this.isFiltered(type)) && !this.craftBypass.contains(type)) {
			return true;
		}
		return false;
	}
	
	private boolean isFiltered(Material type) {
		for(String material : this.craftFilters) {
			if(type.toString().contains(material)) {
				return true;
			}
		}
		return false;
	}
}
