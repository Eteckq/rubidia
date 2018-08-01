package me.pmilon.RubidiaCore.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Gender;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RPlayersUI extends ListMenuUIHandler<RPlayer> {

	private int LIST_ID_SEARCH = 1;
	public RPlayersUI(Player p) {
		super(p, "RPlayers management menu", "Menu de gestion des RPlayers", 5);
		for(RPlayer rp : Core.rcoll.data()){
			if(rp.getLastConnectionDate() > 0){
				this.list.add(rp);
			}
		}
		Collections.sort(this.list, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	            return (int) (rp1.getLastConnectionDate()-rp2.getLastConnectionDate());
	        }
	    });
		for(RPlayer rp : Core.rcoll.data()){
			if(rp.getLastConnectionDate() == 0){
				this.list.add(rp);
			}
		}
	}

	@Override
	protected void onOpen() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_SEARCH){
					this.list.clear();
					for(RPlayer rp : Core.rcoll.data()){
						if(rp.getName().toLowerCase().contains(this.getMessage().toLowerCase())){
							this.list.add(rp);
						}
					}
				}
			}
		}
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				if(rp.isOp()){
					int slot = e.getRawSlot();
					Core.uiManager.requestUI(new RPlayerManagerMenu(this.getHolder(), this.get(slot)));
				}
			}
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList("§7" + rp.translateString("Click here to search for a profile", "Cliquez ici pour chercher un profil")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		this.close(true, this.LIST_ID_SEARCH);
		rp.sendMessage("§aEnter the name of the profile you are looking for", "§aEntrez le nom du profil que vous recherchez");
	}

	@Override
	protected void onPageTurn() {
	}

	@Override
	public String getType() {
		return "RPLAYERS_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected ItemStack getItem(RPlayer e) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (byte)3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName((e.isOnline() ? "§2[✔] §a" : "§4[✘] §c") + e.getName() + (e.isVip() ? " §8[§6§lVIP§8]" : ""));
		List<String> lore = new ArrayList<String>();
		lore.add("§f§m-------------------");
		if(e.isPublicData() && e.isProfileUpdated()){
			if(!e.getSex().equals(Gender.UNKNOWN)){
				lore.add("§8" + rp.translateString("Gender ", "Sexe ") + "§7" + rp.translateString(e.getSex().getEn().toLowerCase(), e.getSex().getFr().toLowerCase()));
			}
			if(e.getBirthDate() > 10*Utils.MILLIS_IN_YEAR && e.getBirthDate() < System.currentTimeMillis()-6*Utils.MILLIS_IN_YEAR){
				double age = (double) ((long)(System.currentTimeMillis()-e.getBirthDate()))/Utils.MILLIS_IN_YEAR;
				lore.add("§8" + rp.translateString("Age ", "Âge ") + "§7" + String.valueOf(Utils.round(age, 2)) +  rp.translateString("", " ans"));
			}
			lore.add("");
		}
		double ratio = e.getRExp()/LevelUtils.getRLevelTotalExp(e);
		long time = System.currentTimeMillis()-e.getLastDivorce();
		lore.addAll(Arrays.asList("§8" + rp.translateString("Level ", "Niveau ") + "§7" + e.getRLevel(), "§8" + rp.translateString("Experience ", "Expérience ") + "§7" + String.valueOf(Utils.round(ratio,2)) + "%", "§8" + rp.translateString("Class ", "Classe ") + "§7" + rp.translateString(e.getRClass().getDisplayEn(), e.getRClass().getDisplayFr()), "§8" + rp.translateString("Kills ", "Meurtres ") + "§7" + e.getKills(), "§8" + rp.translateString("Gaming time ", "Temps de jeu ") + "§7" + TimeUnit.MILLISECONDS.toHours(e.getGamingTime()) + "h", "§8" + (e.getCouple() == null ? (time >= Settings.TIME_BEFORE_WEDDING_PROPOSAL ? rp.translateString("Single", "Célibataire") : rp.translateString("Divorced for ", "Divorcé depuis ") + "§7" + TimeUnit.MILLISECONDS.toHours(time) + "h") : rp.translateString("Married to ", "Marié à ") + "§7" + e.getCouple().getCompanion(e).getName())));
		GMember member = GMember.get(e);
		if(member.hasGuild()){
			Guild guild = member.getGuild();
			lore.addAll(Arrays.asList("", "§8" + rp.translateString("Guild ", "Guilde ") + "§7" + guild.getName(), "§8" + rp.translateString("Rank ", "Rang ") + "§7" + member.getRank().getName(), ""));
		}
		Date date = new Date(e.getLastConnectionDate());
		int n = 0;
		for(int i = 0;i < e.getSaves().length;i++){
			if(e.getSaves()[i] != null){
				n++;
			}
		}
		lore.addAll(Arrays.asList("§8" + rp.translateString("Characters ", "Personnages ") + "§7" + n, "§8" + rp.translateString("Last connected on ", "Dernière connexion le "), "§7" + new SimpleDateFormat("dd/MM/yyyy").format(date) + rp.translateString("§8,§7 ", " §8à§7 ") + new SimpleDateFormat("HH:mm").format(date)));
		meta.setLore(lore);
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(e.getUniqueId())));
		skull.setItemMeta(meta);
		return skull;
	}

}
