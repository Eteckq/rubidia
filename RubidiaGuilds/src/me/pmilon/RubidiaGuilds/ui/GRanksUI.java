package me.pmilon.RubidiaGuilds.ui;

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
import me.pmilon.RubidiaCore.ui.UIHandler;
import me.pmilon.RubidiaCore.ui.UIType;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class GRanksUI extends UIHandler {
	
	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	
	ItemStack ITEM_INFO = new ItemStack(Material.INK_SACK, 1, (short)7);
	ItemStack ITEM_OPEN = new ItemStack(Material.INK_SACK, 1, (short)13);
	ItemStack ITEM_RANKINFO = new ItemStack(Material.INK_SACK, 1, (short)9);

	private int SLOT_BACK = 0;
	private int SLOT_RANK = 1;
	private int SLOT_DEFAULTRANK = 8;
	
	private Guild guild;
	public GRanksUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getGuild().getName() + rp.translateString(" : Ranks", " : Rangs"),32));
		ItemMeta META = ITEM_INFO.getItemMeta();
		META.setDisplayName(rp.translateString("§e§lClick to manage permissions", "§e§lCliquez pour gérer les permissions"));
		ITEM_INFO.setItemMeta(META);
		META.setDisplayName(rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir"));
		ITEM_OPEN.setItemMeta(META);
		META.setDisplayName(rp.translateString("§e§lClick to cycle", "§e§lCliquez pour cycler"));
		ITEM_RANKINFO.setItemMeta(META);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public UIType getType() {
		return UIType.GUILD_RANKS;
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
				if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
					this.getUIManager().requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
				}else if(slot == this.SLOT_DEFAULTRANK || slot == this.SLOT_DEFAULTRANK+9){
					if(gm.getPermission(Permission.DEFAULT_RANK)){
						Rank rank = null;
						for(int i = this.getGuild().getDefaultRankId()+1;i < this.getGuild().getRanks().length;i++){
							Rank rk = this.getGuild().getRanks()[i];
							if(rk != null){
								rank = rk;
								break;
							}
						}
						if(rank == null){
							for(int i = 1;i < this.getGuild().getDefaultRankId();i++){
								Rank rk = this.getGuild().getRanks()[i];
								if(rk != null){
									rank = rk;
									break;
								}
							}
						}
						if(rank != null)this.getGuild().setDefaultRankId(rank.getId());
						this.getMenu().setItem(this.SLOT_DEFAULTRANK, this.getDefaultRank());
					}
				}else{
					if(slot > 8){
						Rank rank = this.getGuild().getRanks()[slot-9-this.SLOT_RANK];
						if(rank == null){
							rp.sendMessage("§cPlease set a name and a display item for this rank first.", "§cDéfinissez d'abord un nom et un item pour ce rang.");
							return;
						}
						Core.uiManager.requestUI(new GRankPrefsUI(this.getHolder(), this.getGuild(), rank, 1));
					}else{
						if(e.isLeftClick()){
							this.close(true, slot-this.SLOT_RANK);
							rp.sendMessage("§aTake an item in your hands and type in the name of this rank. These will then be linked.", "§aPrenez un item entre vos mains et entrez dans le chat le nom de ce rang.");
						}else{
							if(slot > this.SLOT_RANK && slot < this.SLOT_RANK+6){
								Rank rank = null;
								for(int i = slot-this.SLOT_RANK+1;i < this.getGuild().getRanks().length;i++){
									Rank rk = this.getGuild().getRanks()[i];
									if(rk != null){
										rank = rk;
										break;
									}
								}
								for(GMember member : this.getGuild().getMembers()){
									if(member.getRankId() == slot-this.SLOT_RANK){
										member.setRank(rank);
									}
								}
								this.getGuild().getRanks()[slot-this.SLOT_RANK] = null;
								this.getMenu().setItem(slot, this.getDefault());
								rp.sendMessage("§cRank successfully deleted. All members of this rank are now " + rank.getName().toLowerCase() + "s.", "§cLe rang a été supprimé. Tous les membres sont désormais des " + rank.getName().toLowerCase() + "s.");
							}else rp.sendMessage("§cYou cannot modify this rank!", "§cVous ne pouvez modifier ce rang !");
						}
					}
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
				String name = StringUtils.right(StringUtils.capitalize(ChatColor.stripColor(this.getMessage())).replaceAll(" ", ""), 10);
				
				boolean ok = true;
				for(Rank rank : this.getGuild().getRanks()){
					if(rank != null){
						if(rank.getName().equals(name)){
							ok = false;
							break;
						}
					}
				}
				
				if(ok){
					ItemStack temp = this.getHolder().getInventory().getItemInMainHand();
					ItemStack item = temp.hasItemMeta() ? temp.clone() : new ItemStack(temp.getType(), 1, temp.getDurability());
					item.setAmount(1);
					ItemMeta meta = item.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
					item.setItemMeta(meta);
					Rank rank = this.getGuild().getRanks()[this.getListeningId()];
					if(rank == null){
						rank = new Rank(item, name, this.getListeningId(), Permission.getDefault(false), new boolean[8], new boolean[8]);
						this.getGuild().getRanks()[this.getListeningId()] = rank;
					}else{
						rank.setItemStack(item);
						rank.setName(name);
					}
				}else{
					rp.sendMessage("§cAnother rank already holds this name!","§cUn autre rang porte déjà ce nom !");
				}
			}
		}
		
		getMenu().setItem(SLOT_BACK, this.getBack());
		getMenu().setItem(SLOT_BACK+9, this.ITEM_OPEN);
		
		getMenu().setItem(SLOT_DEFAULTRANK, this.getDefaultRank());
		getMenu().setItem(SLOT_DEFAULTRANK+9, this.ITEM_RANKINFO);
		
		for(int i = 0;i < this.getGuild().getRanks().length;i++){
			Rank rank = this.getGuild().getRanks()[i];
			ItemStack item = null;
			if(rank != null){
				item = rank.getItemStack();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§6§l" + rank.getName());
				meta.setLore(Arrays.asList("§7" + rp.translateString("Left-click to edit name and item.", "Cliquez gauche pour éditer le nom et l'item."), "§7" + rp.translateString("Right-click to delete this rank.", "Cliquez droit pour supprimer ce rang."), "§8§o" + rp.translateString("All members of this rank will then become recruits.", "Tous les membres de ce rang seront alors des recrues."), "", "§7" + rp.translateString("Click on the button below to manage this rank's permissions.", "Cliquez sur le bouton ci-dessous pour gérer les permissions de ce rang.")));
				item.setItemMeta(meta);
			}else item = this.getDefault();
			
			this.getMenu().setItem(i+this.SLOT_RANK, item);
			this.getMenu().setItem(i+this.SLOT_RANK+9, this.ITEM_INFO);
		}
		
		return this.getHolder().openInventory(getMenu()) != null;
	}

	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§l" + rp.translateString("Main menu", "Menu principal"));
		META_BACK.setLore(Arrays.asList(rp.translateString("§7Get back to the main menu.", "§7Retourner au menu principal."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getDefault(){
		ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.translateString("NEW RANK", "NOUVEAU RANG"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Left-click to create a new rank at this position.", "Cliquez gauche pour créer un nouveau rang à cette position.")));
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack getDefaultRank(){
		ItemStack ITEM_DEFAULTRANK = this.getGuild().getDefaultRank().getItemStack();
		ItemMeta META = ITEM_DEFAULTRANK.getItemMeta();
		META.setDisplayName("§6§l" + rp.translateString("Default Rank", "Rang attribué par défaut"));
		META.setLore(Arrays.asList(rp.translateString("§7Cycle through the 4 different available", "§7Cyclez parmis les 4 différents rangs"), rp.translateString("§7ranks to choose the one that will be", "§7disponible pour choisir celui qui sera"), rp.translateString("§7assigned to a new member by default.", "§7attribué par défaut à un nouveau membre."),"", rp.translateString("§7This rank's permissions will be the same for your allies.", "§7Les permissions de ce rang seront les mêmes pour vos alliés."), rp.translateString("§7(Only concerns territory protection permissions)", "§7(Ne concerne que les permissions de protection des territoires))"), "", rp.translateString("§e§lClick to cycle", "§e§lCliquez pour cycler")));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM_DEFAULTRANK.setItemMeta(META);
		return ITEM_DEFAULTRANK;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
