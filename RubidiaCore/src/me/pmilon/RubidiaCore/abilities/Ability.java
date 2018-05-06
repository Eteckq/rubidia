package me.pmilon.RubidiaCore.abilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerAbilityEvent;

public class Ability {
	
	private String[] name;
	private List<String> description;
	private RClass rClass;
	private Mastery mastery;
	private int index;
	private boolean passive;
	private String sequence;
	private String[] suppInfo;
	private String[] unit;
	private boolean toggleable;
	
	private int levelMax;
	private double vigorMin;
	private double vigorPerLevel;
	private double damagesMin;
	private double damagesPerLevel;
	public Ability(String[] name, List<String> description, RClass rClass, Mastery mastery, int index, boolean passive, String sequence, int levelMax, double vigorMin, double vigorPerLevel, double damagesMin, double damagesPerLevel, String[] suppInfo, String[] unit, boolean toggleable){
		this.name = name;
		this.description = description;
		this.rClass = rClass;
		this.mastery = mastery;
		this.index = index;
		this.passive = passive;
		this.sequence = sequence;
		this.levelMax = levelMax;
		this.vigorMin = vigorMin;
		this.vigorPerLevel = vigorPerLevel;
		this.damagesMin = damagesMin;
		this.damagesPerLevel = damagesPerLevel;
		this.suppInfo = suppInfo;
		this.unit = unit;
		this.toggleable = toggleable;
	}
	
	public List<String> getDescription() {
		return description;
	}
	
	public void setDescription(List<String> description) {
		this.description = description;
	}

	public RClass getRClass() {
		return rClass;
	}

	public void setRClass(RClass rClass) {
		this.rClass = rClass;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getVigorMin() {
		return vigorMin;
	}

	public void setVigorMin(double vigorMin) {
		this.vigorMin = vigorMin;
	}

	public double getVigorPerLevel() {
		return vigorPerLevel;
	}

	public void setVigorPerLevel(double vigorPerLevel) {
		this.vigorPerLevel = vigorPerLevel;
	}

	public double getDamagesMin() {
		return damagesMin;
	}

	public void setDamagesMin(double damagesMin) {
		this.damagesMin = damagesMin;
	}

	public double getDamagesPerLevel() {
		return damagesPerLevel;
	}

	public void setDamagesPerLevel(double damagesPerLevel) {
		this.damagesPerLevel = damagesPerLevel;
	}

	public boolean isPassive() {
		return passive;
	}

	public void setPassive(boolean passive) {
		this.passive = passive;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public int getLevelMax() {
		return levelMax;
	}

	public void setLevelMax(int levelMax) {
		this.levelMax = levelMax;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}
	
	public void doAbility(Player p, RPlayer rp, double damages, double neednrj){
		try {
			Method method1 = Abilities.class.getDeclaredMethod("do" + this.getRClass().toString() + this.getIndex(), Player.class, RPlayer.class, double.class, double.class);
			method1.setAccessible(true);
			RPlayerAbilityEvent event = new RPlayerAbilityEvent(rp, this);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				method1.invoke(null, p, rp, damages, neednrj);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Mastery getMastery() {
		return mastery;
	}

	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
	}

	public String[] getSuppInfo() {
		return suppInfo;
	}

	public void setSuppInfo(String[] suppInfo) {
		this.suppInfo = suppInfo;
	}

	public String[] getUnit() {
		return unit;
	}

	public void setUnit(String[] unit) {
		this.unit = unit;
	}

	public boolean isToggleable() {
		return toggleable;
	}

	public void setToggleable(boolean toggleable) {
		this.toggleable = toggleable;
	}
}
