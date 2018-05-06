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
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
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

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class Abilities {

	private static Plugin plugin;
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static void setPlugin(Plugin plugin) {
		Abilities.plugin = plugin;
	}
	
	public static void doPALADIN1(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(1, true);
		rp.addNrj(-neednrj);
		new BukkitTask(Abilities.getPlugin()){
			int step = 0;
			@Override
			public void run() {
				if(p.isDead()){
					this.cancel();
				}else{
					if(step < 50){
						p.setSprinting(false);
						Vector v = p.getEyeLocation().getDirection();
						Vector vm = new Vector(0, -0.35, 0);
						p.setVelocity(v.multiply(0.5).add(vm));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, .25F);
						List<Entity> near = p.getNearbyEntities(1, 1, 1);
						Core.playAnimEffect(ParticleEffect.SMOKE_NORMAL, p.getLocation(), .25F, .25F, .25F, .001F, 25);
						if(!near.isEmpty()){
							for(LivingEntity enear : Core.toLivingEntityList(near)){
								DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
								p.getWorld().createExplosion(p.getLocation(), 0);
								double xenear = enear.getLocation().getX();
								double zenear = enear.getLocation().getX();
								double x = p.getLocation().getX();
								double z = p.getLocation().getX();
								Vector vdir = new Vector(0.5/(xenear-x), 3, 0.5/(zenear-z));
								enear.setVelocity(vdir.multiply(-0.25));
							}
							this.cancel();
						}
					}else{
						this.cancel();
					}
				}
				step++;
			}
			
			@Override
			public void onCancel() {
				rp.setActiveAbility(1, false);
			}
			
		}.runTaskTimer(0, 1);
	}
	public static void doPALADIN2(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(2, true);
		rp.addNrj(-neednrj);
		p.setVelocity(new Vector(0, 1.5, 0));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
		new BukkitTask(Abilities.getPlugin()){
			Location lastLocation = p.getLocation().clone();

			@Override
			public void run() {
				if(!p.isDead()){
					if(lastLocation.getWorld().equals(p.getWorld())){
						if(lastLocation.distanceSquared(p.getLocation()) < .016){
							cancel();
						}
					}
				}else cancel();
				lastLocation = p.getLocation().clone();
			}

			@Override
			public void onCancel() {
				if(!p.isDead()){
		            p.setFallDistance(-100);
		            p.setVelocity(new Vector(0, -3, 0));
				    new BukkitTask(Abilities.getPlugin()){

						@Override
						public void run() {
				        	if(p.isDead()){
				        		this.cancel();
				        	}else{
				                if (((LivingEntity)p).isOnGround()){
				                	Core.playAnimEffect(ParticleEffect.LAVA, p.getLocation(), .5F, .5F, .5F, .001F, 75);
				                	Core.playAnimEffect(ParticleEffect.SMOKE_NORMAL, p.getLocation(), .5F, .5F, .5F, .001F, 75);
				                	Core.playAnimEffect(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 5);
				                	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
									p.setFallDistance((float) 0.0);
									List<Entity> near = p.getNearbyEntities(3, 3, 3);
									for(LivingEntity enear : Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY)){
										DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
										enear.setVelocity(new Vector(0,.8,0));
									}
									this.cancel();
					            }
				        	}
						}

						@Override
						public void onCancel() {
							rp.setActiveAbility(2, false);
						}
				    	
				    }.runTaskTimer(0, 0);
				}
			}
			
		}.runTaskTimer(5,0);
	}
	public static double doPALADIN3(final Player p){
		RPlayer rp = RPlayer.get(p);
		Ability ability = AbilitiesAPI.getAbility(rp.getRClass(), 3);
		return rp.getAbLevel3()*ability.getDamagesPerLevel()+ability.getDamagesMin();
	}
	public static double doPALADIN4(final Player p){
		RPlayer rp = RPlayer.get(p);
		Ability ability = AbilitiesAPI.getAbility(rp.getRClass(), 4);
		return rp.getAbLevel4()*ability.getDamagesPerLevel()+ability.getDamagesMin();
	}
	public static void doPALADIN5(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(5, true);
		rp.addNrj(-neednrj);
		final int yawOffset = 46;
		final List<LivingEntity> hurt = new ArrayList<LivingEntity>();
		new BukkitTask(Abilities.getPlugin()){
			int step = 0;
			
			@Override
			public void run() {
				Location location = p.getLocation();
				List<LivingEntity> around = Core.toDamageableLivingEntityList(p, p.getNearbyEntities(2.5, 2.5, 2.5), RDamageCause.ABILITY);
				if(step < Math.floor(360/yawOffset)+1){
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, .5F, 1);
					Location destination = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), p.getEyeLocation().getYaw()+yawOffset, p.getEyeLocation().getPitch());
					p.teleport(destination);
					Core.playAnimEffect(ParticleEffect.LAVA, p.getLocation().subtract(0,.05,0), .25F, .2F, .25F, .1F, 4);
					Location direction = p.getLocation().toVector().add(p.getEyeLocation().getDirection().normalize().multiply(1.8)).toLocation(p.getWorld());
					for(LivingEntity near : around){
						if(!hurt.contains(near)){
							if(near.getLocation().distanceSquared(direction) < 1){
								DamageManager.damage(near, p, damages, RDamageCause.ABILITY);
								Location nearLocation = near.getLocation();
								double nearX = nearLocation.getX();
								double nearZ = nearLocation.getZ();
								double X = location.getX();
								double Z = location.getZ();
								Vector motion = new Vector(.5/(nearX-X),.75,.5/(nearZ-Z));
								near.setVelocity(motion);
								hurt.add(near);
							}
						}
					}
				}else{
					for(LivingEntity near : around){
						if(!hurt.contains(near)){
							DamageManager.damage(near, p, damages, RDamageCause.ABILITY);
							Location nearLocation = near.getLocation();
							double nearX = nearLocation.getX();
							double nearZ = nearLocation.getZ();
							double X = location.getX();
							double Z = location.getZ();
							Vector motion = new Vector(.5/(nearX-X),.75,.5/(nearZ-Z));
							near.setVelocity(motion);
						}
					}
					this.cancel();
				}
				step++;
			}

			@Override
			public void onCancel() {
				rp.setActiveAbility(5, false);
			}
			
		}.runTaskTimer(0, 0);
	}
	public static void doPALADIN6(final Player p, final RPlayer rp, final double damages, final double neednrj){
		if(rp.isActiveAbility(6)){
			BukkitTask.tasks.get(p.getMetadata("paladinTask6").get(0).asInt()).cancel();
		}else{
			rp.setActiveAbility(6, true);
			final WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			packet.setCenterX(p.getLocation().getX());
			packet.setCenterZ(p.getLocation().getZ());
			packet.setRadius(0);
			packet.setWarningDistance(Integer.MAX_VALUE);
			packet.sendPacket(p);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
			final List<PotionEffect> effects = new ArrayList<PotionEffect>();
			effects.addAll(Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2, true, false),
					new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1, true, false),
					new PotionEffect(PotionEffectType.SPEED, 100, 1, true, false),
					new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 2, true, false),
					new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, true, false),
					new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0, true, false)));
			for(PotionEffect effect : effects){
				p.addPotionEffect(effect, false);
			}
			
			int taskId = new BukkitTask(Abilities.getPlugin()){
				int step = 0;
				int step2 = 0;

				@Override
				public void run() {
					if(step >= 4){
						if(rp.hasNrj(neednrj)){
							rp.addNrj(-neednrj);
							step = 0;
						}else{
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
							ItemMessage.sendMessage(p, rp.translateString("§cNot enough vigor!", "§cPas assez de vigueur !"), 40);
							this.cancel();
						}
					}
					
					if(step2 == 10){
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
						for(PotionEffect effect : effects){
							p.addPotionEffect(effect, false);
						}
						step2 = 0;
					}
					
					Core.playAnimEffect(ParticleEffect.VILLAGER_ANGRY, p.getEyeLocation(), .25F, .25F, .25F, .5F, 5);
					step++;
					step2++;
				}

				@Override
				public void onCancel() {
					packet.setRadius(Integer.MAX_VALUE);
					packet.setWarningDistance(0);
					packet.sendPacket(p);
					for(PotionEffect effect : p.getActivePotionEffects()){
						p.removePotionEffect(effect.getType());
					}
					rp.setActiveAbility(6, false);
				}
				
			}.runTaskTimer(0, 5).getTaskId();
			p.setMetadata("paladinTask6", new FixedMetadataValue(Abilities.getPlugin(), taskId));
		}
	}
	public static void doPALADIN7(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(7, true);
		rp.addNrj(-neednrj);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 255, true, false), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 3, true, false), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 4, true, false), true);
		p.setSneaking(true);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
		final CylinderEffect s = new CylinderEffect(new EffectManager(EffectLib.instance()));
		s.particle = ParticleEffect.FLAME;
		s.type = EffectType.REPEATING;
		s.particles = 250;
		s.radius = 1.5F;
		s.height = 3;
		s.angularVelocityX = 0;
		s.angularVelocityY = 0;
		s.angularVelocityZ = 0;
		s.setDynamicOrigin(new DynamicLocation(p.getLocation().add(0,1,0)));
		s.start();
		new BukkitTask(Abilities.getPlugin()){

			int step = 0;
			int scream = 0;
			@Override
			public void run(){
				step += 1;
				scream += 1;
				if(s.radius > 0){
					if(s.radius > 1.1){
						if(step >= 8){
							s.radius -= 0.1;
							s.height -= 0.05;
							step = 0;
						}
					}else if(s.radius >= 0.9){
						if(step >= 5){
							s.radius -= 0.1;
							s.height -= 0.05;
							step = 0;
						}
					}else{
						if(step >= 2){
							s.radius -= 0.15;
							s.height -= 0.075;
							step = 0;
						}
					}
				}
				s.setDynamicOrigin(new DynamicLocation(p.getLocation().add(0,1,0)));
				if(scream >= 5){
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
					scream = 0;
				}

				List<Entity> near = p.getNearbyEntities(5, 5, 5);
				for(Entity e : near){
					if(e instanceof LivingEntity){
						Vector v = new Vector(p.getLocation().getX()-e.getLocation().getX(), p.getLocation().getY()-e.getLocation().getY(), p.getLocation().getZ()-e.getLocation().getZ());
						e.setVelocity(v.multiply(0.05));
						((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false), true);
					}
				}
			}

			@Override
			public void onCancel() {
				p.setSneaking(false);
				p.getWorld().createExplosion(p.getLocation(), 0);
				List<Entity> near = p.getNearbyEntities(3, 3, 3);
				for(LivingEntity enear : Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY)){
					Vector v = new Vector(1/(enear.getLocation().getX()-p.getLocation().getX()), .2, 1/(enear.getLocation().getZ()-p.getLocation().getZ()));
					enear.setVelocity(v);
					DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
				}
				Core.playAnimEffect(ParticleEffect.EXPLOSION_HUGE, p.getLocation(), 1F, 1F, 1F, 1F, 3);
				s.cancel(true);
				rp.setActiveAbility(7, false);
			}
			
		}.runTaskTimerCancelling(1, 0, 60);
	}
	public static void doPALADIN8(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(8, true);
		rp.addNrj(-neednrj);
		p.setVelocity(new Vector(0,.6,0));
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				p.setVelocity(new Vector(0,-1,0));
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(5);

		new BukkitTask(Abilities.getPlugin()){

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(p.isDead()){
					this.cancel();
				}else if(p.isOnGround()){
					final Location center = Locations.getCenter(p.getLocation().subtract(0,1,0));
					center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1, .1F);
					Core.playAnimEffect(ParticleEffect.FLAME, p.getLocation(), .1F, .1F, .1F, .1F, 12);
					final List<LivingEntity> hurt = new ArrayList<LivingEntity>();
					
					for(int i = 0;i < 7;i++){
						final int length = i;
						new BukkitTask(Abilities.getPlugin()){

							@Override
							public void run() {
								Vector vector = new Vector(length,0,0);
								
								for(int i = 0;i < 360;i++){
									VectorUtils.rotateAroundAxisY(vector, 1);
									Location location = Locations.getSafeLocation(center.toVector().add(vector).toLocation(center.getWorld())).subtract(0,1,0);
									Core.playAnimEffect(ParticleEffect.BLOCK_CRACK, location.add(0,.5,0), .5F, .5F, .5F, 1, 3, new BlockData(location.getBlock().getType(), location.getBlock().getData()));
									List<Entity> near = p.getNearbyEntities(length, length, length);
									for(LivingEntity enear : Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY)){
										if(!hurt.contains(enear)){
											DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
											enear.setVelocity(new Vector(0,.78,0));
											hurt.add(enear);
										}
									}
								}
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(2*i);
					}
					this.cancel();
				}
			}

			@Override
			public void onCancel() {
				rp.setActiveAbility(8, false);
			}
			
		}.runTaskTimer(5,0);
	}
	
	
	
	public static void doRANGER1(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(1, true);
		rp.addNrj(-neednrj);
		new BukkitTask(Abilities.getPlugin()){
			int step = 0;

			@Override
			public void run() {
				if(p.isDead()){
					this.cancel();
				}else{
					if(step < damages){
						Arrow arrow = p.launchProjectile(Arrow.class, p.getEyeLocation().getDirection().multiply(2));
						arrow.setBounce(false);
						arrow.setTicksLived(12000);
						Core.playAnimEffect(ParticleEffect.CRIT, p.getLocation(), .01F, .01F, .01F, .1F, 5);
						p.getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 1);
						p.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(-.05));
						rp.setNextAttackFactor(1.0);
					}else this.cancel();
				}
				step++;
			}

			@Override
			public void onCancel() {
				rp.setActiveAbility(1, false);
			}
			
		}.runTaskTimer(0, 5);
	}
	public static void doRANGER2(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(2, true);
		rp.addNrj(-neednrj);
		final Arrow w = p.launchProjectile(Arrow.class, p.getEyeLocation().getDirection().multiply(2));
		w.setBounce(false);
		p.getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 1);
		w.getWorld().playSound(w.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
		p.setMetadata("rangerTask2", new FixedMetadataValue(Abilities.getPlugin(), new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				if(w.isOnGround()){
					this.cancel();
				}
				Core.playAnimEffect(ParticleEffect.FLAME, w.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
			}

			@Override
			public void onCancel() {
				List<Entity> wanear = w.getNearbyEntities(4, 4, 4);
				for(Entity e : wanear){
					if(e instanceof Player){
						if(!e.equals(p))
							RPlayer.get(((Player)e)).sendMessage("§cI've got bad news for ya...", "§cJ'ai des mauvaises nouvelles...");
					}
				}
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						w.getWorld().playSound(w.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .1F);
						Core.playAnimEffect(ParticleEffect.EXPLOSION_HUGE, w.getLocation(), 0, 0, 0, 1, 1);
						for(LivingEntity e : Core.toLivingEntityList(w.getNearbyEntities(4, 4, 4))){
							DamageManager.damage(e, p, damages, RDamageCause.ABILITY);
						}
						w.remove();
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(20);
				rp.setActiveAbility(2, false);
				p.removeMetadata("rangerTask2", Abilities.getPlugin());
			}
			
		}.runTaskTimer(0, 1).getTaskId()));
	}
	public static void doRANGER3AND4(Player p, final LivingEntity d){
		final RPlayer rp = RPlayer.get(p);
		if(d instanceof Player){
			Player target = (Player)d;
			GMember mtarget = GMember.get(target);
			GMember member = GMember.get(p);
			if(member.hasGuild()){
				if(member.getGuild().isPeaceful())return;
				if(mtarget.hasGuild()){
					if(member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
						return;
					}
				}
			}
		}
		
		if(rp.getAbLevel3() > 0 && !d.hasMetadata("rangerPoison")){
			Ability ability3 = AbilitiesAPI.getAbility(RClass.RANGER, 3);
			final double damages3 = rp.getAbLevel3()*ability3.getDamagesPerLevel()+ability3.getDamagesMin();
			d.setMetadata("rangerPoison", new FixedMetadataValue(Abilities.getPlugin(), rp.getUniqueId()));
			new BukkitTask(Abilities.getPlugin()){

				public void run() {
					if(!d.isDead() && d.isValid()){
						DamageManager.damage(d, rp.getPlayer(), damages3, RDamageCause.ABILITY);
						Core.playAnimEffect(ParticleEffect.REDSTONE, d.getLocation().add(0, .8, 0), .2f, .5f, .2f, 1, 50);
					}else this.cancel();
				}

				@Override
				public void onCancel() {
					d.removeMetadata("rangerPoison", Abilities.getPlugin());
				}
				
			}.runTaskTimerCancelling(0, 20, 100);
		}
		if(rp.getAbLevel4() > 0){
			Ability ability4 = AbilitiesAPI.getAbility(RClass.RANGER, 4);
			double damages4 = rp.getAbLevel4()*ability4.getDamagesPerLevel()+ability4.getDamagesMin();
			d.setFireTicks((int) (damages4*20));
		}
	}
	public static void doRANGER5(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(5, true);
		rp.addNrj(-neednrj);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, true, false), true);
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				Vector target = p.getEyeLocation().getDirection();
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1, 1);
				Arrow shot = p.launchProjectile(Arrow.class, target.multiply(25));
				shot.setBounce(false);
				shot.setCritical(true);
				shot.setTicksLived(12000);
				shot.setKnockbackStrength(5);
				shot.setMetadata("SniperShot", new FixedMetadataValue(Core.instance, p.getName()));
				
				p.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(-.5));
				rp.setActiveAbility(5, false);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(60);
	}
	public static void doRANGER6(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(6, true);
		RPlayer.get(p).addNrj(-neednrj);
		new BukkitTask(Abilities.getPlugin()){
			
			@Override
			public void run() {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1);
				double a = Utils.random.nextDouble() * 2 * Math.PI;
				double dist = Utils.random.nextDouble() * 3.6 + .4;
				Location loc = p.getLocation().clone().add(dist * Math.sin(a), 5, dist * Math.cos(a));
				Arrow ef = p.getWorld().spawnArrow(loc, new Vector(0, -1, 0), 3, 0);
				ef.setShooter(p);
				ef.setBounce(false);
				ef.setTicksLived(12000);
				ef.setKnockbackStrength(0);
			}

			@Override
			public void onCancel() {
				for(Entity e : p.getNearbyEntities(3, 3, 3)){
					if(e instanceof Arrow)e.remove();
				}
				rp.setActiveAbility(6, false);
			}
			
		}.runTaskTimerCancelling(0, 2, (long) (2*damages));
	}
	public static void doRANGER7(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(7, true);
		final LivingEntity entity = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 200);
		if(entity != null){
			rp.addNrj(-neednrj);
			final Arrow arrow = p.launchProjectile(Arrow.class, new Vector(0,2.5,0));
			arrow.setMetadata("Aiming", new FixedMetadataValue(Core.instance, entity.getUniqueId().toString()));
			arrow.setBounce(false);
			arrow.setCritical(false);
			arrow.setTicksLived(12000);
			arrow.setKnockbackStrength(3);
			int taskId = new BukkitTask(Abilities.getPlugin()){

				@Override
				public void run() {
					if(!arrow.isDead() && !arrow.isOnGround() && !arrow.isInsideVehicle()){
						final Vector motion;
						if(arrow.getLocation().getBlockX() != entity.getLocation().getBlockX() && arrow.getLocation().getBlockZ() != entity.getLocation().getBlockZ())motion = new Vector(entity.getLocation().getX()-arrow.getLocation().getX(), 0, entity.getLocation().getZ()-arrow.getLocation().getZ()).normalize().multiply(1.5);
						else motion = new Vector(entity.getLocation().getX()-arrow.getLocation().getX(), entity.getLocation().getY()-arrow.getLocation().getY(), entity.getLocation().getZ()-arrow.getLocation().getZ()).normalize().multiply(1.5);
						arrow.setVelocity(motion);
						arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1.8F);
						Core.playAnimEffect(ParticleEffect.BLOCK_CRACK, arrow.getLocation(), .1F, .1F, .1F, 1, 8, new BlockData(Material.REDSTONE_BLOCK, (byte)0));
					}else this.cancel();
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(7, false);
				}
				
			}.runTaskTimer(15, 0).getTaskId();
			p.setMetadata("rangerTask7", new FixedMetadataValue(Abilities.getPlugin(), taskId));
		}else{
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cNo enemy in sight!", "§cPas d'ennemi en vue !");
			rp.setActiveAbility(7, false);
		}
	}
	public static void doRANGER8(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(8, true);
		rp.addNrj(-neednrj);
		rp.sendMessage("§dYou are now legendary charged!", "§dVous êtes désormais chargé légendaire !");
		int taskId = new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				Core.playAnimEffect(ParticleEffect.SPELL_WITCH, p.getLocation().add(0,.4,0), .2F, .8F, .2F, 1, 2);
			}

			@Override
			public void onCancel() {
				rp.sendMessage("§5You are no longer legendary charged.", "§5Vous n'êtes plus chargé légendaire.");
			}
			
		}.runTaskTimer(0, 0).getTaskId();
		p.setMetadata("LegendaryCharged", new FixedMetadataValue(Abilities.getPlugin(), taskId));
	}
	public static void doRANGER8DAMAGES(final Player p, final RPlayer rp, final LivingEntity entity){
		entity.setFallDistance(-255);
		Ability ability = AbilitiesAPI.getAbility(RClass.RANGER, 8);
		final double damages = rp.getAbLevel5()*ability.getDamagesPerLevel()+ability.getDamagesMin();
		BukkitTask.tasks.get(p.getMetadata("LegendaryCharged").get(0).asInt()).cancel();
		p.removeMetadata("LegendaryCharged", Abilities.getPlugin());
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				entity.setVelocity(new Vector(0,1.75,0));
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						final double number = 9;
						for(int i = 0;i < number;i++){
							final int index = i;
							new BukkitTask(Abilities.getPlugin()){

								@Override
								public void run() {
									if(index < 5){
										Location toSpawn = entity.getLocation().toVector().add(new Vector(Utils.random.nextDouble(), Utils.random.nextDouble(), Utils.random.nextDouble()).normalize().multiply(3)).toLocation(entity.getWorld());
										Vector direction = new Vector(entity.getLocation().getX()-toSpawn.getX(), entity.getLocation().getY()-toSpawn.getY(), entity.getLocation().getZ()-toSpawn.getZ());
										final Arrow arrow = entity.getWorld().spawnArrow(toSpawn, direction, .6F, 12);
										arrow.setCritical(false);
										arrow.setBounce(false);
										arrow.setKnockbackStrength(0);
										new BukkitTask(Abilities.getPlugin()){

											@Override
											public void run() {
												arrow.remove();
												Core.playAnimEffect(ParticleEffect.EXPLOSION_LARGE, entity.getLocation(), 1, 1, 1, .5F, 5);
												entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(5);
									}else if(index == number-1){
										new BukkitTask(Abilities.getPlugin()){

											@Override
											public void run() {
												entity.setVelocity(new Vector(0,-1.75,0));
												Core.playAnimEffect(ParticleEffect.EXPLOSION_HUGE, entity.getLocation(), 1, 1, 1, .5F, 5);
												entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);
												new BukkitTask(Abilities.getPlugin()){

													@Override
													public void run() {
														if(entity.isDead())this.cancel();
														else if(entity.isOnGround()){
															this.cancel();
															entity.setFallDistance(0);
															Core.playAnimEffect(ParticleEffect.LAVA, entity.getLocation(), .3F, .3F, .3F, 1, 23);
															Core.playAnimEffect(ParticleEffect.CLOUD, entity.getLocation(), .5F, .5F, .5F, .1F, 46);
															entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
															DamageManager.damage(entity, p, damages, RDamageCause.ABILITY);
														}
													}

													@Override
													public void onCancel() {
														rp.setActiveAbility(8, false);
													}
													
												}.runTaskTimer(0, 0);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(5);
									}
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(i*2);
						}
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(15);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(1);
	}
	
	
	
	public static void doMAGE1(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(1, true);
		RPlayer.get(p).addNrj(-neednrj);
		final double x = p.getTargetBlock((Set<Material>) null, 15).getX();
		final double y = (p.getTargetBlock((Set<Material>) null, 15).getY())-1;
		final double z = p.getTargetBlock((Set<Material>) null, 15).getZ();
		
		Location loc = new Location(p.getWorld(), x, y+5, z);
		MageMeteor explo = new MageMeteor(new EffectManager(EffectLib.instance()));
		explo.setDynamicOrigin(new DynamicLocation(loc.add(0, 3,0)));
		explo.setDynamicTarget(new DynamicLocation(loc.subtract(0, 3, 0)));
		explo.setPlayer(p);
		explo.start();
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				new BukkitTask(Abilities.getPlugin()){
					int step = 0;

					@Override
					public void run() {
						if(step <= (int)damages){
							double a = Utils.random.nextDouble() * 2 * Math.PI;
							double dist = Utils.random.nextDouble() * 3;
							Location loc = (new Location(p.getWorld(), x, y+5, z)).clone().add(dist * Math.sin(a), 0, dist * Math.cos(a));
							Fireball f = p.getWorld().spawn(loc, Fireball.class);
							f.setMetadata("mageMeteor", new FixedMetadataValue(Abilities.getPlugin(), p.getUniqueId().toString()));
							f.setDirection(new Vector(0, -2.5, 0));
							f.setYield(0);
							f.setShooter(p);
						}else this.cancel();
						step++;
					}

					@Override
					public void onCancel() {
						rp.setActiveAbility(1, false);
					}
					
				}.runTaskTimer(0, 6);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(10);
	}
	public static void doMAGE2(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(2, true);
		final List<Entity> near = p.getNearbyEntities(4, 4, 4);
		if(!Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY).isEmpty()){
			rp.addNrj(-neednrj);
			for(final LivingEntity enear : Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY)){
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1, .25F);
				final FakeLightning lightning = new FakeLightning(false);
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						lightning.display(enear);
					}

					@Override
					public void onCancel() {
						DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
						rp.setActiveAbility(2, false);
					}
					
				}.runTaskTimerCancelling(0,4,16);
			}
		}else{
			rp.sendMessage("§cThere is nobody to strike on around here!", "§cIl n'y a personne à foudroyer ici !");
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.setActiveAbility(2, false);
		}
	}
	public static void doMAGE3(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(3, true);
		RPlayer.get(p).addNrj(-neednrj);
		int range = (int) damages;
		float yaw = p.getEyeLocation().getYaw();
		float pitch = p.getEyeLocation().getPitch();
		double x = p.getTargetBlock((Set<Material>) null, range).getX();
		double y = p.getTargetBlock((Set<Material>) null, range).getY();
		double z = p.getTargetBlock((Set<Material>) null, range).getZ();
    	Core.playAnimEffect(ParticleEffect.FLAME, p.getLocation(), .5F, .5F, .5F, .001F, 75);
		TeleportHandler.teleport(p, new Location(p.getWorld(), x, y+1, z, yaw, pitch));
		p.setFallDistance(-100);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
    	Core.playAnimEffect(ParticleEffect.FLAME, p.getLocation(), .5F, .5F, .5F, .001F, 75);
	    new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
	        	if(p.isDead())this.cancel();
	        	else{
	                if (((LivingEntity)p).isOnGround()){
	    				p.setFallDistance((float) 0.0);
						this.cancel();
		            }
	        	}
			}

			@Override
			public void onCancel() {
			}
	    	
	    }.runTaskTimer(0, 0);
		rp.setActiveAbility(3, false);
	}
	public static void doMAGE4(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(4, true);
		RPlayer.get(p).addNrj(-neednrj);
		GMember member = GMember.get(p);
		double x = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - p.getHealth();
		final double y = damages;
		p.setHealth(p.getHealth() + (y > x ? x : y));
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		Core.playAnimEffect(ParticleEffect.HEART, p.getEyeLocation().add(0,.5,0), 0, 0, 0, 1, 1);
		Core.playAnimEffect(ParticleEffect.VILLAGER_HAPPY, p.getLocation().add(0,1,0), .4F, .4F, .4F, 1, 60);
		List<Entity> near = p.getNearbyEntities(3.5, 3.5, 3.5);
		for(Player pnear : Core.toPlayerList(near)){
			GMember mtarget = GMember.get(pnear);
			if(!member.hasGuild() || (member.hasGuild() && mtarget.hasGuild())){
				if(!member.hasGuild() || member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
					double x1 = pnear.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - pnear.getHealth();
					pnear.setHealth(pnear.getHealth() + (y > x1 ? x1 : y));
					pnear.playSound(pnear.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
					Core.playAnimEffect(ParticleEffect.HEART, pnear.getEyeLocation().add(0,.5,0), 0, 0, 0, 1, 1);
					Core.playAnimEffect(ParticleEffect.VILLAGER_HAPPY, pnear.getLocation().add(0,1,0), .4F, .4F, .4F, 1, 60);
				}
			}
		}
		rp.setActiveAbility(4, false);
	}
	public static void doMAGE5(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(5, true);
		RPlayer.get(p).addNrj(-neednrj);
		new BukkitTask(Abilities.getPlugin()){

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for(int i = 0;i < 3;i++){
					Vector v = RandomUtils.getRandomVector();
					double factor = 4 * Utils.random.nextDouble();
					v.normalize().multiply(factor);
					p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection().multiply(1.5));
					List<Material> transparents = Arrays.asList(Material.AIR, Material.SNOW, Material.TORCH, Material.LADDER, Material.FENCE, Material.FENCE_GATE, Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.DEAD_BUSH, Material.WEB, Material.SAPLING, Material.VINE, Material.WATER_LILY, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.LAVA, Material.WATER, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);
					Block b = p.getLocation().subtract(0,1,0).getBlock();
					Block block = p.getLocation().getBlock();
					if(block.getType().equals(Material.AIR) && !block.getType().equals(Material.SNOW) && !(transparents.contains(b.getType()))){
				        for(Player player : Bukkit.getOnlinePlayers()){
				        	player.sendBlockChange(block.getLocation(), Material.SNOW, (byte)Utils.random.nextInt(4));
				        }
					}
					for(BlockFace bf : BlockFace.values()){
						Block relative = block.getRelative(bf);
						if((relative.getType().equals(Material.AIR) || relative.getType().equals(Material.LONG_GRASS)) && !block.getType().equals(Material.SNOW) && !(transparents.contains(b.getRelative(bf).getType()))){
					        for(Player player : Bukkit.getOnlinePlayers()){
					        	player.sendBlockChange(relative.getLocation(), Material.SNOW, (byte)Utils.random.nextInt(4));
					        }
						}
					}
				}
			}

			@Override
			public void onCancel() {
				rp.setActiveAbility(5, false);
			}
			
		}.runTaskTimerCancelling(0, 1, 71);
	}
	public static void doMAGE5DAMAGES(Player p, LivingEntity d){
		Ability ability = AbilitiesAPI.getAbility(RClass.MAGE, 5);
		RPlayer rp = RPlayer.get(p);
		double damages = rp.getAbLevel5()*ability.getDamagesPerLevel()+ability.getDamagesMin();
		d.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, true, false), true);
		DamageManager.damage(d, p, damages, RDamageCause.ABILITY);
	}
	public static void doMAGE6(final Player p, final RPlayer rp, final double damages, final double neednrj){
		final LivingEntity target = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 5.5);
		if(target != null && DamageManager.canDamage(p, target, RDamageCause.ABILITY)){
			rp.setActiveAbility(6, true);
			rp.addNrj(-neednrj);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 70, 255, true, false), true);
			final List<LivingEntity> near = Core.toDamageableLivingEntityList(p, target.getNearbyEntities(1, 1, 1), RDamageCause.ABILITY);
			final Vector[] relatives = new Vector[near.size()];
			for(int i = 0;i < near.size();i++){
				LivingEntity e = near.get(i);
				e.setGravity(false);
				relatives[i] = e.getLocation().toVector().subtract(target.getLocation().toVector());
			}
			target.setGravity(false);
			
			new BukkitTask(Abilities.getPlugin()){

				@Override
				public void run() {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 1.91F);
				}

				@Override
				public void onCancel() {
					new BukkitTask(Abilities.getPlugin()){

						@Override
						public void run() {
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 1.94F);
						}

						@Override
						public void onCancel() {
							new BukkitTask(Abilities.getPlugin()){

								@Override
								public void run() {
									p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 1.96F);
								}

								@Override
								public void onCancel() {
									new BukkitTask(Abilities.getPlugin()){

										@Override
										public void run() {
											p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 1.98F);
										}

										@Override
										public void onCancel() {
											new BukkitTask(Abilities.getPlugin()){

												@Override
												public void run() {
													p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 2.0F);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskTimerCancelling(0,1,5);
										}
										
									}.runTaskTimerCancelling(0,2,5);
								}
								
							}.runTaskTimerCancelling(0,3,15);
						}
						
					}.runTaskTimerCancelling(0,4,20);
				}
				
			}.runTaskTimerCancelling(0,5,25);
			
			new BukkitTask(Abilities.getPlugin()){

				@Override
				public void run() {
					Location location = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(6.5));
					for(int i = 0;i < near.size();i++){
						LivingEntity e = near.get(i);
						e.teleport(location.clone().add(relatives[i]), TeleportCause.PLUGIN);
						Core.playAnimEffect(ParticleEffect.SPELL_WITCH, e.getLocation().add(0,-.15,0), .2F, .8F, .2F, .65F, 3);
						relatives[i] = relatives[i].multiply(.96);
					}
					target.teleport(location, TeleportCause.PLUGIN);
					Core.playAnimEffect(ParticleEffect.SPELL_WITCH, target.getLocation().add(0,-.15,0), .2F, .8F, .2F, .65F, 3);
				}

				@Override
				public void onCancel() {
					for(LivingEntity e : near){
						e.setGravity(true);
						e.setFallDistance(-100);
						ParticleEffect.EXPLOSION_NORMAL.display(0, 0, 0, 1, 1, e.getLocation(), 32);
						e.getWorld().playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10.0F, 1F);
						e.setVelocity(e.getLocation().toVector().subtract(p.getLocation().toVector()).normalize());
						DamageManager.damage(e, p, damages, RDamageCause.ABILITY);
					}
					target.setGravity(true);
					target.setFallDistance(-100);
					ParticleEffect.EXPLOSION_NORMAL.display(0, 0, 0, 1, 1, target.getLocation(), 32);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10.0F, 1F);
					target.setVelocity(target.getLocation().toVector().subtract(p.getLocation().toVector()).normalize());
					DamageManager.damage(target, p, damages, RDamageCause.ABILITY);
					rp.setActiveAbility(6, false);
				}
				
			}.runTaskTimerCancelling(0,0,70);
		}else rp.sendMessage("§cYou aren't targetting any reachable enemy!", "§cVous ne visez aucun ennemi atteignable !");
	}
	public static void doMAGE7(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(7, true);
		rp.addNrj(-neednrj);
		for(int i = 0;i < damages;i++){
			new BukkitTask(Abilities.getPlugin()){
				
				@Override
				public void run(){
					Vector direction = new Vector(Utils.random.nextDouble(), 0, Utils.random.nextDouble()).multiply(Utils.random.nextInt(4)*(Utils.random.nextDouble()+1));
					Location destination = p.getLocation().toVector().add(direction).toLocation(p.getWorld());
					Location finalDestination = Locations.getSafeLocation(destination);
					
					final PigZombie zombie = finalDestination.getWorld().spawn(finalDestination, PigZombie.class);
					
					EntityCreature entity = ((CraftCreature)zombie).getHandle();
					entity.goalSelector = new PathfinderGoalSelector(entity.getWorld().methodProfiler);
					entity.targetSelector = new PathfinderGoalSelector(entity.getWorld().methodProfiler);
					entity.goalSelector.a(0, new PathfinderGoalFloat(entity));
					entity.goalSelector.a(2, new PathfinderGoalMinionAttack(entity, 1.48D, false));
					entity.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entity, 1.0D));
					entity.goalSelector.a(7, new PathfinderGoalRandomStroll(entity, 1.0D));
					entity.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
					entity.goalSelector.a(8, new PathfinderGoalRandomLookaround(entity));
					entity.targetSelector.a(2, new PathfinderGoalMinionTarget(rp, entity, 16.0, true));
					
					zombie.setBaby(true);
					zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
					zombie.setHealth(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
					zombie.setCanPickupItems(false);
					zombie.setMetadata("Minion", new FixedMetadataValue(Core.instance, rp.getUniqueId()));
					Weapon helmet = Weapons.nearest(rp.getRLevel()-12, false, rp.getRClass(), "HELMET");
					Weapon chestplate = Weapons.nearest(rp.getRLevel()-12, false, rp.getRClass(), "CHESTPLATE");
					Weapon leggings = Weapons.nearest(rp.getRLevel()-12, false, rp.getRClass(), "LEGGINGS");
					Weapon boots = Weapons.nearest(rp.getRLevel()-12, false, rp.getRClass(), "BOOTS");
					Weapon sword = Weapons.nearest(rp.getRLevel()-12, true, null, Utils.random.nextBoolean() ? "SWORD" : "AXE");
					Weapon shield = Weapons.nearest(rp.getRLevel()-12, false, null, "SHIELD");
					if(helmet != null)zombie.getEquipment().setHelmet(helmet.getNewItemStack(null));
					if(chestplate != null)zombie.getEquipment().setChestplate(chestplate.getNewItemStack(null));
					if(leggings != null)zombie.getEquipment().setLeggings(leggings.getNewItemStack(null));
					if(boots != null)zombie.getEquipment().setBoots(boots.getNewItemStack(null));
					if(sword != null)zombie.getEquipment().setItemInMainHand(sword.getNewItemStack(null));
					if(shield != null)zombie.getEquipment().setItemInOffHand(shield.getNewItemStack(null));
					zombie.getEquipment().setHelmetDropChance(0);
					zombie.getEquipment().setChestplateDropChance(0);
					zombie.getEquipment().setLeggingsDropChance(0);
					zombie.getEquipment().setBootsDropChance(0);
					zombie.getEquipment().setItemInMainHandDropChance(0);
					zombie.getEquipment().setItemInOffHandDropChance(0);
					
		            Core.playAnimEffect(ParticleEffect.CLOUD, zombie.getLocation(), .5F, .5F, .5F, .1F, 34);
		            Core.playAnimEffect(ParticleEffect.LAVA, zombie.getLocation(), .5F, .5F, .5F, .1F, 11);
		            zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, .8F, 1);
		            
		            new BukkitTask(Abilities.getPlugin()){

						@Override
						public void run() {
							if(!zombie.isDead()){
					            Core.playAnimEffect(ParticleEffect.CLOUD, zombie.getLocation(), .5F, .5F, .5F, .1F, 23);
					            Core.playAnimEffect(ParticleEffect.LAVA, zombie.getLocation(), .5F, .5F, .5F, .1F, 8);
					            zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, .8F, 1);
							}
							zombie.remove();
						}

						@Override
						public void onCancel() {
						}
						
		            }.runTaskLater(24*20);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(i*7);
		}
        
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
				rp.setActiveAbility(7, false);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater((long) (damages*7+21*20));
	}
	public static void doMAGE8(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(8, true);
		rp.addNrj(-neednrj);
		p.setWalkSpeed(0);
		
		final Location center = p.getLocation();
		Vector vector = new Vector(3,0,0);
		final List<Block> blocks = new ArrayList<Block>();
		final List<Material> types = new ArrayList<Material>();
		for(int i = 0;i < 60;i++){
			VectorUtils.rotateAroundAxisY(vector, 6);
			Block block = Locations.getSafeLocation(center.toVector().add(vector).toLocation(center.getWorld())).getBlock();
			if(!blocks.contains(block)){
				blocks.add(block);
				types.add(block.getType());
			}
		}
		
		final int duration = 50;
		final int ticksBetween = duration/(blocks.size()+1);
		new BukkitTask(Abilities.getPlugin()){
			int step = 0;
			int ticks = 0;
			int ticksStep = 0;

			@Override
			public void run() {
				if(step < duration){
					Core.playAnimEffect(ParticleEffect.SMOKE_LARGE, p.getLocation(), 2F, .1F, 2F, .1F, 11);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, step);
					if(ticks == ticksBetween){
						if(blocks.size() > ticksStep){
							final int id = ticksStep;
							blocks.get(id).setType(Material.FIRE);
							new BukkitTask(Abilities.getPlugin()){

								@Override
								public void run() {
									this.cancel();
								}

								@Override
								public void onCancel() {
									for(int i = 0;i < blocks.size();i++){
										blocks.get(i).setType(types.get(i));
									}
								}
								
							}.runTaskLater(90);
							ticksStep++;
						}
						ticks = 0;
					}
				}else{
					this.cancel();
					
					final FakeLightning lightning = new FakeLightning(false);
					for(int i = 0;i < 9;i++){
						new BukkitTask(Core.instance){
							public void run(){
								Location destination = p.getLocation().toVector().add(new Vector(Utils.random.nextDouble(), 0, Utils.random.nextDouble()).multiply(Utils.random.nextInt(4)*(Utils.random.nextDouble()+Utils.random.nextInt(2)+1))).toLocation(p.getWorld());
								lightning.display(destination);
								Core.playAnimEffect(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 1, 1, 1, .5F, 5);
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
								
								for(LivingEntity entity : Core.toDamageableLivingEntityList(p, p.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY)){
									DamageManager.damage(entity, p, damages, RDamageCause.ABILITY);
								}
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(i*4);
					}
				}
				step++;
				ticks++;
			}

			@Override
			public void onCancel() {
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						Core.playAnimEffect(ParticleEffect.EXPLOSION_HUGE, p.getLocation(), 1, 1, 1, .5F, 5);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);
						
						for(LivingEntity entity : Core.toDamageableLivingEntityList(p, p.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY)){
							DamageManager.damage(entity, p, damages, RDamageCause.ABILITY);
						}

						p.setWalkSpeed(.2F);
						rp.setActiveAbility(8, false);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(29);
			}
			
		}.runTaskTimer(0, 1);
	}
	
	
	
	public static void doASSASSIN1(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(1, true);
		LivingEntity entity = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 4.0);
		if(entity != null){
			Location edloc = entity.getLocation();
			if(edloc.distanceSquared(p.getLocation()) < 33){
				rp.addNrj(-neednrj);
				double xedloc = edloc.getX();
				double yedloc = edloc.getY();
				double zedloc = edloc.getZ();
				Location ploc = p.getLocation();
				double xploc = ploc.getX();
				double zploc = ploc.getZ();
				Vector v = new Vector((xedloc-xploc)*1.4, 0, (zedloc-zploc)*1.4);
				double xv = xploc + v.getX();
				double yv = yedloc + v.getY();
				double zv = zploc + v.getZ();
				float yaw = p.getEyeLocation().getYaw();
				if(yaw > 0){
					yaw = yaw-180;
				}else{
					yaw = yaw+180;
				}
				p.getWorld().playEffect(p.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, .25F);
				Location finalloc = Locations.getSafeLocation(new Location(p.getWorld(), xv, yv, zv, yaw, 0.0f));
				TeleportHandler.teleport(p, finalloc);
				p.getWorld().playEffect(p.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);

				List<Entity> wanear = p.getNearbyEntities(2, 2, 2);
				for(LivingEntity enear : Core.toDamageableLivingEntityList(p, wanear, RDamageCause.ABILITY)){
					DamageManager.damage(enear, p, damages, RDamageCause.ABILITY);
				}
			}else{
				rp.sendMessage("§cTarget is too far!","§cLa cible est trop loin !");
			}
		}else{
			rp.sendMessage("§cThere is nobody in front of you!","§cIl n'y a personne en face de vous !");
		}
		rp.setActiveAbility(1, false);
	}
	public static void doASSASSIN2(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(2, true);
		rp.addNrj(-neednrj);
		Core.playAnimEffect(ParticleEffect.FLAME, p.getLocation(), .5F, .5F, .5F, .1F, 100);
		Core.playAnimEffect(ParticleEffect.CLOUD, p.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
		if(rp.isVanished())rp.setVanished(false);
		for(Player player : Bukkit.getOnlinePlayers()){
			player.hidePlayer(p);
		}
		final ItemStack helmet = p.getEquipment().getHelmet();
		if(helmet != null){
			ItemStack pumpkin = helmet.clone();
			pumpkin.setType(Material.PUMPKIN);
			p.getEquipment().setHelmet(pumpkin);
		}else p.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
		rp.sendMessage("§7Viouf! Nobody's seeing you!", "§7Viouf! Plus personne ne vous voit !");
		new BukkitTask(Core.instance){
			public void run(){
				rp.sendMessage("§cYour cloak begin to disappear!", "§cVotre camouflage commence à disparaître !");
				new BukkitTask(Core.instance){
					public void run(){
						Core.playAnimEffect(ParticleEffect.FLAME, p.getLocation(), .5F, .5F, .5F, .1F, 100);
						Core.playAnimEffect(ParticleEffect.CLOUD, p.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
						for(Player player : Bukkit.getOnlinePlayers()){
							player.showPlayer(p);
						}
						if(helmet != null)p.getEquipment().setHelmet(helmet);
						else p.getEquipment().setHelmet(new ItemStack(Material.AIR, 1));
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
	public static double doASSASSIN3(final Player p){
		RPlayer rp = RPlayer.get(p);
		Ability ability = AbilitiesAPI.getAbility(RClass.ASSASSIN, 3);
		return (rp.getAbLevel3()*ability.getDamagesPerLevel()+ability.getDamagesMin())/100.0D-1;
	}
	public static void doASSASSIN4(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(4, true);
		rp.addNrj(-neednrj);
		Location eyes = p.getEyeLocation().getDirection().normalize().toLocation(p.getWorld());
		double x = eyes.getX()*1.65;
		double z = eyes.getZ()*1.65;
		p.getWorld().createExplosion(p.getLocation(), 0);
		List<Entity> near = p.getNearbyEntities(3, 3, 3);
		for(LivingEntity e : Core.toDamageableLivingEntityList(p, near, RDamageCause.ABILITY)){
			DamageManager.damage(e, p, damages, RDamageCause.ABILITY);
		}
		p.setVelocity(new Vector(-x*rp.getAbLevel(4)*.21, rp.getAbLevel4()*0.07+.28, -z*rp.getAbLevel(4)*.21));
		p.setFallDistance(-100);
		new BukkitTask(Abilities.getPlugin()){

			@Override
			public void run() {
	        	if(p.isDead()){
	        		this.cancel();
	        	}else{
	                if (((LivingEntity)p).isOnGround()){
						p.setFallDistance((float) 0.0);
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2, true, false), true);
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
	public static void doASSASSIN5(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(5, true);
		int range = 9;
		final LivingEntity target = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 50.0);
		if(target != null){
			if(target.getLocation().distanceSquared(p.getLocation()) <= range*range){
				rp.addNrj(-neednrj);
				final AttributeModifier modifier = new AttributeModifier("RubidiaAssassinTrap", -10000.0, Operation.ADD_NUMBER);
				final Player player = target instanceof Player ? (Player)target : null;
				if(player != null){
					player.setWalkSpeed(0);
				}else{
					target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
				}
				target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false), true);
				new BukkitTask(Abilities.getPlugin()){

					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						Block block = target.getLocation().subtract(0,1,0).getBlock();
						Core.playAnimEffect(ParticleEffect.SPELL_WITCH, target.getLocation(), .3F, .1F, .3F, .1F, 4);
						Core.playAnimEffect(ParticleEffect.SPELL_INSTANT, target.getLocation(), .1F, .1F, .1F, .1F, 1);
						Core.playAnimEffect(ParticleEffect.BLOCK_CRACK, target.getLocation(), .3F, .1F, .3F, .1F, 4, new BlockData(block.getType(), block.getData()));
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
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(5, false);
			}
		}else{
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(5, false);
		}
	}
	public static void doASSASSIN6(final Player p, final RPlayer rp, final double damages, final double neednrj){
		if(rp.isActiveAbility(6)){
			BukkitTask.tasks.get(p.getMetadata("assassinTask6").get(0).asInt()).cancel();
		}else{
			rp.setActiveAbility(6, true);
			if(rp.hasNrj(neednrj)){
				int taskId = new BukkitTask(Abilities.getPlugin()){
					int step = 0;

					@Override
					public void run() {
						Core.playAnimEffect(ParticleEffect.CLOUD, p.getLocation().add(0,1,0), 1, 1, 1, .001F, 50);
						Core.playAnimEffect(ParticleEffect.REDSTONE, p.getLocation().add(0,1,0), 1, 1, 1, 0, 42);
						Core.playAnimEffect(ParticleEffect.SMOKE_NORMAL, p.getLocation().add(0,1,0), 1, 1, 1, .001F, 22);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
						
						if(step >= 5){
							if(rp.hasNrj(neednrj)){
								for(LivingEntity e : Core.toDamageableLivingEntityList(p, p.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY)){
									DamageManager.damage(e, p, damages*.25, RDamageCause.ABILITY);
								}
								rp.addNrj(-neednrj);
								step = 0;
							}else{
								p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
								ItemMessage.sendMessage(p, rp.translateString("§cNot enough vigor!", "§cPas assez de vigueur !"), 40);
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
				p.setMetadata("assassinTask6", new FixedMetadataValue(Abilities.getPlugin(), taskId));
			}else{
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				ItemMessage.sendMessage(p, rp.translateString("§cNot enough vigor!", "§cPas assez de vigueur !"), 40);
				rp.setActiveAbility(6, false);
			}
		}
	}
	public static void doASSASSIN7(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(7, true);
		double range = 7;
		
		final LivingEntity e = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 50.0);
		if(e != null){
			if(e.getLocation().distanceSquared(p.getLocation()) <= range*range){
				rp.addNrj(-neednrj);
				Location entityLoc = e.getLocation();
				Location pLoc = p.getLocation();
				Vector motion = new Vector(entityLoc.getX()-pLoc.getX(), entityLoc.getY()-pLoc.getY(),entityLoc.getZ()-pLoc.getZ()).multiply(.45);
				p.setMetadata("AssassinGamemode", new FixedMetadataValue(Core.instance, p.getGameMode().toString()));
				p.setGameMode(GameMode.SPECTATOR);
				p.setVelocity(motion);
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						if(p.getLocation().distanceSquared(e.getLocation()) < 3){
							p.setGameMode(GameMode.valueOf(p.getMetadata("AssassinGamemode").get(0).asString()));
							DamageManager.damage(e, p, damages, RDamageCause.ABILITY);
							e.getWorld().createExplosion(e.getLocation(), 0);
							Core.playAnimEffect(ParticleEffect.LAVA, e.getLocation(), .3F, .3F, .3F, 1, 16);
							e.setVelocity(new Vector(1/(e.getLocation().getX()-p.getLocation().getX()),1,1/(e.getLocation().getZ()-p.getLocation().getZ())));
							this.cancel();
						}
					}

					@Override
					public void onCancel() {
						rp.setActiveAbility(7, false);
					}
					
				}.runTaskTimer(0, 0);
			}else{
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(7, true);
			}
		}else{
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(7, true);
		}
	}
	public static void doASSASSIN8(final Player p, final RPlayer rp, final double damages, final double neednrj){
		rp.setActiveAbility(8, true);
		double range = 6;
		
		final LivingEntity e = Utils.getInSightDamageableEntity(p, RDamageCause.ABILITY, 50.0);
		if(e != null){
			if(e.getLocation().distanceSquared(p.getLocation()) <= range*range){
				p.setSprinting(false);
				rp.addNrj(-neednrj);
				Vector toe = e.getLocation().toVector().subtract(p.getLocation().toVector());
				p.setVelocity(toe.multiply(.5).add(new Vector(0,1,0)));
				new BukkitTask(Abilities.getPlugin()){

					@Override
					public void run() {
						Vector toe = e.getLocation().toVector().subtract(p.getLocation().toVector());
						p.setVelocity(toe);
						new BukkitTask(Abilities.getPlugin()){

							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								Core.playAnimEffect(ParticleEffect.ENCHANTMENT_TABLE, p.getLocation(), .5F, .5F, .5F, 1, 20);
								if(p.isOnGround()){
									Block block = p.getLocation().subtract(0,1,0).getBlock();
									Core.playAnimEffect(ParticleEffect.BLOCK_CRACK, p.getLocation(), .5F, .5F, .5F, 1, 50, new BlockData(block.getType(), block.getData()));
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 1);
									final List<LivingEntity> nearest = Core.toDamageableLivingEntityList(p, p.getNearbyEntities(2, 2, 2), RDamageCause.ABILITY);
									if(!nearest.isEmpty()){
										for(LivingEntity enear : nearest){
											DamageManager.damage(enear, p, damages/2, RDamageCause.ABILITY);
											enear.setVelocity(new Vector(0,1.5,0));
											enear.setFallDistance(-100);
										}
										TeleportHandler.teleport(p, new Location(nearest.get(0).getWorld(), nearest.get(0).getLocation().getX(),nearest.get(0).getLocation().getY(),nearest.get(0).getLocation().getZ(), p.getEyeLocation().getYaw(), p.getEyeLocation().getPitch()));
										p.setVelocity(new Vector(0,1.62,0));
										p.setFallDistance(-100);
										new BukkitTask(Abilities.getPlugin()){

											@Override
											public void run() {
												Core.playAnimEffect(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 1, 1, 1, 1, 7);
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
												
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
																		Core.playAnimEffect(ParticleEffect.LAVA, enear.getLocation(), .5F, .5F, .5F, 1, 50);
																		enear.getWorld().playSound(enear.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
																		DamageManager.damage(enear, p, damages/2, RDamageCause.ABILITY);
																		enear.setFallDistance(0);
																		onGround.add(enear);
																	}
																}
															}
														}else this.cancel();
													}

													@Override
													public void onCancel() {
														p.setFallDistance(0);
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
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cThis enemy is too far!", "§cCet ennemi est trop loin !");
				rp.setActiveAbility(8, false);
			}
		}else{
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			rp.sendMessage("§cThere is nobody in front of you!", "§cIl n'y a personne en face de vous !");
			rp.setActiveAbility(8, false);
		}
	}
}
