package me.pmilon.RubidiaCore.abilities;

import java.util.Arrays;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbilitiesListener implements Listener{

	public AbilitiesListener(){
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				if(!rp.getRClass().equals(RClass.VAGRANT) && event.getItem() != null){
					String keystroke = rp.registerAbilityClick(event);
					
					if(!keystroke.isEmpty()){
						for(RAbility ability : RAbility.getAvailable(rp)){
							if(!ability.isPassive()){
								String seq = ability.getSequence();
								String[] part = seq.split(",");
								boolean active = keystroke.contains(part[0]);
								if(part.length > 1){
									if(part[1].contains("!SN"))active = active && !player.isSneaking();
									else if(part[1].contains("SN"))active = active && player.isSneaking();
									
									if(part[1].contains("!SP"))active = active && !player.isSprinting();
									else if(part[1].contains("SP"))active = active && player.isSprinting();
								}
								
								if(active){
									ability.execute(rp);
									rp.setKeystroke("");
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		for(int i = 1;i < 8;i++){
			p.setMetadata("activeskill" + i, new FixedMetadataValue(Core.instance, false));
		}
		
		if(rp != null){
			if(rp.getRClass().equals(RClass.MAGE)){
				p.setMetadata("mageAttack", new FixedMetadataValue(Core.instance, false));
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		if(rp.getLoadedSPlayer() != null){
			if(rp.getRClass().equals(RClass.MAGE)){
				if(rp.isActiveAbility(7)){
					for(Entity entity : p.getWorld().getEntities()){
						if(entity.hasMetadata("Minion")){
							if(entity.getMetadata("Minion").get(0).asString().equals(p.getName())){
								entity.remove();
					            Core.playAnimEffect(Particle.CLOUD, entity.getLocation(), .5F, .5F, .5F, .1F, 23);
					            Core.playAnimEffect(Particle.LAVA, entity.getLocation(), .5F, .5F, .5F, .1F, 8);
					            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, .8F, 1);
								rp.setActiveAbility(7, false);
							}
						}
					}
				}
			}else if(rp.getRClass().equals(RClass.ASSASSIN)){
				if(rp.isActiveAbility(7)){
					if(p.getGameMode().equals(GameMode.SPECTATOR)){
						p.setGameMode(GameMode.valueOf(p.getMetadata("AssassinGamemode").get(0).asString()));
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Inventory inv = e.getClickedInventory();
		HumanEntity human = e.getWhoClicked();
		
		if(inv != null){
			if(human instanceof Player){
				Player player = (Player)human;
				RPlayer rp = RPlayer.get(player);
				if(rp.getRClass().equals(RClass.ASSASSIN) && rp.isActiveAbility(2)){
					if(e.getSlotType().equals(SlotType.ARMOR)){
						if(e.getCurrentItem().getType().equals(Material.PUMPKIN)){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAttack(RubidiaEntityDamageEvent event){
		if(!event.isCancelled()){
			LivingEntity damager = event.getDamager();
			LivingEntity target = (LivingEntity) event.getEntity();
			if(event.getProjectile() != null){
				if(damager instanceof Player){
					Player player = (Player)damager;
					RPlayer rp = RPlayer.get(player);
					Projectile projectile = event.getProjectile();
					if(projectile instanceof Arrow){
						Arrow arrow = (Arrow) event.getProjectile();
						if(rp.getRClass().equals(RClass.RANGER)){
							if(arrow.hasMetadata("snipershot")){
								arrow.remove();
								event.setDamages(RAbility.RANGER_5.getAbility().getDamages(rp));
							}else if(arrow.hasMetadata("aimingArrow")){
								arrow.remove();
								arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, .5F);
								event.setDamages(RAbility.RANGER_7.getAbility().getDamages(rp));
								BukkitTask.tasks.get(arrow.getMetadata("aimingArrow").get(0).asInt()).cancel();
							}else if(player.hasMetadata("legendaryCharged")){
								arrow.remove();
								RAbility.ASSASSIN_8.getAbility().animate(rp, target);
							}else if(player.hasMetadata("explosiveArrow")){
								BukkitTask task = BukkitTask.tasks.get(player.getMetadata("explosiveArrow").get(0).asInt());
								if(task != null) {
									task.cancel();
								}
							}
							if(rp.getAbilityLevel(3) > 0){
								RAbility.RANGER_3.getAbility().animate(rp, target);
							}
							if(rp.getAbilityLevel(4) > 0){
								RAbility.RANGER_4.getAbility().animate(rp, target);
							}
						}
					}else if(projectile instanceof Snowball){
						if(rp.getRClass().equals(RClass.MAGE)){
							if(rp.isActiveAbility(5)){
								event.setCancelled(true);
								RAbility.MAGE_5.getAbility().damage(rp, Arrays.asList(target));
							}
						}
					}
				}
			}else{
				LivingEntity damaged = (LivingEntity) event.getEntity();
				if(damager instanceof Player){
					if(damaged instanceof Player){
						if(!(((Player) damaged).canSee((Player) damager))){
							event.setDamages(event.getDamages()*.25);
						}
					}
					if(damaged.hasMetadata("Minion")){
						if(damaged.getMetadata("Minion").get(0).asString().equals(damager.getName()))event.setCancelled(true);
					}
				}else if(damager instanceof Zombie){
					if(damager.hasMetadata("Minion")){
						if(damaged.hasMetadata("Minion"))event.setCancelled(true);
						else event.setDamager(Bukkit.getPlayer(damager.getMetadata("Minion").get(0).asString()));//to update HealthBar and set appropriate damageSource
					}
				}
			}
		}
	}

	@EventHandler
	public void onExplode(ExplosionPrimeEvent event){
		Entity entity = event.getEntity();
		if(entity.hasMetadata("mageMeteor")){
			Player player = Bukkit.getPlayer(UUID.fromString(entity.getMetadata("mageMeteor").get(0).asString()));
			RPlayer rp = RPlayer.get(player);
			RAbility.MAGE_1.getAbility().damage(rp, Core.toDamageableLivingEntityList(player, entity.getNearbyEntities(2.5, 2.5, 2.5), RDamageCause.ABILITY));
		}
	}
}
