package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.events.RPlayerUseWeaponEvent;
import me.pmilon.RubidiaCore.mage.MageAttack;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaGuilds.utils.Settings;

public class WeaponsListener implements Listener {
	
	private static HashMap<RPlayer, Integer> wrongDamages = new HashMap<RPlayer, Integer>();
	private static HashMap<Projectile, ItemStack> projectiles = new HashMap<Projectile, ItemStack>();
	private static HashMap<Projectile, Boolean> critical = new HashMap<Projectile, Boolean>();

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event){
		final Player player = (Player)event.getWhoClicked();
		final RPlayer rp = RPlayer.get(player);
		final Inventory inv = event.getInventory();
		if(inv != null){
			if(inv.getType().equals(InventoryType.CRAFTING)){
				if(event.getSlotType().equals(SlotType.ARMOR)){
					ItemStack is = event.getCursor();
					if(is != null){
						if(!is.getType().equals(Material.AIR)){
							RItem rItem = new RItem(is);
							if(rItem.isWeapon()){
								Weapon weapon = rItem.getWeapon();
								if(weapon != null){
									if(!weapon.isAttack() && !weapon.canUse(rp)){
										event.setCancelled(true);
										rp.sendMessage("§cVous ne pouvez utiliser cette pièce d'armure !");
										player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event){
		ItemStack item = null;
		Projectile projectile = event.getEntity();
		ProjectileSource source = projectile.getShooter();
		if(source instanceof LivingEntity){
			LivingEntity damager = (LivingEntity)source;
			item = damager.getEquipment().getItemInMainHand();
			if(damager instanceof Player){
				RItem rItem = new RItem(item);
				if(rItem.isWeapon()){//no need to know if weapon is attack and rp can use because interact event is called before
					RPlayerUseWeaponEvent e = new RPlayerUseWeaponEvent(RPlayer.get((Player)damager), rItem.getWeapon());
					Bukkit.getPluginManager().callEvent(e);
					if(e.isCancelled()){
						event.setCancelled(true);
						return;
					}
				}
			}
			critical.put(projectile, !damager.isOnGround());
		}
		projectiles.put(projectile, item);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				final Player player = event.getPlayer();
				final RPlayer rp = RPlayer.get(player);
				ItemStack is = player.getEquipment().getItemInMainHand();
				if(is != null){
					RItem rItem = new RItem(is);
					if(rItem.isWeapon()){
						Weapon weapon = rItem.getWeapon();
						if(weapon.isAttack()){
							if(weapon.canUse(rp)){
								final boolean critical = !player.isOnGround();
								if(weapon.getWeaponUse().equals(WeaponUse.MAGIC)){
									RPlayerUseWeaponEvent evt = new RPlayerUseWeaponEvent(rp, weapon);
									Bukkit.getPluginManager().callEvent(evt);
									if(!evt.isCancelled()){
										if(event.getAction().toString().contains("LEFT_CLICK")){
											event.setCancelled(true);
											
											if(rp.canAttackMagic){
												rp.canAttackMagic = false;
										        new MageAttack(player, is, critical).run();
												new BukkitTask(Core.instance){
													public void run(){
														rp.canAttackMagic = true;
													}

													@Override
													public void onCancel() {
													}
												}.runTaskLater(3);
											}
										}
									}else event.setCancelled(true);
								}else if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
									if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !Settings.INTERACT_BLOCKS.contains(event.getClickedBlock().getType()))){
										event.setCancelled(true);
										
										if(!rp.getReloadingWeapons().containsKey(weapon.getUUID())){
											final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().normalize().multiply(5.0));
											arrow.setCritical(true);
											arrow.setMetadata("unpickable", new FixedMetadataValue(Core.instance, true));
											if(is.getItemMeta().hasEnchant(Enchantment.ARROW_KNOCKBACK)){
												arrow.setKnockbackStrength(is.getItemMeta().getEnchantLevel(Enchantment.ARROW_KNOCKBACK)*2);
											}
											player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-1));
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													if(arrow.isOnGround()){
														this.cancel();
													}else{
														if(critical){
															arrow.getLocation().getWorld().spawnParticle(Particle.CRIT_MAGIC, arrow.getLocation(), 1, 0, 0, 0);
														}
													}
												}

												@Override
												public void onCancel() {
													new BukkitTask(Core.instance){

														@Override
														public void run() {
															arrow.remove();
														}

														@Override
														public void onCancel() {
														}
														
													}.runTaskLater(100);
												}
												
											}.runTaskTimer(0, 0);

											rp.reloadWeapon(weapon);
										}
									}
								}
							}else{
								WeaponsListener.wrongDamages(rp);
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR) //To keep worldguard protection!
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(!e.isCancelled()){
			Entity damager = e.getDamager();
			Entity entity = e.getEntity();

			if(entity instanceof LivingEntity){
				LivingEntity damaged = (LivingEntity)entity;
				if(damager instanceof Projectile){
					Projectile projectile = (Projectile) damager;
					ProjectileSource source = projectile.getShooter();
					if(source instanceof LivingEntity){
						LivingEntity launcher = (LivingEntity)source;
						if(projectiles.containsKey(projectile)){
							ItemStack item = projectiles.get(projectile);
							projectiles.remove(projectile);
							if(item != null){
								e.setCancelled(true);
								double damages = DamageManager.getDamages(launcher, damaged, item, RDamageCause.RANGE, projectile instanceof Arrow ? ((Arrow)projectile).isCritical() : false, false);
								DamageManager.damageEvent(e, damages, RDamageCause.RANGE, critical.containsKey(projectile) ? critical.get(projectile) : false);
							}
						}
					}
				}else if(damager instanceof LivingEntity){
					LivingEntity entity1 = (LivingEntity)damager;
					if(e.getCause().equals(DamageCause.ENTITY_ATTACK)){
						if(e.getDamage() > 0){
							e.setCancelled(true);
							ItemStack item = entity1.getEquipment().getItemInMainHand();
							if(item != null){
								RItem rItem = new RItem(item);
								if(rItem.isWeapon()){
									Weapon weapon = rItem.getWeapon();
									if(entity1 instanceof Player){
										Player player = (Player)entity1;
										RPlayer rp = RPlayer.get(player);
										if(weapon != null){
											if(weapon.isAttack()){
												if(!weapon.canUse(rp)){
													WeaponsListener.wrongDamages(rp);
													return;
												}else{
													RPlayerUseWeaponEvent event = new RPlayerUseWeaponEvent(rp, weapon);
													Bukkit.getPluginManager().callEvent(event);
													if(event.isCancelled())return;
												}
											}
										}
									}
								}
								rItem = null;
							}
							double damages = DamageManager.getDamages(entity1, damaged, item, RDamageCause.MELEE, !entity1.isOnGround(), false);
							DamageManager.damageEvent(e, damages, RDamageCause.MELEE, !entity1.isOnGround());
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemChange(PlayerItemHeldEvent event){
		if(!event.isCancelled()){
			Player player = event.getPlayer();
			ItemStack oldStack = player.getInventory().getItem(event.getPreviousSlot());
			if(oldStack != null){
				RItem oldItem = new RItem(oldStack);
				if(oldItem.isWeapon()){
					Weapon weapon = oldItem.getWeapon();
					if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
						RPlayer rp = RPlayer.get(player);
						if(rp.getReloadingWeapons().containsKey(weapon.getUUID())){
							rp.getReloadingWeapons().get(weapon.getUUID()).cancel();
						}
					}
				}
			}

			ItemStack newStack = player.getInventory().getItem(event.getNewSlot());
			if(newStack != null){
				RItem newItem = new RItem(newStack);
				if(newItem.isWeapon()){
					Weapon weapon = newItem.getWeapon();
					if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
						RPlayer rp = RPlayer.get(player);
						if(rp.getReloadingWeapons().containsKey(weapon.getUUID())){
							rp.getReloadingWeapons().get(weapon.getUUID()).run();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onAttack(RPlayerUseWeaponEvent event){
		RPlayer rp = event.getRPlayer();
		Weapon weapon = event.getWeapon();
		if(weapon != null && rp.isOnline()){
			double attackSpeedFactor = rp.getAttackSpeedFactor();
			double ratio = Math.min((System.currentTimeMillis()-rp.getLastAttack())*weapon.getAttackSpeed()*attackSpeedFactor/1000.0D,1.0);
			rp.setNextAttackFactor(Math.pow(ratio,2));
			rp.setLastAttack(System.currentTimeMillis());
			rp.getPlayer().setCooldown(weapon.getType(), (int)Math.round((1-ratio)*20/(weapon.getAttackSpeed()*attackSpeedFactor)));
		}
	}
	
	public static void wrongDamages(RPlayer rp){
		if(!rp.isOp()){
			if(wrongDamages.containsKey(rp)){
				int damages = wrongDamages.get(rp)+1;
				if(damages > 6)damages = 1;
				wrongDamages.put(rp, damages);
			}else wrongDamages.put(rp, 1);
			
			if(wrongDamages.get(rp) == 1)rp.sendMessage("§cVous ne savez pas manier cette arme !");
		}
		
	}
}
