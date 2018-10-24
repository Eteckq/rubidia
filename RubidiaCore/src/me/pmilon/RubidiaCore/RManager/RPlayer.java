package me.pmilon.RubidiaCore.RManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.Smiley;
import me.pmilon.RubidiaCore.RChat.ChatType;
import me.pmilon.RubidiaCore.RChat.RChat;
import me.pmilon.RubidiaCore.RChat.RChatMessage;
import me.pmilon.RubidiaCore.RChat.RChatUtils;
import me.pmilon.RubidiaCore.couples.Couple;
import me.pmilon.RubidiaCore.couples.Couples;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.duels.RBooster;
import me.pmilon.RubidiaCore.duels.RBooster.RBoosterType;
import me.pmilon.RubidiaCore.duels.RDuel;
import me.pmilon.RubidiaCore.events.RPlayerClassChangeEvent;
import me.pmilon.RubidiaCore.events.RPlayerLevelChangeEvent;
import me.pmilon.RubidiaCore.events.RPlayerPreChatMessageEvent;
import me.pmilon.RubidiaCore.events.RPlayerRequestDuelEvent;
import me.pmilon.RubidiaCore.events.RPlayerXPEvent;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.handlers.JobsHandler.JobTask;
import me.pmilon.RubidiaCore.handlers.TradingHandler;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerChat;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSetSlot;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing.PiercingType;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.tags.NameTags;
import me.pmilon.RubidiaCore.tasks.BossBarTimer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaQuests.QuestHelpRunnable;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.shops.PlayerShop;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class RPlayer {
	
	private final static List<RPlayer> onlines = new ArrayList<RPlayer>();

	private final String uuid;
	private String name;
	private String lang;
	private Gender sex;
	private long birthDate;
	private boolean profileUpdated;
	private boolean notifonfriendjoin;
	private boolean notifonchatrequest;
	private boolean invocation;
	private boolean teleportation;
	private int combatLevel;
	private boolean clickSound;
	private boolean effects;
	private boolean music;
	private boolean usingTextures;
	private boolean usingCycle;
	private Long vip;
	private SPlayer[] saves = new SPlayer[4];
	private int lastLoadedSPlayerId;
	private long lastConnectionDate;
	private long gamingTime;
	private int pendingRubis;
	private String coupleUUID;
	private long lastDivorce;
	private int chatHeight;
	private int chatboxHeight;
	private int chatboxWidth;
	private boolean publicData;
	
	private boolean modified;
	private boolean[] activeAbilities = new boolean[]{false,false,false,false,false,false,false,false};
	public Location connectionLocation = null;
	public RPlayer lastWelcome = null;
	private SPlayer loadedSPlayer;
	public int weaponsIndex = 0;
	public boolean canAttackMagic = true;
	public final HashMap<LivingEntity, BossBarTimer> barTimers = new HashMap<LivingEntity, BossBarTimer>();
	public final HashMap<String, AttributeModifier> attributeModifiers = new HashMap<String, AttributeModifier>();
	public boolean updateModifiersValue = false;
	private String keystroke = "";
	private int keystrokeTaskId = -1;
	private Long lastAttack = 0L;
	private double nextAttackFactor = 1.0;
	public ItemStack smileyHelmet;
	public BukkitTask smileyTask;
	private Player player;
	private final List<String> pendingMessages = new ArrayList<String>();
	private BukkitTask muteTask;
	public RPlayer fiance;
	public BukkitTask engagementTask;
	public int badword = 0;
	public final List<PNJHandler> glowingPNJs = new ArrayList<PNJHandler>();
	private RChat chat;
	private boolean usingChat;
	private boolean vanished = false;
	public int shoutIndex = 0;
	private PlayerShop shop;
	private QuestHelpRunnable questHelpTask;
	private Long lastCombat = System.currentTimeMillis();
	private BukkitTask resurrectionTask;
	private final List<RDuel> duels = new ArrayList<RDuel>();
	private RDuel currentDuel;
	private final HashMap<String,Long> lastCompetitiveDuelDates = new HashMap<String,Long>();
	private final List<RBooster> activeRBoosters = new ArrayList<RBooster>();
	private final HashMap<String, BukkitTask> reloadingWeapons = new HashMap<String, BukkitTask>();

	public RPlayer(String uuid, String name, String lang, Gender sex, long birthDate, boolean profileUpdated, boolean notifonfriendjoin, boolean notifonchatrequest, boolean invocation, boolean teleportation, int combatLevel,
			boolean clickSound, boolean effects, boolean music, boolean usingTextures, boolean usingCycle, boolean publicData,
			Long vip, boolean modified, SPlayer[] saves, int lastLoadedSPlayerId, long lastConnectionDate, long gamingTime, int pendingRubis, String coupleUUID, long lastDivorce,
			int chatHeight, int chatboxWidth, int chatboxHeight, boolean usingChat){
		this.uuid = uuid;
		this.name = name;
		this.lang = lang;
		this.sex = sex;
		this.birthDate = birthDate;
		this.profileUpdated = profileUpdated;
		this.notifonfriendjoin = notifonfriendjoin;
		this.notifonchatrequest = notifonchatrequest;
		this.invocation = invocation;
		this.teleportation = teleportation;
		this.combatLevel = combatLevel;
		this.effects = effects;
		this.music = music;
		this.usingTextures = usingTextures;
		this.usingCycle = usingCycle;
		this.publicData = publicData;
		this.vip = vip;
		this.clickSound = clickSound;
		this.modified = modified;
		this.saves = saves;
		this.lastLoadedSPlayerId = lastLoadedSPlayerId;
		this.lastConnectionDate = lastConnectionDate;
		this.gamingTime = gamingTime;
		this.pendingRubis = pendingRubis;
		this.coupleUUID = coupleUUID;
		this.lastDivorce = lastDivorce;
		this.chatHeight = chatHeight;
		this.chatboxWidth = chatboxWidth;
		this.chatboxHeight = chatboxHeight;
		this.usingChat = usingChat;
	}

	public static RPlayer get(Player p){
		return Core.rcoll.get(p);
	}
	
	public static RPlayer get(GMember member){
		return Core.rcoll.get(member);
	}
	
	public static RPlayer getFromName(String name){
		return Core.rcoll.getFromName(name);
	}
	
	public boolean is(RPlayer rp){
		if(rp.getUniqueId().equals(this.uuid))return true;
		return false;
	}
	
	public boolean is(Player p){
		if(p.getUniqueId().toString().equals(this.uuid))return true;
		return false;
	}
	
	public static void broadcastMessage(String en, String fr){
		for(RPlayer rp : RPlayer.getOnlines()){
			rp.sendMessage(en == null ? fr : en, fr);
		}
	}

	public static List<RPlayer> getOnlines() {
		return onlines;
	}
	
	////////////////////
	//     GETTER     //
	////////////////////
	
	public String getUniqueId(){
		return this.uuid;}
	public String getName(){
		return this.name;
	}
	public String getRawLanguage(){
		return this.lang;
	}
	public int getRLevel(){
		return this.getLoadedSPlayer().getRLevel();
	}
	public double getRExp(){
		return this.getLoadedSPlayer().getRExp();
	}
	public RClass getRClass(){
		return this.getLoadedSPlayer().getRClass();
	}
	public RJob getRJob(){
		return this.getLoadedSPlayer().getRJob();
	}
	public Mastery getMastery(){
		return this.getLoadedSPlayer().getMastery();
	}
	public int getSkillPoints(){
		return this.getLoadedSPlayer().getSkp();
	}
	public int getAbLevel1(){
		return this.getLoadedSPlayer().getFirstability();
	}
	public int getAbLevel2(){
		return this.getLoadedSPlayer().getSecondability();
	}
	public int getAbLevel3(){
		return this.getLoadedSPlayer().getThirdability();
	}
	public int getAbLevel4(){
		return this.getLoadedSPlayer().getFourthability();
	}
	public int getAbLevel5(){
		return this.getLoadedSPlayer().getFifthability();
	}
	public int getAbLevel6(){
		return this.getLoadedSPlayer().getSixthability();
	}
	public int getAbLevel7() {
		return this.getLoadedSPlayer().getSeventhability();
	}
	public void setAbLevel7(int seventhability) {
		this.getLoadedSPlayer().setSeventhability(seventhability);
	}
	public int getAbLevel8() {
		return this.getLoadedSPlayer().getEighthability();
	}
	public void setAbLevel8(int eighthability) {
		this.getLoadedSPlayer().setEighthability(eighthability);
	}
	public double getNrj(){
		if(this.isOnline())if(this.isOp())return this.getMaxNrj();
		return this.getLoadedSPlayer().getCurrentnrj();
	}
	public double getMaxNrj(){
		return (100 + (this.getIntelligence()*5))*(1+this.getAdditionalFactor(BuffType.MAX_ENERGY));
	}
	public double getNrjPerSecond(){
		return (1 + (this.getEndurance()*.02))*(1+this.getAdditionalFactor(BuffType.ENERGY_REGEN));
	}
	public int getKills(){
		return this.getLoadedSPlayer().getKills();
	}
	public int getRenom(){
		return this.getLoadedSPlayer().getRenom();
	}
	public boolean getNotifOnFriendJoin(){
		return this.notifonfriendjoin;
	}
	public boolean getNotifOnChatRequest(){
		return this.notifonchatrequest;
	}
	public boolean getEffects(){
		return this.effects;
	}
	public boolean getMusic(){
		return this.music;
	}
	public boolean isVip(){
		return this.vip > 0;
	}
	public Long getVip(){
		return this.vip;
	}
	public HashMap<Integer, ItemStack> getCreativeHM(){
		return this.getLoadedSPlayer().getCreative();
	}
	public HashMap<Integer, ItemStack> getSurvivalHM(){
		return this.getLoadedSPlayer().getSurvival();
	}
	public int getBank(){
		return this.getLoadedSPlayer().getBank();
	}
	public Player getPlayer(){
		return this.player;
	}
	public boolean getWouldLikeInvocation(){
		return this.invocation;
	}
	public boolean getWouldLikeTeleportation(){
		return this.teleportation;
	}
	public HashMap<JobTask, Integer> getJobScores(){
		return this.getLoadedSPlayer().getJobscores();
	}
	public String getJobName(){
		if(this.getRJob().equals(RJob.ALCHEMIST))return this.translateString("§dAlchemist", "§dAlchimiste");
		else if(this.getRJob().equals(RJob.FARMER))return this.translateString("§eFarmer", "§eFermier");
		else if(this.getRJob().equals(RJob.HUNTER))return this.translateString("§cHunter", "§cChasseur");
		else if(this.getRJob().equals(RJob.LUMBERMAN))return this.translateString("§aLumberman", "§aBûcheron");
		else if(this.getRJob().equals(RJob.MINER))return this.translateString("§bMiner", "§bMineur");
		return "§7Sans emploi";
	}
	
	////////////////////
	//     SETTER     //
	////////////////////
	
	public void setRLevel(int rlevel, RXPSource source){
		RPlayerLevelChangeEvent levelEvent = new RPlayerLevelChangeEvent(this, this.getLoadedSPlayer().getRLevel(), rlevel, source);
		Bukkit.getPluginManager().callEvent(levelEvent);
		if(!levelEvent.isCancelled()){
			this.getLoadedSPlayer().setRLevel(levelEvent.getNewRLevel(), levelEvent.getSource());
			this.refreshRLevelDisplay();
		}
		if(this.isOnline()){
			this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			Core.playAnimEffect(Particle.VILLAGER_HAPPY, this.getPlayer().getLocation().add(0,1,0), .4F, .4F, .4F, 1, 60);
			NameTags.update();
		}
	}
	public void setRExp(double rexp, RXPSource source){
		this.getLoadedSPlayer().setRExp(rexp, source, this);
		if(this.isOnline())this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, .1F);
		this.refreshRLevelDisplay();
	}
	public void refreshRLevelDisplay() {
		if(this.isOnline()){
			Player p = this.getPlayer();
			p.setLevel(this.getRLevel());
			p.setExp((float) (this.getRExp()/LevelUtils.getRLevelTotalExp(this)));
		}
	}
	public void setName(String name){
		this.name = name;
		this.setModified(true);
	}
	public void setRClass(RClass rclass){
		RPlayerClassChangeEvent event = new RPlayerClassChangeEvent(this, this.getRClass(), rclass);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			event.getRPlayer().getLoadedSPlayer().setRClass(event.getNewRClass());
		}
	}
	public void setRJob(RJob rjob){
		this.getLoadedSPlayer().setRJob(rjob);
	}
	public void setMastery(Mastery mastery){
		this.getLoadedSPlayer().setMastery(mastery);
	}
	public void setSkillPoints(int skp){
		this.getLoadedSPlayer().setSkp(skp);
	}
	public void setAbLevel1(int firstability){
		this.getLoadedSPlayer().setFirstability(firstability);
	}
	public void setAbLevel2(int secondability){
		this.getLoadedSPlayer().setSecondability(secondability);
	}
	public void setAbLevel3(int thirdability){
		this.getLoadedSPlayer().setThirdability(thirdability);
	}
	public void setAbLevel4(int fourthability){
		this.getLoadedSPlayer().setFourthability(fourthability);
	}
	public void setAbLevel5(int fifthability){
		this.getLoadedSPlayer().setFifthability(fifthability);
	}
	public void setAbLevel6(int sixthability){
		this.getLoadedSPlayer().setSixthability(sixthability);
	}
	public void setNrj(double currentnrj){
		if(currentnrj < 0)currentnrj = 0;
		if(currentnrj > this.getMaxNrj())currentnrj = this.getMaxNrj();
		this.getLoadedSPlayer().setCurrentnrj(currentnrj);
		this.updateNrj();
		this.setModified(true);
	}
	public void setKills(int kills){
		this.getLoadedSPlayer().setKills(kills);
	}
	public void setRenom(int renom){
		this.getLoadedSPlayer().setRenom(renom);
		if(this.isOnline())NameTags.update();
	}
	public void setNotifOnFriendJoin(boolean notifonfriendjoin){
		this.notifonfriendjoin = notifonfriendjoin;
		this.setModified(true);
	}
	public void setNotifOnChatRequest(boolean notifonchatrequest){
		this.notifonchatrequest = notifonchatrequest;
		this.setModified(true);
	}
	public void setEffects(boolean effects){
		this.effects = effects;
		this.setModified(true);
	}
	public void setMusic(boolean music){
		this.music = music;
		this.setModified(true);
	}
	public void setVip(Long vip){
		this.vip = vip;
		this.setModified(true);
	}
	public void setPlayer(Player p){
		this.player = p;
		this.setModified(true);
	}
	public void setWouldLikeInvocation(boolean invocation){
		this.invocation = invocation;
		this.setModified(true);
	}
	public void setWouldLikeTeleportation(boolean teleportation){
		this.teleportation = teleportation;
		this.setModified(true);
	}
	public void setJobScores(HashMap<JobTask, Integer> jobscores){
		this.getLoadedSPlayer().setJobscores(jobscores);
	}
	
	public boolean hasNrj(double i){
		return this.getNrj() >= i;
	}
	public boolean hasWeaponInHand(){
		if(this.isOnline()){
			String material = this.getPlayer().getEquipment().getItemInMainHand().getType().toString();
			return (this.getRClass().equals(RClass.PALADIN) && material.contains("_AXE")) || (this.getRClass().equals(RClass.RANGER) && material.contains("BOW")) || (this.getRClass().equals(RClass.MAGE) && material.contains("_HOE")) || (this.getRClass().equals(RClass.ASSASSIN) && material.contains("_SWORD")) || (this.getRClass().equals(RClass.VAGRANT) && (material.contains("_SWORD") || material.contains("_AXE") || material.contains("BOW")));
		}
		return false;
	}
	public boolean isMaster(){
		return this.getMastery().equals(Mastery.MASTER);
	}
	public boolean isHero(){
		return this.getMastery().equals(Mastery.HERO);
	}
	public boolean isOnline(){
		return this.getPlayer() != null;
	}
	public boolean isInDuel(){
		return this.getCurrentDuel() != null;
	}
	public RPlayer getDuelOpponent(){
		if(this.isInDuel()){
			if(this.getCurrentDuel().getChallenger().equals(this))return this.getCurrentDuel().getChallenged();
			else return this.getCurrentDuel().getChallenger();
		}
		return null;
	}
	public Player getTradeRequestOpponent(){
		if(TradingHandler.traderequest.containsKey(this.getPlayer()))return TradingHandler.traderequest.get(this.getPlayer());
		return null;
	}
	
	public void addNrj(double amount){
		this.setNrj(this.getNrj()+amount);
	}
	public String getClassName(){
		String classname = "";
		if(this.getRClass().equals(RClass.PALADIN)){
			classname = "§aPaladin";
			if(this.isMaster()){
				classname = this.translateString("§2§l[MASTER] §aPaladin", "§2§l[MAITRE] §aPaladin");
			}else if(this.isHero()){
				classname = this.translateString("§2§l[HERO] §aPaladin", "§2§l[HEROS] §aPaladin");
			}
		}else if(this.getRClass().equals(RClass.RANGER)){
			classname = "§bRanger";
			if(this.isMaster()){
				classname = this.translateString("§9§l[MASTER] §bRanger", "§9§l[MAITRE] §bRanger");
			}else if(this.isHero()){
				classname = this.translateString("§9§l[HERO] §bRanger", "§9§l[HEROS] §bRanger");
			}
		}else if(this.getRClass().equals(RClass.MAGE)){
			classname = "§eMage";
			if(this.isMaster()){
				classname = this.translateString("§6§l[MASTER] §eMage", "§6§l[MAITRE] §eMage");
			}else if(this.isHero()){
				classname = this.translateString("§6§l[HERO] §eMage", "§6§l[HEROS] §eMage");
			}
		}else if(this.getRClass().equals(RClass.ASSASSIN)){
			classname = "§cAssassin";
			if(this.isMaster()){
				classname = this.translateString("§4§l[MASTER] §cAssassin", "§4§l[MAITRE] §cAssassin");
			}else if(this.isHero()){
				classname = this.translateString("§4§l[HERO] §cAssassin", "§4§l[HEROS] §cAssassin");
			}
		}else if(this.getRClass().equals(RClass.VAGRANT)){
			classname = this.translateString("§7Vagrant", "§7Vagabond");
		}
		return classname;
	}
	public String getEvolutionClassName(){
		String classname = "";
		if(this.getRClass().equals(RClass.PALADIN)){
			if(this.isMaster()){
				classname = this.translateString("§2§l[HERO] §aPaladin", "§2§l[HEROS] §aPaladin");
			}else if(!this.isHero()){
				classname = this.translateString("§2§l[MASTER] §aPaladin", "§2§l[MAITRE] §aPaladin");
			}
		}else if(this.getRClass().equals(RClass.RANGER)){
			if(this.isMaster()){
				classname = this.translateString("§9§l[HERO] §bRanger", "§9§l[HEROS] §bRanger");
			}else if(!this.isHero()){
				classname = this.translateString("§9§l[MASTER] §bRanger", "§9§l[MAITRE] §bRanger");
			}
		}else if(this.getRClass().equals(RClass.MAGE)){
			if(this.isMaster()){
				classname = this.translateString("§6§l[HERO] §eMage", "§6§l[HEROS] §eMage");
			}else if(!this.isHero()){
				classname = this.translateString("§6§l[MASTER] §eMage", "§6§l[MAITRE] §eMage");
			}
		}else if(this.getRClass().equals(RClass.ASSASSIN)){
			if(this.isMaster()){
				classname = this.translateString("§4§l[HERO] §cAssassin", "§4§l[HEROS] §cAssassin");
			}else if(!this.isHero()){
				classname = this.translateString("§4§l[MASTER] §cAssassin", "§4§l[MAITRE] §cAssassin");
			}
		}
		return classname;
	}
	public double getMaxHealth(){
		return (20+this.getEndurance()*.75)*(1+this.getAdditionalFactor(BuffType.MAX_HEALTH));
	}
	public int resetStats(){
		int skd = this.getLoadedSPlayer().getSkd() + this.getLoadedSPlayer().getStrength() + this.getLoadedSPlayer().getEndurance() + this.getLoadedSPlayer().getAgility() + this.getLoadedSPlayer().getIntelligence() + this.getLoadedSPlayer().getPerception();
		this.setSkillDistinctionPoints(skd);
		this.setStrength(0);
		this.setEndurance(0);
		this.setAgility(0);
		this.setIntelligence(0);
		this.setPerception(0);
		this.setNrj(this.getMaxNrj());
		if(this.isOnline()){
			this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
			this.getPlayer().setHealth(this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
			this.sendMessage("§aYour statistics have been resetted!", "§aVos statistiques ont été réinitialisées.");
		}
		return skd;
	}
	public int resetAbilities(){
		int skp = this.getLoadedSPlayer().getSkp() + this.getAbLevel1() + this.getAbLevel2() + this.getAbLevel3() + this.getAbLevel4() + this.getAbLevel5() + this.getAbLevel6() + this.getAbLevel7() + this.getAbLevel8();
		this.setSkillPoints(skp);
		this.setAbLevel1(0);
		this.setAbLevel2(0);
		this.setAbLevel3(0);
		this.setAbLevel4(0);
		this.setAbLevel5(0);
		this.setAbLevel6(0);
		this.setAbLevel7(0);
		this.setAbLevel8(0);
		if(this.isOnline())this.sendMessage("§aYour abilities have been resetted!", "§aVos compétences ont été réinitialisées.");
		return skp;
	}
	@SuppressWarnings("deprecation")
	public String getLanguage(){
		if(this.isOnline())this.lang = this.getPlayer().spigot().getLocale();
		return this.lang;
	}
	public String translateString(String sen, String sfr){
		if(this.getLanguage().contains("fr"))return sfr;
		return sen;
	}
	public void sendMessage(String sen, String sfr){
		if(this.isOnline()){
			String message = this.translateString(sen, sfr);
			this.getChat().addInfo(message);
			this.getChat().update();
		}
	}
	public void sendMessage(BaseComponent component){
		if(this.isOnline()){
			this.getChat().addInfo(WrappedChatComponent.fromJson(ComponentSerializer.toString(component)));
			this.getChat().update();
		}
	}
	public void sendActionBar(String messageen, String messagefr){
		if(!this.isOnline())return;
		WrapperPlayServerChat packet = new WrapperPlayServerChat(new PacketContainer(PacketType.Play.Server.CHAT));
		packet.setMessage(WrappedChatComponent.fromText(this.getLanguage().contains("fr") ? messagefr : messageen));
		packet.setPosition(EnumWrappers.ChatType.GAME_INFO);
		packet.sendPacket(this.getPlayer());
    }
	public void sendActionBar(String message){
		this.sendActionBar(message, message);
	}
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if(this.isOnline()){
	        this.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
    }
	public double addRExp(double rexp, RXPSource source) {
		RPlayerXPEvent xpEvent = new RPlayerXPEvent(this, rexp, source);
		Bukkit.getPluginManager().callEvent(xpEvent);
		if(!xpEvent.isCancelled()){
			this.setRExp(this.getRExp()+xpEvent.getXP(), source);
			return xpEvent.getXP();
		}
		return 0;
	}
	public void updateNrj(){
		if(this.isOnline()){
			if(!this.isOp()){
				ItemStack stack = this.getPlayer().getInventory().getItem(8);
				if(stack != null){
					if(!stack.getType().equals(Material.BLACK_STAINED_GLASS_PANE)){
						int slot = this.getPlayer().getInventory().firstEmpty();
						if(slot == -1)this.getPlayer().getWorld().dropItem(this.getPlayer().getLocation(), stack);
						else this.getPlayer().getInventory().setItem(slot, stack);
						ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("nrj");
						item.setItemMeta(meta);
						this.getPlayer().getInventory().setItem(8, item);
					}
				}
				WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot();
				packet.setWindowId(0);
				packet.setSlot(44);
				packet.setSlotData(this.getNrjItem());
				packet.sendPacket(this.getPlayer());
			}
		}
	}
	public ItemStack getNrjItem(){
		ItemStack item = new ItemStack(Material.DIAMOND_HOE,this.getNrj() > 126 ? 1 : (int)this.getNrj());
		ItemMeta meta = item.getItemMeta();
		((Damageable) meta).setDamage((int) (Material.DIAMOND_HOE.getMaxDurability()*(.9353+.06406150*(this.getNrj()/this.getMaxNrj()))));
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§6☇  §e" + (int)this.getNrj() + "§6/" + (int)this.getMaxNrj() + " EP");
		item.setItemMeta(meta);
		return item;
	}
	public boolean isInCombat(){
		return (System.currentTimeMillis() - this.getLastCombat() <= 10000) || this.isInDuel() || this.getKeystroke().length() > 0;//10 sec
	}
	public boolean isAtLeast(Mastery mastery){
		return this.getMastery().getId() >= mastery.getId();
	}
	public int getAbLevel(int index){
		if(index > 8 || index < 1)return 0;
		try {
			Method method = this.getClass().getDeclaredMethod("getAbLevel" + index);
			method.setAccessible(true);
			return (int)method.invoke(this);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void setAbLevel(int index, int level){
		if(index > 8 || index < 1)return;
		try {
			Method method = this.getClass().getDeclaredMethod("setAbLevel" + index, int.class);
			method.setAccessible(true);
			method.invoke(this, level);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public boolean knows(RPlayer rpp){
		if(!rpp.equals(this)){
			GMember gm = GMember.get(this);
			if(gm.hasGuild()){
				GMember gmm = GMember.get(rpp);
				if(gmm.hasGuild()){
					if(gm.getGuild().equals(gmm.getGuild())){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void prechat(String message, ChatType type){
		RPlayer target = null;
		if(message.startsWith("!")){
			type = ChatType.SHOUT;
			message = message.replaceFirst("!", "").trim();
		}else if(message.startsWith(">")){
			message = message.replaceFirst(">", "").trim();
			String[] parts = message.split(" ");
			if(parts.length > 0){
				target = RPlayer.getFromName(parts[0]);
				if(target != null && target.isOnline()){
					message = message.replaceFirst(Pattern.quote(parts[0]), "").trim();
					type = ChatType.PRIVATE;
				}
			}
		}
		RPlayerPreChatMessageEvent event = new RPlayerPreChatMessageEvent(this, target, message, type);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled() && !event.getMessage().isEmpty()){
			final RPlayer eventrp = event.getRPlayer();
			String m = event.getMessage();
			
			if(Smiley.hasSmiley(m) && !eventrp.isInCombat()){
				final Player player = eventrp.getPlayer();
				if(!Smiley.isSmileying(player))eventrp.smileyHelmet = player.getEquipment().getHelmet().clone();
				Smiley.setSmileying(player, true);
				player.getEquipment().setHelmet(Smiley.base("http://textures.minecraft.net/texture/" + Smiley.urls.get(Smiley.smileys.indexOf(Smiley.getSmiley(m)))));
				if(eventrp.smileyTask != null)eventrp.smileyTask.cancel();
				eventrp.smileyTask = new BukkitTask(Core.instance){
					public void run(){
						if(eventrp.smileyHelmet != null)player.getEquipment().setHelmet(eventrp.smileyHelmet);
						else player.getEquipment().setHelmet(new ItemStack(Material.AIR, 1));
						Smiley.setSmileying(player, false);
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(5*20);
			}
			
			if(!event.getMessageType().equals(ChatType.PRIVATE) || !event.getPrivateTarget().equals(eventrp)){
				GMember gm = GMember.get(eventrp);
				new RChatMessage(eventrp, event.getPrivateTarget(), gm, gm.getGuild(), event.getMessageType(), message, eventrp.getPlayer().getEquipment().getItemInMainHand()).send();
			}else event.setCancelled(true);
		}
	}
	
	public TextComponent buildItemMessage(String m){
		TextComponent other = new TextComponent(m.split("  ")[0] + " ");
		
		ItemStack inHand = this.getPlayer().getEquipment().getItemInMainHand();
		
		String color = "";
		String message = m.split("  ")[1];
		String[] partsm = message.split(" ");
		String[] colors = message.split("§");
		if(colors.length > 1)color = "§" + colors[colors.length-1].split("")[0];
		
		for(int i = 0;i < partsm.length;i++){
			if(partsm[i].contains("%item")){
				if(!inHand.getType().equals(Material.AIR)){
				    net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(inHand);
					BaseComponent[] hoverItem;
					HoverEvent hoverEvent;
					if(inHand.hasItemMeta()){
					    net.minecraft.server.v1_13_R2.NBTTagCompound compound = new NBTTagCompound();
					    if(nmsItemStack.hasTag())compound = nmsItemStack.save(compound);
					    hoverItem = new BaseComponent[]{new TextComponent(compound.toString())};
					    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverItem);
					}else{
					    hoverItem = new BaseComponent[]{new TextComponent(nmsItemStack.getName().getString())};
					    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverItem);
					}
					TextComponent item = new TextComponent("§9[" + nmsItemStack.getName() + "§9]");
					item.setHoverEvent(hoverEvent);
					
					other.addExtra(new TextComponent(" " + color));
					other.addExtra(item);
				}
			}else other.addExtra(new TextComponent(" " + color + partsm[i]));
		}
		return other;
	}
	
	public boolean getClickSound() {
		return clickSound;
	}

	public void setClickSound(boolean clickSound) {
		this.clickSound = clickSound;
		this.setModified(true);
	}

	public boolean isOp() {
		if(this.isOnline()){
			return this.getPlayer().isOp() && this.getPlayer().getGameMode().equals(GameMode.CREATIVE);
		}
		return false;
	}

	public List<Quest> getActiveQuests() {
		return this.getLoadedSPlayer().getActiveQuests();
	}

	public void setActiveQuests(List<Quest> activeQuests) {
		this.getLoadedSPlayer().setActiveQuests(activeQuests);
	}

	public List<Quest> getDoneQuests() {
		return this.getLoadedSPlayer().getDoneQuests();
	}

	public void setDoneQuests(List<Quest> doneQuests) {
		this.getLoadedSPlayer().setDoneQuests(doneQuests);
	}

	public List<Quest> getQuestsOfType(ObjectiveType... types){
		List<Quest> quests = new ArrayList<Quest>();
		if(!this.getActiveQuests().isEmpty()){
			for(Quest quest : this.getActiveQuests()){
				if(!quest.getObjectivesByType(types).isEmpty()){
					quests.add(quest);
				}
			}
		}
		return quests;
	}
	
	public boolean hasQuestOfType(ObjectiveType type){
		return !this.getQuestsOfType(type).isEmpty();
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public int getStrength() {
		return this.getLoadedSPlayer().getStrength()+this.getBonus(PiercingType.STRENGTH);
	}

	public void setStrength(int strength) {
		this.getLoadedSPlayer().setStrength(strength);
	}

	public int getEndurance() {
		return this.getLoadedSPlayer().getEndurance()+this.getBonus(PiercingType.ENDURANCE);
	}

	public void setEndurance(int endurance) {
		this.getLoadedSPlayer().setEndurance(endurance);
	}

	public int getAgility() {
		return this.getLoadedSPlayer().getAgility()+this.getBonus(PiercingType.AGILITY);
	}

	public void setAgility(int agility) {
		this.getLoadedSPlayer().setAgility(agility);
	}

	public int getIntelligence() {
		return this.getLoadedSPlayer().getIntelligence()+this.getBonus(PiercingType.INTELLIGENCE);
	}

	public void setIntelligence(int intelligence) {
		this.getLoadedSPlayer().setIntelligence(intelligence);
	}

	public int getPerception() {
		return this.getLoadedSPlayer().getPerception()+this.getBonus(PiercingType.PERCEPTION);
	}

	public void setPerception(int perception) {
		this.getLoadedSPlayer().setPerception(perception);
	}
	
	public void addStrength(int amount){
		this.getLoadedSPlayer().setStrength(this.getLoadedSPlayer().getStrength()+amount);
	}
	
	public void addEndurance(int amount){
		this.getLoadedSPlayer().setEndurance(this.getLoadedSPlayer().getEndurance()+amount);
	}
	
	public void addAgility(int amount){
		this.getLoadedSPlayer().setAgility(this.getLoadedSPlayer().getAgility()+amount);
	}
	
	public void addIntelligence(int amount){
		this.getLoadedSPlayer().setIntelligence(this.getLoadedSPlayer().getIntelligence()+amount);
	}
	
	public void addPerception(int amount){
		this.getLoadedSPlayer().setPerception(this.getLoadedSPlayer().getPerception()+amount);
	}

	
	public int getSkillDistinctionPoints() {
		return this.getLoadedSPlayer().getSkd();
	}

	public void setSkillDistinctionPoints(int skd) {
		this.getLoadedSPlayer().setSkd(skd);
	}
	
	
	public double getAverageMeleeDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.MELEE, false, true),3);
	}
	
	public double getAverageCriticalMeleeDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.MELEE, true, true),3);
	}
	
	public double getAverageRangedDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.RANGE, false, true),3);
	}
	
	public double getAverageCriticalRangedDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.RANGE, true, true),3);
	}
	
	public double getAverageMagicDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.MAGIC, false, true),3);
	}
	
	public double getAverageCriticalMagicDamages(ItemStack item){
		return Utils.round(DamageManager.getDamages(this.getPlayer(), null, item, RDamageCause.MAGIC, true, true),3);
	}
	
	public double getAverageDefense(){
		return Utils.round(DamageManager.getDamageResistance(RDamageCause.MELEE, this.getPlayer(), true)*(1+this.getAdditionalFactor(BuffType.DEFENSE)),3);
	}
	
	public double getDefenseFactor(){
		return this.getEndurance()*Settings.ENDURANCE_FACTOR_DEFENSE+this.getAdditionalFactor(BuffType.DEFENSE);
	}
	
	public double getCriticalStrikeDamagesFactor(){
		return 1+this.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES+this.getAdditionalFactor(BuffType.CRITIC_DAMAGE);
	}
	
	public double getCriticalStrikeChanceFactor(){
		return this.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE+this.getAdditionalFactor(BuffType.CRITIC_CHANCE);
	}
	
	public double getAbilityDamagesFactor(){
		return 1+this.getIntelligence()*Settings.INTELLIGENCE_FACTOR_ABILITY_DAMAGES+this.getAdditionalFactor(BuffType.ABILITY_DAMAGE);
	}
	
	public double getAbilityDefenseFactor(){
		return this.getEndurance()*Settings.ENDURANCE_FACTOR_ABILITY_DEF+this.getAdditionalFactor(BuffType.ABILITY_DEFENSE);
	}
	
	public double getBlockChanceFactor(){
		return this.getPerception()*Settings.PERCEPTION_FACTOR_BLOCK_CHANCE+this.getAdditionalFactor(BuffType.BLOCK_CHANCE);
	}
	
	public double getLootBonusChanceFactor(){
		double luckFactor = 0.0;
		if(this.isOnline()){
			ItemStack item = this.getPlayer().getInventory().getItemInMainHand();
			if(item.hasItemMeta()){
				ItemMeta meta = item.getItemMeta();
				if(meta.hasEnchant(Enchantment.LUCK)){
					luckFactor += meta.getEnchantLevel(Enchantment.LUCK)*Settings.ENCHANTMENT_LUCK_FACTOR;
				}
			}
		}
		return this.getPerception()*Settings.PERCEPTION_FACTOR_LOOT_CHANCE+this.getAdditionalFactor(BuffType.LOOT_BONUS)+luckFactor;
	}
	
	public double getAeroplaneCost(){
		return 40*(1-this.getPerception()*Settings.PERCEPTION_FACTOR_LIFT_COST+this.getAdditionalFactor(BuffType.LIFT_COST));//(this.getPerception()*.04+.32)*Set.getFactor(this.getPlayer(), BuffType.AEROPLANE_SPEED);
	}
	
	public double getAttackSpeedFactor(){
		return 1+this.getAgility()*Settings.AGILITY_FACTOR_ATTACK_SPEED+this.getAdditionalFactor(BuffType.ATTACK_SPEED);
	}
	
	public double getXPFactor(){
		return (Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris")).get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ? 2.0 : 1.0)*(this.isVip() ? 2.0 : 1.0)+this.getAdditionalFactor(BuffType.XP);
	}
	
	public Quest getFollowedQuest() {
		return this.getLoadedSPlayer().getFollowedQuest();
	}

	public void setFollowedQuest(Quest followedQuest) {
		this.getLoadedSPlayer().setFollowedQuest(followedQuest);
	}

	public SPlayer getLoadedSPlayer() {
		return loadedSPlayer;
	}

	public void setLoadedSPlayer(SPlayer loadedSPlayer) {
		this.loadedSPlayer = loadedSPlayer;
	}

	public SPlayer[] getSaves() {
		return saves;
	}

	public void setSaves(SPlayer[] saves) {
		this.saves = saves;
	}

	public int getLastLoadedSPlayerId() {
		return lastLoadedSPlayerId;
	}

	public void setLastLoadedSPlayerId(int lastLoadedSPlayerId) {
		this.lastLoadedSPlayerId = lastLoadedSPlayerId;
		this.setModified(true);
	}

	public void load(final int id){
		final SPlayer sp = this.getSaves()[id];
		this.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 444, true, false), true);
		this.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 140, 444, true, false), true);
		this.getPlayer().setWalkSpeed(0);
		new BukkitTask(Core.instance){

			@Override
			public void run() {
				sendTitle(translateString("§fLoading character...","§fChargement du personnage..."), translateString("§7" + sp.getRClass().getDisplayEn().toUpperCase() + "  |  LEVEL " + sp.getRLevel(),"§7" + sp.getRClass().getDisplayFr().toUpperCase() + "  |  NIVEAU " + sp.getRLevel()), 20, 0, 20);
			}

			@Override
			public void onCancel() {
				if(Smiley.isSmileying(getPlayer())){
					smileyTask.run();
				}
				for(Pet pet : getLoadedSPlayer().getPets()){
					if(pet.isActive()){
						pet.despawn();
					}
				}
				getLoadedSPlayer().setLastLocation(getPlayer().getLocation());
				getLoadedSPlayer().setLastHealth(getPlayer().getHealth());
				getLoadedSPlayer().setLastFoodLevel(getPlayer().getFoodLevel());
				PlayerInventory inventory = getPlayer().getInventory();
				for(int i = 0;i < inventory.getSize();i++){
					getLoadedSPlayer().getLastInventory().put(i, inventory.getItem(i));
				}
				getLoadedSPlayer().getLastInventory().put(101, inventory.getHelmet());
				getLoadedSPlayer().getLastInventory().put(102, inventory.getChestplate());
				getLoadedSPlayer().getLastInventory().put(103, inventory.getLeggings());
				getLoadedSPlayer().getLastInventory().put(104, inventory.getBoots());
				getLoadedSPlayer().setLoaded(false);
				if(getPlayer().isOnline() && !getPlayer().isDead()){
					sendTitle("","",0,0,0);
					inventory.clear();
					inventory.setHelmet(new ItemStack(Material.AIR));
					inventory.setChestplate(new ItemStack(Material.AIR));
					inventory.setLeggings(new ItemStack(Material.AIR));
					inventory.setBoots(new ItemStack(Material.AIR));
					for(int i : sp.getLastInventory().keySet()){
						if(i < inventory.getSize())inventory.setItem(i, sp.getLastInventory().get(i));
					}
					if(sp.getLastInventory().containsKey(101))inventory.setHelmet(sp.getLastInventory().get(101));
					if(sp.getLastInventory().containsKey(102))inventory.setChestplate(sp.getLastInventory().get(102));
					if(sp.getLastInventory().containsKey(103))inventory.setLeggings(sp.getLastInventory().get(103));
					if(sp.getLastInventory().containsKey(104))inventory.setBoots(sp.getLastInventory().get(104));
					if(sp.getLastLocation() != null)getPlayer().teleport(sp.getLastLocation());
					else getPlayer().teleport(getPlayer().getWorld().getSpawnLocation());
					for(Pet pet : sp.getPets()){
						if(pet.isActive()){
							pet.despawn();
						}
					}
					getPlayer().setWalkSpeed(.2F);
					setLastLoadedSPlayerId(id);
					setLoadedSPlayer(sp);
					getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
					getPlayer().setHealth(sp.getLastHealth() <= getMaxHealth() ? sp.getLastHealth() : getMaxHealth());
					getPlayer().setFoodLevel(sp.getLastFoodLevel());
					refreshRLevelDisplay();
					sp.setLoaded(true);
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							me.pmilon.RubidiaQuests.utils.Utils.updateFollowedQuest(getPlayer(), true);
							NameTags.update();
							getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							sendTitle(translateString("§fCharacter loaded!","§fPersonnage chargé !"), translateString("§7Have fun on Rubidia!","§7Bon jeu sur Rubidia !"), 0, 60, 20);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(1);
				}
			}
			
		}.runTaskTimerCancelling(0, 40, 120);
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
		this.setModified(true);
	}

	public String getKeystroke() {
		return keystroke;
	}

	public void setKeystroke(String keystroke) {
		this.keystroke = keystroke;
	}

	public int getKeystrokeTaskId() {
		return keystrokeTaskId;
	}

	public void setKeystrokeTaskId(int keystrokeTaskId) {
		this.keystrokeTaskId = keystrokeTaskId;
	}

	public Long getLastAttack() {
		return lastAttack;
	}

	public void setLastAttack(Long lastAttack) {
		this.lastAttack = lastAttack;
	}

	public double getNextAttackFactor() {
		return nextAttackFactor;
	}

	public void setNextAttackFactor(double nextAttackFactor) {
		this.nextAttackFactor = nextAttackFactor;
	}
	
	public List<Piercing> getPiercings(){
		List<Piercing> piercings = new ArrayList<Piercing>();
		if(this.isOnline()){
			for(ItemStack item : this.getPlayer().getInventory().getArmorContents()){
				if(item != null){
					RItem rItem = new RItem(item);
					if(rItem.isWeapon()){
						Weapon weapon = rItem.getWeapon();
						if(!weapon.getPiercings().isEmpty()){
							for(Piercing piercing : weapon.getPiercings()){
								piercings.add(piercing);
							}
						}
					}
				}
			}
			ItemStack inHand = this.getPlayer().getInventory().getItemInMainHand();
			if(inHand != null){
				RItem rItem = new RItem(inHand);
				if(rItem.isWeapon()){
					Weapon weapon = rItem.getWeapon();
					if(!weapon.getPiercings().isEmpty()){
						for(Piercing piercing : weapon.getPiercings()){
							piercings.add(piercing);
						}
					}
				}
			}
			ItemStack inOffHand = this.getPlayer().getInventory().getItemInOffHand();
			if(inOffHand != null){
				RItem rItem = new RItem(inOffHand);
				if(rItem.isWeapon()){
					Weapon weapon = rItem.getWeapon();
					if(!weapon.getPiercings().isEmpty()){
						for(Piercing piercing : weapon.getPiercings()){
							piercings.add(piercing);
						}
					}
				}
			}
		}
		return piercings;
	}
	
	public int getBonus(PiercingType type){
		int bonus = 0;
		for(Piercing piercing : this.getPiercings()){
			if(piercing.getType().equals(type)){
				bonus += type.getAmount();
			}
		}
		return bonus;
	}

	public boolean isUsingTextures() {
		return usingTextures;
	}

	public void setUsingTextures(boolean usingTextures) {
		this.usingTextures = usingTextures;
		this.setModified(true);
	}
	
	public void updateResourcePack(){
		if(this.isOnline()){
			if(this.isUsingTextures()){
				String version = "1.5.5";
				this.sendMessage("§eInstalling §6§lRubidiaPack§e (v" + version + ")...", "§eInstallation de §6§lRubidiaPack§e (v" + version + ")...");
				this.getPlayer().setResourcePack("http://r.milon.pro/downloads/RubidiaPack" + version + ".zip");
			}else{
				String version = "1.3.5";
				this.sendMessage("§eInstalling §6§lRubidiaPackLight§e (v" + version + ")...", "§eInstallation de §6§lRubidiaPackLight§e (v" + version + ")...");
				this.getPlayer().setResourcePack("http://r.milon.pro/downloads/RubidiaPackLight" + version + ".zip");
			}
		}
		return;
	}

	public long getLastConnectionDate() {
		return lastConnectionDate;
	}

	public void setLastConnectionDate(long lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
		this.setModified(true);
	}

	public List<String> getPendingMessages() {
		return pendingMessages;
	}

	public long getGamingTime() {
		return gamingTime;
	}

	public void setGamingTime(long gamingTime) {
		this.gamingTime = gamingTime;
		this.setModified(true);
	}

	public List<Pet> getPets(){
		return this.getLoadedSPlayer().getPets();
	}

	public int getPendingRubis() {
		return pendingRubis;
	}

	public void setPendingRubis(int pendingRubis) {
		this.pendingRubis = pendingRubis;
	}

	public BukkitTask getMuteTask() {
		return muteTask;
	}

	public void setMuteTask(BukkitTask muteTask) {
		this.muteTask = muteTask;
	}
	
	public void mute(long duration){
		this.muteTask = new BukkitTask(Core.instance){

			@Override
			public void run() {
				unmute(false);
			}

			@Override
			public void onCancel() {
			}
			
		};
		if(duration == 0)this.sendMessage("§eYou have been muted.", "§eVotre langue a été coupée.");
		else{
			this.muteTask.runTaskLater(duration*20);
			duration *= 1000;
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			duration -= TimeUnit.MINUTES.toMillis(minutes);
			long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
			duration -= TimeUnit.SECONDS.toMillis(seconds);
			String time = String.format("%02dm %02ds", minutes, seconds);
			this.sendMessage("§eYou have been muted for §6" + time, "§eVotre langue a été coupée pour §6" + time);
		}
	}
	
	public void unmute(boolean killTask){
		if(killTask)this.muteTask.cancel();
		this.muteTask = null;
		this.sendMessage("§eYou have been unmuted.", "§eVotre langue vous a été rendue.");
	}
	
	public boolean isMuted(){
		return this.muteTask != null;
	}

	public long getBirthDate() {
		return birthDate;
	}
	
	public String getFormattedBirthDate(){
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date(this.getBirthDate()));
	}

	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
		this.setModified(true);
	}

	public Gender getSex() {
		return sex;
	}

	public void setSex(Gender sex) {
		this.sex = sex;
		this.setModified(true);
	}

	public boolean isProfileUpdated() {
		return profileUpdated;
	}

	public void setProfileUpdated(boolean profileUpdated) {
		this.profileUpdated = profileUpdated;
		this.setModified(true);
	}
	
	public void fiance(RPlayer rp){
		if(this.engagementTask != null)this.engagementTask.cancel();
		this.fiance = rp;
		if(this.fiance != null){
			this.engagementTask = new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(isOnline()){
						Core.playAnimEffect(Particle.HEART, getPlayer().getLocation().add(0,.5,0), .2F, .3F, .2F, 0, 4);
					}
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimer(0, 20);
		}
	}

	public boolean marry(){
		if(this.fiance != null){
			if(this.isOnline() && this.fiance.isOnline()){
				if(this.engagementTask != null){
					this.engagementTask.cancel();
					this.engagementTask = null;
				}
				
				if(this.fiance.engagementTask == null){
					DialogManager.setNoDialog(this.getPlayer());
					DialogManager.setNoDialog(this.fiance.getPlayer());

					final FireworkEffect effect = FireworkEffect.builder()
							.flicker(true)
							.trail(true)
							.with(Type.STAR)
							.withColor(Color.FUCHSIA)
							.withColor(Color.PURPLE)
							.withColor(Color.WHITE)
							.withColor(Color.RED)
							.withFade(Color.FUCHSIA)
							.withFade(Color.PURPLE)
							.withFade(Color.SILVER)
							.withFade(Color.RED)
							.build();
					Vector link = this.fiance.getPlayer().getLocation().toVector().subtract(getPlayer().getLocation().toVector());
					final Location location = getPlayer().getLocation().add(link.multiply(.5));
					for(int i = 0;i < 8;i++){
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								double angle = RandomUtils.random.nextDouble()*Math.PI*2;
								Firework f1 = getPlayer().getWorld().spawn(location.clone().add(.4*Math.cos(angle), .25, .4*Math.sin(angle)), Firework.class);
								FireworkMeta fm1 = f1.getFireworkMeta();
								fm1.addEffect(effect);
								fm1.setPower(RandomUtils.random.nextInt(3));
								f1.setFireworkMeta(fm1);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(i*9);
					}
					
					Couple couple = Couples.newDefault(this, this.fiance, System.currentTimeMillis());
					this.setCoupleUUID(couple.getUUID());
					this.fiance.setCoupleUUID(couple.getUUID());
					
					this.sendMessage("§aYou are now married to " + this.fiance.getName() + "! Type /x to open your couple menu.", "§aVous êtes désormais marié à " + this.fiance.getName() + " ! Entrez /x pour ouvrir le menu du couple.");
					this.fiance.sendMessage("§aYou are now married to " + this.getName() + "! Type /x to open your couple menu.", "§aVous êtes désormais marié à " + this.getName() + " ! Entrez /x pour ouvrir le menu du couple.");
					
					this.fiance.fiance = null;
					this.fiance = null;
					
					return true;
				}else this.sendMessage("§eWaiting for §6" + this.fiance.getName() + "§e's answer...", "§eEn attente de la réponse de §6" + this.fiance.getName() + "§e...");
			}
		}
		return false;
	}
	
	public double getAdditionalFactor(BuffType... types){
		double factor = 0;
		if(this.isOnline()){
			List<Set> sets = new ArrayList<Set>();
			for(ItemStack armor : this.getPlayer().getEquipment().getArmorContents()){
				if(armor != null){
					RItem rItem = new RItem(armor);
					if(rItem.isWeapon()){
						Weapon weapon = rItem.getWeapon();
						if(weapon != null){
							if(weapon.isSetItem()){
								Set set = weapon.getSet();
								if(!sets.contains(set)){
									for(Buff buff : weapon.getSet().getActiveBuffs(this.getPlayer())){
										for(BuffType type : types){
											if(buff.getType().equals(type)){
												factor += buff.getFactor();
											}
										}
									}
									sets.add(set);
								}
							}
						}
					}
				}
			}
			
			ItemStack mainHand = this.getPlayer().getInventory().getItemInMainHand();
			if(mainHand != null){
				RItem rItem = new RItem(mainHand);
				if(rItem.isWeapon()){
					Weapon weapon = rItem.getWeapon();
					if(weapon != null){
						if(weapon.isSetItem()){
							Set set = weapon.getSet();
							if(!sets.contains(set)){
								for(Buff buff : weapon.getSet().getActiveBuffs(this.getPlayer())){
									for(BuffType type : types){
										if(buff.getType().equals(type)){
											factor += buff.getFactor();
										}
									}
								}
								sets.add(set);
							}
						}
					}
				}
			}
			
			ItemStack offHand = this.getPlayer().getInventory().getItemInOffHand();
			if(offHand != null){
				RItem rItem = new RItem(offHand);
				if(rItem.isWeapon()){
					Weapon weapon = rItem.getWeapon();
					if(weapon != null){
						if(weapon.isSetItem()){
							Set set = weapon.getSet();
							if(!sets.contains(set)){
								for(Buff buff : weapon.getSet().getActiveBuffs(this.getPlayer())){
									for(BuffType type : types){
										if(buff.getType().equals(type)){
											factor += buff.getFactor();
										}
									}
								}
								sets.add(set);
							}
						}
					}
				}
			}
			
			Couple couple = this.getCouple();
			if(couple != null){
				for(Buff buff : couple.getAvailableBuffs()){
					for(BuffType type : types){
						if(buff.getType().equals(type)){
							factor += buff.getFactor();
						}
					}
				}
			}
			
			for(RBooster booster : this.getActiveRBoosters()){
				for(BuffType type : types){
					if(booster.getType().equals(type)){
						factor += booster.getFactor();
					}
				}
			}
		}
		return factor;
	}

	public String getCoupleUUID() {
		return coupleUUID;
	}

	public void setCoupleUUID(String coupleUUID) {
		this.coupleUUID = coupleUUID;
		this.setModified(true);
	}
	
	public Couple getCouple(){
		return Couples.get(this.getCoupleUUID());
	}
	
	public RPlayer getCompanion(){
		Couple couple = this.getCouple();
		if(couple != null){
			return couple.getCompanion(this);
		}
		return null;
	}

	public long getLastDivorce() {
		return lastDivorce;
	}

	public void setLastDivorce(long lastDivorce) {
		this.lastDivorce = lastDivorce;
		this.setModified(true);
	}

	public void removeModifier(Attribute attribute, String name) {
		if(this.isOnline()){
			if(this.attributeModifiers.containsKey(name)){
				for(AttributeModifier modifier : this.getPlayer().getAttribute(attribute).getModifiers()){
					if(modifier.getName().equals(name)){
						this.getPlayer().getAttribute(attribute).removeModifier(modifier);
						break;
					}
				}
				this.attributeModifiers.remove(name);
			}
		}
	}
	
	public void addModifier(Attribute attribute, AttributeModifier modifier){
		if(this.isOnline()){
			if(this.attributeModifiers.containsKey(modifier.getName())){
				AttributeModifier current = this.attributeModifiers.get(modifier.getName());
				if(Math.abs(current.getAmount()-modifier.getAmount()) >= .0004){
					for(AttributeModifier modif : this.getPlayer().getAttribute(attribute).getModifiers()){
						if(modif.getName().equals(modifier.getName())){
							current = modif;
							break;
						}
					}
					this.getPlayer().getAttribute(attribute).removeModifier(current);
					this.getPlayer().getAttribute(attribute).addModifier(modifier);
				}
			}else{
				this.getPlayer().getAttribute(attribute).addModifier(modifier);
			}
			this.attributeModifiers.put(modifier.getName(), modifier);
		}
	}

	public RChat getChat() {
		if(this.isOnline() && chat == null){
			chat = new RChat(this);
		}
		return chat;
	}

	public void setChat(RChat chat) {
		this.chat = chat;
	}

	public int getChatboxHeight() {
		return chatboxHeight;
	}

	public void setChatboxHeight(int chatboxHeight) {
		this.chatboxHeight = chatboxHeight;
		if(this.chatboxHeight > RChatUtils.MAX_CHAT_HEIGHT)this.chatboxHeight = RChatUtils.MAX_CHAT_HEIGHT;
		if(this.chatboxHeight < RChatUtils.MIN_CHAT_HEIGHT)this.chatboxHeight = RChatUtils.MIN_CHAT_HEIGHT;
		this.setModified(true);
	}

	public int getChatboxWidth() {
		return chatboxWidth;
	}

	public void setChatboxWidth(int chatboxWidth) {
		this.chatboxWidth = chatboxWidth;
		if(this.chatboxWidth > RChatUtils.MAX_CHAT_WIDTH)this.chatboxWidth = RChatUtils.MAX_CHAT_WIDTH;
		if(this.chatboxWidth < RChatUtils.MIN_CHAT_WIDTH)this.chatboxWidth = RChatUtils.MIN_CHAT_WIDTH;
		this.setModified(true);
	}

	public int getChatHeight() {
		return chatHeight;
	}

	public void setChatHeight(int chatHeight) {
		this.chatHeight = chatHeight;
		if(this.chatHeight > this.chatboxHeight-5)this.chatHeight = this.chatboxHeight-5;
		if(this.chatHeight < 0)this.chatHeight = 0;
		this.setModified(true);
	}

	public boolean isUsingChat() {
		return usingChat;
	}

	public void setUsingChat(boolean usingChat) {
		this.usingChat = usingChat;
		this.setModified(true);
	}

	public boolean isVanished() {
		return vanished;
	}

	public void setVanished(boolean vanished) {
		this.vanished = vanished;
		if(vanished){
			for(RPlayer rp : RPlayer.getOnlines()){
				if(!rp.isOp() && !rp.equals(this))rp.getPlayer().hidePlayer(Core.instance, this.getPlayer());
				rp.getChat().addInfo("§6[-] §e" + this.getName() + rp.translateString(" left the game", " vient de se déconnecter"));
				rp.getChat().update();
			}
		}else{
			for(RPlayer rp : RPlayer.getOnlines()){
				if(!rp.isOp() && !rp.equals(this))rp.getPlayer().showPlayer(Core.instance, this.getPlayer());
				rp.getChat().addInfo("§6[+] §e" + this.getName() + rp.translateString(" joined the game", " vient de se connecter"));
				rp.getChat().update();
			}
		}
	}

	public PlayerShop getShop() {
		return shop;
	}

	public void setShop(PlayerShop shop) {
		this.shop = shop;
	}

	public QuestHelpRunnable getQuestHelpTask() {
		return questHelpTask;
	}

	public void setQuestHelpTask(QuestHelpRunnable questHelpTask) {
		this.questHelpTask = questHelpTask;
	}

	public Long getLastCombat() {
		return lastCombat;
	}

	public void setLastCombat(Long lastCombat) {
		this.lastCombat = lastCombat;
	}

	public BukkitTask getResurrectionTask() {
		return resurrectionTask;
	}

	public void setResurrectionTask(BukkitTask resurrectionTask) {
		this.resurrectionTask = resurrectionTask;
	}

	public void requestDuel(RPlayer rp, boolean competitive){
		RPlayerRequestDuelEvent event = new RPlayerRequestDuelEvent(this, rp);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			RPlayer rp1 = event.getRPlayer();
			RPlayer rp2 = event.getOpponent();
			if(!rp1.isInDuel()){
				if(rp2.isInDuel()){
					rp1.sendMessage("§4" + rp2.getName() + " §cis already in duel against §4" + rp2.getDuelOpponent().getName() + "§c!", "§4" + rp2.getName() + " §cest déjà en duel contre §4" + rp2.getDuelOpponent().getName() + " §c!");
				}else{
					RDuel duel = rp1.getRequestedDuelTo(rp2);
					if(duel != null){
						duel.cancelRequest();
					}else{
						duel = rp2.getRequestedDuelTo(rp1);
						if(duel != null){
							duel.start();
						}else{
							duel = new RDuel(rp1,rp2,competitive);
							duel.request();
						}
					}
				}
			}
		}
	}
	
	public RDuel getRequestedDuelTo(RPlayer rp){
		for(RDuel duel : this.getDuels()){
			if(duel.getChallenged().equals(rp)){
				return duel;
			}
		}
		return null;
	}
	
	public RDuel getCurrentDuel(){
		return this.currentDuel;
	}
	
	public void setCurrentDuel(RDuel duel){
		this.currentDuel = duel;
	}

	public List<RDuel> getDuels(){
		return this.duels;
	}
	
	public Long getLastCompetitiveDuelDateAgainst(RPlayer rp){
		if(this.getLastCompetitiveDuelDates().containsKey(rp.getUniqueId())){
			return this.getLastCompetitiveDuelDates().get(rp.getUniqueId());
		}
		return 0L;
	}

	public HashMap<String,Long> getLastCompetitiveDuelDates() {
		return lastCompetitiveDuelDates;
	}

	
	public List<RBooster> getActiveRBoosters() {
		return activeRBoosters;
	}
	
	public boolean hasActiveBooster(RBoosterType type){
		return this.getActiveBooster(type) != null;
	}
	
	public RBooster getActiveBooster(RBoosterType type){
		for(RBooster booster : this.getActiveRBoosters()){
			if(booster.getType().equals(type.getType())){
				return booster;
			}
		}
		return null;
	}

	
	public boolean isUsingCycle() {
		return usingCycle;
	}

	
	public void setUsingCycle(boolean usingCycle) {
		this.usingCycle = usingCycle;
		this.setModified(true);
	}

	public HashMap<String, BukkitTask> getReloadingWeapons() {
		return reloadingWeapons;
	}
	
	public void reloadWeapon(final Weapon weapon){
		if(this.getReloadingWeapons().containsKey(weapon.getUUID())){
			this.getReloadingWeapons().get(weapon.getUUID()).run();
		}else{
			final long stepDelay = (long) Math.round(20.0/(3*weapon.getAttackSpeed()));
			this.getReloadingWeapons().put(weapon.getUUID(), new BukkitTask(Core.instance){
				boolean slowActive = false;

				@Override
				public void run() {
					if(isOnline()){
						this.slowActive = Utils.addSafeBuff(getPlayer(), new PotionEffect(PotionEffectType.SLOW, (int) stepDelay, 5, true, false));
						getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, .25F, .4F);
						getReloadingWeapons().put(weapon.getUUID(), new BukkitTask(Core.instance){
							boolean slowActive = false;

							@Override
							public void run() {
								if(isOnline()){
									this.slowActive = Utils.addSafeBuff(getPlayer(), new PotionEffect(PotionEffectType.SLOW, (int) stepDelay, 4, true, false));
									getReloadingWeapons().put(weapon.getUUID(), new BukkitTask(Core.instance){
										boolean slowActive = false;

										@Override
										public void run() {
											if(isOnline()){
												this.slowActive = Utils.addSafeBuff(getPlayer(), new PotionEffect(PotionEffectType.SLOW, (int) stepDelay, 2, true, false));
												getReloadingWeapons().put(weapon.getUUID(), new BukkitTask(Core.instance){
													boolean slowActive = false;

													@Override
													public void run() {
														if(isOnline()){
															getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, .25F, .6F);
															getReloadingWeapons().remove(weapon.getUUID());
														}
													}

													@Override
													public void onCancel() {
														if(this.slowActive){
															getPlayer().removePotionEffect(PotionEffectType.SLOW);
														}
													}
													
												}.runTaskLater(stepDelay));
											}
										}

										@Override
										public void onCancel() {
											if(this.slowActive){
												getPlayer().removePotionEffect(PotionEffectType.SLOW);
											}
										}
										
									}.runTaskLater(stepDelay));
								}
							}

							@Override
							public void onCancel() {
								if(this.slowActive){
									getPlayer().removePotionEffect(PotionEffectType.SLOW);
								}
							}
							
						}.runTaskLater(stepDelay));
					}
				}

				@Override
				public void onCancel() {
					if(this.slowActive){
						getPlayer().removePotionEffect(PotionEffectType.SLOW);
					}
				}
				
			}.runTaskLater(0));
		}
	}
	
	public boolean isActiveAbility(int index){
		return this.activeAbilities[index-1];
	}
	
	public void setActiveAbility(int index, boolean flag){
		this.activeAbilities[index-1] = flag;
	}

	public boolean isPublicData() {
		return publicData;
	}

	public void setPublicData(boolean publicData) {
		this.publicData = publicData;
		this.setModified(true);
	}

	public void setBank(int bank) {
		this.getLoadedSPlayer().setBank(bank);
		this.setModified(true);
	}

	public int getMaxBankAmount() {
		if(this.isVip())return -1;
		return 3000;
	}
}
