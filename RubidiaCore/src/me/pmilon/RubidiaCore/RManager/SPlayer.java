package me.pmilon.RubidiaCore.RManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.handlers.JobsHandler.JobTask;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class SPlayer {

	private int id;
	private int rlevel;
	private double rexp;
	private RClass rClass;
	private RJob rJob;
	private Mastery mastery;
	private int skp;
	private int skd;
	private int firstability;
	private int secondability;
	private int thirdability;
	private int fourthability;
	private int fifthability;
	private int sixthability;
	private int seventhability;
	private int eighthability;
	private int strength;
	private int endurance;
	private int agility;
	private int intelligence;
	private int perception;
	private double currentnrj;
	private int kills;
	private int renom;
	private int lastmoneyamount;
	private List<Pet> pets;
	private HashMap<JobTask, Integer> jobscores;
	private HashMap<Integer, ItemStack> creative;
	private HashMap<Integer, ItemStack> survival;
	private HashMap<Integer, ItemStack> bank;
	private int pendingBalance;
	private List<Quest> activeQuests;
	private List<Quest> doneQuests;
	private Quest followedQuest;
	private Location lastLocation;
	private HashMap<Integer, ItemStack> lastInventory = new HashMap<Integer, ItemStack>();
	private HashMap<Integer, ItemStack> enderchest = new HashMap<Integer, ItemStack>();
	private double lastHealth;
	private int lastFoodLevel;
	
	private boolean loaded = false;
	private boolean modified = false;
	public SPlayer(int id, int rlevel, double rexp, RClass rClass, RJob rJob,
			Mastery mastery, int skp, int skd, int firstability, int secondability, int thirdability, int fourthability,
			int fifthability, int sixthability, int seventhability, int eighthability, int strength, int endurance, int agility, int intelligence, int perception, double currentnrj, int kills, int renom, int lastmoneyamount, List<Pet> pets, HashMap<JobTask, Integer> jobscores,
			HashMap<Integer, ItemStack> creative, HashMap<Integer, ItemStack> survival, HashMap<Integer,ItemStack> bank, int pendingBalance,List<Quest> activeQuests, List<Quest> doneQuests, Quest followedQuest, Location lastLocation, HashMap<Integer, ItemStack> lastInventory, HashMap<Integer, ItemStack> enderchest,
			double lastHealth, int lastFoodLevel){
		this.id = id;
		this.rlevel = rlevel;
		this.rexp = rexp;
		this.rClass = rClass;
		this.rJob = rJob;
		this.mastery = mastery;
		this.skp = skp;
		this.skd = skd;
		this.firstability = firstability;
		this.secondability = secondability;
		this.thirdability = thirdability;
		this.fourthability = fourthability;
		this.fifthability = fifthability;
		this.sixthability = sixthability;
		this.seventhability = seventhability;
		this.eighthability = eighthability;
		this.strength = strength;
		this.endurance = endurance;
		this.agility = agility;
		this.intelligence = intelligence;
		this.perception = perception;
		this.currentnrj = currentnrj;
		this.kills = kills;
		this.renom = renom;
		this.lastmoneyamount = lastmoneyamount;
		this.pets = pets;
		this.jobscores = jobscores;
		this.creative = creative;
		this.survival = survival;
		this.bank = bank;
		this.pendingBalance = pendingBalance;
		this.activeQuests = activeQuests;
		this.doneQuests = doneQuests;
		this.followedQuest = followedQuest;
		this.lastLocation = lastLocation;
		this.lastInventory = lastInventory;
		this.enderchest = enderchest;
		this.lastHealth = lastHealth;
		this.lastFoodLevel = lastFoodLevel;
	}
	public int getRLevel() {
		return rlevel;
	}
	public void setRLevel(int rlevel, RXPSource source) {
		this.rlevel = rlevel;
		this.setModified(true);
	}
	public double getRExp() {
		return rexp;
	}
	public void setRExp(double rexp, RXPSource source, RPlayer rp) {
		this.rexp = rexp;
		if(this.rexp >= LevelUtils.getRLevelTotalExp(this.rlevel)){
			int newLevel = this.rlevel;
			double exp = this.rexp;
			while(exp >= LevelUtils.getRLevelTotalExp(newLevel)){
				exp -= LevelUtils.getRLevelTotalExp(newLevel);
				newLevel += 1;
			}
			this.rexp = exp;
			rp.setRLevel(newLevel, source);
		}
		this.setModified(true);
	}
	public RClass getRClass() {
		return rClass;
	}
	public void setRClass(RClass rClass) {
		this.rClass = rClass;
		this.setModified(true);
	}
	public RJob getRJob() {
		return rJob;
	}
	public void setRJob(RJob rJob) {
		this.rJob = rJob;
		this.setModified(true);
	}
	public Mastery getMastery() {
		return mastery;
	}
	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
		this.setModified(true);
	}
	public int getSkp() {
		return skp;
	}
	public void setSkp(int skp) {
		this.skp = skp;
		this.setModified(true);
	}
	public int getSkd() {
		return skd;
	}
	public void setSkd(int skd) {
		this.skd = skd;
		this.setModified(true);
	}
	public int getFirstability() {
		return firstability;
	}
	public void setFirstability(int firstability) {
		this.firstability = firstability;
		this.setModified(true);
	}
	public int getSecondability() {
		return secondability;
	}
	public void setSecondability(int secondability) {
		this.secondability = secondability;
		this.setModified(true);
	}
	public int getThirdability() {
		return thirdability;
	}
	public void setThirdability(int thirdability) {
		this.thirdability = thirdability;
		this.setModified(true);
	}
	public int getFourthability() {
		return fourthability;
	}
	public void setFourthability(int fourthability) {
		this.fourthability = fourthability;
		this.setModified(true);
	}
	public int getFifthability() {
		return fifthability;
	}
	public void setFifthability(int fifthability) {
		this.fifthability = fifthability;
		this.setModified(true);
	}
	public int getSixthability() {
		return sixthability;
	}
	public void setSixthability(int sixthability) {
		this.sixthability = sixthability;
		this.setModified(true);
	}
	public int getSeventhability() {
		return seventhability;
	}
	public void setSeventhability(int seventhability) {
		this.seventhability = seventhability;
		this.setModified(true);
	}
	public int getEighthability() {
		return eighthability;
	}
	public void setEighthability(int eighthability) {
		this.eighthability = eighthability;
		this.setModified(true);
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
		this.setModified(true);
	}
	public int getEndurance() {
		return endurance;
	}
	public void setEndurance(int endurance) {
		this.endurance = endurance;
		this.setModified(true);
	}
	public int getAgility() {
		return agility;
	}
	public void setAgility(int agility) {
		this.agility = agility;
		this.setModified(true);
	}
	public int getIntelligence() {
		return intelligence;
	}
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
		this.setModified(true);
	}
	public int getPerception() {
		return perception;
	}
	public void setPerception(int perception) {
		this.perception = perception;
		this.setModified(true);
	}
	public double getCurrentnrj() {
		return currentnrj;
	}
	public void setCurrentnrj(double currentnrj) {
		this.currentnrj = currentnrj;
		this.setModified(true);
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
		this.setModified(true);
	}
	public int getRenom() {
		return renom;
	}
	public void setRenom(int renom) {
		this.renom = renom;
		this.setModified(true);
	}
	public int getLastmoneyamount() {
		return lastmoneyamount;
	}
	public void setLastmoneyamount(int lastmoneyamount) {
		this.lastmoneyamount = lastmoneyamount;
		this.setModified(true);
	}
	public HashMap<JobTask, Integer> getJobscores() {
		return jobscores;
	}
	public void setJobscores(HashMap<JobTask, Integer> jobscores) {
		this.jobscores = jobscores;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getCreative() {
		return creative;
	}
	public void setCreative(HashMap<Integer, ItemStack> creative) {
		this.creative = creative;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getSurvival() {
		return survival;
	}
	public void setSurvival(HashMap<Integer, ItemStack> survival) {
		this.survival = survival;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getBank() {
		return bank;
	}
	public void setBank(HashMap<Integer, ItemStack> bank) {
		this.bank = bank;
		this.setModified(true);
	}
	public List<Quest> getActiveQuests() {
		return activeQuests;
	}
	public void setActiveQuests(List<Quest> activeQuests) {
		this.activeQuests = activeQuests;
		this.setModified(true);
	}
	public List<Quest> getDoneQuests() {
		return doneQuests;
	}
	public void setDoneQuests(List<Quest> doneQuests) {
		this.doneQuests = doneQuests;
		this.setModified(true);
	}
	public Quest getFollowedQuest() {
		return followedQuest;
	}
	public void setFollowedQuest(Quest followedQuest) {
		this.followedQuest = followedQuest;
		this.setModified(true);
	}
	
	public void save(RPlayer rp){
		String path = "players." + rp.getUniqueId() + ".saves." + this.getId();
		Configs.getPlayerConfig().set(path + ".rLevel", this.getRLevel());
		Configs.getPlayerConfig().set(path + ".rExp", this.getRExp());
		Configs.getPlayerConfig().set(path + ".rClass", this.getRClass().toString());
		Configs.getPlayerConfig().set(path + ".rJob", this.getRJob().toString());
		Configs.getPlayerConfig().set(path + ".mastery", this.getMastery().toString());
		Configs.getPlayerConfig().set(path + ".skillpoints", this.getSkp());
		Configs.getPlayerConfig().set(path + ".distinctionpoints", this.getSkd());
		Configs.getPlayerConfig().set(path + ".ability.1", this.getFirstability());
		Configs.getPlayerConfig().set(path + ".ability.2", this.getSecondability());
		Configs.getPlayerConfig().set(path + ".ability.3", this.getThirdability());
		Configs.getPlayerConfig().set(path + ".ability.4", this.getFourthability());
		Configs.getPlayerConfig().set(path + ".ability.5", this.getFifthability());
		Configs.getPlayerConfig().set(path + ".ability.6", this.getSixthability());
		Configs.getPlayerConfig().set(path + ".ability.7", this.getSeventhability());
		Configs.getPlayerConfig().set(path + ".ability.8", this.getEighthability());
		Configs.getPlayerConfig().set(path + ".strength", this.getStrength());
		Configs.getPlayerConfig().set(path + ".endurance", this.getEndurance());
		Configs.getPlayerConfig().set(path + ".agility", this.getAgility());
		Configs.getPlayerConfig().set(path + ".intelligence", this.getIntelligence());
		Configs.getPlayerConfig().set(path + ".perception", this.getPerception());
		Configs.getPlayerConfig().set(path + ".currentnrj", this.getCurrentnrj());
		Configs.getPlayerConfig().set(path + ".kills", this.getKills());
		Configs.getPlayerConfig().set(path + ".renom", this.getRenom());
		Configs.getPlayerConfig().set(path + ".lastMoneyAmount", this.getLastmoneyamount());
		Configs.getPlayerConfig().set(path + ".lastLocation", this.getLastLocation());
		Configs.getPlayerConfig().set(path + ".lastHealth", this.getLastHealth());
		Configs.getPlayerConfig().set(path + ".lastFoodLevel", this.getLastFoodLevel());
		Configs.getPlayerConfig().set(path + ".pendingBalance", this.getPendingBalance());
		for(JobTask task : this.getJobscores().keySet())Configs.getPlayerConfig().set(path + ".jobscores." + task.toString(), this.getJobscores().get(task));
		for(int i : this.getCreative().keySet()){
			Configs.getPlayerConfig().set(path + ".gamemode.creative." + i, this.getCreative().get(i));
		}
		for(int i : this.getSurvival().keySet()){
			Configs.getPlayerConfig().set(path + ".gamemode.survival." + i, this.getSurvival().get(i));
		}
		for(int i = 0;i < 18;i++){
			if(this.getBank().containsKey(i)){
				Configs.getPlayerConfig().set(path + ".bank." + i, this.getBank().get(i));
			}else{
				Configs.getPlayerConfig().set(path + ".bank." + i, null);
			}
		}
		for(int i : this.getLastInventory().keySet()){
			Configs.getPlayerConfig().set(path + ".lastInventory." + i, this.getLastInventory().get(i));
		}
		for(int i : this.getEnderchest().keySet())Configs.getPlayerConfig().set(path + ".enderChest." + i, this.getEnderchest().get(i));
		List<String> pets = new ArrayList<String>();
		for(Pet pet : this.getPets())pets.add(pet.getUUID());
		Configs.getPlayerConfig().set(path + ".pets", pets);
		List<String> activeQuests = new ArrayList<String>();
		for(Quest quest : this.getActiveQuests())activeQuests.add(quest.getUUID());
		Configs.getPlayerConfig().set(path + ".activeQuests", activeQuests);
		List<String> doneQuests = new ArrayList<String>();
		for(Quest quest : this.getDoneQuests())doneQuests.add(quest.getUUID());
		Configs.getPlayerConfig().set(path + ".doneQuests", doneQuests);
		if(this.getFollowedQuest() != null)Configs.getPlayerConfig().set(path + ".followedQuest", this.getFollowedQuest().getUUID());
		else Configs.getPlayerConfig().set(path + ".followedQuest", null);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		this.setModified(true);
	}
	public Location getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getLastInventory() {
		return lastInventory;
	}
	public void setLastInventory(HashMap<Integer, ItemStack> lastInventory) {
		this.lastInventory = lastInventory;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getEnderchest() {
		return enderchest;
	}
	public void setEnderchest(HashMap<Integer, ItemStack> enderchest) {
		this.enderchest = enderchest;
		this.setModified(true);
	}
	public double getLastHealth() {
		return lastHealth;
	}
	public void setLastHealth(double lastHealth) {
		this.lastHealth = lastHealth;
		this.setModified(true);
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	public int getLastFoodLevel() {
		return lastFoodLevel;
	}
	public void setLastFoodLevel(int lastFoodLevel) {
		this.lastFoodLevel = lastFoodLevel;
		this.setModified(true);
	}
	public int getPendingBalance() {
		return pendingBalance;
	}
	public void setPendingBalance(int pendingBalance) {
		this.pendingBalance = pendingBalance;
		this.setModified(true);
	}
	public List<Pet> getPets() {
		return pets;
	}
	public void setPets(List<Pet> pets) {
		this.pets = pets;
		this.setModified(true);
	}
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
}
