package me.pmilon.RubidiaCore.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.bukkit.block.data.type.Snow;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
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
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.VectorUtils;
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

public enum RAbility {
	
	PALADIN_1(new Ability("Charge", Arrays.asList("Le paladin fonce droit devant lui", "jusqu'à ce qu'il rencontre", "un obstacle, qu'il explosera"),
			RClass.PALADIN, Mastery.ADVENTURER, 1, false, "DGG,SP", "", "", 0) {

		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){
				int step = 0;
				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else{
						if(step < 50){
							player.setSprinting(false);
							Vector v = player.getEyeLocation().getDirection();
							Vector vm = new Vector(0, -0.35, 0);
							player.setVelocity(v.multiply(0.5).add(vm));
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, .25F);
							List<LivingEntity> near = Core.toLivingEntityList(player.getNearbyEntities(1, 1, 1));
							Core.playAnimEffect(Particle.SMOKE_NORMAL, player.getLocation(), .25F, .25F, .25F, .001F, 25);
							if(!near.isEmpty()){
								getInstance().damage(rp, near);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			player.getWorld().createExplosion(player.getLocation(), 0);
			Vector vdir = new Vector(0.5/(target.getLocation().getX()-player.getLocation().getX()),
					3,
					0.5/(target.getLocation().getZ()-player.getLocation().getZ()));
			target.setVelocity(vdir.multiply(-0.25));
		}
		
	}),
	PALADIN_2(new Ability("Coup fatal", Arrays.asList("Le paladin saute en hauteur pour", "frapper le sol de toute sa force"),
			RClass.PALADIN, Mastery.ADVENTURER, 2, false, "DGD,SN", "", "", 0) {

		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){
				int step = 0;
				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else{
						if(step < 50){
							player.setSprinting(false);
							Vector v = player.getEyeLocation().getDirection();
							Vector vm = new Vector(0, -0.35, 0);
							player.setVelocity(v.multiply(0.5).add(vm));
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, .25F);
							List<LivingEntity> near = Core.toLivingEntityList(player.getNearbyEntities(1, 1, 1));
							Core.playAnimEffect(Particle.SMOKE_NORMAL, player.getLocation(), .25F, .25F, .25F, .001F, 25);
							if(!near.isEmpty()){
								getInstance().damage(rp, near);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			player.getWorld().createExplosion(player.getLocation(), 0);
			Vector vdir = new Vector(0.5/(target.getLocation().getX()-player.getLocation().getX()),
					3,
					0.5/(target.getLocation().getZ()-player.getLocation().getZ()));
			target.setVelocity(vdir.multiply(-0.25));
		}
		
	}),
	PALADIN_3(new Ability("Furie", Arrays.asList("Le paladin possède une force", "naturellement développée"),
			RClass.PALADIN, Mastery.ADVENTURER, 3, true, "", "", "%", 0) {

		@Override
		public void run(final RPlayer rp) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	PALADIN_4(new Ability("Peau de fer", Arrays.asList("Le paladin possède une défense", "naturellement développée"),
			RClass.PALADIN, Mastery.ADVENTURER, 4, true, "", "Défense", "%", 0) {

		@Override
		public void run(final RPlayer rp) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	PALADIN_5(new Ability("Danse de lames", Arrays.asList("Le paladin entraîne ses haches", "dans une virevolte effrénée,", "causant de lourds dégâts","aux ennemis alentours"),
			RClass.PALADIN, Mastery.MASTER, 5, false, "DDD,SN", "", "", 0) {

		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final int yawOffset = 46;
			final List<LivingEntity> hurt = new ArrayList<LivingEntity>();
			new BukkitTask(Core.instance){
				int step = 0;
				
				@Override
				public void run() {
					Location location = player.getLocation();
					List<LivingEntity> around = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(2.5, 2.5, 2.5), RDamageCause.ABILITY);
					around.removeAll(hurt);
					getInstance().damage(rp, around);
					hurt.addAll(around);
					if(step < Math.floor(360/yawOffset)+1){
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, .5F, 1);
						Location destination = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), player.getEyeLocation().getYaw()+yawOffset, player.getEyeLocation().getPitch());
						player.teleport(destination);
						Core.playAnimEffect(Particle.LAVA, player.getLocation().subtract(0,.05,0), .25F, .2F, .25F, .1F, 4);
						Location direction = player.getLocation().toVector().add(player.getEyeLocation().getDirection().normalize().multiply(1.8)).toLocation(player.getWorld());
						player.teleport(direction);
					}else this.cancel();
					step++;
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(5, false);
				}
				
			}.runTaskTimer(0, 0);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector motion = new Vector(.5/(target.getLocation().getX()-player.getLocation().getX()),
					.75,
					.5/(target.getLocation().getZ()-player.getLocation().getZ()));
			target.setVelocity(motion);
		}
				
	}),
	PALADIN_6(new Ability("Rage", Arrays.asList("Le paladin entre en frénésie,", "augmentant sa force, se vitesse", "et sa défense"),
			RClass.PALADIN, Mastery.MASTER, 6, false, "DDD,!SN", "", "", 50) {
		
		List<PotionEffect> effects = Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2, true, false),
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1, true, false),
				new PotionEffect(PotionEffectType.SPEED, 100, 1, true, false),
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 2, true, false),
				new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, true, false),
				new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0, true, false));
		
		@Override
		public void run(final RPlayer rp) {
			Player player = rp.getPlayer();
			WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			packet.setCenterX(player.getLocation().getX());
			packet.setCenterZ(player.getLocation().getZ());
			packet.setRadius(0);
			packet.setWarningDistance(Integer.MAX_VALUE);
			packet.sendPacket(player);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			
			for(PotionEffect effect : effects){
				player.addPotionEffect(effect, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
			Player player = rp.getPlayer();
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			for(PotionEffect effect : effects){
				player.addPotionEffect(effect, false);
			}
			Core.playAnimEffect(Particle.VILLAGER_ANGRY, player.getEyeLocation(), .25F, .25F, .25F, .5F, 5);
		}

		@Override
		public void onCancel(RPlayer rp) {
			Player player = rp.getPlayer();
			WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			packet.setCenterX(player.getLocation().getX());
			packet.setCenterZ(player.getLocation().getZ());
			packet.setRadius(Integer.MAX_VALUE);
			packet.setWarningDistance(0);
			packet.sendPacket(player);
			for(PotionEffect effect : player.getActivePotionEffects()){
				player.removePotionEffect(effect.getType());
			}
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	PALADIN_7(new Ability("Métafusion", Arrays.asList("Le paladin concentre sa force durant", "2 secondes, après lesquelles", "il déchaîne une one de choc"),
			RClass.PALADIN, Mastery.HERO, 7, false, "DGG,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 255, true, false), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 3, true, false), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 4, true, false), true);
			player.setSneaking(true);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
			new BukkitTask(Core.instance){

				int scream = 0;
				@Override
				public void run(){
					scream += 1;
					if(scream >= 5){
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
						scream = 0;
					}

					List<Entity> near = player.getNearbyEntities(5, 5, 5);
					for(Entity e : near){
						if(e instanceof LivingEntity){
							Vector v = new Vector(player.getLocation().getX()-e.getLocation().getX(), player.getLocation().getY()-e.getLocation().getY(), player.getLocation().getZ()-e.getLocation().getZ());
							e.setVelocity(v.multiply(0.05));
							((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false), true);
						}
					}
				}

				@Override
				public void onCancel() {
					player.setSneaking(false);
					player.getWorld().createExplosion(player.getLocation(), 0);
					List<LivingEntity> near = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY);
					getInstance().damage(rp, near);
					Core.playAnimEffect(Particle.EXPLOSION_HUGE, player.getLocation(), 1F, 1F, 1F, 1F, 3);
					rp.setActiveAbility(7, false);
				}
				
			}.runTaskTimerCancelling(1, 0, 60);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector v = new Vector(1/(target.getLocation().getX()-player.getLocation().getX()),
					.2,
					1/(target.getLocation().getZ()-player.getLocation().getZ()));
			target.setVelocity(v);
		}
				
	}),
	PALADIN_8(new Ability("Frappe du golem", Arrays.asList("Le paladin frappe le sol", "en libérant une onde de choc"),
			RClass.PALADIN, Mastery.HERO, 8, false, "GDD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.setVelocity(new Vector(0,.6,0));
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					player.setVelocity(new Vector(0,-1,0));
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(5);

			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else if(player.isOnGround()){
						final Location center = Locations.getCenter(player.getLocation().subtract(0,1,0));
						center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1, .1F);
						Core.playAnimEffect(Particle.FLAME, player.getLocation(), .1F, .1F, .1F, .1F, 12);
						final List<LivingEntity> hurt = new ArrayList<LivingEntity>();
						
						for(int i = 0;i < 7;i++){
							final int length = i;
							new BukkitTask(Core.instance){

								@Override
								public void run() {
									Vector vector = new Vector(length,0,0);
									
									for(int i = 0;i < 360;i++){
										VectorUtils.rotateAroundAxisY(vector, 1);
										Location location = Locations.getSafeLocation(center.toVector().add(vector).toLocation(center.getWorld())).subtract(0,1,0);
										Core.playAnimEffect(Particle.BLOCK_CRACK, location.clone().add(0,.5,0), .5F, .5F, .5F, 1, 3, location.getBlock().getBlockData());
										List<LivingEntity> near = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(length, length, length), RDamageCause.ABILITY);
										near.removeAll(hurt);
										getInstance().damage(rp, near);
										hurt.addAll(near);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			target.setVelocity(new Vector(0,.78,0));
		}
				
	}),
	
	
	RANGER_1(new Ability("Salve", Arrays.asList("Le ranger lance un grand nombre", "de flèches en un cours laps de temps"),
			RClass.RANGER, Mastery.ADVENTURER, 1, false, "GDG,SN", "Flèches", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final double damages = getInstance().getDamages(rp);
			new BukkitTask(Core.instance){
				int step = 0;

				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else{
						if(step < damages){
							Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().multiply(2));
							arrow.setBounce(false);
							arrow.setTicksLived(12000);
							Core.playAnimEffect(Particle.CRIT, player.getLocation(), .01F, .01F, .01F, .1F, 5);
							player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
							player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-.05));
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_2(new Ability("Flèche explosive", Arrays.asList("Le ranger lance une flèche", "explosant au premier contact"),
			RClass.RANGER, Mastery.ADVENTURER, 2, false, "DGD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().multiply(2));
			arrow.setBounce(false);
			player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
			arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
			player.setMetadata("explosiveArrow", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(arrow.isOnGround()){
						this.cancel();
					}
					Core.playAnimEffect(Particle.FLAME, arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
				}

				@Override
				public void onCancel() {
					List<Entity> wanear = arrow.getNearbyEntities(4, 4, 4);
					for(Entity e : wanear){
						if(e instanceof Player){
							if(!e.equals(player))
								RPlayer.get(((Player)e)).sendMessage("§cJ'ai des mauvaises nouvelles...");
						}
					}
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .1F);
							Core.playAnimEffect(Particle.EXPLOSION_HUGE, arrow.getLocation(), 0, 0, 0, 1, 1);
							getInstance().damage(rp, Core.toLivingEntityList(arrow.getNearbyEntities(4, 4, 4)));
							arrow.remove();
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(20);
					rp.setActiveAbility(2, false);
					player.removeMetadata("explosiveArrow", Core.instance);
				}
				
			}.runTaskTimer(0, 1).getTaskId()));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	RANGER_3(new Ability("Flèches empoisonnées", Arrays.asList("Les flèches du ranger sont toutes", "trempées dans une solution d'arsenic", "pour qu'elles empoisonnent leurs cibles"),
			RClass.RANGER, Mastery.ADVENTURER, 3, true, "", "Durée de l'effet", "sec", 0) {
		
		@Override
		public void run(final RPlayer rp) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(final RPlayer rp, final LivingEntity target) {
			final Player player = rp.getPlayer();
			if(target instanceof Player){
				Player ptarget = (Player)target;
				GMember mtarget = GMember.get(ptarget);
				GMember member = GMember.get(rp);
				if(member.hasGuild()){
					if(member.getGuild().isPeaceful())return;
					if(mtarget.hasGuild()){
						if(member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
							return;
						}
					}
				}
			}

			if(target.hasMetadata("poisonedArrow")) {
				BukkitTask task = BukkitTask.tasks.get(player.getMetadata("poisonedArrow").get(0).asInt());
				if(task != null) {
					task.cancel();
				}
			}
			
			target.setMetadata("poisonedArrow", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

				public void run() {
					if(!target.isDead() && target.isValid()){
						DamageManager.damage(target, rp.getPlayer(), getInstance().getDamages(rp), RDamageCause.ABILITY);
						Core.playAnimEffect(Particle.REDSTONE, target.getLocation().add(0, .8, 0), .2f, .5f, .2f, 1, 50);
					}else this.cancel();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimerCancelling(0, 20, 100).getTaskId()));
		}
				
	}),
	RANGER_4(new Ability("Flèches de feu", Arrays.asList("Les flèches du ranger sont couvertes", "d'une poudre qui s'emflamme au premier contact"),
			RClass.RANGER, Mastery.ADVENTURER, 4, false, "", "Durée de l'effet", "sec", 0) {
		
		@Override
		public void run(final RPlayer rp) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			if(target instanceof Player){
				Player ptarget = (Player)target;
				GMember mtarget = GMember.get(ptarget);
				GMember member = GMember.get(rp);
				if(member.hasGuild()){
					if(member.getGuild().isPeaceful())return;
					if(mtarget.hasGuild()){
						if(member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
							return;
						}
					}
				}
			}

			target.setFireTicks((int) (RAbility.RANGER_4.getAbility().getDamages(rp)*20));
		}
				
	}),
	RANGER_5(new Ability("Tir de précision", Arrays.asList("Le ranger augmente sa précision", "pendant 3 secondes, après lesquelles", "il décoche une flèche incroyablement puissante"),
			RClass.RANGER, Mastery.MASTER, 5, false, "DGG,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, true, false), true);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					Vector target = player.getEyeLocation().getDirection();
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, 1);
					Arrow shot = player.launchProjectile(Arrow.class, target.multiply(25));
					shot.setBounce(false);
					shot.setCritical(true);
					shot.setTicksLived(12000);
					shot.setKnockbackStrength(5);
					shot.setMetadata("snipershot", new FixedMetadataValue(Core.instance, player.getUniqueId().toString()));
					
					player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-.5));
					rp.setActiveAbility(5, false);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(60);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_6(new Ability("Pluie de flèches", Arrays.asList("Le ranger décoche des flèches pour", "créer une pluie dans ses alentours"),
			RClass.RANGER, Mastery.MASTER, 6, false, "GDD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){
				
				@Override
				public void run() {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1);
					double a = RandomUtils.random.nextDouble() * 2 * Math.PI;
					double dist = RandomUtils.random.nextDouble() * 3.6 + .4;
					Location loc = player.getLocation().clone().add(dist * Math.sin(a), 5, dist * Math.cos(a));
					Arrow ef = player.getWorld().spawnArrow(loc, new Vector(0, -1, 0), 3, 0);
					ef.setShooter(player);
					ef.setBounce(false);
					ef.setTicksLived(12000);
					ef.setKnockbackStrength(0);
				}

				@Override
				public void onCancel() {
					for(Entity e : player.getNearbyEntities(3, 3, 3)){
						if(e instanceof Arrow)e.remove();
					}
					rp.setActiveAbility(6, false);
				}
				
			}.runTaskTimerCancelling(0, 2, (long) (2*getInstance().getDamages(rp)));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	RANGER_7(new Ability("Flèche chercheuse", Arrays.asList("Le ranger contrôle la flèche", "pour toucher l'ennemi ciblé le plus proche"),
			RClass.RANGER, Mastery.HERO, 7, false, "DDD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			final Player player = rp.getPlayer();
			final LivingEntity entity = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 200);
			if(entity != null){
				this.takeVigor(rp);
				final Arrow arrow = player.launchProjectile(Arrow.class, new Vector(0,2.5,0));
				arrow.setBounce(false);
				arrow.setCritical(false);
				arrow.setTicksLived(12000);
				arrow.setKnockbackStrength(3);
				arrow.setMetadata("aimingArrow", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(!arrow.isDead() && !arrow.isOnGround() && !arrow.isInsideVehicle()){
							final Vector motion;
							if(arrow.getLocation().getBlockX() != entity.getLocation().getBlockX()
									&& arrow.getLocation().getBlockZ() != entity.getLocation().getBlockZ()) {
								motion = new Vector(entity.getLocation().getX()-arrow.getLocation().getX(),
										0,
										entity.getLocation().getZ()-arrow.getLocation().getZ()).normalize().multiply(1.5);
							}else{
								motion = new Vector(entity.getLocation().getX()-arrow.getLocation().getX(),
										entity.getLocation().getY()-arrow.getLocation().getY(),
										entity.getLocation().getZ()-arrow.getLocation().getZ()).normalize().multiply(1.5);
							}
							arrow.setVelocity(motion);
							arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.8F);
							Core.playAnimEffect(Particle.BLOCK_CRACK, arrow.getLocation(), .1F, .1F, .1F, 1, 8, Material.REDSTONE_BLOCK.createBlockData());
						}else this.cancel();
					}

					@Override
					public void onCancel() {
						rp.setActiveAbility(7, false);
					}
					
				}.runTaskTimer(15, 0).getTaskId()));
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cPas d'ennemi en vue !");
				rp.setActiveAbility(7, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	RANGER_8(new Ability("Flèche légendaire", Arrays.asList("Le ranger concentre son agilité", "dans sa prochaine flèche valide,", "pulvérisant la cible ainsi touchée"),
			RClass.RANGER, Mastery.HERO, 8, false, "DDD,!SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			rp.setActiveAbility(8, true);
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			rp.sendMessage("§dVous êtes désormais chargé légendaire !");
			player.setMetadata("legendaryCharged", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

				@Override
				public void run() {
					Core.playAnimEffect(Particle.SPELL_WITCH, player.getLocation().add(0,.4,0), .2F, .8F, .2F, 1, 2);
				}

				@Override
				public void onCancel() {
					rp.sendMessage("§5Vous n'êtes plus chargé légendaire.");
				}
				
			}.runTaskTimer(0, 0).getTaskId()));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(final RPlayer rp, final LivingEntity target) {
			target.setFallDistance(-255);
			final Player player = rp.getPlayer();
			BukkitTask.tasks.get(player.getMetadata("legendaryCharged").get(0).asInt()).cancel();
			player.removeMetadata("legendaryCharged", Core.instance);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					target.setVelocity(new Vector(0,1.75,0));
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							final double number = 9;
							for(int i = 0;i < number;i++){
								final int index = i;
								new BukkitTask(Core.instance){

									@Override
									public void run() {
										if(index < 5){
											Location toSpawn = target.getLocation().toVector().add(new Vector(RandomUtils.random.nextDouble(), RandomUtils.random.nextDouble(), RandomUtils.random.nextDouble()).normalize().multiply(3)).toLocation(target.getWorld());
											Vector direction = new Vector(target.getLocation().getX()-toSpawn.getX(), target.getLocation().getY()-toSpawn.getY(), target.getLocation().getZ()-toSpawn.getZ());
											final Arrow arrow = target.getWorld().spawnArrow(toSpawn, direction, .6F, 12);
											arrow.setCritical(false);
											arrow.setBounce(false);
											arrow.setKnockbackStrength(0);
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													arrow.remove();
													Core.playAnimEffect(Particle.EXPLOSION_LARGE, target.getLocation(), 1, 1, 1, .5F, 5);
													target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(5);
										}else if(index == number-1){
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													target.setVelocity(new Vector(0,-1.75,0));
													Core.playAnimEffect(Particle.EXPLOSION_HUGE, target.getLocation(), 1, 1, 1, .5F, 5);
													target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);
													new BukkitTask(Core.instance){

														@Override
														public void run() {
															if(target.isDead())this.cancel();
															else if(target.isOnGround()){
																this.cancel();
																target.setFallDistance(0);
																Core.playAnimEffect(Particle.LAVA, target.getLocation(), .3F, .3F, .3F, 1, 23);
																Core.playAnimEffect(Particle.CLOUD, target.getLocation(), .5F, .5F, .5F, .1F, 46);
																target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
																DamageManager.damage(target, player, getInstance().getDamages(rp), RDamageCause.ABILITY);
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
				
	}),
	
	
	MAGE_1(new Ability("Pluie de météores", Arrays.asList("Le mage invoque une pluie de météores", "au dessus de la cible"),
			RClass.MAGE, Mastery.ADVENTURER, 1, false, "DGG,!SN", "Météores", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final double damages = this.getDamages(rp);
			final double x = player.getTargetBlock(null, 15).getX();
			final double y = player.getTargetBlock(null, 15).getY() - 1;
			final double z = player.getTargetBlock(null, 15).getZ();
			
			Location loc = new Location(player.getWorld(), x, y+5, z);
			MageMeteor explo = new MageMeteor(loc.clone().add(0, 3, 0), loc.clone().subtract(0, 3, 0));
			explo.setPlayer(player);
			explo.run();
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					new BukkitTask(Core.instance){
						int step = 0;

						@Override
						public void run() {
							if(step <= (int)damages){
								double a = RandomUtils.random.nextDouble() * 2 * Math.PI;
								double dist = RandomUtils.random.nextDouble() * 3;
								Location loc = (new Location(player.getWorld(), x, y+5, z)).clone().add(dist * Math.sin(a), 0, dist * Math.cos(a));
								Fireball f = player.getWorld().spawn(loc, Fireball.class);
								f.setMetadata("mageMeteor", new FixedMetadataValue(Core.instance, player.getUniqueId().toString()));
								f.setDirection(new Vector(0, -2.5, 0));
								f.setYield(0);
								f.setShooter(player);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_2(new Ability("Cercle de la mort", Arrays.asList("Le mage apelle la foudre pour la faire", "s'abattre sur les ennemis alentours"),
			RClass.MAGE, Mastery.ADVENTURER, 2, false, "DGD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			final Player player = rp.getPlayer();
			final List<LivingEntity> targets = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(4, 4, 4), RDamageCause.ABILITY);
			if(!targets.isEmpty()){
				this.takeVigor(rp);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1, .25F);
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						getInstance().damage(rp, targets);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskTimerCancelling(0,4,16);
			}else{
				rp.sendMessage("§cIl n'y a personne à foudroyer ici !");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.setActiveAbility(2, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			FakeLightning lightning = new FakeLightning(false);
			lightning.display(target);
		}
				
	}),
	MAGE_3(new Ability("Téléportation", Arrays.asList("Le mage courbe l'epace-temps pour", "se téléporter là où il regarde"),
			RClass.MAGE, Mastery.ADVENTURER, 3, false, "DDD,SP", "Portée", "blocs", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			int range = (int) this.getDamages(rp);
			float yaw = player.getEyeLocation().getYaw();
			float pitch = player.getEyeLocation().getPitch();
			double x = player.getTargetBlock(null, range).getX();
			double y = player.getTargetBlock(null, range).getY();
			double z = player.getTargetBlock(null, range).getZ();
	    	Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .001F, 75);
			TeleportHandler.teleport(player, new Location(player.getWorld(), x, y+1, z, yaw, pitch));
			player.setFallDistance(-100);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
	    	Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .001F, 75);
		    new BukkitTask(Core.instance){

				@Override
				public void run() {
		        	if(player.isDead())this.cancel();
		        	else{
		                if (((LivingEntity)player).isOnGround()){
		    				player.setFallDistance((float) 0.0);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_4(new Ability("Soin", Arrays.asList("Le mage crée une zone de soin", "instantané autour de lui"),
			RClass.MAGE, Mastery.ADVENTURER, 4, false, "DDD,SN", "Vie restaurée", "PV", 0) {
		
		@Override
		public void run(RPlayer rp) {
			this.takeVigor(rp);
			Player player = rp.getPlayer();
			GMember member = GMember.get(player);
			double x = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - player.getHealth();
			final double y = this.getDamages(rp);
			player.setHealth(player.getHealth() + (y > x ? x : y));
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
			Core.playAnimEffect(Particle.HEART, player.getEyeLocation().add(0,.5,0), 0, 0, 0, 1, 1);
			Core.playAnimEffect(Particle.VILLAGER_HAPPY, player.getLocation().add(0,1,0), .4F, .4F, .4F, 1, 60);
			List<Entity> near = player.getNearbyEntities(3.5, 3.5, 3.5);
			for(Player pnear : Core.toPlayerList(near)){
				GMember mtarget = GMember.get(pnear);
				if(!member.hasGuild() || (member.hasGuild() && mtarget.hasGuild())){
					if(!member.hasGuild() || member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
						double x1 = pnear.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - pnear.getHealth();
						pnear.setHealth(pnear.getHealth() + (y > x1 ? x1 : y));
						pnear.playSound(pnear.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
						Core.playAnimEffect(Particle.HEART, pnear.getEyeLocation().add(0,.5,0), 0, 0, 0, 1, 1);
						Core.playAnimEffect(Particle.VILLAGER_HAPPY, pnear.getLocation().add(0,1,0), .4F, .4F, .4F, 1, 60);
					}
				}
			}
			rp.setActiveAbility(4, false);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_5(new Ability("Vent glacé", Arrays.asList("Le mage façonne un vent glacé", "blessant et ralentissant les ennemis touchés"),
			RClass.MAGE, Mastery.MASTER, 5, false, "DGG", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					for(int i = 0;i < 3;i++){
						Vector v = RandomUtils.getRandomVector();
						double factor = 4 * RandomUtils.random.nextDouble();
						v.normalize().multiply(factor);
						player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection().multiply(1.5));
						Block b = player.getLocation().subtract(0,1,0).getBlock();
						Block block = player.getLocation().getBlock();
						if(block.getType().equals(Material.AIR) && !block.getType().equals(Material.SNOW) && b.getType().isSolid() && !b.getType().toString().contains("FENCE")){
					        for(Player player : Bukkit.getOnlinePlayers()){
					        	Snow snow = (Snow) Material.SNOW.createBlockData();
					        	snow.setLayers(RandomUtils.random.nextInt(4));
					        	player.sendBlockChange(block.getLocation(), snow);
					        }
						}
						for(BlockFace bf : BlockFace.values()){
							Block relative = block.getRelative(bf);
							if((relative.getType().equals(Material.AIR) || relative.getType().equals(Material.TALL_GRASS)) && !block.getType().equals(Material.SNOW) && b.getRelative(bf).getType().isSolid()){
						        for(Player player : Bukkit.getOnlinePlayers()){
						        	Snow snow = (Snow) Material.SNOW.createBlockData();
						        	snow.setLayers(RandomUtils.random.nextInt(4));
						        	player.sendBlockChange(relative.getLocation(), snow);
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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, true, false), true);
		}
				
	}),
	MAGE_6(new Ability("Kinésitechnie", Arrays.asList("Le mage maîtrise les pouvoirs kinétiques,", "chamboulant la situation dans les alentours"),
			RClass.MAGE, Mastery.MASTER, 6, false, "GDD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			final Player player = rp.getPlayer();
			final LivingEntity target = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 5.5);
			if(target != null && DamageManager.canDamage(player, target, RDamageCause.ABILITY)){
				this.takeVigor(rp);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 70, 255, true, false), true);
				final List<LivingEntity> near = Core.toDamageableLivingEntityList(player, target.getNearbyEntities(1, 1, 1), RDamageCause.ABILITY);
				final Vector[] relatives = new Vector[near.size()];
				for(int i = 0;i < near.size();i++){
					LivingEntity e = near.get(i);
					e.setGravity(false);
					relatives[i] = e.getLocation().toVector().subtract(target.getLocation().toVector());
				}
				target.setGravity(false);
				
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.91F);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.94F);
							}

							@Override
							public void onCancel() {
								new BukkitTask(Core.instance){

									@Override
									public void run() {
										player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.96F);
									}

									@Override
									public void onCancel() {
										new BukkitTask(Core.instance){

											@Override
											public void run() {
												player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.98F);
											}

											@Override
											public void onCancel() {
												new BukkitTask(Core.instance){

													@Override
													public void run() {
														player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 2.0F);
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
				
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(6.5));
						for(int i = 0;i < near.size();i++){
							LivingEntity e = near.get(i);
							e.teleport(location.clone().add(relatives[i]), TeleportCause.PLUGIN);
							Core.playAnimEffect(Particle.SPELL_WITCH, e.getLocation().add(0,-.15,0), .2F, .8F, .2F, .65F, 3);
							relatives[i] = relatives[i].multiply(.96);
						}
						target.teleport(location, TeleportCause.PLUGIN);
						Core.playAnimEffect(Particle.SPELL_WITCH, target.getLocation().add(0,-.15,0), .2F, .8F, .2F, .65F, 3);
					}

					@Override
					public void onCancel() {
						near.add(target);
						getInstance().damage(rp, near);
						rp.setActiveAbility(6, false);
					}
					
				}.runTaskTimerCancelling(0,0,70);
			}else{
				rp.sendMessage("§cVous ne visez aucun ennemi atteignable !");
				rp.setActiveAbility(6, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			target.setGravity(true);
			target.setFallDistance(-100);
			target.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, target.getLocation(), 1);
			target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10.0F, 1F);
			target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize());
		}
				
	}),
	MAGE_7(new Ability("Armée du Nether", Arrays.asList("Le mage invoque une armée de minions", "des enfers qui le défendra"),
			RClass.MAGE, Mastery.HERO, 7, false, "DDG", "Minions", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			rp.setActiveAbility(7, true);
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final double damages = this.getDamages(rp);
			for(int i = 0;i < damages;i++){
				new BukkitTask(Core.instance){
					
					@Override
					public void run(){
						Vector direction = new Vector(RandomUtils.random.nextDouble(), 0, RandomUtils.random.nextDouble()).multiply(RandomUtils.random.nextInt(4)*(RandomUtils.random.nextDouble()+1));
						Location destination = player.getLocation().toVector().add(direction).toLocation(player.getWorld());
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
						Weapon sword = Weapons.nearest(rp.getRLevel()-12, true, null, RandomUtils.random.nextBoolean() ? "SWORD" : "AXE");
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
						
			            Core.playAnimEffect(Particle.CLOUD, zombie.getLocation(), .5F, .5F, .5F, .1F, 34);
			            Core.playAnimEffect(Particle.LAVA, zombie.getLocation(), .5F, .5F, .5F, .1F, 11);
			            zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, .8F, 1);
			            
			            new BukkitTask(Core.instance){

							@Override
							public void run() {
								if(!zombie.isDead()){
						            Core.playAnimEffect(Particle.CLOUD, zombie.getLocation(), .5F, .5F, .5F, .1F, 23);
						            Core.playAnimEffect(Particle.LAVA, zombie.getLocation(), .5F, .5F, .5F, .1F, 8);
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
	        
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					rp.setActiveAbility(7, false);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater((long) (damages*7+21*20));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_8(new Ability("Armageddon", Arrays.asList("Le mage crée un cercle de feu autour de lui", "avant de détruire le terrain"),
			RClass.MAGE, Mastery.HERO, 8, false, "DDD,!SN!SP", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			rp.setActiveAbility(8, true);
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.setWalkSpeed(0);
			
			final Location center = player.getLocation();
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
			new BukkitTask(Core.instance){
				int step = 0;
				int ticks = 0;
				int ticksStep = 0;

				@Override
				public void run() {
					if(step < duration){
						Core.playAnimEffect(Particle.SMOKE_LARGE, player.getLocation(), 2F, .1F, 2F, .1F, 11);
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, step);
						if(ticks == ticksBetween){
							if(blocks.size() > ticksStep){
								final int id = ticksStep;
								blocks.get(id).setType(Material.FIRE);
								new BukkitTask(Core.instance){

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
									Location destination = player.getLocation().toVector().add(new Vector(RandomUtils.random.nextDouble(), 0, RandomUtils.random.nextDouble()).multiply(RandomUtils.random.nextInt(4)*(RandomUtils.random.nextDouble()+RandomUtils.random.nextInt(2)+1))).toLocation(player.getWorld());
									lightning.display(destination);
									Core.playAnimEffect(Particle.EXPLOSION_LARGE, player.getLocation(), 1, 1, 1, .5F, 5);
									player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
									
									getInstance().damage(rp, Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY));
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
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							Core.playAnimEffect(Particle.EXPLOSION_HUGE, player.getLocation(), 1, 1, 1, .5F, 5);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);

							getInstance().damage(rp, Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY));

							player.setWalkSpeed(.2F);
							rp.setActiveAbility(8, false);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(29);
				}
				
			}.runTaskTimer(0, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	
	
	ASSASSIN_1(new Ability("Attaque furtive", Arrays.asList("L'assassin transperce l'ennemi cible", "extrêmement rapidement"),
			RClass.ASSASSIN, Mastery.ADVENTURER, 1, false, "DDG,!SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			Player player = rp.getPlayer();
			LivingEntity entity = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 4.0);
			if(entity != null){
				Location edloc = entity.getLocation();
				if(edloc.distanceSquared(player.getLocation()) < 33){
					this.takeVigor(rp);
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

					List<LivingEntity> targets = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(2, 2, 2), RDamageCause.ABILITY);
					this.damage(rp, targets);
				}else{
					rp.sendMessage("§cLa cible est trop loin !");
				}
			}else{
				rp.sendMessage("§cIl n'y a personne en face de vous !");
			}
			rp.setActiveAbility(1, false);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_2(new Ability("Camouflage", Arrays.asList("L'assassin se camoufle entièrement", "pendant un laps de temps"),
			RClass.ASSASSIN, Mastery.ADVENTURER, 2, false, "DDD,SN", "Durée", " sec", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .1F, 100);
			Core.playAnimEffect(Particle.CLOUD, player.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
			if(rp.isVanished())rp.setVanished(false);
			for(Player target : Bukkit.getOnlinePlayers()){
				target.hidePlayer(Core.instance, player);
			}
			final ItemStack helmet = player.getEquipment().getHelmet();
			if(helmet != null){
				ItemStack pumpkin = helmet.clone();
				pumpkin.setType(Material.PUMPKIN);
				player.getEquipment().setHelmet(pumpkin);
			}else player.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
			rp.sendMessage("§7Viouf! Plus personne ne vous voit !");
			new BukkitTask(Core.instance){
				public void run(){
					rp.sendMessage("§cVotre camouflage commence à disparaître !");
					new BukkitTask(Core.instance){
						public void run(){
							Core.playAnimEffect(Particle.FLAME, player.getLocation(), .5F, .5F, .5F, .1F, 100);
							Core.playAnimEffect(Particle.CLOUD, player.getEyeLocation(), .5F, .5F, .5F, .1F, 100);
							for(Player target : Bukkit.getOnlinePlayers()){
								target.showPlayer(Core.instance, player);
							}
							if(helmet != null)player.getEquipment().setHelmet(helmet);
							else player.getEquipment().setHelmet(new ItemStack(Material.AIR, 1));
							rp.sendMessage("§4Votre camouflage a disparu !");
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
				
			}.runTaskLater((long) (this.getDamages(rp)*20));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_3(new Ability("Vitesse flash", Arrays.asList("L'assassin se déplacer naturellement", "plus rapidement que quiconque"),
			RClass.ASSASSIN, Mastery.ADVENTURER, 3, false, "", "Vitesse", "%", 0) {
		
		@Override
		public void run(final RPlayer rp) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_4(new Ability("Évasion", Arrays.asList("L'assassin s'échappe du combat en lâchant", "une grenade le propulsant haut et loin"),
			RClass.ASSASSIN, Mastery.ADVENTURER, 4, false, "DGD,SN", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			Location eyes = player.getEyeLocation().getDirection().normalize().toLocation(player.getWorld());
			double x = eyes.getX()*1.65;
			double z = eyes.getZ()*1.65;
			player.getWorld().createExplosion(player.getLocation(), 0);
			List<LivingEntity> targets = Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY);
			this.damage(rp, targets);
			player.setVelocity(new Vector(-x*rp.getAbLevel(4)*.21, rp.getAbilityLevel(4)*0.07+.28, -z*rp.getAbLevel(4)*.21));
			player.setFallDistance(-100);
			new BukkitTask(Core.instance){

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

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_5(new Ability("Piège", Arrays.asList("L'assassin enlace sa cible de sorte qu'elle", "ne puisse bouger pendant un laps de temps"),
			RClass.ASSASSIN, Mastery.MASTER, 5, false, "DDG,SN", "Durée", " sec", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			int range = 9;
			final Player player = rp.getPlayer();
			final LivingEntity target = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(target != null){
				if(target.getLocation().distanceSquared(player.getLocation()) <= range*range){
					this.takeVigor(rp);
					final AttributeModifier modifier = new AttributeModifier("RubidiaAssassinTrap", -10000.0, Operation.ADD_NUMBER);
					if(target instanceof Player){
						((Player) target).setWalkSpeed(0);
					}else{
						target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
					}
					target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false), true);
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							Block block = target.getLocation().subtract(0,1,0).getBlock();
							Core.playAnimEffect(Particle.SPELL_WITCH, target.getLocation(), .3F, .1F, .3F, .1F, 4);
							Core.playAnimEffect(Particle.SPELL_INSTANT, target.getLocation(), .1F, .1F, .1F, .1F, 1);
							Core.playAnimEffect(Particle.BLOCK_CRACK, target.getLocation(), .3F, .1F, .3F, .1F, 4, block.getBlockData());
						}

						@Override
						public void onCancel() {
							if(target instanceof Player){
								((Player) target).setWalkSpeed(.2F);
							}else{
								target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
							}
							target.removePotionEffect(PotionEffectType.JUMP);
							rp.setActiveAbility(5, false);
						}
						
					}.runTaskTimerCancelling(0, 1, (long) this.getDamages(rp)*20);
				}else{
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(5, false);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(5, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_6(new Ability("Diversion", Arrays.asList("L'assassin crée un épais nuage", "empoisonné autour de lui"),
			RClass.ASSASSIN, Mastery.MASTER, 6, false, "DDD,!SN", "", " / sec", 5) {
		
		@Override
		public void run(final RPlayer rp) {
			Player player = rp.getPlayer();
			Core.playAnimEffect(Particle.CLOUD, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 50);
			Core.playAnimEffect(Particle.REDSTONE, player.getLocation().add(0,1,0), 1, 1, 1, 0, 42);
			Core.playAnimEffect(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 22);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
			Player player = rp.getPlayer();
			Core.playAnimEffect(Particle.CLOUD, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 50);
			Core.playAnimEffect(Particle.REDSTONE, player.getLocation().add(0,1,0), 1, 1, 1, 0, 42);
			Core.playAnimEffect(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 1, 1, 1, .001F, 22);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
			this.damage(rp, Core.toDamageableLivingEntityList(player, player.getNearbyEntities(3, 3, 3), RDamageCause.ABILITY), .25);
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	}),
	ASSASSIN_7(new Ability("Attaque perçante", Arrays.asList("L'assassin traverse sa cible,", "l'obligeant à reculer de quelques blocs"),
			RClass.ASSASSIN, Mastery.HERO, 7, false, "GGD,SP", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			double range = 7;
			
			final Player player = rp.getPlayer();
			final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(e != null){
				if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
					this.takeVigor(rp);
					Location entityLoc = e.getLocation();
					Location pLoc = player.getLocation();
					Vector motion = new Vector(entityLoc.getX()-pLoc.getX(), entityLoc.getY()-pLoc.getY(),entityLoc.getZ()-pLoc.getZ()).multiply(.45);
					player.setMetadata("assassinGamemode", new FixedMetadataValue(Core.instance, player.getGameMode().toString()));
					player.setGameMode(GameMode.SPECTATOR);
					player.setVelocity(motion);
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							if(player.getLocation().distanceSquared(e.getLocation()) < 3){
								player.setGameMode(GameMode.valueOf(player.getMetadata("assassinGamemode").get(0).asString()));
								DamageManager.damage(e, player, getInstance().getDamages(rp), RDamageCause.ABILITY);
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
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(7, true);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(7, true);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_8(new Ability("Super Smash", Arrays.asList("L'assassin dash très violemment sur sa cible"),
			RClass.ASSASSIN, Mastery.HERO, 8, false, "DGD", "", "", 0) {
		
		@Override
		public void run(final RPlayer rp) {
			double range = 6;
			
			final Player player = rp.getPlayer();
			final double damages = this.getDamages(rp);
			final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(e != null){
				if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
					player.setSprinting(false);
					this.takeVigor(rp);
					Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
					player.setVelocity(toe.multiply(.5).add(new Vector(0,1,0)));
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
							player.setVelocity(toe);
							new BukkitTask(Core.instance){

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
												DamageManager.damage(enear, player, damages/2., RDamageCause.ABILITY);
												enear.setVelocity(new Vector(0,1.5,0));
												enear.setFallDistance(-100);
											}
											TeleportHandler.teleport(player, new Location(nearest.get(0).getWorld(), nearest.get(0).getLocation().getX(),nearest.get(0).getLocation().getY(),nearest.get(0).getLocation().getZ(), player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch()));
											player.setVelocity(new Vector(0,1.62,0));
											player.setFallDistance(-100);
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													Core.playAnimEffect(Particle.EXPLOSION_LARGE, player.getLocation(), 1, 1, 1, 1, 7);
													player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
													
													for(LivingEntity enear : nearest){
														enear.setVelocity(new Vector(0,-2,0));
													}

													final List<LivingEntity> onGround = new ArrayList<LivingEntity>();
													new BukkitTask(Core.instance){

														@Override
														public void run() {
															if(!onGround.containsAll(nearest)){
																for(LivingEntity enear : nearest){
																	if(enear.isOnGround()){
																		if(!onGround.contains(enear)){
																			Core.playAnimEffect(Particle.LAVA, enear.getLocation(), .5F, .5F, .5F, 1, 50);
																			enear.getWorld().playSound(enear.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
																			DamageManager.damage(enear, player, damages/2., RDamageCause.ABILITY);
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
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(8, false);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(8, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			
			
		}
				
	});
	
	private final Ability ability;
	private RAbility(Ability ability) {
		this.ability = ability;
	}
	
	
	public Ability getAbility() {
		return ability;
	}
	
	public List<String> getDescription() {
		return ability.getDescription();
	}

	public boolean isPassive() {
		return ability.isPassive();
	}

	public String getSequence() {
		return ability.getSequence();
	}

	public String getName() {
		return ability.getName();
	}

	public Mastery getMastery() {
		return ability.getMastery();
	}

	public String getSuppInfo() {
		return ability.getSuppInfo();
	}

	public String getUnit() {
		return ability.getUnit();
	}

	public int getToggleableTicks() {
		return ability.getToggleableTicks();
	}
	
	public boolean isToggleable() {
		return ability.isToggleable();
	}

	public RClass getRClass() {
		return ability.getRClass();
	}

	public int getIndex() {
		return ability.getIndex();
	}
	
	public void execute(RPlayer rp) {
		ability.execute(rp);
	}

	public double getDamages(RPlayer rp) {
		return ability.getDamages(rp);
	}
	
	public double getVigorCost(RPlayer rp) {
		return ability.getVigorCost(rp);
	}

	public RAbilitySettings getSettings() {
		return ability.getSettings();
	}

	public static List<RAbility> getAvailable(RPlayer rp) {
		List<RAbility> abilities = new ArrayList<RAbility>();
		if(!rp.getRClass().equals(RClass.VAGRANT)) {
			for(int i = 1;i < 9;i++){
				RAbility ability = RAbility.valueOf(rp.getRClass().toString() + "_" + i);
				if(rp.getAbilityLevel(i) > 0 && (!rp.isActiveAbility(i) || ability.isToggleable())){
					abilities.add(ability);
				}
			}
		}
		return abilities;
	}
	
	
	public static List<RAbility> getAvailable(RClass rClass){
		List<RAbility> abilities = new ArrayList<RAbility>();
		if(!rClass.equals(RClass.VAGRANT)) {
			for(int i = 1;i < 9;i++){
				abilities.add(RAbility.valueOf(rClass.toString() + "_" + i));
			}
		}
		return abilities;
	}

}
