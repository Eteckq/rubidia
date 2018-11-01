package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
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
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;

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
									String usage = weapon.canUse(rp);
									if(!weapon.isAttack()){
										if(!usage.isEmpty()) {
											event.setCancelled(true);
											String name = is.getType().toString();
											if(name.contains("_HELMET")) {
												rp.sendMessage("§cVous ne pouvez porter ce casque car " + usage);
											} else if(name.contains("_CHESTPLATE")) {
												rp.sendMessage("§cVous ne pouvez porter ce plastron car " + usage);
											} else if(name.contains("_LEGGINGS")) {
												rp.sendMessage("§cVous ne pouvez porter cette paire de gants car " + usage);
											} else if(name.contains("_BOOTS")) {
												rp.sendMessage("§cVous ne pouvez porter cette paire de bottes car " + usage);
											}
											player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
										}
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
						String usage = weapon.canUse(rp);
						if(weapon.isAttack()){
							if(usage.isEmpty()){
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
									if(event.getAction().equals(Action.RIGHT_CLICK_AIR)
											|| (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getClickedBlock().getType().isInteractable())){
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
							}/* else {
								WeaponsListener.wrongDamages(rp);
								event.setCancelled(true);
							}*///useless now that we reset mainHand item if weapon is not usable (see Weapons.checkEquipment)
						} else {
							if(!usage.isEmpty()) {
								event.setCancelled(true);
								String name = is.getType().toString();
								if(name.contains("_HELMET")) {
									rp.sendMessage("§cVous ne pouvez porter ce casque car " + usage);
								} else if(name.contains("_CHESTPLATE")) {
									rp.sendMessage("§cVous ne pouvez porter ce plastron car " + usage);
								} else if(name.contains("_LEGGINGS")) {
									rp.sendMessage("§cVous ne pouvez porter cette paire de gants car " + usage);
								} else if(name.contains("_BOOTS")) {
									rp.sendMessage("§cVous ne pouvez porter cette paire de bottes car " + usage);
								} else if(is.getType().equals(Material.SHIELD)) {
									rp.sendMessage("§cVous ne pouvez utiliser ce bouclier car " + usage);
								}
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
							} else {
								if(is.getType().equals(Material.SHIELD)) {
									event.setCancelled(true);
									ItemStack offHand = player.getEquipment().getItemInOffHand();
									player.getEquipment().setItemInOffHand(player.getEquipment().getItemInMainHand());
									player.getEquipment().setItemInMainHand(offHand);
									player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, 1);
									Utils.updateInventory(player);
								}
							}
						}
					} else if(is.getType().toString().contains("_HELMET")) {
						event.setCancelled(true);
						rp.sendMessage("§cCe casque doit être éveillé avant d'être porté");
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					} else if(is.getType().toString().contains("_CHESTPLATE")) {
						event.setCancelled(true);
						rp.sendMessage("§cCe plastron doit être éveillé avant d'être porté");
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					} else if(is.getType().toString().contains("_LEGGINGS")) {
						event.setCancelled(true);
						rp.sendMessage("§cCette paire de gants doit être éveillée avant d'être portée");
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					} else if(is.getType().toString().contains("_BOOTS")) {
						event.setCancelled(true);
						rp.sendMessage("§cCette paire de bottes doit être éveillée avant d'être portée");
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					} else if(is.getType().equals(Material.SHIELD)) {
						event.setCancelled(true);
						rp.sendMessage("§cCe bouclier doit être éveillé avant d'être porté");
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerShear(PlayerShearEntityEvent event) {
		ItemStack mainHand = event.getPlayer().getEquipment().getItemInMainHand();
		if(mainHand != null) {
			if(mainHand.getType().equals(Material.SHEARS)) {
				RItem rItem = new RItem(mainHand);
				if(rItem.isWeapon()) {
					event.setCancelled(true);
				}
			}
		} else {
			ItemStack offHand = event.getPlayer().getEquipment().getItemInOffHand();
			if(offHand != null) {
				if(offHand.getType().equals(Material.SHEARS)) {
					RItem rItem = new RItem(offHand);
					if(rItem.isWeapon()) {
						event.setCancelled(true);
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
												if(/*!*/weapon.canUse(rp).isEmpty())/*{
													WeaponsListener.wrongDamages(rp);
													return;//useless now that we reset mainHand if weapon not usable (see Weapons.checkEquipment)
												}else*/{
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
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		ItemStack item = event.getItem();
		if(Weapons.TOOLS.contains(item.getType())) {
			int realDamage = ((Damageable) item.getItemMeta()).getDamage();
			double damageStep = Weapons.getSkinFactor(item.getType());
			double damageShift = damageStep/2.;
			if(realDamage > 0) {//probability = 1/N where N is the parameter of the uniform distribution (mean at N/2 so...)
				if(RandomUtils.random.nextDouble() < 1./(Settings.TOOLS_DAMAGE_CORRECTOR*item.getType().getMaxDurability()*damageStep)) {
					double damageFactor = realDamage/((double) item.getType().getMaxDurability());
					int currentStep = (int) ((damageFactor - damageShift)/damageStep + 1);
					int nextNearestDamage = (int) Math.ceil(item.getType().getMaxDurability()*(damageStep*currentStep + damageShift));
					int damage = nextNearestDamage - realDamage;
					event.setDamage(damage);
					return;
				} else {
					event.setCancelled(true);
					return;
				}
			} else {
				event.setDamage((int) Math.ceil(item.getType().getMaxDurability()*damageShift));
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemChange(PlayerItemHeldEvent event){
		if(!event.isCancelled()){
			Player player = event.getPlayer();
			RPlayer rp = RPlayer.get(player);
			ItemStack oldStack = player.getInventory().getItem(event.getPreviousSlot());
			if(oldStack != null){
				RItem oldItem = new RItem(oldStack);
				if(oldItem.isWeapon()){
					Weapon weapon = oldItem.getWeapon();
					if(weapon.isAttack()) {
						if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
							if(rp.isReloading(weapon)){
								rp.getReloadingWeapons().get(weapon.getUUID()).cancel();
							}
						}
					}
				}
			}

			ItemStack newStack = player.getInventory().getItem(event.getNewSlot());
			if(newStack != null){
				RItem newItem = new RItem(newStack);
				if(newItem.isWeapon()){
					if(player.getGameMode().equals(GameMode.SURVIVAL)) {
						player.setGameMode(GameMode.ADVENTURE);
					}

					Weapon weapon = newItem.getWeapon();
					if(weapon.isAttack()) {
						String usage = weapon.canUse(rp);
						if(usage.isEmpty()) {
							if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
								if(rp.isReloading(weapon)){
									rp.reloadWeapon(weapon);
								}
							}
						} else {
							event.setCancelled(true);
							rp.sendMessage("§cVous ne pouvez utiliser cette arme car " + usage);
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
						}
					}
				} else {
					if(player.getGameMode().equals(GameMode.ADVENTURE)) {
						player.setGameMode(GameMode.SURVIVAL);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemChange(PlayerSwapHandItemsEvent event) {
		if(!event.isCancelled()){
			Player player = event.getPlayer();
			RPlayer rp = RPlayer.get(player);
			ItemStack offHand = event.getOffHandItem();
			if(offHand != null){
				RItem offItem = new RItem(offHand);
				if(offItem.isWeapon()){
					Weapon weapon = offItem.getWeapon();
					if(weapon.isAttack()) {
						if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
							if(rp.isReloading(weapon)){
								rp.getReloadingWeapons().get(weapon.getUUID()).cancel();
							}
						}
					} else {
						if(offHand.getType().equals(Material.SHIELD)) {
							String usage = weapon.canUse(rp);
							if(!usage.isEmpty()) {
								event.setCancelled(true);
								rp.sendMessage("§cVous ne pouvez utiliser ce bouclier car " + usage);
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
							}
						}
					}
				} else if(offHand.getType().equals(Material.SHIELD)) {
					event.setCancelled(true);
					rp.sendMessage("§cCe bouclier doit être éveillé avant d'être porté");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				}
			}

			ItemStack mainHand = event.getMainHandItem();
			if(mainHand != null){
				RItem mainItem = new RItem(mainHand);
				if(mainItem.isWeapon()){
					if(player.getGameMode().equals(GameMode.SURVIVAL)) {
						player.setGameMode(GameMode.ADVENTURE);
					}
					
					Weapon weapon = mainItem.getWeapon();
					if(weapon.isAttack()) {
						String usage = weapon.canUse(rp);
						if(usage.isEmpty()) {
							if(weapon.getWeaponUse().equals(WeaponUse.RANGE)){
								if(rp.isReloading(weapon)){
									rp.reloadWeapon(weapon);
								}
							}
						} else {
							event.setCancelled(true);
							rp.sendMessage("§cVous ne pouvez utiliser cette arme car " + usage);
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
						}
					}
				} else {
					if(player.getGameMode().equals(GameMode.ADVENTURE)) {
						player.setGameMode(GameMode.SURVIVAL);
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
			double ratio = Math.min((System.currentTimeMillis()-rp.getLastAttack())*weapon.getAttackSpeed()*attackSpeedFactor/1000.,1.);
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
