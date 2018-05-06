package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.UIHandler;
import me.pmilon.RubidiaCore.ui.UIType;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.events.ASyncGMemberCreateGuildEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GCreateMenuUI extends UIHandler {
	
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DESC = new ItemStack(Material.EMPTY_MAP, 1);
	private ItemStack ITEM_CREATE = new ItemStack(Material.BOOK_AND_QUILL, 1);
	
	private int SLOT_NAME = 1;
	private int SLOT_DESC = 2;
	private int SLOT_PEACE = 4;
	private int SLOT_CREATE = 7;

	private int LISTENINGID_NAME = 1;
	private int LISTENINGID_DESC = 2;
	
	private boolean peaceful = false;
	
	public GCreateMenuUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("Guild creation", "Création de guilde"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public UIType getType() {
		return UIType.GUILD_CREATE;
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player player) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				if(slot == this.SLOT_NAME){
					rp.sendMessage("§aType the desired name in the chat! (MAX: " + Guild.NAME_LENGTH + " characters)", "§aEntrez le nom désiré dans le chat ! (MAX: " + Guild.NAME_LENGTH + " caractères)");
					this.close(true, this.LISTENINGID_NAME);
				}else if(slot == this.SLOT_DESC){
					rp.sendMessage("§aType the desired description in the chat!", "§aEntrez la description désirée dans le chat !");
					this.close(true, this.LISTENINGID_DESC);
				}else if(slot == this.SLOT_PEACE){
					//this.setPeaceful(!this.isPeaceful());
					//this.menu.setItem(this.SLOT_PEACE, this.getPeace());
					rp.sendMessage("§cThis option is disabled.", "§cCette option est désativée.");
				}else if(slot == this.SLOT_CREATE){
					Guild guild = GuildsPlugin.gcoll.addDefault(GuildsPlugin.guildCreationName.get(gm), GuildsPlugin.guildCreationDescription.get(gm), gm, this.isPeaceful());
					ASyncGMemberCreateGuildEvent event = new ASyncGMemberCreateGuildEvent(guild, gm);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())event.getGuild().disband();
					rp.sendMessage("§aYou've successfully founded §2§l" + gm.getGuild().getName() + "§a!", "§aVous venez de fonder la guilde §2§l" + gm.getGuild().getName() + " §a!");
					Core.broadcast("§2" + this.getHolder().getName() + " §ahas just founded §2§l" + gm.getGuild().getName() + "§a!", "§2" + this.getHolder().getName() + " §avient de fonder la guilde §2§l" + gm.getGuild().getName() + "§a !", this.getHolder());
					this.close(false);
				}
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
						GuildsPlugin.guildCreationName.put(gm, name);
					}else rp.sendMessage("§cA guild with this name already exists!", "§cUne guilde avec le même nom existe déjà !");
				}else if(this.getListeningId() == this.LISTENINGID_DESC)GuildsPlugin.guildCreationDescription.put(gm, this.getMessage().replace('§', '?'));
			}
		}
		this.menu.setItem(this.SLOT_NAME, this.getGuildName());
		this.menu.setItem(this.SLOT_DESC, this.getGuildDesc());
		this.menu.setItem(this.SLOT_PEACE, this.getPeace());
		this.menu.setItem(this.SLOT_CREATE, this.getCreator());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	
	private ItemStack getGuildName(){
		ItemMeta paperm = ITEM_NAME.getItemMeta();
		String name = GuildsPlugin.guildCreationName.get(gm);
		if(name == null){
			name = rp.translateString(gm.getName() + "sGuild",gm.getName() + "Guilde");
			GuildsPlugin.guildCreationName.put(gm, name);
		}
		paperm.setDisplayName("§a§l" + rp.translateString("Guild Name", "Nom de Guilde"));
		paperm.setLore(Arrays.asList(rp.translateString("§7Change your guild name", "§7Changez votre nom de guilde"), "", rp.translateString("§6§lCurrent name: ", "§6§lNom actuel : ") + "§e" + name));
		ITEM_NAME.setItemMeta(paperm);
		return ITEM_NAME;
	}
	private ItemStack getGuildDesc(){
		ItemMeta paperm = ITEM_DESC.getItemMeta();
		String description  = (GuildsPlugin.guildCreationDescription.get(gm));
		if(description == null){
			description = rp.translateString("New Guild!", "Nouvelle Guilde !");
			GuildsPlugin.guildCreationDescription.put(gm, description);
		}
		paperm.setDisplayName("§a§l" + rp.translateString("Guild Description", "Description de Guilde"));
		paperm.setLore(Arrays.asList(rp.translateString("§7Change your guild description", "§7Changez votre description de guilde"), "", rp.translateString("§6§lCurrent description: ", "§6§lDescription actuelle : ") + "§e" + (description.length() > Guild.DESC_LENGTH ? StringUtils.abbreviate(description, Guild.DESC_LENGTH) : description)));
		ITEM_DESC.setItemMeta(paperm);
		return ITEM_DESC;
	}
	private ItemStack getPeace(){
		ItemStack stack = new ItemStack(Material.INK_SACK, 1, (short) (this.isPeaceful() ? 10 : 8));
		ItemMeta paperm = stack.getItemMeta();
		paperm.setDisplayName((this.isPeaceful() ? "§a§l" : "§c§l") + rp.translateString("Peaceful Guild", "Guilde en paix"));
		paperm.setLore(Arrays.asList(rp.translateString("§7Peaceful guilds are invulnerable to TNT,", "§7Les guildes en paix sont invulnérables à la TNT,"), rp.translateString("§7skills and damages in general EVERYWHERE.", "§7aux compétences et aux dégâts en général de PARTOUT."), rp.translateString("§7It's a way to disable PVP and play with your friends!", "§7C'est un moyen de désactiver le PVP tout en jouant avec vos amis !"), rp.translateString("§7Their territories can't be claimed by other guilds", "§7Leurs territoires ne peuvent être conquis par d'autres guildes"), rp.translateString("§7and they can't be declared as enemy to any guild.", "§7et ne peuvent être déclarées ennemies à aucune guilde.")));
		stack.setItemMeta(paperm);
		return stack;
	}
	private ItemStack getCreator(){
		ItemMeta paperm = ITEM_CREATE.getItemMeta();
		paperm.setDisplayName("§a§l" + rp.translateString("Found Guild", "Fonder la guilde"));
		ITEM_CREATE.setItemMeta(paperm);
		return ITEM_CREATE;
	}

	public boolean isPeaceful() {
		return peaceful;
	}

	public void setPeaceful(boolean peaceful) {
		this.peaceful = peaceful;
	}
	
	
}
