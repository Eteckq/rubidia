package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;
import java.util.Random;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilUI extends UIHandler {

	RPlayer rp = RPlayer.get(this.getHolder());
	private final ItemStack INHAND;
	private final ItemStack ITEM_0;
	private final ItemStack ITEM_1;
	private final Block ANVIL;
	private final int COST;
	private int FINAL_ITEM_SLOT = 6;
	private boolean DONE = false;
	
	public AnvilUI(Player p, Block anvil, ItemStack is_0, ItemStack is_1, ItemStack is_2) {
		super(p);
		this.setMenu(Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Anvil", "Forge")));
		this.ITEM_0 = is_0;
		this.ITEM_1 = is_1;
		this.INHAND = is_2;
		this.ANVIL = anvil;
		this.COST = Utils.defineAnvilCost(this.getHolder(), INHAND);
		this.getHolder().closeInventory();
		rp.refreshRLevelDisplay();
	}

	@Override
	public String getType() {
		return "ANVIL_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(2, ITEM_0);
		this.getMenu().setItem(3, ITEM_1);
		this.getMenu().setItem(FINAL_ITEM_SLOT, INHAND);
		this.getMenu().setItem(0, this.getInfos());
		this.getMenu().setItem(8, this.getInfos());
		this.getMenu().setItem(1, this.getItemInfos());
		this.getMenu().setItem(4, this.getItemInfos());
		this.getMenu().setItem(5, this.getFinalInfos());
		this.getMenu().setItem(7, this.getFinalInfos());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	private ItemStack getFinalInfos() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)5);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(rp.translateString("§aFinal item", "§aItem final"));
		meta.setLore(Arrays.asList("§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-", rp.translateString("§7Confirm your item forging", "§7Confirmez la modification"), rp.translateString("§7by clicking it.", "§7en cliquant dessus."), rp.translateString("§cCost: §4" + COST + "§c Emeralds", "§cCoût : §4" + COST + "§c émeraudes"), "§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-"));
		is.setItemMeta(meta);
		return is;
	}

	private ItemStack getItemInfos() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)14);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(rp.translateString("§4Base items", "§4Items de base"));
		meta.setLore(Arrays.asList(rp.translateString("§cUsed items for the item forging", "§cItems utilisés pour la modification")));
		is.setItemMeta(meta);
		return is;
	}

	private ItemStack getInfos() {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)7);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(rp.translateString("§8Anvil", "§8Forge"));
		meta.setLore(Arrays.asList(rp.translateString("§7Validation menu", "§7Menu de validation")));
		is.setItemMeta(meta);
		return is;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			int slot = e.getRawSlot();
			if(slot == FINAL_ITEM_SLOT || slot == FINAL_ITEM_SLOT-1 || slot == FINAL_ITEM_SLOT+1){
				RPlayer rp = RPlayer.get(p);
				if(rp.getBalance() >= COST){
					EconomyHandler.withdrawBalanceITB(this.getHolder(), COST);
					p.getInventory().addItem(INHAND);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 3, 3);
					Random r = new Random();
					if(r.nextInt(4) == 0){
						if(ANVIL.getData()+4 < 12){
							ANVIL.setData((byte) (ANVIL.getData()+4));
						}else{
							ANVIL.setType(Material.AIR);
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 3, 3);
						}
					}
					DONE = true;
				}else{
					p.sendMessage(rp.translateString("§cYou don't have enough emeralds!", "§cVous n'avez pas assez d'émeraudes !"));
					DONE = false;
				}
				this.closeUI();
			}else if(slot == 2 || slot == 3 || slot == 1 || slot == 4)this.closeUI();
			else e.setCancelled(true);
			e.setCancelled(true);
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, final Player p) {
		if(!DONE){
			if(ITEM_0 != null)p.getInventory().addItem(ITEM_0);
			if(ITEM_1 != null)p.getInventory().addItem(ITEM_1);
		}else{
			if(ITEM_1 != null){
				if(ITEM_1.getAmount() > 1){
					ITEM_1.setAmount(ITEM_1.getAmount()-1);//because item in slot 1 is the dominating
					p.getInventory().addItem(ITEM_1);
				}
			}
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
}
