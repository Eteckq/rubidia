package me.pmilon.RubidiaCore.ritems.weapons;

import org.bukkit.Material;

public enum BuffType {

	LIFT_COST("elytra lift cost", "coût d'élévation en élytres", Material.ELYTRA),//RPlayer
	WALK_SPEED("walk speed", "vitesse de déplacement", Material.FEATHER),//Core.task2
	ATTACK_SPEED("attack speed", "vitesse d'attaque", Material.NAME_TAG),//RPlayer
	ABILITY_DAMAGE("ability damages", "dégâts des compétences", Material.MAGMA_CREAM),//RPlayer
	ABILITY_DEFENSE("magical defense", "défense magique", Material.SLIME_BALL),//RPlayer
	MELEE_DAMAGE("melee damages", "dégâts de mêlée", Material.DIAMOND_SWORD),//DamageManager.getDamages
	RANGED_DAMAGE("ranged damages", "dégâts à distance", Material.BOW),//DamageManager.getDamages
	MAGIC_DAMAGE("magic damages", "dégâts magiques", Material.GOLDEN_HOE),//DamageManager.getDamages
	DEFENSE("physical defense", "défense physique", Material.DIAMOND_CHESTPLATE),//RPlayer
	MAX_HEALTH("max health", "vie maximale", Material.EGG),//RPlayer.getMaxHealth
	MAX_ENERGY("max energy", "vigueur maximale", Material.CAULDRON),//RPlayer.getMaxNrj
	ENERGY_REGEN("energy regen speed", "vitesse de régénération de la vigueur", Material.CAKE),//Core.task1
	CRITIC_DAMAGE("critical strike damages", "dégâts des coups critiques", Material.ANVIL),//RPlayer
	CRITIC_CHANCE("critical strike chance", "chance de coup critique", Material.ANVIL),//RPlayer
	BLOCK_CHANCE("block chance", "blocage", Material.ENCHANTED_BOOK),//RPlayer
	LOOT_BONUS("rare loot chance", "chance de butin rare", Material.CHEST),//RlivingEntity.kill
	XP("XP", "XP", Material.EXPERIENCE_BOTTLE);//RPlayer
	
	private String displayen;
	private String displayfr;
	private Material material;
	private BuffType(String displayen, String displayfr, Material material){
		this.displayen = displayen;
		this.displayfr = displayfr;
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getDisplayEn() {
		return displayen;
	}

	public void setDisplayEn(String displayen) {
		this.displayen = displayen;
	}

	public String getDisplayFr() {
		return displayfr;
	}

	public void setDisplayFr(String displayfr) {
		this.displayfr = displayfr;
	}
	
}
