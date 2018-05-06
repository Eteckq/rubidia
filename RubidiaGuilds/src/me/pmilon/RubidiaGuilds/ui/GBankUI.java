package me.pmilon.RubidiaGuilds.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;

public class GBankUI extends UIHandler {
	
	private Guild guild;
	public GBankUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, this.getGuild().getName() + " : " + rp.translateString("Guild bank", "Banque de guilde"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_BANK_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		/*
		// MODIF ETECK
		//
		// Pour que je m'y retrouve un peu mieux, j'ai remplacer les fonctions que tu appelles souvent
		// dans des variables, ici "e.getCurrentItem().getType()" -> variable "itemType"
		//
		// Je le fais seulement ici, et si ça te dérange pas je le ferai peut être ailleurs
		// Je change rien d'autre pour l'instant, j'attends ton accord ^^
		// (Je n'ai bien sûr rien changé au fonctionnement du code, ça me parait juste plus lisible !)
		//
		*/
		
		Material itemType = e.getCurrentItem().getType();
		
		if(e.getCurrentItem() != null){
			if(e.isShiftClick()){
				if(!itemType.equals(Material.EMERALD) && !itemType.equals(Material.EMERALD_BLOCK) && !itemType.equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("§cThis is a bank, not a storage!", "§cC'est une banque, pas un coffre !");
				}else{
					if(!gm.getPermission(Permission.BANK_WITHDRAW)){
						e.setCancelled(true);
						rp.sendMessage("§cYou don't have permission to withdraw emeralds from your guild's bank!", "§cVous n'avez pas la permission de retirer des émeraudes de la banque de votre guilde !");
					}
					
					final GBankUI bankUI = this;
					Bukkit.getScheduler().runTaskLater(GuildsPlugin.instance, new Runnable(){
						public void run(){
							bankUI.save();
							for(GBankUI bankHandler : bankUI.getGuild().banks){
								if(!bankHandler.equals(this))bankHandler.update();
							}
						}
					}, 1);
				}
			}
		}
	}

	@Override
	public void onInventoryClick(final InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(!e.getCursor().getType().equals(Material.EMERALD) && !e.getCursor().getType().equals(Material.EMERALD_BLOCK) && !e.getCursor().getType().equals(Material.AIR)){
				e.setCancelled(true);
				rp.sendMessage("§cThis is a bank, not a storage!", "§cC'est une banque, pas un coffre !");
			}else{
				if(e.getCursor().getType().equals(Material.EMERALD) || e.getCursor().getType().equals(Material.EMERALD_BLOCK)){
					if(e.isRightClick()){
						if(!gm.getPermission(Permission.BANK_WITHDRAW)){
							e.setCancelled(true);
							rp.sendMessage("§cYou don't have permission to withdraw emeralds from your guild's bank!", "§cVous n'avez pas la permission de retirer des émeraudes de la banque de votre guilde !");
						}
					}else if(!gm.getPermission(Permission.BANK_DEPOSIT)){
						e.setCancelled(true);
						rp.sendMessage("§cYou don't have permission to depose emeralds in your guild's bank!", "§cVous n'avez pas la permission de déposer des émeraudes dans la banque de votre guilde !");
					}
				}else if(e.getCursor().getType().equals(Material.AIR)){
					if(!gm.getPermission(Permission.BANK_WITHDRAW)){
						e.setCancelled(true);
						rp.sendMessage("§cYou don't have permission to withdraw emeralds from your guild's bank!", "§cVous n'avez pas la permission de retirer des émeraudes de la banque de votre guilde !");
					}
				}
				
				final GBankUI bankUI = this;
				Bukkit.getScheduler().runTaskLater(GuildsPlugin.instance, new Runnable(){
					public void run(){
						bankUI.save();
						for(GBankUI bankHandler : bankUI.getGuild().banks){
							if(!bankHandler.equals(bankUI))bankHandler.update();
						}
					}
				}, 1);
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		this.getGuild().banks.remove(this);
	}

	@Override
	protected boolean openWindow() {
		for(int slot = 0;slot < 9; slot++){
			if(this.getGuild().getBank().containsKey(slot)){
				getMenu().setItem(slot, this.getGuild().getBank().get(slot));
			}
		}
		this.getGuild().banks.add(this);
		return this.getHolder().openInventory(getMenu()) != null;
	}
	
	public void update(){
		for(int slot = 0;slot < 9; slot++){
			if(this.getGuild().getBank().containsKey(slot)){
				getMenu().setItem(slot, this.getGuild().getBank().get(slot));
			}
		}
		Utils.updateInventory(this.getHolder());
	}

	protected void save(){
		for(int slot = 0; slot < getMenu().getSize(); slot++){
			this.getGuild().getBank().put(slot, getMenu().getItem(slot));
		}
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
}
