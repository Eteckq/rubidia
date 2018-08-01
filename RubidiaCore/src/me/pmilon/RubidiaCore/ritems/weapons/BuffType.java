package me.pmilon.RubidiaCore.ritems.weapons;

import org.bukkit.Material;

public enum BuffType {

	LIFT_COST("elytra lift cost", "co�t d'�l�vation en �lytres", Material.ELYTRA),//RPlayer
	WALK_SPEED("walk speed", "vitesse de d�placement", Material.FEATHER),//Core.task2
	ATTACK_SPEED("attack speed", "vitesse d'attaque", Material.NAME_TAG),//RPlayer
	ABILITY_DAMAGE("ability damages", "d�g�ts des comp�tences", Material.MAGMA_CREAM),//RPlayer
	ABILITY_DEFENSE("magical defense", "d�fense magique", Material.SLIME_BALL),//RPlayer
	MELEE_DAMAGE("melee damages", "d�g�ts de m�l�e", Material.DIAMOND_SWORD),//DamageManager.getDamages
	RANGED_DAMAGE("ranged damages", "d�g�ts � distance", Material.BOW),//DamageManager.getDamages
	MAGIC_DAMAGE("magic damages", "d�g�ts magiques", Material.GOLDEN_HOE),//DamageManager.getDamages
	DEFENSE("physical defense", "d�fense physique", Material.DIAMOND_CHESTPLATE),//RPlayer
	MAX_HEALTH("max health", "vie maximale", Material.EGG),//RPlayer.getMaxHealth
	MAX_ENERGY("max energy", "vigueur maximale", Material.CAULDRON),//RPlayer.getMaxNrj
	ENERGY_REGEN("energy regen speed", "vitesse de r�g�n�ration de la vigueur", Material.CAKE),//Core.task1
	CRITIC_DAMAGE("critical strike damages", "d�g�ts des coups critiques", Material.ANVIL),//RPlayer
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
