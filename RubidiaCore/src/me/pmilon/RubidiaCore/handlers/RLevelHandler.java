package me.pmilon.RubidiaCore.handlers;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerClassChangeEvent;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RPlayerLevelChangeEvent;
import me.pmilon.RubidiaCore.events.RPlayerXPEvent;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.events.RXPSourceType;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.tags.NameTags;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.RandomUtils;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class RLevelHandler implements Listener{

	private final Core plugin;
	public RLevelHandler(Core core) {
		this.plugin = core;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		final Player p = e.getPlayer();
		if(Core.rcoll.contains(e.getPlayer())){
			final RPlayer rp = RPlayer.get(p);
			new BukkitTask(this.plugin){
				public void run(){
					rp.refreshRLevelDisplay();
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(5);
		}
	}

	@EventHandler
	public void onExperienceAcquire(PlayerExpChangeEvent e){
		e.setAmount(0);
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent e){
		e.setExpToDrop(0);
	}
	
	@EventHandler
	public void onRLevelChange(RPlayerLevelChangeEvent e){
		RPlayer rp = e.getRPlayer();
		if(e.getNewRLevel() > Settings.LEVEL_MAX){
			e.setNewRLevel(Settings.LEVEL_MAX);
			rp.sendMessage("§cVous avez atteint notre limite de niveau. Nous admirons votre détermination et votre activité sur notre serveur. Merci de nous soutenir !");
			return;
		}
		int newLevel = e.getNewRLevel();
		int oldLevel = e.getOldRLevel();
		int diff = newLevel - oldLevel;
		int skp = 0;
		int skd = 0;
		if(diff > 0){
			for(int i = 1;i <= diff;i++){
				if(oldLevel+i <= Mastery.ADVENTURER.getLevel()){
					skp++;
					skd++;
				}
				skp++;
				skd++;
			}
		}
		rp.setSkillPoints(rp.getSkillPoints()+skp);
		rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()+skd);
		if(newLevel >= Mastery.ASPIRANT.getLevel() && rp.getMastery().equals(Mastery.ADVENTURER))rp.setMastery(Mastery.ASPIRANT);
		
		if(rp.isOnline()){
			if(!e.getSource().getType().equals(RXPSourceType.SPLAYER_UPDATE)){
				final Player p = rp.getPlayer();
				if(e.getSource().getType().equals(RXPSourceType.COMMAND))rp.setRExp(LevelUtils.getRLevelTotalExp(newLevel)*(p.getExp()), new RXPSource(RXPSourceType.ADJUST, null, null));
				
				rp.sendTitle(("§6§lNIVEAU SUPERIEUR !"), ("§eVous gagnez " + skp + " SKP & " + skd + " DP !"), 0, 100, 20);
				LevelUtils.firework(p.getLocation());
				
				if(newLevel >= Mastery.ADVENTURER.getLevel() && rp.getMastery().equals(Mastery.VAGRANT)){
					rp.sendMessage("§aIl est temps pour vous de devenir un aventurier !");
					rp.sendMessage("§aDemandez à un §lMENTOR§a de choisir une classe !");
				}
				if(newLevel >= Mastery.MASTER.getLevel() && rp.getMastery().equals(Mastery.ASPIRANT)){
					rp.sendMessage("§aIl est temps pour vous de devenir un " + rp.getEvolutionClassName() + " §a!");
					rp.sendMessage("§aDemandez à un §lPRECEPTEUR§a d'évoluer, et ainsi apprendre 2 nouvelles compétences !");
				}
				if(newLevel >= Mastery.HERO.getLevel() && rp.getMastery().equals(Mastery.MASTER)){
					rp.sendMessage("§aIl est temps pour vous de devenir un " + rp.getEvolutionClassName() + " §a!");
					rp.sendMessage("§aDemandez à un §lPREFET§a d'évoluer, et ainsi apprendre 2 nouvelles compétences et obtenir un §oParchemin de redistribution §a!");
				}
				if(newLevel >= Settings.LEVEL_JOB && rp.getRJob().equals(RJob.JOBLESS)){
					rp.sendMessage("§aVous pouvez d'ores et déjà obtenir un job §a!");
					rp.sendMessage("§aDemandez-en un §lDOYEN§a pour gagner de l'argent facilement §a!");
				}
				
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
				
				if(e.getOldRLevel() <= 5 && e.getNewRLevel() > 5){
					rp.sendMessage("§6Désormais, vous perdrez 33% de votre inventaire à votre mort !");
				}
			}
		}else if(e.getSource().getType().equals(RXPSourceType.COMMAND) && diff < 0)rp.setRExp(0, new RXPSource(RXPSourceType.ADJUST, null, null));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRPlayerDeath(RPlayerDeathEvent event){
		if(!event.isCancelled()){
			if(event.getRPlayer().getRLevel() <= 5){
				event.setKeepInventory(true);
				event.getRPlayer().sendMessage("§6You didn't lose your inventory because you're only level §6" + event.getRPlayer().getRLevel() + "§e!", "§eVous n'avez pas perdu votre inventaire car vous n'êtes que niveau §6" + event.getRPlayer().getRLevel() + " §e!");
			}else{
				List<Integer> slots = new ArrayList<Integer>();
				for(int i = 0;i < event.getInventoryDrops().length;i++){
					ItemStack item = event.getInventoryDrops()[i];
					if(item != null){
						if(!item.getType().equals(Material.AIR)){
							slots.add(i);
						}
					}
				}
				for(int i = 0;i < event.getArmorDrops().length;i++){
					ItemStack item = event.getArmorDrops()[i];
					if(item != null){
						if(!item.getType().equals(Material.AIR)){
							slots.add(37+i);
						}
					}
				}
				if(slots.size() > 0){
					ItemStack[] inventory = new ItemStack[event.getInventoryDrops().length];
					ItemStack[] armor = new ItemStack[event.getArmorDrops().length];
					List<Integer> nums = new ArrayList<Integer>();
					int slot = slots.get(RandomUtils.random.nextInt(slots.size()));
					for(int i = 0;i < slots.size()*.34;i++){
						while(nums.contains(slot)){
							slot = slots.get(RandomUtils.random.nextInt(slots.size()));
						}
						nums.add(slot);
						ItemStack item;
						if(slot < 37)item = event.getInventoryDrops()[slot];
						else item = event.getArmorDrops()[40-slot];
						if(item != null){
							if(item.hasItemMeta()){
								ItemMeta meta = item.getItemMeta();
								if(meta.hasEnchant(REnchantment.SOUL_BIND)){
									int level = meta.getEnchantLevel(REnchantment.SOUL_BIND);
									if(RandomUtils.random.nextInt(100) < level*20)continue;//keeping it
								}
							}
						}
						if(slot < 37)inventory[slot] = item;
						else armor[40-slot] = item;//losing it
					}
					event.setInventoryDrops(inventory);
					event.setArmorDrops(armor);
				}
			}
		}
	}
	
	@EventHandler
	public void onClassChange(RPlayerClassChangeEvent event){
		RPlayer rp = event.getRPlayer();
		if(event.getNewRClass().equals(RClass.VAGRANT) && !rp.getMastery().equals(Mastery.HERO) && !rp.getMastery().equals(Mastery.MASTER)){
			rp.setMastery(Mastery.VAGRANT);
		}else if(rp.getRLevel() >= Mastery.ASPIRANT.getLevel() && rp.getMastery().equals(Mastery.VAGRANT)){
			rp.setMastery(Mastery.ASPIRANT);
		}else if(rp.getRLevel() >= Mastery.ADVENTURER.getLevel() && rp.getMastery().equals(Mastery.VAGRANT)){
			rp.setMastery(Mastery.ADVENTURER);
		}
		
		new BukkitTask(Core.instance){
			public void run(){
				NameTags.update();
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(1);
	}
	
	@EventHandler
	public void onRXP(RPlayerXPEvent e){
		RPlayer rp = e.getRPlayer();
		e.setXP(e.getXP()*rp.getXPFactor());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onOreBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(!e.isCancelled()){
			if(player != null){
				Block block = e.getBlock();
				Material type = block.getType();
				if(type.toString().contains("ORE")){
					if(!player.getEquipment().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
						RPlayer rp = RPlayer.get(player);
						rp.addRExp(LevelUtils.getRExpForBlock(type), new RXPSource(RXPSourceType.BLOCK, block, null));
					}
				}
			}
		}
	}

	@EventHandler
	public void onMobSpawning(CreatureSpawnEvent event){
		if(event.getSpawnReason().equals(SpawnReason.SPAWNER)){
			event.getEntity().setMetadata("SpawnReason", new FixedMetadataValue(Core.instance, "SPAWNER"));
		}
	}
}
