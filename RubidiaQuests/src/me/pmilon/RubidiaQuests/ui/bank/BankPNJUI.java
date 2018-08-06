package me.pmilon.RubidiaQuests.ui.bank;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.BankPNJ;

public class BankPNJUI extends UIHandler {

	private BankPNJ pnj;
	private int SLOT_CHANGE = 0, SLOT_WITHDRAW = 2, SLOT_BANK = 4;
	private BukkitTask task;
	public BankPNJUI(Player p, BankPNJ pnj) {
		super(p);
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, rp.translateString("BANK", "BANQUE"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "BANK_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				e.setCancelled(true);
				int slot = e.getRawSlot();
				if(slot == this.SLOT_CHANGE)this.getHolder().openMerchant(this.getPnj().getEntity(), true);
				else if(slot == this.SLOT_WITHDRAW){
					int balance = rp.getLoadedSPlayer().getPendingBalance();
					int amount = Utils.getAmountCanHold(this.getHolder(), new ItemStack(Material.EMERALD, 1));
					if(amount > 0){
						rp.getLoadedSPlayer().setPendingBalance(balance-amount);
						if(rp.getLoadedSPlayer().getPendingBalance() <= 0){
							rp.getLoadedSPlayer().setPendingBalance(0);
							for(int i = 0;i < balance;i++){
								this.getHolder().getInventory().addItem(new ItemStack(Material.EMERALD,1));
							}
							rp.sendMessage("§aYou took back your pending balance (§e" + balance + " §aemeralds)!", "§aVous avez retiré votre solde en attente (§e" + balance + " §aémeraudes) !");
						}else{
							for(int i = 0;i < amount;i++){
								this.getHolder().getInventory().addItem(new ItemStack(Material.EMERALD,1));
							}
							rp.sendMessage("§aYou took back a part of your pending balance (§e" + amount + " §aemeralds)!", "§aVous avez retiré une partie de votre solde en attente (§e" + amount + " §aémeraudes) !");
						}
						this.menu.setItem(this.SLOT_WITHDRAW, this.getWithdraw());
					}else rp.sendMessage("§cYou cannot hold more emeralds!", "§cVous ne pouvez porter plus d'émeraudes !");
				}
				else if(slot == this.SLOT_BANK)Core.uiManager.requestUI(new SafeUI(this.getHolder()));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		this.task.cancel();
	}

	@Override
	protected boolean openWindow() {
		this.task = new BukkitTask(QuestsPlugin.instance){

			@Override
			public void run() {
				menu.setItem(SLOT_CHANGE, getRandomChange());
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0, 20);
		this.menu.setItem(this.SLOT_WITHDRAW, this.getWithdraw());
		this.menu.setItem(this.SLOT_BANK, this.getBank());
		return this.getHolder().openInventory(this.menu) != null;
	}

	private ItemStack getRandomChange(){
		ItemStack item = new ItemStack(RandomUtils.random.nextBoolean() ? Material.EMERALD : Material.EMERALD_BLOCK, RandomUtils.random.nextInt(64)+1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lChange");
		meta.setLore(Arrays.asList("§7" + rp.translateString("Change " + EconomyHandler.EMERALD_BLOCK_VALUE + " emeralds into 1 emerald block (and vice versa)","Changer " + EconomyHandler.EMERALD_BLOCK_VALUE + " émeraudes en 1 bloc d'émeraude (et vice versa)")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getWithdraw(){
		ItemStack item = new ItemStack(Material.IRON_DOOR, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.translateString("Pending balance", "Solde en attente"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Take back your pending balance", "Retirer votre solde en attente"), "§7 > §f" + rp.getLoadedSPlayer().getPendingBalance() + " §7" + rp.translateString("emeralds", "émeraudes")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getBank(){
		ItemStack item = new ItemStack(Material.ENDER_CHEST,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.translateString("Safe", "Coffre-fort"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Open your safe", "Ouvrir votre coffre-fort")));
		item.setItemMeta(meta);
		return item;
	}

	public BankPNJ getPnj() {
		return pnj;
	}

	public void setPnj(BankPNJ pnj) {
		this.pnj = pnj;
	}
	
}
