package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.events.GMemberDisbandGuildEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildDisbandMenu extends UIHandler {

	private Guild guild;
	public GuildDisbandMenu(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Guild disband", "Dissolution de guilde"));
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	@Override
	public String getType() {
		return "GUILD_DISBAND_MENU";
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		if(e.getCurrentItem() != null){
			e.setCancelled(true);
			int slot = e.getRawSlot();
			if(slot == 4){
			}else{
				short damage = e.getCurrentItem().getDurability();
				if(damage == 5){
					GMemberDisbandGuildEvent event = new GMemberDisbandGuildEvent(this.getGuild(), gm);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						event.getGuild().disband();
						this.close(false);
						rp.sendMessage("§cYou disbanded §a§l" + event.getGuild().getName() + "§c.", "§cVous avez dissout §4§l" + event.getGuild().getName() + "§c.");
					}
				}else Core.uiManager.requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
			}
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}
	
	@Override
	protected boolean openWindow() {
		ItemStack ok = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
		ItemStack notok = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
		ItemMeta meta = ok.getItemMeta();
		meta.setDisplayName(rp.translateString("§2§lDISBAND GUILD", "§2§lDISSOUDRE LA GUILDE"));
		meta.setLore(Arrays.asList(rp.translateString("§aDisband guild and lose all its progression", "§aDissoudre la guilde et perdre toute sa progression")));
		ok.setItemMeta(meta);
		meta.setDisplayName(rp.translateString("§4§lKEEP GUILD", "§4§lGARDER LA GUILD"));
		meta.setLore(Arrays.asList(rp.translateString("§cKeep guild and all its progression", "§cGarder la guilde et conserver sa progression")));
		notok.setItemMeta(meta);
		for(int i = 0;i < 4;i++){
			this.menu.setItem(i, ok);
			this.menu.setItem(8-i, notok);
		}
		
		ItemStack guild = new ItemStack(Material.BOOK, 1);
		ItemMeta qmeta = guild.getItemMeta();
		qmeta.setDisplayName("§6§l" + this.getGuild().getName());
		qmeta.setLore(Arrays.asList("", rp.translateString("§7Are you sure you want to disband this guild?", "§7Êtes-vous certain de vouloir dissoudre cette guilde ?")));
		guild.setItemMeta(qmeta);
		this.menu.setItem(4, guild);
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
