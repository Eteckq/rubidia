package me.pmilon.RubidiaCore.ui;

import me.pmilon.RubidiaCore.ritems.backpacks.BackPack;
import me.pmilon.RubidiaCore.ritems.general.RItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BackpackUI extends UIHandler {

	private BackPack pack;
	
	public BackpackUI(Player p, BackPack pack) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 27, rp.translateString("BackPack", "Sac à dos"));
		this.pack = pack;
		pack.setModified(true);
	}

	@Override
	public UIType getType() {
		return UIType.BACKPACK;
	}

	@Override
	protected boolean openWindow() {
		for(Integer i : this.getBackPack().getContent().keySet()){
			this.getMenu().setItem(i, this.getBackPack().getContent().get(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			RItem rItem = new RItem(e.getCurrentItem());
			if(rItem.isBackPack()){
				e.setCancelled(true);
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
		this.getBackPack().getContent().clear();
		for(int i = 0;i < this.getMenu().getSize();i++){
			this.getBackPack().getContent().put(i, this.getMenu().getItem(i));
		}
		this.getBackPack().setModified(true);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	public BackPack getBackPack() {
		return pack;
	}

	public void setBackPack(BackPack pack) {
		this.pack = pack;
	}

}
