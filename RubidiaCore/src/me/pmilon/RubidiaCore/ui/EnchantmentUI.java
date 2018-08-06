package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentUI extends UIHandler {

	RPlayer rp = RPlayer.get(this.getHolder());
	private final ItemStack INHAND = this.getHolder().getEquipment().getItemInMainHand().clone();
	private final ItemMeta INHANDM = this.getHolder().getEquipment().getItemInMainHand().getItemMeta();
	private int COST1 = Utils.defineEnchantmentCost(this.getHolder(), INHAND, INHANDM.getEnchants(), 1);
	private int COST2 = Utils.defineEnchantmentCost(this.getHolder(), INHAND, INHANDM.getEnchants(), 2);
	private int COST3 = Utils.defineEnchantmentCost(this.getHolder(), INHAND, INHANDM.getEnchants(), 3);
	private int COST4 = Utils.defineEnchantmentCost(this.getHolder(), INHAND, INHANDM.getEnchants(), 4);
	private final List<Enchantment> available = new ArrayList<Enchantment>();
	private final Location location;
	private final int SELECTED_SLOT;
	
	public EnchantmentUI(Location location, Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Enchantment", "Enchantement"));
		this.location = location;
		this.SELECTED_SLOT = p.getInventory().getHeldItemSlot();
	}
	
	@Override
	public String getType(){
		return "ENCHANTMENT_MENU";
	}

	@Override
	protected boolean openWindow() {
		RItem rItem = new RItem(INHAND);
		Map<Enchantment, Integer> enchantms = INHANDM.getEnchants();
		int bLevel = this.getMaxBookshelfLevel();
		boolean canEnchant = false;

		for(Enchantment enchant : REnchantment.values()){
			if((!rItem.isWeapon() || !enchant.equals(Enchantment.DURABILITY)) && !enchant.equals(Enchantment.MENDING)){
				if(enchant.canEnchantItem(INHAND) || rItem.isWeapon() && rItem.getWeapon().isAttack() && REnchantment.WEAPONS_ENCHANTMENTS.contains(enchant)){
					canEnchant = true;
					if(!enchantms.containsKey(enchant)){
						available.add(enchant);
					}
				}
			}
		}
		if(!canEnchant || me.pmilon.RubidiaQuests.utils.Utils.isQuestItem(INHAND)){
			rp.sendMessage("§cYou cannot enchant this item!", "§cVous ne pouvez pas enchanter cet item !");
			this.getUIManager().playerSessions.remove(this.getUIManager().getSession(this.getHolder()).getIdentifier());
			return false;
		}else if(available.isEmpty()){
			rp.sendMessage("§cThis item is already fully enchanted!", "§cCet item est déjà complètement enchanté !");
			return false;
		}
		ItemMeta INHANDMTEMP = INHANDM.clone();
		for(int i = 1;i < 5;i++){
			if(bLevel >= i){
				INHANDMTEMP.setDisplayName(rp.translateString("§aRandom enchantment | §aPalliate §2" + i, "§aEnchantement aléatoire | §aPallier §2" + i));
				INHANDMTEMP.setLore(Arrays.asList("§8§m                                               ", "", rp.translateString("§7Randomly enchant this", "§7Enchantez cet item aléatoirement"), rp.translateString("§7item by clicking it.", "§7en cliquant dessus."), rp.translateString("§7Enchantments level " + (i == 4 ? "4-5" : (i == 3 ? "2-5" : (i == 2 ? "1-3" : "1-2"))) + " (max)", "§7Enchantements niveau " + (i == 4 ? "4-5" : (i == 3 ? "2-5" : (i == 2 ? "1-3" : "1-2"))) + " (max)"), "", rp.translateString("§cCost: §4" + this.getCost(i) + "§c emeralds", "§cCoût : §4" + this.getCost(i) + "§c émeraudes")));
				INHAND.setItemMeta(INHANDMTEMP);
				INHAND.setAmount(i);
				this.getMenu().setItem(i*2-1, INHAND);
			}else this.getMenu().setItem(i*2-1, this.getNo(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				RPlayer rp = RPlayer.get(p);
				Enchantment enchant = available.get(RandomUtils.random.nextInt(available.size()));
			    int level = this.getLevel(e.getRawSlot());
			    int elvl = this.getEnchantmentIntervalLevel(level);
				int bLevel = this.getMaxBookshelfLevel();
				if(level > bLevel){
					rp.sendMessage("§cYou need " + level*9 + " bookshelfs around this table to unlock this palliate", "§cVous devez positionner " + level*9 + " bibliothèques autour de la table d'enchantement pour débloquer ce pallier !");
					e.setCancelled(true);
					return;
				}
				
				if(rp.getBalance() >= this.getCost(level)){
					ItemStack is = p.getEquipment().getItemInMainHand();
					if(is.getType().equals(INHAND.getType()) && this.SELECTED_SLOT == p.getInventory().getHeldItemSlot()){
						EconomyHandler.withdrawBalanceITB(this.getHolder(), this.getCost(level));
						if(elvl > enchant.getMaxLevel())elvl = enchant.getMaxLevel();
						INHANDM.addEnchant(enchant, elvl, true);
						is.setItemMeta(INHANDM);
						RItem rItem = new RItem(is);
						if(rItem.isWeapon())rItem.getWeapon().updateState(rp, is);
						else{
							for(Enchantment ench : INHANDM.getEnchants().keySet()){
								if(ench.equals(REnchantment.SOUL_BIND)){
									int lvl = INHANDM.getEnchants().get(ench);
									String name = "§7Liaison spirituelle " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : lvl == 5 ? "V" : "???");
									if(INHANDM.hasLore()){
										boolean found = false;
										List<String> lore = Utils.getModifiableCopy(INHANDM.getLore());
										for(int i = 0;i < lore.size();i++){
											if(lore.get(i).contains("Liaison spirituelle")){
												lore.set(i, name);
												found = true;
												break;
											}
										}//useless
										if(!found){
											for(int i = lore.size()-1;i >= 0;i--){
												if(lore.size() > i+1)lore.set(i+1, lore.get(i));
												else lore.add(lore.get(i));
											}
											lore.set(0, name);
										}
										INHANDM.setLore(lore);
									}else INHANDM.setLore(Arrays.asList(name));
									break;
								}
							}
							is.setItemMeta(INHANDM);
						}
						rp.sendMessage("§aStrange powers have got over your item!", "§aD'étranges puissances ont pris le contrôle de votre item !");
					}else rp.sendMessage("§cA problem occurred. Please retry.", "§cUn problème est survenu. Réessayez.");
				}else rp.sendMessage("§cYou don't have enough emeralds!", "§cVous n'avez pas assez d'émeraudes !");
				this.close(false);
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		//nothing to do here ??
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick() || e.getCurrentItem().equals(p.getEquipment().getItemInMainHand()))e.setCancelled(true);
	}
	
	
	private int getCost(int level){
		if(level == 1)return COST1;
		else if(level == 2)return COST2;
		else if(level == 3)return COST3;
		else if(level >= 4)return COST4;
		return 0;
	}
	private int getLevel(int slot){
		if(slot == 1)return 1;
		else if(slot == 3)return 2;
		else if(slot == 5)return 3;
		else if(slot == 7)return 4;
		return 0;
	}
	private int getEnchantmentIntervalLevel(int level){
		if(level == 1)return RandomUtils.random.nextInt(2)+1;
		else if(level == 2)return RandomUtils.random.nextInt(3)+1;
		else if(level == 3)return RandomUtils.random.nextInt(3)+2;
		else if(level >= 4)return RandomUtils.random.nextInt(2)+4;
		return 0;
	}
	
	private int getMaxBookshelfLevel(){
		int bookshelfs = 0;
		for(int x = -5;x <= 5;x++){
			for(int y = -5;y <= 5;y++){
				for(int z = -5;z <= 5;z++){
					if(this.getLocation().clone().add(x,y,z).getBlock().getType().equals(Material.BOOKSHELF)){
						bookshelfs += 1;
					}
				}
			}
		}
		return (int) ((double) bookshelfs/9.0D);
	}
	
	private ItemStack getNo(int level){
		ItemStack stack = new ItemStack(Material.BARRIER, level);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(rp.translateString("§4Palliate locked - Enchantments lvl." + (level == 4 ? "3-5" : (level == 3 ? "2-4" : (level == 2 ? "1-3" : "1-2"))), "§4Pallier bloqué - Enchantements n." + (level == 4 ? "3-5" : (level == 3 ? "2-4" : (level == 2 ? "1-3" : "1-2")))));
		meta.setLore(Arrays.asList(rp.translateString("§cYou need " + (level*9) + " bookshelfs around this", "§cVous avez besoin de positionner " + (level*9) + " bibliothèques"), rp.translateString("§ctable to unlock this palliate!", "§cautour de cette table pour débloquer ce pallier !")));
		stack.setItemMeta(meta);
		return stack;
	}

	public Location getLocation() {
		return location;
	}
}
