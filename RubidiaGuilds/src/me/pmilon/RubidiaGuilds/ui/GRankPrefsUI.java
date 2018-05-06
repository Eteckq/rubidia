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

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.UIHandler;
import me.pmilon.RubidiaCore.ui.UIType;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class GRankPrefsUI extends UIHandler {

	private final Rank rank;
	private int page_id;
	private int start_page;

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_HOMES = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_CLAIMS = new ItemStack(Material.FENCE, 1);
	private ItemStack ITEM_INVITE = new ItemStack(Material.CAKE, 1);
	private ItemStack ITEM_BUILD = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_USEDOORS = new ItemStack(Material.SPRUCE_DOOR_ITEM, 1);
	private ItemStack ITEM_USECHESTS = new ItemStack(Material.CHEST, 1);
	private ItemStack ITEM_NEXT = new ItemStack(Material.ARROW, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DESC = new ItemStack(Material.EMPTY_MAP, 1);
	private ItemStack ITEM_CLAIMSPREFS = new ItemStack(Material.DARK_OAK_FENCE, 1);
	private ItemStack ITEM_RANKSPREFS = new ItemStack(Material.LADDER, 1);
	private ItemStack ITEM_MEMBERSPREFS = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
	private ItemStack ITEM_RELATIONS = new ItemStack(Material.WATCH, 1);
	private ItemStack ITEM_BANK = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_OFFER = new ItemStack(Material.EXP_BOTTLE, 1);
	private ItemStack ITEM_MOBSDAMAGE = new ItemStack(Material.MONSTER_EGG, 1);
	
	ItemStack ITEM_DISABLED = new ItemStack(Material.INK_SACK, 1, (short)8);
	ItemStack ITEM_ENABLED = new ItemStack(Material.INK_SACK, 1, (short)10);
	ItemStack ITEM_INFO = new ItemStack(Material.INK_SACK, 1, (short)13);

	private int SLOT_BACK = 0;
	private int SLOT_NEXT = 8;
	
	//PAGE 1
	private int SLOT_INVITE = 1;
	private int SLOT_CLAIMS = 2;
	private int SLOT_HOMES = 3;
	private int SLOT_BUILD = 5;
	private int SLOT_USEDOORS = 6;
	private int SLOT_USECHESTS = 7;
	
	//PAGE 2
	private int SLOT_NAME = 1;
	private int SLOT_DESC = 2;
	private int SLOT_DISPLAY = 3;
	private int SLOT_CLAIMSPREFS = 4;
	private int SLOT_RANKSPREFS = 5;
	private int SLOT_MEMBERSPREFS = 6;
	private int SLOT_DEFAULTRANK = 7;
	
	//PAGE 3
	private int SLOT_TKBANK = 1;
	private int SLOT_GVBANK = 2;
	private int SLOT_OFFER = 4;
	private int SLOT_MOBSDAMAGE = 6;
	private int SLOT_RELATIONS = 8;
	
	private Guild guild;
	public GRankPrefsUI(Player p, Guild guild, Rank rank, int page) {
		super(p);
		this.guild = guild;
		this.rank = rank;
		this.start_page = page;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getGuild().getName() + " : " + rp.translateString(this.rank.getName() + "s' permissions", "Permissions des " + this.rank.getName().toLowerCase() + "s" + "s"),32));
		ItemMeta META = ITEM_DISABLED.getItemMeta();
		META.setDisplayName(rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer"));
		ITEM_DISABLED.setItemMeta(META);
		ITEM_ENABLED.setItemMeta(META);
		META.setDisplayName(rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir"));
		ITEM_INFO.setItemMeta(META);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public UIType getType() {
		return UIType.GUILD_RANKS_PREFS;
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
				if(this.page_id == 1){
					if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
						this.getUIManager().requestUI(new GRanksUI(this.getHolder(), this.getGuild()));
					}else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
						this.setPage(2);
					}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader() || rp.isOp()){
						if(slot == this.SLOT_CLAIMS || slot == this.SLOT_CLAIMS+9){
							this.rank.setPermission(Permission.CLAIM, !this.rank.getPermission(Permission.CLAIM));
							this.getMenu().setItem(this.SLOT_CLAIMS, this.getCanClaim());
							this.getMenu().setItem(this.SLOT_CLAIMS+9, this.rank.getPermission(Permission.CLAIM) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_INVITE || slot == this.SLOT_INVITE+9){
							this.rank.setPermission(Permission.INVITE, !this.rank.getPermission(Permission.INVITE));
							this.getMenu().setItem(this.SLOT_INVITE, this.getCanInvite());
							this.getMenu().setItem(this.SLOT_INVITE+9, this.rank.getPermission(Permission.INVITE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_BUILD || slot == this.SLOT_BUILD+9){
							this.rank.setPermission(Permission.BUILD, !this.rank.getPermission(Permission.BUILD));
							this.getMenu().setItem(this.SLOT_BUILD, this.getCanBuild());
							this.getMenu().setItem(this.SLOT_BUILD+9, this.rank.getPermission(Permission.BUILD) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_USEDOORS || slot == this.SLOT_USEDOORS+9){
							this.rank.setPermission(Permission.USE_DOORS, !this.rank.getPermission(Permission.USE_DOORS));
							this.getMenu().setItem(this.SLOT_USEDOORS, this.getCanUseDoors());
							this.getMenu().setItem(this.SLOT_USEDOORS+9, this.rank.getPermission(Permission.USE_DOORS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_USECHESTS || slot == this.SLOT_USECHESTS+9){
							this.rank.setPermission(Permission.USE_CHESTS, !this.rank.getPermission(Permission.USE_CHESTS));
							this.getMenu().setItem(this.SLOT_USECHESTS, this.getCanUseChests());
							this.getMenu().setItem(this.SLOT_USECHESTS+9, this.rank.getPermission(Permission.USE_CHESTS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_HOMES || slot == this.SLOT_HOMES+9){
							Core.uiManager.requestUI(new HomePermissionsUI(this.getHolder(),this.getGuild(), this.rank, this.page_id));
						}
						this.rank.resetPermissions(this.getGuild());
					}else rp.sendMessage("§cYou cannot modify permissions for this rank!", "§cVous ne pouvez modifier les permissions de ce rang !");
				}else if(this.page_id == 2){
					if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
						this.setPage(1);
					}else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
						this.setPage(3);
					}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader()){
						if(slot == this.SLOT_NAME || slot == this.SLOT_NAME+9){
							this.rank.setPermission(Permission.RENAME, !this.rank.getPermission(Permission.RENAME));
							this.getMenu().setItem(this.SLOT_NAME, this.getCanRename());
							this.getMenu().setItem(this.SLOT_NAME+9, this.rank.getPermission(Permission.RENAME) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_DESC || slot == this.SLOT_DESC+9){
							this.rank.setPermission(Permission.DESCRIPTION, !this.rank.getPermission(Permission.DESCRIPTION));
							this.getMenu().setItem(this.SLOT_DESC, this.getCanChangeDescription());
							this.getMenu().setItem(this.SLOT_DESC+9, this.rank.getPermission(Permission.DESCRIPTION) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_DISPLAY || slot == this.SLOT_DISPLAY+9){
							this.rank.setPermission(Permission.CAPE, !this.rank.getPermission(Permission.CAPE));
							this.getMenu().setItem(this.SLOT_DISPLAY, this.getCanModifyDisplay());
							this.getMenu().setItem(this.SLOT_DISPLAY+9, this.rank.getPermission(Permission.CAPE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_CLAIMSPREFS || slot == this.SLOT_CLAIMSPREFS+9){
							this.rank.setPermission(Permission.CLAIM_PREFS, !this.rank.getPermission(Permission.CLAIM_PREFS));
							this.getMenu().setItem(this.SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
							this.getMenu().setItem(this.SLOT_CLAIMSPREFS+9, this.rank.getPermission(Permission.CLAIM_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_RANKSPREFS || slot == this.SLOT_RANKSPREFS+9){
							this.rank.setPermission(Permission.RANK_PREFS, !this.rank.getPermission(Permission.RANK_PREFS));
							this.getMenu().setItem(this.SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
							this.getMenu().setItem(this.SLOT_RANKSPREFS+9, this.rank.getPermission(Permission.RANK_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_MEMBERSPREFS || slot == this.SLOT_MEMBERSPREFS+9){
							this.rank.setPermission(Permission.MEMBER_PREFS, !this.rank.getPermission(Permission.MEMBER_PREFS));
							this.getMenu().setItem(this.SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
							this.getMenu().setItem(this.SLOT_MEMBERSPREFS+9, this.rank.getPermission(Permission.MEMBER_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_DEFAULTRANK || slot == this.SLOT_DEFAULTRANK+9){
							this.rank.setPermission(Permission.DEFAULT_RANK, !this.rank.getPermission(Permission.DEFAULT_RANK));
							this.getMenu().setItem(this.SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
							this.getMenu().setItem(this.SLOT_DEFAULTRANK+9, this.rank.getPermission(Permission.DEFAULT_RANK) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}
						this.rank.resetPermissions(this.getGuild());
					}else rp.sendMessage("§cYou cannot modify permissions for this rank!", "§cVous ne pouvez modifier les permissions de ce rang !");
				}else if(this.page_id == 3){
					if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
						this.setPage(2);
					}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader()){
						if(slot == this.SLOT_TKBANK || slot == this.SLOT_TKBANK+9){
							this.rank.setPermission(Permission.BANK_WITHDRAW, !this.rank.getPermission(Permission.BANK_WITHDRAW));
							this.getMenu().setItem(this.SLOT_TKBANK, this.getCanTakeBank());
							this.getMenu().setItem(this.SLOT_TKBANK+9, this.rank.getPermission(Permission.BANK_WITHDRAW) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_GVBANK || slot == this.SLOT_GVBANK+9){
							this.rank.setPermission(Permission.BANK_DEPOSIT, !this.rank.getPermission(Permission.BANK_DEPOSIT));
							this.getMenu().setItem(this.SLOT_GVBANK, this.getCanGiveBank());
							this.getMenu().setItem(this.SLOT_GVBANK+9, this.rank.getPermission(Permission.BANK_DEPOSIT) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_OFFER || slot == this.SLOT_OFFER+9){
							this.rank.setPermission(Permission.OFFER, !this.rank.getPermission(Permission.OFFER));
							this.getMenu().setItem(this.SLOT_OFFER, this.getCanOffer());
							this.getMenu().setItem(this.SLOT_OFFER+9, this.rank.getPermission(Permission.OFFER) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_RELATIONS || slot == this.SLOT_RELATIONS+9){
							this.rank.setPermission(Permission.RELATIONS, !this.rank.getPermission(Permission.RELATIONS));
							this.getMenu().setItem(this.SLOT_RELATIONS, this.getCanManageRelations());
							this.getMenu().setItem(this.SLOT_RELATIONS+9, this.rank.getPermission(Permission.RELATIONS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}else if(slot == this.SLOT_MOBSDAMAGE || slot == this.SLOT_MOBSDAMAGE+9){
							this.rank.setPermission(Permission.DAMAGE_MOBS, !this.rank.getPermission(Permission.DAMAGE_MOBS));
							this.getMenu().setItem(this.SLOT_MOBSDAMAGE, this.getCanDamageMobs());
							this.getMenu().setItem(this.SLOT_MOBSDAMAGE+9, this.rank.getPermission(Permission.DAMAGE_MOBS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
						}
						this.rank.resetPermissions(this.getGuild());
					}else rp.sendMessage("§cYou cannot modify permissions for this rank!", "§cVous ne pouvez modifier les permissions de ce rang !");
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
		this.setPage(start_page);
		
		return this.getHolder().openInventory(this.getMenu()) != null;
	}
	
	private void setPage(int index){
		if(index == 1){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack());
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext(2));
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_CLAIMS, this.getCanClaim());
			getMenu().setItem(SLOT_CLAIMS+9, (this.rank.getPermission(Permission.CLAIM) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_BUILD, this.getCanBuild());
			getMenu().setItem(SLOT_BUILD+9, (this.rank.getPermission(Permission.BUILD) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_INVITE, this.getCanInvite());
			getMenu().setItem(SLOT_INVITE+9, (this.rank.getPermission(Permission.INVITE) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_USEDOORS, this.getCanUseDoors());
			getMenu().setItem(SLOT_USEDOORS+9, (this.rank.getPermission(Permission.USE_DOORS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_USECHESTS, this.getCanUseChests());
			getMenu().setItem(SLOT_USECHESTS+9, (this.rank.getPermission(Permission.USE_CHESTS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_HOMES, this.getHomes());
			getMenu().setItem(SLOT_HOMES+9, this.ITEM_INFO);
			
		}else if(index == 2){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack(1));
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext(3));
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_NAME, this.getCanRename());
			getMenu().setItem(SLOT_NAME+9, (this.rank.getPermission(Permission.RENAME) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DESC, this.getCanChangeDescription());
			getMenu().setItem(SLOT_DESC+9, (this.rank.getPermission(Permission.DESCRIPTION) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DISPLAY, this.getCanModifyDisplay());
			getMenu().setItem(SLOT_DISPLAY+9, (this.rank.getPermission(Permission.CAPE) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
			getMenu().setItem(SLOT_CLAIMSPREFS+9, (this.rank.getPermission(Permission.CLAIM_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
			getMenu().setItem(SLOT_RANKSPREFS+9, (this.rank.getPermission(Permission.RANK_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
			getMenu().setItem(SLOT_MEMBERSPREFS+9, (this.rank.getPermission(Permission.MEMBER_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
			getMenu().setItem(SLOT_DEFAULTRANK+9, (this.rank.getPermission(Permission.DEFAULT_RANK) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
		}else if(index == 3){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack(2));
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_TKBANK, this.getCanTakeBank());
			getMenu().setItem(SLOT_TKBANK+9, (this.rank.getPermission(Permission.BANK_WITHDRAW) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_GVBANK, this.getCanGiveBank());
			getMenu().setItem(SLOT_GVBANK+9, (this.rank.getPermission(Permission.BANK_DEPOSIT) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_OFFER, this.getCanOffer());
			getMenu().setItem(SLOT_OFFER+9, (this.rank.getPermission(Permission.OFFER) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_RELATIONS, this.getCanManageRelations());
			getMenu().setItem(SLOT_RELATIONS+9, (this.rank.getPermission(Permission.RELATIONS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_MOBSDAMAGE, this.getCanDamageMobs());
			getMenu().setItem(SLOT_MOBSDAMAGE+9, (this.rank.getPermission(Permission.DAMAGE_MOBS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
		}
		this.page_id = index;
	}
	
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName(rp.translateString("§6§lRanks' List", "§6§lListe des rangs"));
		META_BACK.setLore(Arrays.asList(rp.translateString("§7Get back to ranks' list.", "§7Retourner à la liste des rangs."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getBack(int index){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§lPage #" + index);
		META_BACK.setLore(Arrays.asList(rp.translateString("§7Get back to page #" + index + ".", "§7Retourner à la page #" + index + "."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getNext(int index){
		ItemMeta META = ITEM_NEXT.getItemMeta();
		META.setDisplayName("§6§lPage #" + index);
		META.setLore(Arrays.asList(rp.translateString("§7Go to page #" + index + ".", "§7Aller à la page #" + index + "."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_NEXT.setItemMeta(META);
		return ITEM_NEXT;
	}
	private ItemStack getCanClaim(){
		ItemMeta META = ITEM_CLAIMS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CLAIM) ? "§a§l" : "§c§l") + rp.translateString("Claim permission", "Permission de conquérir"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to manage", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à gérer"), rp.translateString("§7your guild's territory.", "§7les territoires de votre guilde."), rp.translateString("§7Includes permission to start a raid.", "§7Inclut la permission de lancer un raid."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_CLAIMS.setItemMeta(META);
		return ITEM_CLAIMS;
	}
	private ItemStack getCanBuild(){
		ItemMeta META = ITEM_BUILD.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.BUILD) ? "§a§l" : "§c§l") + rp.translateString("Build permission", "Permission de construire"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to build", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à construire"), rp.translateString("§7inside your guild's territory.", "§7à l'intérieur du territoire."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_BUILD.setItemMeta(META);
		return ITEM_BUILD;
	}
	private ItemStack getCanInvite(){
		ItemMeta META = ITEM_INVITE.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.INVITE) ? "§a§l" : "§c§l") + rp.translateString("Invite permission", "Permission d'invitation"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to invite", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à inviter"), rp.translateString("§7players to join your guild.", "§7d'autres joueurs à rejoindre votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_INVITE.setItemMeta(META);
		return ITEM_INVITE;
	}
	private ItemStack getCanUseDoors(){
		ItemMeta META = ITEM_USEDOORS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.USE_DOORS) ? "§a§l" : "§c§l") + rp.translateString("Interact permission", "Permission d'intéraction"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to use", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à utiliser"), rp.translateString("§7doors, gates, buttons, plates...", "§7les portes, boutons, plaques de pression..."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_USEDOORS.setItemMeta(META);
		return ITEM_USEDOORS;
	}
	private ItemStack getCanUseChests(){
		ItemMeta META = ITEM_USECHESTS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.USE_CHESTS) ? "§a§l" : "§c§l") + rp.translateString("Use permission", "Permission d'utilisation du matériel"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to use", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à utiliser"), rp.translateString("§7chests, furnaces, enchantment tables...", "§7les coffres, fours, tables d'enchantement..."), rp.translateString("§7Remember: trapped chests will §nalways§7 be accessible!", "§7Les coffres piégés seront §ntoujours§7 accessibles !"), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_USECHESTS.setItemMeta(META);
		return ITEM_USECHESTS;
	}
	private ItemStack getCanRename(){
		ItemMeta META = ITEM_NAME.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RENAME) ? "§a§l" : "§c§l") + rp.translateString("Rename permission", "Permission de renommer"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s"), rp.translateString("§7rename your guild.", "§7à renommer votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_NAME.setItemMeta(META);
		return ITEM_NAME;
	}
	private ItemStack getCanChangeDescription(){
		ItemMeta META = ITEM_DESC.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DESCRIPTION) ? "§a§l" : "§c§l") + rp.translateString("Change description permission", "Permission de modification de description"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7your guild's description.", "§7la description de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_DESC.setItemMeta(META);
		return ITEM_DESC;
	}
	private ItemStack getCanModifyClaimsPrefs(){
		ItemMeta META = ITEM_CLAIMSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CLAIM_PREFS) ? "§a§l" : "§c§l") + rp.translateString("Claim preferences modification permission", "Permission de modification des préférences de territoire"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7your guild's territory preferences.", "§7les préférences de protection du territoire."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_CLAIMSPREFS.setItemMeta(META);
		return ITEM_CLAIMSPREFS;
	}
	private ItemStack getCanModifyRanksUnderPrefs(){
		ItemMeta META = ITEM_RANKSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RANK_PREFS) ? "§a§l" : "§c§l") + rp.translateString("Subordinate ranks permissions management permission", "Permission de gestion des permissions de rangs inférieurs"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to manage", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à gérer"), rp.translateString("§7ranks of lower position's permissions.", "§7les permissions des rangs de position inférieure."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_RANKSPREFS.setItemMeta(META);
		return ITEM_RANKSPREFS;
	}
	private ItemStack getCanModifyPerMemberPrefs(){
		ItemMeta META = ITEM_MEMBERSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.MEMBER_PREFS) ? "§a§l" : "§c§l") + rp.translateString("Per member permissions management permission", "Permission de gestion des permissions par membre"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7modify your guild's per member permissions.", "§7les permissions par membre de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_MEMBERSPREFS.setItemMeta(META);
		return ITEM_MEMBERSPREFS;
	}
	private ItemStack getCanModifyDisplay(){
		ItemStack ITEM_DISPLAY = this.getGuild().getCape();
		ItemMeta META = ITEM_DISPLAY.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CAPE) ? "§a§l" : "§c§l") + rp.translateString("Guild's cape modification permission", "Permission de modification de la cape de guilde"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7modify your guild's cape.", "§7la cape de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_DISPLAY.setItemMeta(META);
		return ITEM_DISPLAY;
	}
	private ItemStack getHomes(){
		ItemMeta META = ITEM_HOMES.getItemMeta();
		META.setDisplayName("§6§l" + rp.translateString("Guilds homes permissions", "Permissions des points de rassemblement"));
		META.setLore(Arrays.asList(rp.translateString("§7Manage " + this.rank.getName().toLowerCase() + "s' per home permissions.", "§7Gérer les permissions par PR des " + this.rank.getName().toLowerCase() + "s."), "", rp.translateString("§e§lClick to open", "§e§lCliquez pour ouvrir")));
		ITEM_HOMES.setItemMeta(META);
		return ITEM_HOMES;
	}
	private ItemStack getCanSetDefaultRank(){
		ItemStack ITEM = this.getGuild().getDefaultRank().getItemStack();
		ItemMeta META = ITEM.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DEFAULT_RANK) ? "§a§l" : "§c§l") + rp.translateString("Default rank permission", "Permission de modification du rang par défaut"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7modify your guild's default rank.", "§7le rang par défaut de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM.setItemMeta(META);
		return ITEM;
	}
	private ItemStack getCanManageRelations(){
		ItemMeta META = ITEM_RELATIONS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RELATIONS) ? "§a§l" : "§c§l") + rp.translateString("Relations management permission", "Permission de gestion des relations"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to modify", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), rp.translateString("§7manage your guild's relations (alliances/oppositions).", "§7les relations de votre guilde (alliances/oppositions)."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_RELATIONS.setItemMeta(META);
		return ITEM_RELATIONS;
	}
	private ItemStack getCanTakeBank(){
		ItemMeta META = ITEM_BANK.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.BANK_WITHDRAW) ? "§a§l" : "§c§l") + rp.translateString("Bank withdraw permission", "Permission de retrait de la banque"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to withdraw", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à retirer"), rp.translateString("§7emeralds from your guild's bank.", "§7des émeraudes de la banque de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_BANK.setItemMeta(META);
		return ITEM_BANK;
	}
	private ItemStack getCanGiveBank(){
		ItemMeta META = ITEM_BANK.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.BANK_DEPOSIT) ? "§a§l" : "§c§l") + rp.translateString("Bank deposit permission", "Permission de dépôt dans la banque"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to depose", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à déposer"), rp.translateString("§7emeralds in your guild's bank.", "§7des émeraudes dans la banque de votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_BANK.setItemMeta(META);
		return ITEM_BANK;
	}
	private ItemStack getCanOffer(){
		ItemMeta META = ITEM_OFFER.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.OFFER) ? "§a§l" : "§c§l") + rp.translateString("Offering permission", "Permission d'offrande"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to offer", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à faire"), rp.translateString("§7and level up your guild.", "§7des offrandes pour votre guilde."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_OFFER.setItemMeta(META);
		return ITEM_OFFER;
	}
	private ItemStack getCanDamageMobs(){
		ItemMeta META = ITEM_MOBSDAMAGE.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DAMAGE_MOBS) ? "§a§l" : "§c§l") + rp.translateString("Tamed mob damage permission", "Permission de dégâts aux monstres apprivoisés"));
		META.setLore(Arrays.asList(rp.translateString("§7Allow " + this.rank.getName().toLowerCase() + "s to damage", "§7Autoriser les " + this.rank.getName().toLowerCase() + "s à infliger"), rp.translateString("§7tamed mobs inside your territory.", "§7des dégâts aux monstres apprivoisés."), "", rp.translateString("§e§lClick to toggle", "§e§lCliquez pour basculer")));
		ITEM_MOBSDAMAGE.setItemMeta(META);
		return ITEM_MOBSDAMAGE;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
}
