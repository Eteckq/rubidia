package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class GRelationsUI extends UIHandler {

	private Guild guild;
	private ItemStack ITEM_ENEMY = new ItemStack(Material.TNT, 1);
	private ItemStack ITEM_INFO = new ItemStack(Material.WATCH, 1);
	private ItemStack ITEM_ALLY = new ItemStack(Material.GOLD_SPADE, 1);
	
	private int SLOT_ENEMY = 1;
	private int SLOT_INFO = 4;
	private int SLOT_ALLY = 7;
	
	public GRelationsUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(gm.getGuild().getName() + " | Relations | " + guild.getName(), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_RELATIONS_MENU";
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			e.setCancelled(true);
			int slot = e.getRawSlot();
			if(slot == this.SLOT_ALLY){
				if(gm.getPermission(Permission.RELATIONS)){
					if(gm.getGuild().getRelationTo(guild).equals(Relation.ALLY)){//if allies, we break the alliance without request
						gm.getGuild().removeAlly(guild);
						guild.removeAlly(gm.getGuild());
						gm.getGuild().broadcastMessage(Relation.MEMBER, "§&d§l" + guild.getName() + " §&cis no longer our ally!", "§&cLa guilde §&d§l" + guild.getName() + " §&cn'est plus notre alliée !");
						guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&cis no longer our ally!", "§&cLa guilde §&d§l" + gm.getGuild().getName() + " §&cn'est plus notre alliée !");
					}else{
						gm.getGuild().removeEnemy(guild);//else they are no longer enemies bc we request an alliance
						if(guild.getAllies().contains(gm.getGuild())){//if target guild has requested an alliance, we form it
							gm.getGuild().addAlly(guild);
							gm.getGuild().broadcastMessage(Relation.MEMBER, "§&cWe are now allied with §&d§l" + guild.getName() +" §&c!", "§&cNous sommes désormais alliés à la guilde §&d§l" + guild.getName() + " §&c!");
							guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&chas accepted the alliance !", "§&cLa guilde §&d§l" + gm.getGuild().getName() + " §&ca accepté l'alliance !");
						}else if(gm.getGuild().getAllies().contains(guild)){//we cancel request if already asked
							gm.getGuild().removeAlly(guild);
							rp.sendMessage("§eYou cancelled alliance request sent to §6§l" + guild.getName() + ".", "§eVous avez annulé la requête d'alliance envoyée à §6§l" + guild.getName() + "§e.");
							guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&ccancelled its alliance request.", "§&cLa guilde §&d§l" + gm.getGuild().getName() + " §&ca annulé sa requête d'alliance.");
						}else{//else we request an alliance
							gm.getGuild().addAlly(guild);
							rp.sendMessage("§eYou requested an alliance to §6§l" + guild.getName() + "§e.", "§eVous avez demandé une alliance à la guilde §6§l" + guild.getName() + "§e.");
							guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&chas asked an alliance !", "§&cLa guilde §&d§l" + gm.getGuild().getName() + " §&ca demandé une alliance !");
						}
					}
				}else rp.sendMessage("§cYou don't have permission to manage guild relations!", "§cVous n'avez pas la permission de gérer les relations de guilde !");
			}else if(slot == this.SLOT_ENEMY){
				if(gm.getPermission(Permission.RELATIONS)){
					if(gm.getGuild().getRelationTo(guild).equals(Relation.ENEMY)){//if enemies, we request a neutrality
						gm.getGuild().removeEnemy(guild);//they are no longer our enemies either way
						if(guild.getEnemies().contains(gm.getGuild())){//if 
							rp.sendMessage("§eYou asked §6§l" + guild.getName() + " a neutral relation.", "§eVous avez demandé à §6§l" + guild.getName() + " §eune relation neutre.");
							guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&chas sent a neutral relation wish.", "§&d§l" + gm.getGuild().getName() + " §&ca envoyé une requête de neutralité.");
						}else{
							guild.broadcastMessage(Relation.MEMBER, "§&d§l" + gm.getGuild().getName() + " §&care now longer our enemies!", "§&d§l" + gm.getGuild().getName() + " §&cne sont plus nos ennemis !");
							gm.getGuild().broadcastMessage(Relation.MEMBER, "§&d§l" + guild.getName() + " §&care now longer our enemies!", "§&d§l" + guild.getName() + " §&cne sont plus nos ennemis !");
						}
					}else{
						if(!gm.getGuild().isPeaceful()){
							if(!guild.isPeaceful()){
								if(gm.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
									gm.getGuild().removeAlly(guild);
									guild.removeAlly(gm.getGuild());
								}
								
								gm.getGuild().addEnemy(guild);
								gm.getGuild().broadcastMessage(Relation.ENEMY, "§&d§l" + guild.getName() + " §&care now our enemies!", "§&d§l" + guild.getName() + " §&csont désormais nos ennemis !");
								guild.broadcastMessage(Relation.ENEMY, "§&d§l" + gm.getGuild().getName() + " §&care now our enemies!", "§&d§l" + gm.getGuild().getName() + " §&csont désormais nos ennemis !");
							}else rp.sendMessage("§4" + guild.getName() + " §cis a peaceful guild! They cannot maintain any opposition.","§4" + guild.getName() + " §cest une guilde en paix ! Elle ne peut entretenir aucune opposition.");
						}else rp.sendMessage("§cYour guild is a peaceful one! You cannot maintain any opposition.","§cVotre guilde est en paix ! Vous ne pouvez entretenir aucune opposition.");
					}
				}else rp.sendMessage("§cYou don't have permission to manage guild relations!", "§cVous n'avez pas la permission de gérer les relations de guilde !");
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_ENEMY, this.getEnemy());
		this.getMenu().setItem(this.SLOT_INFO, this.getInfos());
		this.getMenu().setItem(this.SLOT_ALLY, this.getAlly());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}
	
	
	private ItemStack getEnemy(){
		ItemMeta meta = ITEM_ENEMY.getItemMeta();
		if(gm.getGuild().getEnemies().contains(guild) && !guild.getEnemies().contains(gm.getGuild())){
			meta.setDisplayName(rp.translateString("§c§lBreak opposition", "§c§lBriser l'opposition"));
			meta.setLore(Arrays.asList(rp.translateString("§7Set a neutral relation between", "§7Définir une relation neutre entre"), rp.translateString("§7your guild and " + guild.getName() + ".", "§7votre guilde et " + guild.getName() + ".")));
		}else if(guild.getEnemies().contains(gm.getGuild())){
			meta.setDisplayName(rp.translateString("§c§lRequest neutral relation", "§c§lDemander une relation neutre"));
			meta.setLore(Arrays.asList(rp.translateString("§7Send a neutral relation request to " + guild.getName(), "§7Envoyer une requête de neutralité à " + guild.getName())));
		}else{
			meta.setDisplayName(rp.translateString("§c§lSet opposition to §4§l" + guild.getName(), "§c§lDéfinir une opposition à §4§l" + guild.getName()));
			meta.setLore(Arrays.asList(rp.translateString("§7Set an opposition relation between", "§7Définir une relation d'oppposition entre"), rp.translateString("§7your guild and " + guild.getName() + ".", "§7votre guilde et " + guild.getName() + ".")));
		}
		ITEM_ENEMY.setItemMeta(meta);
		return ITEM_ENEMY;
	}
	private ItemStack getAlly(){
		ItemMeta meta = ITEM_ALLY.getItemMeta();
		if(gm.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
			meta.setDisplayName(rp.translateString("§a§lBreak alliance", "§a§lBriser l'alliance"));
			meta.setLore(Arrays.asList(rp.translateString("§7Set a neutral relation between", "§7Définir une relation neutre entre"), rp.translateString("§7your guild and " + guild.getName() + ".", "§7votre guilde et " + guild.getName() + ".")));
		}else if(gm.getGuild().getAllies().contains(guild)){
			meta.setDisplayName(rp.translateString("§a§lCancel sent ally request", "§a§lAnnuler la requête d'alliance"));
			meta.setLore(Arrays.asList(rp.translateString("§7Cancel alliance request sent to " + guild.getName(), "§7Annuler la requête d'alliance envoyée à " + guild.getName())));
		}else if(guild.getAllies().contains(gm.getGuild())){
			meta.setDisplayName(rp.translateString("§a§lAccept ally request", "§a§lAccepter la requête d'alliance"));
			meta.setLore(Arrays.asList(rp.translateString("§7Accept alliance request sent from " + guild.getName(), "§7Accepter la requête d'alliance de " + guild.getName())));
		}else{
			meta.setDisplayName(rp.translateString("§a§lRequest alliance", "§a§lDemander une alliance"));
			meta.setLore(Arrays.asList(rp.translateString("§7Send alliance request to " + guild.getName(), "§7Envoyer une requête d'alliance à " + guild.getName())));
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM_ALLY.setItemMeta(meta);
		return ITEM_ALLY;
	}
	private ItemStack getInfos(){
		ItemMeta meta = ITEM_INFO.getItemMeta();
		meta.setDisplayName("§6§lInformations");
		meta.setLore(Arrays.asList(rp.translateString("§eAlliance: §7allows " + guild.getName() + "'s members", "§eAlliance : §7Permet aux membres de la guilde " + guild.getName()), rp.translateString("§7to interact, use and build on your territory.", "§7d'intéragir, utiliser et construire sur votre territoire."), rp.translateString("§7(Based on their permissions)", "§7(En fonction de leurs permissions)"), rp.translateString("§eOpposition: §7allows your guild's members", "§eOpposition : §7permet aux membres de votre guilde"), rp.translateString("§7to raid " + guild.getName() + "'s territories.", "§7de lancer des offensives contre la guilde " + guild.getName() + "."), rp.translateString("§7(Based on their permissions)", "§7(En fonction de leurs permissions)")));
		ITEM_INFO.setItemMeta(meta);
		return ITEM_INFO;
	}
	
	public void updateAlly(){
		this.getMenu().setItem(this.SLOT_ALLY, this.getAlly());
		Utils.updateInventory(this.getHolder());
	}

	public void updateEnemy(){
		this.getMenu().setItem(this.SLOT_ENEMY, this.getEnemy());
		Utils.updateInventory(this.getHolder());
	}
}
