package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaQuests.ui.bank.SafeUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EconomyHandler {
	
	public final static int EMERALD_BLOCK_VALUE = 64;
	public final static double KEEPING_FEE = .1;
	
	public static boolean withdrawBalanceITB(Player p, int cost){
		cost = EconomyHandler.withdrawFromInventory(p, cost);
		cost = EconomyHandler.withdrawFromBank(p, cost);
		return cost <= 0;
	}
	/*public static boolean withdrawBalanceBTI(Player p, int cost){
		cost = EconomyHandler.withdrawFromBank(p, cost);
		cost = EconomyHandler.withdrawFromInventory(p, cost);
		return cost <= 0;
	}*/
	
	public static int withdrawFromInventory(Player p, int cost){
		RPlayer rp = RPlayer.get(p);
		if(!rp.isOp() && cost > 0){
			for(int i = 0;i < 2;i++){//take emeralds first, then blocks
				for(int slot = p.getInventory().getSize()-1;slot >= 0;slot--){
					if(cost > 0){
						ItemStack stack = p.getInventory().getItem(slot);
						if(stack != null){
							if(!EconomyHandler.isQuestItem(stack)){
								if(stack.getAmount() > 0){
									if(i == 0 && stack.getType().equals(Material.EMERALD)){
										cost -= stack.getAmount();
										if(cost >= 0)p.getInventory().setItem(slot, new ItemStack(Material.AIR));
										else stack.setAmount(-cost);
									}else if(i == 1 && stack.getType().equals(Material.EMERALD_BLOCK)){
										if(cost >= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE){
											p.getInventory().setItem(slot, new ItemStack(Material.AIR, 1));
											cost -= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE;
										}else{
											int r = cost%EconomyHandler.EMERALD_BLOCK_VALUE;//cost = value*q + r
											int q = (int) ((double)(cost-r)/EconomyHandler.EMERALD_BLOCK_VALUE);
											stack.setAmount(stack.getAmount()-q);
											if(stack.getAmount() <= 0)p.getInventory().setItem(slot, new ItemStack(Material.AIR));
											EconomyHandler.addBalanceITB(p, EconomyHandler.EMERALD_BLOCK_VALUE-r);
											return 0;
										}
									}
								}else p.getInventory().setItem(slot, new ItemStack(Material.AIR));
							}
						}
					}else return 0;
				}
			}
		}
		return cost;
	}
	public static int withdrawFromBank(Player p, int cost){
		RPlayer rp = RPlayer.get(p);
		if(!rp.isOp() && cost > 0){
			if(!Core.uiManager.hasActiveSession(p) || !Core.uiManager.getSession(p).getUIHandler().getType().equals("SAFE_MENU")){
				int maxSlot = rp.isVip() ? 18 : 9;
				for(int i = 0;i < 2;i++){
					for(int slot = maxSlot-1;slot >= 0;slot--){
						if(cost > 0){
							if(rp.getBank().containsKey(slot)){
								ItemStack stack = rp.getBank().get(slot);
								if(stack != null){
									if(!EconomyHandler.isQuestItem(stack)){
										if(stack.getAmount() > 0){
											if(i == 0 && stack.getType().equals(Material.EMERALD)){
												cost -= stack.getAmount();
												if(cost >= 0)rp.getBank().remove(slot);
												else stack.setAmount(-cost);
											}else if(i == 1 && stack.getType().equals(Material.EMERALD_BLOCK)){
												if(cost >= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE){
													rp.getBank().remove(slot);
													cost -= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE;
												}else{
													int r = cost%EconomyHandler.EMERALD_BLOCK_VALUE;
													int q = (int) ((double)(cost-r)/EconomyHandler.EMERALD_BLOCK_VALUE);
													stack.setAmount(stack.getAmount()-q);
													if(stack.getAmount() <= 0)rp.getBank().remove(slot);
													EconomyHandler.addBalanceBTI(p, EconomyHandler.EMERALD_BLOCK_VALUE-r);
													return 0;
												}
											}
										}else rp.getBank().remove(slot);
									}
								}
							}
						}else return 0;
					}
				}
			}else{
				Inventory bank = ((SafeUI) Core.uiManager.getSession(p).getUIHandler()).getMenu();
				for(int i = 0;i < 2;i++){
					for(int slot = bank.getSize()-1;slot >= 0;slot--){
						if(cost > 0){
							ItemStack stack = bank.getItem(slot);
							if(stack != null){
								if(!EconomyHandler.isQuestItem(stack)){
									if(stack.getAmount() > 0){
										if(i == 0 && stack.getType().equals(Material.EMERALD)){
											cost -= stack.getAmount();
											if(cost >= 0)bank.setItem(slot, new ItemStack(Material.AIR));
											else stack.setAmount(-cost);
										}else if(i == 1 && stack.getType().equals(Material.EMERALD_BLOCK)){
											if(cost >= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE){
												bank.setItem(slot, new ItemStack(Material.AIR, 1));
												cost -= stack.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE;
											}else{
												int r = cost%EconomyHandler.EMERALD_BLOCK_VALUE;
												int q = (int) ((double)(cost-r)/EconomyHandler.EMERALD_BLOCK_VALUE);
												stack.setAmount(stack.getAmount()-q);
												if(stack.getAmount() <= 0)bank.setItem(slot, new ItemStack(Material.AIR));
												EconomyHandler.addBalanceBTI(p, EconomyHandler.EMERALD_BLOCK_VALUE-r);
												return 0;
											}
										}
									}else bank.setItem(slot, new ItemStack(Material.AIR));
								}
							}
						}else return 0;
					}
				}
				Utils.updateInventory(p);
			}
		}
		return cost;
	}
	
	public static int getBalance(Player p){
		return getInventoryBalance(p)+getBankBalance(p);
	}
	
	public static int getInventoryBalance(Player p){
		int balance = 0;
		if(p.getInventory().contains(Material.EMERALD) || p.getInventory().contains(Material.EMERALD_BLOCK)){
			for(int slot = 0;slot < p.getInventory().getSize();slot++){
				ItemStack is = p.getInventory().getItem(slot);
				if(is != null){
					if(!EconomyHandler.isQuestItem(is)){
						if(is.getType().equals(Material.EMERALD)){
							balance += is.getAmount();
						}else if(is.getType().equals(Material.EMERALD_BLOCK)){
							balance += is.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE;
						}
					}
				}
			}
		}
		return balance;
	}
	public static int getBankBalance(Player p){
		RPlayer rp = RPlayer.get(p);
		if(Core.uiManager.hasActiveSession(p)){
			if(Core.uiManager.getSession(p).getUIHandler().getType().equals("SAFE_MENU")){
				((SafeUI)Core.uiManager.getSession(p).getUIHandler()).getBalance();
			}
		}
		
		int balance = 0;
		for(ItemStack is : rp.getBank().values()){
			if(is != null){
				if(!EconomyHandler.isQuestItem(is)){
					if(is.getType().equals(Material.EMERALD)){
						balance += is.getAmount();
					}else if(is.getType().equals(Material.EMERALD_BLOCK)){
						balance += is.getAmount()*EconomyHandler.EMERALD_BLOCK_VALUE;
					}
				}
			}
		}
		return balance;
	}
	
	public static boolean addBalanceBTI(Player p, int amount){
		amount = EconomyHandler.addBalanceInBank(p, amount);
		amount = EconomyHandler.addBalanceInInventory(p, amount);
		return amount <= 0;
	}
	public static boolean addBalanceITB(Player p, int amount){
		amount = EconomyHandler.addBalanceInInventory(p, amount);
		amount = EconomyHandler.addBalanceInBank(p, amount);
		return amount <= 0;
	}
	
	public static int addBalanceInInventory(Player p, int amount){
		RPlayer rp = RPlayer.get(p);
		if(amount > 0){
			for(int slot = 0;slot < 36;slot++){
				if(amount > 0){
					if(slot != 8 && (slot != 17 || !rp.getRClass().equals(RClass.RANGER))){
						ItemStack stack = p.getInventory().getItem(slot);
						if(stack != null){
							if(!stack.getType().equals(Material.AIR)){
								if(!EconomyHandler.isQuestItem(stack)){
									if(stack.getType().equals(Material.EMERALD)){
										if(stack.getAmount() < 64){
											if(stack.getAmount() > 0){
												amount -= 64 - stack.getAmount();
												if(amount >= 0)stack.setAmount(64);
												else stack.setAmount(amount+64);
											}else p.getInventory().setItem(slot, new ItemStack(Material.AIR));
										}
									}
								}
								continue;
							}
						}

						amount -= 64;
						if(amount >= 0){
							p.getInventory().setItem(slot, new ItemStack(Material.EMERALD, 64));
							if(slot == 35 && amount > 0){
								rp.getLoadedSPlayer().setPendingBalance((int) (rp.getLoadedSPlayer().getPendingBalance()+amount*(1-EconomyHandler.KEEPING_FEE)));
								rp.sendMessage("§cYou cannot hold more emeralds! Your pending balance has been updated with a §4" + ((int)(EconomyHandler.KEEPING_FEE*100)) + "% §cretention fee. You can get your emeralds back from a banker.", "§cVous ne pouvez porter plus d'émeraudes ! Votre solde en attente a été mis à jour avec §4" + ((int)(EconomyHandler.KEEPING_FEE*100)) + "% §cde frais de conservation. Vous pouvez allez les retirer à l'aide d'un banquier.");
								rp.sendMessage("§cYour new pending balance is: §f" + rp.getLoadedSPlayer().getPendingBalance() + " §cemeralds.", "§cVotre nouveau solde en attente : §f" + rp.getLoadedSPlayer().getPendingBalance() + " §cémeraudes.");
								return 0;
							}
						}else p.getInventory().setItem(slot, new ItemStack(Material.EMERALD, amount+64));
					}
				}else return 0;
			}
		}
		return amount;
	}
	public static int addBalanceInBank(Player p, int amount){
		RPlayer rp = RPlayer.get(p);
		if(amount > 0){
			if(!Core.uiManager.hasActiveSession(p) || !Core.uiManager.getSession(p).getUIHandler().getType().equals("SAFE_MENU")){
				int maxSlot = rp.isVip() ? 18 : 9;
				for(int slot = maxSlot-1;slot >= 0;slot--){
					if(amount > 0){
						if(rp.getBank().containsKey(slot)){
							ItemStack stack = rp.getBank().get(slot);
							if(stack != null){
								if(!EconomyHandler.isQuestItem(stack)){
									if(stack.getType().equals(Material.EMERALD)){
										if(stack.getAmount() > 0){
											if(stack.getAmount() < 64){
												amount -= 64 - stack.getAmount();
												if(amount >= 0)stack.setAmount(64);
												else stack.setAmount(amount+64);
											}
										}else rp.getBank().remove(slot);
										continue;
									}else if(stack.getType().equals(Material.EMERALD_BLOCK)){
										continue;
									}
								}
							}
						}
						
						amount -= 64;
						if(amount >= 0)rp.getBank().put(slot, new ItemStack(Material.EMERALD, 64));
						else rp.getBank().put(slot, new ItemStack(Material.EMERALD, amount+64));
					}else return 0;
				}
			}else{
				Inventory bank = ((SafeUI) Core.uiManager.getSession(p).getUIHandler()).getMenu();
				for(int slot = 0;slot < bank.getSize();slot++){
					if(amount > 0){
						ItemStack stack = bank.getItem(slot);
						if(stack != null){
							if(!EconomyHandler.isQuestItem(stack)){
								if(stack.getType().equals(Material.EMERALD)){
									if(stack.getAmount() < 64){
										if(stack.getAmount() > 0){
											amount -= 64 - stack.getAmount();
											if(amount >= 0)stack.setAmount(64);
											else stack.setAmount(amount+64);
										}else bank.setItem(slot, new ItemStack(Material.AIR));
									}
									continue;
								}else if(stack.getType().equals(Material.EMERALD_BLOCK)){
									continue;
								}
							}
						}
						
						amount -= 64;
						if(amount >= 0)bank.setItem(slot, new ItemStack(Material.EMERALD, 64));
						else bank.setItem(slot, new ItemStack(Material.EMERALD, amount+64));
					}else return 0;
				}
				Utils.updateInventory(p);
			}
		}
		return amount;
	}

	public static boolean isQuestItem(ItemStack is){
		return me.pmilon.RubidiaQuests.utils.Utils.isQuestItem(is);
	}
}
