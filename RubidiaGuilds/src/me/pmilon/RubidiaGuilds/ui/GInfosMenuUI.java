package me.pmilon.RubidiaGuilds.ui;

import java.util.ArrayList;
import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.events.GMemberChangeGuildNameEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class GInfosMenuUI extends UIHandler {

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DESC = new ItemStack(Material.EMPTY_MAP, 1);
	private ItemStack ITEM_OFFER = new ItemStack(Material.EXP_BOTTLE, 1);
	
	private int SLOT_BACK = 0;
	private int SLOT_NAME = 2;
	private int SLOT_DESC = 3;
	private int SLOT_DISPLAY = 5;
	private int SLOT_GLOWING = 6;
	private int SLOT_OFFER = 8;
	
	private int LISTENINGID_NAME = 1;
	private int LISTENINGID_DESC = 2;
	private int LISTID_CAPE = 3;
	
	private Guild guild;
	public GInfosMenuUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_INFOS_MENU";
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				int slot = e.getRawSlot();
				if(slot == this.SLOT_BACK){
					Core.uiManager.requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
				}else if(slot == this.SLOT_NAME){
					if(gm.getPermission(Permission.RENAME)){
						rp.sendMessage("§aRename your guild by typing the desired name in the chat! (MAX: " + Guild.NAME_LENGTH + " characters)", "§aRenommez votre guilde en entrant le nom désiré dans le chat ! (MAX : " + Guild.NAME_LENGTH + "caractères)");
						this.close(true, this.LISTENINGID_NAME);
					}else rp.sendMessage("§cYou don't have permission to rename your guild.", "§cVous n'avez pas la permission de renommer votre guilde.");
				}else if(slot == this.SLOT_DESC){
					if(gm.getPermission(Permission.RENAME)){
						rp.sendMessage("§aChange your guild's description by typing the desired one in the chat!", "§aModifiez votre description de guilde en entrant celle désirée dans le chat !");
						this.close(true, this.LISTENINGID_DESC);
					}else rp.sendMessage("§cYou don't have permission to change your guild's description.", "§cVous n'avez pas la permission de modifier la description de votre guilde.");
				}else if(slot == this.SLOT_DISPLAY){
					if(e.isLeftClick()){
						if(gm.getPermission(Permission.CAPE) || gm.isLeader()){
							this.close(true, this.LISTID_CAPE);
							rp.sendMessage("§aTake a banner in your hands and type in the cost you want to set (members will pay this amount for each cape they order).", "§aPrenez une bannière entre vos mains et entrez son coût (les membres paieront ce montant pour chaque cape commandée).");
						}else rp.sendMessage("§cYou don't have permission to modify your guild's representative item!", "§cVous n'avez pas la permission de modifier l'item de votre guilde !");
					}else{
						if(rp.getBalance() >= this.getGuild().getCapeCost() || gm.isLeader() || rp.isOp()){
							if(!gm.isLeader() && !rp.isOp()){
								EconomyHandler.withdrawBalanceITB(this.getHolder(), this.getGuild().getCapeCost());
								this.getGuild().addBalance(this.getGuild().getCapeCost());
							}
							this.getGuild().broadcastMessage(Relation.MEMBER, "§&d" + gm.getName() + " §&chas purchased a cape!", "§&d" + gm.getName() + " §&ca acheté une cape de guilde !");
							this.getHolder().getInventory().addItem(this.getGuild().getCape());
						}else rp.sendMessage("§cYou don't have enough emeralds!", "§cVous n'avez pas suffisamment d'émeraudes !");
					}
				}else if(slot == this.SLOT_GLOWING){
					if(gm.getPermission(Permission.CAPE) || gm.isLeader())this.getGuild().setGlowing(!this.getGuild().isGlowing());
					this.menu.setItem(this.SLOT_DISPLAY, this.getInfos());
					this.menu.setItem(this.SLOT_GLOWING, this.getGlowing());
				}else if(slot == this.SLOT_OFFER)Core.uiManager.requestUI(new GExpMenuUI(this.getHolder(), this.getGuild()));
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LISTENINGID_NAME){
					String[] chars = ChatColor.stripColor(this.getMessage()).split("");
					String name = "";
					for(int i = 0;i < Guild.NAME_LENGTH;i++){
						if(i == chars.length)break;
						name += chars[i];
					}
					if(Guild.getByName(name) == null){
						GMemberChangeGuildNameEvent event = new GMemberChangeGuildNameEvent(this.getGuild(), gm, name);
						Bukkit.getPluginManager().callEvent(event);
						if(!event.isCancelled())event.getGuild().setName(name);
					}else rp.sendMessage("§cA guild with this name already exists!", "§cUne guilde avec le même nom existe déjà !");
				}else if(this.getListeningId() == this.LISTENINGID_DESC){
					this.getGuild().setDescription(this.getMessage().replace('§', '?'));
				}else if(this.getListeningId() == this.LISTID_CAPE){
					if(Utils.isInteger(this.getMessage())){
						this.getGuild().setCapeCost(Integer.valueOf(this.getMessage()));
						ItemStack item = this.getHolder().getInventory().getItemInMainHand();
						if(item.getType().equals(Material.BANNER)){
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§fCape de " + this.getGuild().getName());
							meta.setLore(new ArrayList<String>());
							meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_POTION_EFFECTS);
							item.setItemMeta(meta);
							ItemStack cape = item.clone();
							cape.setAmount(1);
							this.getGuild().setCape(cape);
						}
					}
				}
			}
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + " : Informations", 32));
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_NAME, this.getGuildName());
		this.menu.setItem(this.SLOT_DESC, this.getGuildDesc());
		this.menu.setItem(this.SLOT_DISPLAY, this.getInfos());
		this.menu.setItem(this.SLOT_GLOWING, this.getGlowing());
		this.menu.setItem(this.SLOT_OFFER, this.getOffer());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§l" + rp.translateString("Main menu", "Menu principal"));
		META_BACK.setLore(Arrays.asList(rp.translateString("§7Get back to the main menu.", "§7Retourner au menu principal."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getGuildName(){
		ItemMeta paperm = ITEM_NAME.getItemMeta();
		paperm.setDisplayName("§a§l" + rp.translateString("Guild Name", "Nom de Guilde"));
		paperm.setLore(Arrays.asList(rp.translateString("§7Change your guild name", "§7Changez votre nom de guilde"), "", rp.translateString("§6§lCurrent name: ", "§6§lNom actuel : ") + "§e" + this.getGuild().getName()));
		ITEM_NAME.setItemMeta(paperm);
		return ITEM_NAME;
	}
	private ItemStack getGuildDesc(){
		ItemMeta paperm = ITEM_DESC.getItemMeta();
		String description  = this.getGuild().getDescription();
		paperm.setDisplayName("§a§l" + rp.translateString("Guild Description", "Description de Guilde"));
		paperm.setLore(Arrays.asList(rp.translateString("§7Change your guild description", "§7Changez votre description de guilde"), "", rp.translateString("§6§lCurrent description: ", "§6§lDescription actuelle : ") + "§e" + StringUtils.abbreviate(description, 22)));
		ITEM_DESC.setItemMeta(paperm);
		return ITEM_DESC;
	}
	private ItemStack getInfos(){
		ItemStack infos = this.getGuild().getCape();
		ItemMeta paperm = infos.getItemMeta();
		paperm.setDisplayName("§a§l" + rp.translateString(this.getGuild().getName() + "'s cape", "Cape de " + this.getGuild().getName()));
		paperm.setLore(Arrays.asList(rp.translateString("§7Your guild's wearable cape (order one by right-clicking!)", "§7La cape de votre guilde (cliquez droit pour en commander une !)"), rp.translateString("§7Replace it with another banner by left-clicking.", "§7Remplacez-la par une autre bannière en cliquant gauche."), "", rp.translateString("§7Price: §f" + this.getGuild().getCapeCost() + " §7emeralds", "§7Prix : §f" + this.getGuild().getCapeCost() + " §7émeraudes"), rp.translateString("§7§oAdded to the guild's bank.", "§7§oReversées dans la banque de guilde")));
		infos.setItemMeta(paperm);
		return this.getGuild().isGlowing() ? Utils.setGlowingWithoutAttributes(infos) : infos;
	}
	private ItemStack getGlowing(){
		ItemStack ITEM_GLOWING = new ItemStack(Material.INK_SACK, 1);
		if(this.getGuild().isGlowing())ITEM_GLOWING.setDurability((short)10);
		else ITEM_GLOWING.setDurability((short)8);
		ItemMeta META = ITEM_GLOWING.getItemMeta();
		META.setDisplayName((this.getGuild().isGlowing() ? "§a§l" : "§c§l") + rp.translateString("Glowing item", "Item brillant"));
		META.setLore(Arrays.asList(rp.translateString("§7Enable or disable glowing effect", "§7Activer ou désactiver l'effet brillant"), rp.translateString("§7on your representative item.", "§7de votre item de guilde.")));
		ITEM_GLOWING.setItemMeta(META);
		return ITEM_GLOWING;
	}
	private ItemStack getOffer(){
		ItemMeta META = ITEM_OFFER.getItemMeta();
		META.setDisplayName("§6§l" + rp.translateString("Offering menu", "Menu des offrandes"));
		META.setLore(Arrays.asList(rp.translateString("§7Open the menu allowing to make offerings", "§7Ouvrir le menu vous permettant de faire des offrandes"), rp.translateString("§7for your guild (from monsters' loot),", "§7pour votre guilde (à partir du butin des monstres),"), rp.translateString("leveling it up and expanding its power.", "§7lui octroyant de l'expérience et de la puissance.")));
		ITEM_OFFER.setItemMeta(META);
		return ITEM_OFFER;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
