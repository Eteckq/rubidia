package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.UIHandler;
import me.pmilon.RubidiaCore.ui.UIType;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.PermissionsHolder;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class HomePermissionsUI extends UIHandler {

	private int SLOT_BACK = 17;
	private int SLOT_HEAD = 8;
	
	private Guild guild;
	private PermissionsHolder subject;
	public int page;
	public HomePermissionsUI(Player p, Guild guild, PermissionsHolder subject, int page) {
		super(p);
		this.guild = guild;
		this.subject = subject;
		this.page = page;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getName() + " : " + rp.translateString("Home permissions", "Permissions de PR"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public UIType getType() {
		return UIType.GUILD_HOME_PERMISSIONS;
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player arg1) {
		if(e.getCurrentItem() != null){
			e.setCancelled(true);
			int slot = e.getRawSlot();
			if(slot == this.SLOT_BACK){
				if(this.isGMember())Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), this.getGuild(), (GMember)this.getSubject(), this.page));
				else Core.uiManager.requestUI(new GRankPrefsUI(this.getHolder(), this.getGuild(), (Rank)this.getSubject(), this.page));
			}else if(slot != this.SLOT_HEAD){
				if(slot < 9){
					this.getSubject().setCanHome(slot, !this.getSubject().canHome(slot));
					this.getMenu().setItem(slot, this.getCanHome(slot));
				}else{
					this.getSubject().setCanSetHome(slot-9, !this.getSubject().canSetHome(slot-9));
					this.getMenu().setItem(slot, this.getCanSetHome(slot-9));
				}
				if(!this.isGMember())((Rank)this.getSubject()).resetPermissions(this.getGuild());
				Utils.updateInventory(this.getHolder());
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		this.getMenu().setItem(this.SLOT_HEAD, this.getHead());
		for(int i = 0;i < 8;i++){
			this.getMenu().setItem(i, this.getCanHome(i));
			this.getMenu().setItem(i+9, this.getCanSetHome(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	public Guild getGuild() {
		return guild;
	}
	
	private ItemStack getCanHome(int index){
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short)(this.getSubject().canHome(index) ? 10 : 8));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((this.getSubject().canHome(index) ? "§a§l" : "§c§l") + rp.translateString("Use permission", "Permission d'utilisation"));
		String name = this.getGuild().getHomes()[index] != null ? " (§f§l" + this.getGuild().getHomes()[index].getName() + "§7)" : "";
		meta.setLore(Arrays.asList("§7" + rp.translateString("Allow " + this.getName() + " to use home #" + index + name, "Autoriser " + this.getName() + " à utiliser le PR #" + index + name)));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCanSetHome(int index){
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short)(this.getSubject().canSetHome(index) ? 10 : 8));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((this.getSubject().canSetHome(index) ? "§a§l" : "§c§l") + rp.translateString("Set permission", "Permission de définition"));
		String name = this.getGuild().getHomes()[index] != null ? " (§f§l" + this.getGuild().getHomes()[index].getName() + "§7)" : "";
		meta.setLore(Arrays.asList("§7" + rp.translateString("Allow " + this.getName() + " to set home #" + index + name, "Autoriser " + this.getName() + " à définir le PR #" + index + name)));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBack(){
		ItemStack item = new ItemStack(Material.MELON, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.translateString("Other permissions", "Autres permissions"));
		meta.setLore(Arrays.asList(rp.translateString("§7Get back to the permissions menu.", "§7Retourner au menu des permissions."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getHead(){
		ItemStack item = null;
		if(this.isGMember()){
			item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setDisplayName("§f" + this.getName());
			meta.setOwner(this.getName());
			item.setItemMeta(meta);
		}else{
			item = ((Rank)this.getSubject()).getItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f" + this.getName());
			item.setItemMeta(meta);
		}
		return item;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public PermissionsHolder getSubject() {
		return subject;
	}

	public void setSubject(PermissionsHolder subject) {
		this.subject = subject;
	}

	public boolean isGMember(){
		return this.getSubject() instanceof GMember;
	}
	
	public String getName(){
		if(this.isGMember())return ((GMember)this.getSubject()).getName();
		else return ((Rank)this.getSubject()).getName().toUpperCase();
	}
}
