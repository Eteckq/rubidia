package me.pmilon.RubidiaCore.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RJob;
import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

@SuppressWarnings("deprecation")
public class JobsHandler implements Listener {
	
	static Core plugin;
	public JobsHandler(Core core){
		plugin = core;
	}
	
	public enum JobTask {
		MINE_COAL(32), MINE_IRON(16), MINE_GOLD(8), MINE_DIAMOND(2),
		BUTCH_LOG(8), BUTCH_LEAVE(48),
		FARM_WHEAT(16), FARM_CARROT(16), FARM_POTATO(16), FARM_PUMPKIN(12), FARM_MELON(12), CRAFT_CAKE(4), FARM_SUGARCANE(24),
		KILL(12),
		BREW_POTION(3);
		
		private int amount;
		private JobTask(int amount){
			this.amount = amount;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	
	public HashMap<Location, Boolean> brewed = new HashMap<Location, Boolean>();
	public HashMap<Player, Location> brewinginventoryopen = new HashMap<Player, Location>();
	public HashMap<Player, Inventory> brewinginventory = new HashMap<Player, Inventory>();
	
	@EventHandler
	public void onBreakJob(BlockBreakEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Block b = e.getBlock();
		if(rp.getRJob().equals(RJob.MINER)){
			if(p.getItemInHand().getType().toString().contains("_PICKAXE")){
				if(b.getType().equals(Material.COAL_ORE)){
					if(rp.getJobScores().containsKey(JobTask.MINE_COAL)){
						rp.getJobScores().put(JobTask.MINE_COAL, rp.getJobScores().get(JobTask.MINE_COAL)+1);
					}else rp.getJobScores().put(JobTask.MINE_COAL, 1);
				}else if(b.getType().equals(Material.IRON_ORE) || b.getType().equals(Material.REDSTONE_ORE)){
					if(rp.getJobScores().containsKey(JobTask.MINE_IRON)){
						rp.getJobScores().put(JobTask.MINE_IRON, rp.getJobScores().get(JobTask.MINE_IRON)+1);
					}else rp.getJobScores().put(JobTask.MINE_IRON, 1);
				}else if(b.getType().equals(Material.GOLD_ORE) || b.getType().equals(Material.LAPIS_ORE)){
					if(rp.getJobScores().containsKey(JobTask.MINE_GOLD)){
						rp.getJobScores().put(JobTask.MINE_GOLD, rp.getJobScores().get(JobTask.MINE_GOLD)+1);
					}else rp.getJobScores().put(JobTask.MINE_GOLD, 1);
				}else if(b.getType().equals(Material.DIAMOND_ORE)){
					if(rp.getJobScores().containsKey(JobTask.MINE_DIAMOND)){
						rp.getJobScores().put(JobTask.MINE_DIAMOND, rp.getJobScores().get(JobTask.MINE_DIAMOND)+1);
					}else rp.getJobScores().put(JobTask.MINE_DIAMOND, 1);
				}
			}
		}else if(rp.getRJob().equals(RJob.LUMBERMAN)){
			if(p.getItemInHand().getType().toString().contains("_AXE")){
				if(b.getType().equals(Material.LOG) || b.getType().equals(Material.LOG_2)){
					if(rp.getJobScores().containsKey(JobTask.BUTCH_LOG)){
						rp.getJobScores().put(JobTask.BUTCH_LOG, rp.getJobScores().get(JobTask.BUTCH_LOG)+1);
					}else rp.getJobScores().put(JobTask.BUTCH_LOG, 1);
				}else if(b.getType().equals(Material.LEAVES) || b.getType().equals(Material.LEAVES_2)){
					if(rp.getJobScores().containsKey(JobTask.BUTCH_LEAVE)){
						rp.getJobScores().put(JobTask.BUTCH_LEAVE, rp.getJobScores().get(JobTask.BUTCH_LEAVE)+1);
					}else rp.getJobScores().put(JobTask.BUTCH_LEAVE, 1);
				}
			}
		}else if(rp.getRJob().equals(RJob.FARMER)){
			ArrayList<BlockFace> blockfaces = new ArrayList<>(Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST));
			if(b.getType().equals(Material.CROPS)){
				if(b.getData() >= 7){
					if(rp.getJobScores().containsKey(JobTask.FARM_WHEAT)){
						rp.getJobScores().put(JobTask.FARM_WHEAT, rp.getJobScores().get(JobTask.FARM_WHEAT)+1);
					}else rp.getJobScores().put(JobTask.FARM_WHEAT, 1);
				}
			}else if(b.getType().equals(Material.CARROT)){
				if(b.getData() >= 7){
					if(rp.getJobScores().containsKey(JobTask.FARM_CARROT)){
						rp.getJobScores().put(JobTask.FARM_CARROT, rp.getJobScores().get(JobTask.FARM_CARROT)+1);
					}else rp.getJobScores().put(JobTask.FARM_CARROT, 1);
				}
			}else if(b.getType().equals(Material.POTATO)){
				if(b.getData() >= 7){
					if(rp.getJobScores().containsKey(JobTask.FARM_POTATO)){
						rp.getJobScores().put(JobTask.FARM_POTATO, rp.getJobScores().get(JobTask.FARM_POTATO)+1);
					}else rp.getJobScores().put(JobTask.FARM_POTATO, 1);
				}
			}else if(b.getType().equals(Material.PUMPKIN)){
				for(BlockFace bf : blockfaces){
					if(b.getRelative(bf).getType().equals(Material.PUMPKIN_STEM)){
						if(rp.getJobScores().containsKey(JobTask.FARM_PUMPKIN)){
							rp.getJobScores().put(JobTask.FARM_PUMPKIN, rp.getJobScores().get(JobTask.FARM_PUMPKIN)+1);
						}else rp.getJobScores().put(JobTask.FARM_PUMPKIN, 1);
						break;
					}
				}
			}else if(b.getType().equals(Material.MELON_BLOCK)){
				for(BlockFace bf : blockfaces){
					if(b.getRelative(bf).getType().equals(Material.MELON_STEM)){
						if(rp.getJobScores().containsKey(JobTask.FARM_MELON)){
							rp.getJobScores().put(JobTask.FARM_MELON, rp.getJobScores().get(JobTask.FARM_MELON)+1);
						}else rp.getJobScores().put(JobTask.FARM_MELON, 1);
						break;
					}
				}
			}else if(b.getType().equals(Material.SUGAR_CANE_BLOCK)){
				int amount = 0;
				for(int i = 0;i < 2;i++){
					if(b.getRelative(BlockFace.UP).getType().equals(Material.SUGAR_CANE_BLOCK)){
						amount++;
					}
				}
				if(rp.getJobScores().containsKey(JobTask.FARM_SUGARCANE)){
					rp.getJobScores().put(JobTask.FARM_SUGARCANE, rp.getJobScores().get(JobTask.FARM_SUGARCANE)+amount);
				}else rp.getJobScores().put(JobTask.FARM_SUGARCANE, amount);
			}
		}
	}
	
