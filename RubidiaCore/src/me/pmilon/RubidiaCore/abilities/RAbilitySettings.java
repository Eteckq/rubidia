package me.pmilon.RubidiaCore.abilities;

import me.pmilon.RubidiaCore.RManager.RClass;

public enum RAbilitySettings {

	PALADIN_1(30,13,.82,1.7,.22),
	PALADIN_2(30,15,1.09,2.05,.26),
	PALADIN_3(40,0,0,0,.4325),
	PALADIN_4(30,0,0,0,.5),
	PALADIN_5(30,19,1.32,2.6,.36),
	PALADIN_6(20,18,-.6,0,0),
	PALADIN_7(20,26,1.8,2.95,.68),
	PALADIN_8(30,31,1.26,3.2,.41),
	
	RANGER_1(20,11,.9,3,.65),
	RANGER_2(30,17,.96,1.3,.41),
	RANGER_3(40,0,0,0,.06),
	RANGER_4(40,0,0,0,.25),
	RANGER_5(30,34,1.32,4,1.36),
	RANGER_6(20,29,1.46,5,1.25),
	RANGER_7(20,29,1.46,2.55,.76),
	RANGER_8(30,36,1.16,3.4,.57),
	
	MAGE_1(30,14,.84,2,.5),
	MAGE_2(30,22,.81,1.78,.26),
	MAGE_3(40,21,.79,2,.2),
	MAGE_4(30,24,1.12,2,.48),
	MAGE_5(30,29,.88,1.15,.18),
	MAGE_6(20,37,1.04,2.35,.69),
	MAGE_7(20,28,1.88,1,.45),
	MAGE_8(30,41,1.19,3.4,.57),
	
	ASSASSIN_1(40,17,.64,1.5,.22),
	ASSASSIN_2(30,24,.83,3,.17),
	ASSASSIN_3(30,0,0,100,3.5),
	ASSASSIN_4(30,29,1.01,.8,.46),
	ASSASSIN_5(30,33,.77,1,.14),
	ASSASSIN_6(20,6,.3,2.3,.17),
	ASSASSIN_7(20,38,1.18,3.1,.72),
	ASSASSIN_8(30,47,1.08,3.9,.62);
	
	private final int levelMax;
	private final double vigorMin;
	private final double vigorPerLevel;
	private final double damagesMin;
	private final double damagesPerLevel;
	private RAbilitySettings(int levelMax, double vigorMin, double vigorPerLevel, double damagesMin, double damagesPerLevel){
		this.levelMax = levelMax;
		this.vigorMin = vigorMin;
		this.vigorPerLevel = vigorPerLevel;
		this.damagesMin = damagesMin;
		this.damagesPerLevel = damagesPerLevel;
	}
	
	public int getLevelMax() {
		return levelMax;
	}

	public double getVigorMin() {
		return vigorMin;
	}

	public double getVigorPerLevel() {
		return vigorPerLevel;
	}

	public double getDamagesMin() {
		return damagesMin;
	}

	public double getDamagesPerLevel() {
		return damagesPerLevel;
	}
	
	public static RAbilitySettings getSettings(RClass rClass, int index) {
		return RAbilitySettings.valueOf(rClass.toString() + "_" + index);
	}
	
}
