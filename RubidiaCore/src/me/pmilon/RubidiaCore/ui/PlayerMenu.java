package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.duels.RDuel;
import me.pmilon.RubidiaCore.handlers.TradingHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.ui.GRelationsUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerMenu extends UIHandler {

	private final Player from;
	private final RPlayer rpfrom;
	private final GMember mfrom;
	
	private int SLOT_DUEL = 0;
	private int SLOT_TRADE = 2;
	private int SLOT_INVITE = 4;
	private int SLOT_INSPECT = 6;
	private int SLOT_MARRY = 8;
	
	private boolean showFiancer = true;
	public PlayerMenu(Player holder, Player from) {
		super(holder);
		this.from = from;
		this.rpfrom = RPlayer.get(from);
		this.mfrom = GMember.get(from);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString(from.getName() + "'s menu", "Menu de " + from.getName()));
		if(rp.getCouple() != null){
			if(!rp.getCouple().getCompanion1().equals(rpfrom) && !rp.getCouple().getCompanion2().equals(rpfrom)){
				this.showFiancer = false;
				this.SLOT_TRADE++;
				this.SLOT_INVITE++;
				this.SLOT_INSPECT+=2;
			}
		}
	}

	@Override
	public String getType() {
		return "PLAYER_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(SLOT_DUEL, this.getDuel());
		this.menu.setItem(SLOT_TRADE, this.getTrade());
		this.menu.setItem(SLOT_INVITE, this.getInvite());
		this.menu.setItem(SLOT_INSPECT, this.getInspect());
		if(this.showFiancer)this.menu.setItem(SLOT_MARRY, this.getMarry());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				if(e.getRawSlot() == SLOT_DUEL){
					int deviation = Math.abs(rp.getRLevel()-this.rpfrom.getRLevel());
					if(deviation <= Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX && System.currentTimeMillis()-rp.getLastCompetitiveDuelDateAgainst(rpfrom) > Settings.COMPETITIVE_DUEL_DELAY_MAX*60*1000L*Math.pow(deviation/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,1.8) && rpfrom.getRequestedDuelTo(rp) == null && rp.getRequestedDuelTo(rpfrom) == null)Core.uiManager.requestUI(new DuelCompetitiveUI(this.rp, this.rpfrom));
					else{
						rp.requestDuel(this.rpfrom,false);
						this.getMenu().setItem(this.SLOT_DUEL, this.getDuel());
						if(rpfrom.getRequestedDuelTo(rp) != null)this.close(false);
					}
				}else if(e.getRawSlot() == SLOT_TRADE){
					TradingHandler.requestTrade(this.getHolder(), this.from);
					this.close(false);
				}else if(e.getRawSlot() == SLOT_INSPECT)Core.uiManager.requestUI(new InspectUI(this.getHolder(), this.from));
				else if(e.getRawSlot() == SLOT_INVITE){
					if(mfrom.hasGuild()){
						if(!gm.hasGuild())mfrom.getGuild().ask(gm);
						else if(!mfrom.getGuild().equals(gm.getGuild()))Core.uiManager.requestUI(new GRelationsUI(this.getHolder(), mfrom.getGuild()));
					}else if(gm.hasGuild()){
						if(gm.getPermission(Permission.INVITE) || gm.isLeader()){
							gm.getGuild().invite(mfrom);
							if(mfrom.isInvited(gm.getGuild()))rp.sendMessage("§eYou invited §6" + mfrom.getName() + " §eto join your guild.", "§eVous avez invité §6" + mfrom.getName() + " §eà rejoindre votre guilde.");
							else rp.sendMessage("§eYou cancelled §6" + mfrom.getName() + "§e's invitation to join the guild.", "§eVous avez annulé l'invitation à rejoindre la guilde de §6" + mfrom.getName() + "§e.");
						}else rp.sendMessage("§cYou don't have permission to invite players to join your guild!","§cVous n'avez pas la permission d'inviter des joueurs à rejoindre votre guilde !");
					}else rp.sendMessage("§cYou don't belong to any guild!", "§cVous n'appartenez à aucune guilde !");
					this.getMenu().setItem(this.SLOT_INVITE, this.getInvite());
				}else if(e.getRawSlot() == SLOT_MARRY){
					if(rp.getCouple() != null && this.showFiancer){
						Core.uiManager.requestUI(new DivorceConfirmationUI(rp));
					}else{
						if(rpfrom.equals(rp.fiance)){
							rp.fiance(null);
							rpfrom.fiance(null);
							rp.sendMessage("§cYou cancelled your wedding proposal...", "§cVous avez annulé votre demande en mariage...");
							rpfrom.sendMessage("§4" + rp.getName() + " §ccancelled the wedding proposal...", "§4" + rp.getName() + " §ca annulé la demande en mariage...");
						}else{
							if(rp.isOp() || rp.getLastDivorce()+Settings.TIME_BEFORE_WEDDING_PROPOSAL <= System.currentTimeMillis()){
								rp.fiance(rpfrom);
								if(rp.equals(rpfrom.fiance)){
									rp.sendMessage("§aYou are now fianced to §2" + rpfrom.getName() + "§a!", "§aVous êtes désormais fiancé(e) à §2" + rpfrom.getName() + " §a!");
									rpfrom.sendMessage("§aYou are now fianced to §2" + rp.getName() + "§a!", "§aVous êtes désormais fiancé(e) à §2" + rp.getName() + " §a!");
									
									rp.sendMessage("§eYou now need the help of a pastor to organize your wedding!", "§eVous avez maintenant besoin de l'aide d'un pasteur pour organiser votre mariage !");
									rpfrom.sendMessage("§eYou now need to ask a pastor to organize your wedding!", "§eVous avez désormais besoin de demander à un pasteur d'organiser votre mariage !");
								}else{
									rp.sendMessage("§eYou proposed to §6" + rpfrom.getName() + "§e...", "§eVous avez demandé §6" + rpfrom.getName() + " §een mariage...");
									rpfrom.sendMessage("§6" + rp.getName() + " §eproposed you!", "§6" + rp.getName() + " §evous a demandé en mariage !");
								}
							}else{
								long time1 = rp.getLastDivorce()+Settings.TIME_BEFORE_WEDDING_PROPOSAL-System.currentTimeMillis();
								long hours1 = TimeUnit.MILLISECONDS.toHours(time1);
								time1 -= TimeUnit.HOURS.toMillis(hours1);
								long minutes1 = TimeUnit.MILLISECONDS.toMinutes(time1);
								time1 -= TimeUnit.MINUTES.toMillis(minutes1);
								long seconds1 = TimeUnit.MILLISECONDS.toSeconds(time1);
								time1 -= TimeUnit.SECONDS.toMillis(seconds1);
								String time = String.format("§4%02d §ch. §4%02d §cmin. §4%02d §cs.", hours1, minutes1, seconds1);
								rp.sendMessage("§cYou just divorced! Please wait " + time, "§cVous venez de divorcer ! Attendez encore " + time);
							}
						}
					}
					this.getMenu().setItem(this.SLOT_MARRY, this.getMarry());
				}
			}
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		//not listening
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private ItemStack getDuel(){
		ItemStack duel = new ItemStack(Material.SHIELD, 1);
		ItemMeta duelm = duel.getItemMeta();
		duelm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		RDuel rduel = rpfrom.getRequestedDuelTo(rp);
		if(rduel != null){
			duelm.setDisplayName(rp.translateString("§a§lAccept " + (rduel.isCompetitive() ? "competitive " : "") + "duel request", "§a§lAccepter le duel" + (rduel.isCompetitive() ? " compétitif" : "")));
			duelm.setLore(Arrays.asList(rp.translateString("§7Accept " + rpfrom.getName() + "'s duel request.", "§7Accepter l'invitation en duel de " + rpfrom.getName() + "."), rp.translateString("§7Do it only when you are ready to fight!", "§7Faîtes-le seulement lorsque vous êtes prêt !"), "", rp.translateString("§eDuel will automatically end after " + Settings.DUEL_TIMEOUT + " min.", "§eLe duel se terminera automatiquement après " + Settings.DUEL_TIMEOUT + " min.")));
		}else{
			if(rp.getRequestedDuelTo(rpfrom) != null){
				duelm.setDisplayName(rp.translateString("§4§lCancel duel request", "§4§lAnnuler l'invitation"));
				duelm.setLore(Arrays.asList(rp.translateString("§7Cancel duel request sent to " + rpfrom.getName() + ".", "§7Annuler l'invitation en duel envoyée à " + rpfrom.getName() + ".")));
			}else{
				duelm.setDisplayName(rp.translateString("§6§lRequest duel", "§6§lInviter en duel"));
				duelm.setLore(Arrays.asList(rp.translateString("§7Send a duel request to " + from.getName() + ".", "§7Envoyer une demande en duel à " + from.getName() + "."), rp.translateString("§7He has " + Settings.DUEL_REQUEST_TIME + " seconds to accept!", "§7Il a " + Settings.DUEL_REQUEST_TIME + " secondes pour accepter !"), "§7" + rp.translateString("If available, a competitive duel will be proposed to you:", "Si disponible, un duel compétitif vous sera proposé :"), "§7" + rp.translateString("It puts your renown at stake. Be careful!", "Il met en jeu votre renom. Soyez prudent !")));
			}
		}
		duel.setItemMeta(duelm);
		return duel;
	}
	private ItemStack getTrade(){
		ItemStack trade = new ItemStack(Material.ENDER_CHEST, 1);
		ItemMeta tradem = trade.getItemMeta();
		if(this.getHolder().equals(rpfrom.getTradeRequestOpponent())){
			tradem.setDisplayName(rp.translateString("§a§lAccept trade request", "§a§lAccepter l'échange"));
			tradem.setLore(Arrays.asList(rp.translateString("§7Accept " + from.getName() + "'s trade request.", "§7Accepter la requête d'échange de " + from.getName() + "."), rp.translateString("§7This trade system is 100% secure!", "§7Ce système d'échange est 100% sans arnaque !"), "", rp.translateString("§7You can cancel the trade by closing the inventory.", "§7Vous pouvez refuser un échange en fermant l'inventaire.")));
		}else{
			tradem.setDisplayName(rp.translateString("§6§lRequest trade", "§6§lDemander un échange"));
			tradem.setLore(Arrays.asList(rp.translateString("§7Send a trade request to " + from.getName() + ".", "§7Envoyer une demande d'échange à " + from.getName() + "."), rp.translateString("§7He has " + TradingHandler.REQUEST_TIME + " seconds to accept!", "§7Il a " + TradingHandler.REQUEST_TIME + " secondes pour accepter !")));
		}
		trade.setItemMeta(tradem);
		return trade;
	}
	private ItemStack getInspect(){
		ItemStack item = new ItemStack(Material.EYE_OF_ENDER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.translateString("Inspect ", "Inspecter ") + rpfrom.getName());
		meta.setLore(Arrays.asList(rp.translateString("§7Get information about " + rpfrom.getName() + "'s equipped armor", "§7Obtenez des informations sur l'armure"), rp.translateString("§7and items in left and right hands.", "§7et les items portés par " + rpfrom.getName())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getInvite(){
		ItemStack invite = new ItemStack(Material.BEACON, 1);
		ItemMeta invitem = invite.getItemMeta();
		if(mfrom.hasGuild()){
			if(gm.hasGuild()){
				if(gm.getGuild().equals(mfrom.getGuild())){
					invitem.setDisplayName("§2§l" + mfrom.getGuild().getName());
					invitem.setLore(Arrays.asList(rp.translateString("§7You already are in the same guild as " + mfrom.getName() + ".", "§7Vous êtes déjà dans la guilde de " + mfrom.getName() + ".")));
				}else{
					invitem.setDisplayName("§6§l" + rp.translateString("Open Guild Relations Menu", "Menu des relations de guilde"));
					invitem.setLore(Arrays.asList(rp.translateString("§7Open guild relations menu to manage the ones", "§7Ouvrir le menu des relations de guilde pour"), rp.translateString("§7you have with " + mfrom.getGuild().getName() + "'s guild.", "§7gérer celles que vous entretenez avec la guilde " + mfrom.getGuild().getName())));
				}
			}else if(gm.isInvited(mfrom.getGuild())){
				invitem.setDisplayName(rp.translateString("§a§lAccept §2§l" + mfrom.getGuild().getName() + "§a§l's join request", "§a§lAccepter l'invitation de §2§l" + mfrom.getGuild().getName()));
				invitem.setLore(Arrays.asList(rp.translateString("§7Join members of " + mfrom.getGuild().getName() + " to fight with them.", "§7Rejoidre les membres de la guilde " + mfrom.getGuild().getName() + ".")));
			}else if(gm.hasAsked(mfrom.getGuild())){
				invitem.setDisplayName(rp.translateString("§c§lCancel join request sent to §2§l" + mfrom.getGuild().getName(), "§c§lAnnuler la requête envoyée à §4§l" + mfrom.getGuild().getName()));
				invitem.setLore(Arrays.asList(rp.translateString("§7Cancel join request you sent to guild " + mfrom.getGuild().getName() + ".", "§7Annuler la requête d'adhésion envoyée à la guilde " + mfrom.getGuild().getName())));
			}else{
				invitem.setDisplayName(rp.translateString("§a§lAsk §2§l" + mfrom.getGuild().getName() + " §a§lto accept you", "§a§lDemander à §2§l" + mfrom.getGuild().getName() + " §a§lde vous ajouter"));
				invitem.setLore(Arrays.asList(rp.translateString("§7Ask " + mfrom.getGuild().getName() + " to add you among", "§7Demandez à la guilde " + mfrom.getGuild().getName() + " de"), rp.translateString("§7its ranks to help surviving!", "§7de vous ajouter parmis ses rangs.")));
			}
		}else{
			if(mfrom.hasAsked(gm.getGuild())){
				invitem.setDisplayName(rp.translateString("§a§lAdd §2§l" + from.getName() + " §a§lto the guild", "§a§lAjouter §2§l" + from.getName() + " §a§là la guilde"));
				invitem.setLore(Arrays.asList(rp.translateString("§7Accept " + from.getName() + "'s request to join your guild.", "§7Accepter " + from.getName() + " dans la guilde.")));
			}else if(mfrom.isInvited(gm.getGuild())){
				invitem.setDisplayName(rp.translateString("§c§lCancel §4§l" + from.getName() + "§c§l's invitation", "§c§lAnnuler l'invitation de §4§l" + from.getName()));
				invitem.setLore(Arrays.asList(rp.translateString("§7Cancel " + from.getName() + "'s invitation", "§7Annuler l'invitation envoyée à " + from.getName()), rp.translateString("§7to join your guild.", "§7à rejoindre votre guilde.")));
			}else{
				invitem.setDisplayName(rp.translateString("§a§lInvite " + from.getName() + " to join the guild", "§a§lInviter §2§l" + from.getName() + " §a§ldans la guilde"));
				invitem.setLore(Arrays.asList(rp.translateString("§7Invite " + from.getName() + " to join you and", "§7Demandez à " + from.getName() + " de se joindre"), rp.translateString("§7your guild to fight together !", "§7à vous et votre guilde.")));
			}
		}
		invite.setItemMeta(invitem);
		return invite;
	}
	private ItemStack getMarry(){
		ItemStack item = new ItemStack(Material.RED_ROSE, 1);
		ItemMeta meta = item.getItemMeta();
		if(rp.getCouple() != null && this.showFiancer){
			meta.setDisplayName("§4§l" + rp.translateString("Divorce", "Divorcer"));
			meta.setLore(Arrays.asList(rp.translateString("§7Divorce with " + from.getName() + ".", "§7Divorcer avec " + from.getName() + ".")));
		}else{
			if(rpfrom.equals(rp.fiance)){
				meta.setDisplayName("§c§l" + rp.translateString("Cancel wedding proposal", "Annuler la demande en mariage"));
				meta.setLore(Arrays.asList(rp.translateString("§7Cancel the wedding proposal you made to " + from.getName() + ".", "§7Annuler la demande de mariage faîte à " + from.getName() + ".")));
			}else{
				if(rp.equals(rpfrom.fiance)){
					meta.setDisplayName("§a§l" + rp.translateString("Marry ", "Marier ") + rpfrom.getName());
					meta.setLore(Arrays.asList(rp.translateString("§7Accept " + from.getName() + "'s wedding proposal.", "§7Accepter la demande en marriage de " + rpfrom.getName() + ".")));
				}else{
					meta.setDisplayName("§6§l" + rp.translateString("Propose ", "Demander ") + rpfrom.getName() + rp.translateString("", " en mariage"));
					meta.setLore(Arrays.asList(rp.translateString("§7Send a wedding proposal to " + from.getName() + ".", "§7Envoyer une demande de mariage à " + from.getName() + "."), rp.translateString("§7To confirm it, you then need to see a pastor.", "§7Pour confirmer le mariage, vous devez ensuite aller voir un pasteur.")));
				}
			}
		}
		item.setItemMeta(meta);
		return item;
	}

	public void updateDuel() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_DUEL, this.getDuel());
	}

	public void updateTrade() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_TRADE, this.getTrade());
	}

	public void updateInvite() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_INVITE, this.getInvite());
	}

}