	@EventHandler
	public void onCraftJob(CraftItemEvent e){
		HumanEntity he = e.getWhoClicked();
		if(he instanceof Player){
			Player p = (Player)he;
			RPlayer rp = RPlayer.get(p);
			if(rp.getRJob().equals(RJob.FARMER)){
				if(e.getCurrentItem().getType().equals(Material.CAKE)){
					if(rp.getJobScores().containsKey(JobTask.CRAFT_CAKE)){
						rp.getJobScores().put(JobTask.CRAFT_CAKE, rp.getJobScores().get(JobTask.CRAFT_CAKE)+1);
					}else rp.getJobScores().put(JobTask.CRAFT_CAKE, 1);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onKillJob(EntityDeathEvent e){
		Entity es = e.getEntity();
		if(es instanceof LivingEntity){
			if(((LivingEntity) es).getKiller() instanceof Player){
				Player p = (Player)((LivingEntity)es).getKiller();
				RPlayer rp = RPlayer.get(p);
				if(rp.getRJob().equals(RJob.HUNTER)){
					if(es.getType().equals(EntityType.ZOMBIE) || es.getType().equals(EntityType.BLAZE) || es.getType().equals(EntityType.CAVE_SPIDER) || es.getType().equals(EntityType.CREEPER) || es.getType().equals(EntityType.ENDERMAN) || es.getType().equals(EntityType.GHAST) || es.getType().equals(EntityType.GUARDIAN) || es.getType().equals(EntityType.MAGMA_CUBE) || es.getType().equals(EntityType.PIG_ZOMBIE) || es.getType().equals(EntityType.SKELETON) || es.getType().equals(EntityType.SLIME) || es.getType().equals(EntityType.SPIDER) || es.getType().equals(EntityType.WITCH)){
						if(rp.getJobScores().containsKey(JobTask.KILL)){
							rp.getJobScores().put(JobTask.KILL, rp.getJobScores().get(JobTask.KILL)+1);
						}else rp.getJobScores().put(JobTask.KILL, 1);
					}else if(es.getType().equals(EntityType.ENDER_DRAGON) || es.getType().equals(EntityType.WITHER) || es.getType().equals(EntityType.GIANT)){
						if(rp.getJobScores().containsKey(JobTask.KILL)){
							rp.getJobScores().put(JobTask.KILL, rp.getJobScores().get(JobTask.KILL)+10);
						}else rp.getJobScores().put(JobTask.KILL, 10);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClickJob(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		RPlayer rp = RPlayer.get(p);
		if(e.getClickedInventory() != null){
			if(e.getClickedInventory().getType().equals(InventoryType.BREWING)){
				if(e.isShiftClick()){
					e.setCancelled(true);
				}else{
					if(e.getRawSlot() != 3){
						if(e.getCurrentItem() != null){
							if(e.getCurrentItem().getType().equals(Material.POTION)){
								ItemStack is1 = e.getCurrentItem();
								Potion potion = Potion.fromItemStack(is1);
								if(potion.getLevel() > 0){
									if(brewinginventory.containsKey(p)){
										if(e.getClickedInventory().equals(brewinginventory.get(p))){
											if(brewed.containsKey(brewinginventoryopen.get(p))){
												if(brewed.get(brewinginventoryopen.get(p))){
													if(rp.getRJob().equals(RJob.ALCHEMIST)){
														if(potion.getLevel() == 1){
															if(rp.getJobScores().containsKey(JobTask.BREW_POTION)){
																rp.getJobScores().put(JobTask.BREW_POTION, rp.getJobScores().get(JobTask.KILL)+3);
															}else rp.getJobScores().put(JobTask.BREW_POTION, 3);
														}else if(potion.getLevel() == 2){
															if(rp.getJobScores().containsKey(JobTask.BREW_POTION)){
																rp.getJobScores().put(JobTask.BREW_POTION, rp.getJobScores().get(JobTask.KILL)+6);
															}else rp.getJobScores().put(JobTask.BREW_POTION, 6);
														}
													}
												}
												brewed.remove(brewinginventoryopen.get(p));
												brewinginventory.remove(p);
												brewinginventoryopen.remove(p);
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
	}
	
	@EventHandler
	public void onBrewJob(BrewEvent e){
		brewed.put(e.getBlock().getLocation(), true);
	}

	@EventHandler
	public void onInteractJob(PlayerInteractEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BREWING_STAND) && !e.isCancelled()){
				Player p = e.getPlayer();
				brewinginventoryopen.put(p, e.getClickedBlock().getLocation());
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e){
		if(e.getInventory().getType().equals(InventoryType.BREWING)){
			brewinginventory.put((Player) e.getPlayer(), e.getInventory());
		}
	}
	
	public static int getWage(Player p, boolean pay){
		int salary = 0;
		RPlayer rp = RPlayer.get(p);
		if(!rp.getRJob().equals(RJob.JOBLESS)){
			for(JobTask task : rp.getJobScores().keySet()){
				int score = rp.getJobScores().get(task);
				while(score >= task.getAmount()){
					score -= task.getAmount();
					salary += 1;
				}
				if(pay)rp.getJobScores().put(task,score);
			}
			if(pay)rp.sendMessage("§aYou were paid §e" + salary + " §aemeralds for your great job during the day!", "§aVous avez été payé §e" + salary + " §aémeraudes pour votre superbe travail tout au long de la journée !");
			EconomyHandler.addBalanceITB(p, salary);
		}
		return salary;
	}
}
