package me.pmilon.RubidiaCore.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ItemMessage;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.entities.pathfinders.PathfinderGoalMinionAttack;
import me.pmilon.RubidiaCore.entities.pathfinders.PathfinderGoalMinionTarget;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.mage.FakeLightning;
import me.pmilon.RubidiaCore.mage.MageMeteor;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerWorldBorder;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Locations;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.VectorUtils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R2.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_13_R2.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Abilities {

	private static Plugin plugin;
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static void setPlugin(Plugin plugin) {
		Abilities.plugin = plugin;
	}
	
	public static void doASSASSIN1(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(1, true);
		LivingEntity entity = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 4.0);
		if(entity != null){
			Location edloc = entity.getLocation();
			if(edloc.distanceSquared(player.getLocation()) < 33){
				rp.addVigor(-neednrj);
				double xedloc = edloc.getX();
				double yedloc = edloc.getY();
				double zedloc = edloc.getZ();
				Location ploc = player.getLocation();
				double xploc = ploc.getX();
				double zploc = ploc.getZ();
				Vector v = new Vector((xedloc-xploc)*1.4, 0, (zedloc-zploc)*1.4);
				double xv = xploc + v.getX();
				double yv = yedloc + v.getY();
				double zv = zploc + v.getZ();
				float yaw = player.getEyeLocation().getYaw();
				if(yaw > 0){
					yaw = yaw-180;
				}else{
					yaw = yaw+180;
				}
				player.getWorld().playEffect(player.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .25F);
				Location finalloc = Locations.getSafeLocation(new Location(player.getWorld(), xv, yv, zv, yaw, 0.0f));
				TeleportHandler.teleport(player, finalloc);
				player.getWorld().playEffect(player.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);

				List<Entity> wanear = player.getNearbyEntities(2, 2, 2);
				for(LivingEntity enear : Core.toDamageableLivingEntityList(player, wanear, RDamageCause.ABILITY)){
					DamageManager.damage(enear, player, damages, RDamageCause.ABILITY);
				}
			}else{
				rp.sendMessage("§cTarget is too far!","§cLa cible est trop loin !");
			}
		}else{
			rp.sendMessage("§cThere is nobody in front of you!","§cIl n'y a personne en face de vous !");
		}
		rp.setActiveAbility(1, false);
	}
	public static void doASSASSIN2(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(2, true);
		rp.addVigor(-neednrj);
		Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .1F, 100);
		Core.playAnimEffect(Particle.CLOUD, player.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
		if(rp.isVanished())rp.setVanished(false);
		for(Player player : Bukkit.getOnlinePlayers()){
			player.hidePlayer(Core.instance, player);
		}
		final ItemStack helmet = player.getEquipment().getHelmet();
		if(helmet != null){
			ItemStack pumpkin = helmet.clone();
			pumpkin.setType(Material.PUMPKIN);
			player.getEquipment().setHelmet(pumpkin);
		}else player.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
		rp.sendMessage("§7Viouf! Nobody's seeing you!", "§7Viouf! Plus personne ne vous voit !");
		new BukkitTask(Core.instance){
			public void run(){
				rp.sendMessage("§cYour cloak begin to disappear!", "§cVotre camouflage commence à disparaître !");
				new BukkitTask(Core.instance){
					public void run(){
						Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .1F, 100);
						Core.playAnimEffect(Particle.CLOUD, player.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
						for(Player player : Bukkit.getOnlinePlayers()){
							player.showPlayer(Core.instance, player);
						}
						if(helmet != null)player.getEquipment().setHelmet(helmet);
						else player.getEquipment().setHelmet(new ItemStack(Material.AIR, 1));
						rp.sendMessage("§4Your cloak has disappeared!", "§4Votre camouflage a disparu !");
						rp.setActiveAbility(2, false);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(70);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater((long) (damages*20));
	}
	public static double doASSASSIN3(final Player player){
		RPlayer rplayer = RPlayer.get(player);
		Ability ability = AbilitiesAPI.getAbility(RClass.ASSASSIN, 3);
		return (rp.getAbilityLevel(3)*ability.getDamagesPerLevel()+ability.getDamagesMin())/100.0D-1;
	}
	public static void doASSASSIN4(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(4, true);
		rp.addVigor(-neednrj);
		Location eyes = player.getEyeLocation().getDirection().normalize().toLocation(player.getWorld());
		double x = eyes.getX()*1.65;
		double z = eyes.getZ()*1.65;
		player.getWorld().createExplosion(player.getLocation(), 0);
		List<Entity> near = player.getNearbyEntities(3, 3, 3);
		for(LivingEntity e : Core.toDamageableLivingEntityList(player, near, RDamageCause.ABILITY)){
			DamageManager.damage(e, player, damages, RDamageCause.ABILITY);
		}
		player.setVelocity(new Vector(-x*rp.getAbLevel(4)*.21, rp.getAbilityLevel(4)*0.07+.28, -z*rp.getAbLevel(4)*.21));
		player.setFallDistance(-100);
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
	        	if(player.isDead()){
	        		this.cancel();
	        	}else{
	                if (((LivingEntity)player).isOnGround()){
						player.setFallDistance((float) 0.0);
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2, true, false), true);
						this.cancel();
		            }
	        	}
			}

			@Override
			public void onCancel() {
				rp.setActiveAbility(4, false);
			}
			
		}.runTaskTimer(25, 2);
	}
	public static void doASSASSIN5(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(5, true);
		int range = 9;
		final LivingEntity target = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
		if(target != null){
			if(target.getLocation().distanceSquared(player.getLocation()) <= range*range){
				rp.addVigor(-neednrj);
				final AttributeModifier modifier = new AttributeModifier("RubidiaAssassinTrap", -10000.0, Operation.ADD_NUMBER);
				final Player player = target instanceof Player ? (Player)target : null;
				if(player != null){
					player.setWalkSpeed(0);
				}else{
					target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
				}
				target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false), true);
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						Block block = target.getLocation().subtract(0,1,0).getBlock();
						Core.playAnimEffect(Particle.SPELL_WITCH, target.getLocation(), .3F, .1F, .3F, .1F, 4);
						Core.playAnimEffect(Particle.SPELL_INSTANT, target.getLocation(), .1F, .1F, .1F, .1F, 1);
						Core.playAnimEffect(Particle.BLOCK_CRACK, target.getLocation(), .3F, .1F, .3F, .1F, 4, block.getBlockData());
					}

					@Override
					public void onCancel() {
						if(player != null){
							player.setWalkSpeed(.2F);
						}else{
							target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
						}
						target.removePotionEffect(PotionEffectType.JUMP);
						rp.setActiveAbility(5, false);
					}
					
				}.runTaskTimerCancelling(0, 1, (long) damages*20);
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(5, false);
			}
		}else{
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(5, false);
		}
	}
	public static void doASSASSIN6(final Player player, final RPlayer rp, final double damages, final double neednrj){
		if(rp.isActiveAbility(6)){
			BukkitTask.tasks.get(player.getMetadata("assassinTask6").get(0).asInt()).cancel();
		}else{
			rp.setActiveAbility(6, true);
			if(rp.hasVigor(neednrj)){
				int taskId = new BukkitTask(Abilities.getPlugin()){
					int step = 0;

					@Override
					public void run() {
						Core.playAnimEffect(Particle.CLOUD, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 50);
						Core.playAnimEffect(Particle.REDSTONE, player.getLocation().add(0,1,0), 1, 1, 1, 0, 42);
						Core.playAnimEffect(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 22);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
						
						if(step >= 5){
							if(rp.hasVigor(neednrj)){
								for(LivingEntity e : Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY)){
									DamageManager.damage(e, player, damages*.25, RDamageCause.ABILITY);
								}
								rp.addVigor(-neednrj);
								step = 0;
							}else{
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
								ItemMessage.sendMessage(player, ("§cPas assez de vigueur !"), 40);
								this.cancel();
							}
						}
						step ++;
					}

					@Override
					public void onCancel() {
						rp.setActiveAbility(6, false);
					}
					
				}.runTaskTimer(0, 1).getTaskId();
				player.setMetadata("assassinTask6", new FixedMetadataValue(Abilities.getPlugin(), taskId));
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				ItemMessage.sendMessage(player, ("§cPas assez de vigueur !"), 40);
				rp.setActiveAbility(6, false);
			}
		}
	}
	public static void doASSASSIN7(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(7, true);
		double range = 7;
		
		final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
		if(e != null){
			if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
				rp.addVigor(-neednrj);
				Location entityLoc = e.getLocation();
				Location pLoc = player.getLocation();
				Vector motion = new Vector(entityLoc.getX()-pLoc.getX(), entityLoc.getY()-pLoc.getY(),entityLoc.getZ()-pLoc.getZ()).multiply(.45);
				player.setMetadata("AssassinGamemode", new FixedMetadataValue(Core.instance, player.getGameMode().toString()));
				player.setGameMode(GameMode.SPECTATOR);
				player.setVelocity(motion);
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						if(player.getLocation().distanceSquared(e.getLocation()) < 3){
							player.setGameMode(GameMode.valueOf(player.getMetadata("AssassinGamemode").get(0).asString()));
							DamageManager.damage(e, player, damages, RDamageCause.ABILITY);
							e.getWorld().createExplosion(e.getLocation(), 0);
							Core.playAnimEffect(Particle.LAVA, e.getLocation(), .3F, .3F, .3F, 1, 16);
							e.setVelocity(new Vector(1/(e.getLocation().getX()-player.getLocation().getX()),1,1/(e.getLocation().getZ()-player.getLocation().getZ())));
							this.cancel();
						}
					}

					@Override
					public void onCancel() {
						rp.setActiveAbility(7, false);
					}
					
				}.runTaskTimer(0, 0);
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(7, true);
			}
		}else{
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(7, true);
		}
	}
	public static void doASSASSIN8(final Player player, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(8, true);
		double range = 6;
		
		final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
		if(e != null){
			if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
				player.setSprinting(false);
				rp.addVigor(-neednrj);
				Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
				player.setVelocity(toe.multiply(.5).add(new Vector(0,1,0)));
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
						player.setVelocity(toe);
						new BukkitTask(Abilities.getPlugin()){

							@Override
							public void run() {
								Core.playAnimEffect(Particle.ENCHANTMENT_TABLE, player.getLocation(), .5F, .5F, .5F, 1, 20);
								if(player.isOnGround()){
									Block block = player.getLocation().subtract(0,1,0).getBlock();
									Core.playAnimEffect(Particle.BLOCK_CRACK, player.getLocation(), .5F, .5F, .5F, 1, 50, block.getBlockData());
									player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);
									final List<LivingEntity> nearest = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(2, 2, 2), RDamageCause.ABILITY);
									if(!nearest.isEmpty()){
										for(LivingEntity enear : nearest){
											DamageManager.damage(enear, player, damages/2, RDamageCause.ABILITY);
											enear.setVelocity(new Vector(0,1.5,0));
											enear.setFallDistance(-100);
										}
										TeleportHandler.teleport(player, new Location(nearest.get(0).getWorld(), nearest.get(0).getLocation().getX(),nearest.get(0).getLocation().getY(),nearest.get(0).getLocation().getZ(), player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch()));
										player.setVelocity(new Vector(0,1.62,0));
										player.setFallDistance(-100);
										new BukkitTask(Abilities.getPlugin()){

											@Override
											public void run() {
												Core.playAnimEffect(Particle.EXPLOSION_LARGE, player.getLocation(), 1, 1, 1, 1, 7);
												player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
												
												for(LivingEntity enear : nearest){
													enear.setVelocity(new Vector(0,-2,0));
												}

												final List<LivingEntity> onGround = new ArrayList<LivingEntity>();
												new BukkitTask(Abilities.getPlugin()){

													@Override
													public void run() {
														if(!onGround.containsAll(nearest)){
															for(LivingEntity enear : nearest){
																if(enear.isOnGround()){
																	if(!onGround.contains(enear)){
																		Core.playAnimEffect(Particle.LAVA, enear.getLocation(), .5F, .5F, .5F, 1, 50);
																		enear.getWorld().playSound(enear.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
																		DamageManager.damage(enear, player, damages/2, RDamageCause.ABILITY);
																		enear.setFallDistance(0);
																		onGround.add(enear);
																	}
																}
															}
														}else this.cancel();
													}

													@Override
													public void onCancel() {
														player.setFallDistance(0);
														rp.setActiveAbility(8, false);
													}
													
												}.runTaskTimerCancelling(0, 0, 100);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(20);
									}
									this.cancel();
								}
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskTimer(0, 0);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(10);
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(8, false);
			}
		}else{
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(8, false);
		}
	}
}
