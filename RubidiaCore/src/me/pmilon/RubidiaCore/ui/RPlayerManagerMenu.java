package me.pmilon.RubidiaCore.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RPlayerManagerMenu extends UIHandler {

	private RPlayer rpp;
	private int SLOT_PROFILE = 0,SLOT_CRCS = 2,SLOT_MARRIAGE = 3,SLOT_OPTIONS = 6,SLOT_VARS = 8;
	public RPlayerManagerMenu(Player p, RPlayer rpp) {
		super(p);
		this.rpp = rpp;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rpp.getName() + "'s profile");
	}

	@Override
	public UIType getType() {
		return UIType.RPLAYER_MANAGER;
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_PROFILE, this.getHead());
		this.getMenu().setItem(this.SLOT_CRCS, this.getCrcs());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	private ItemStack getHead(){
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName((rpp.isOnline() ? "§2[✔] §a" : "§4[✘] §c") + rpp.getName() + (rpp.isVip() ? " §8[§6§lVIP§8]" : ""));
		List<String> lore = new ArrayList<String>();
		lore.add("§f§m-------------------");
		if(!rpp.isPublicData()){
			lore.add("§4§l/!\\ §c" + rp.translateString("Confidential data", "Données confidentielles"));
		}
		if(!rpp.isProfileUpdated()){
			lore.add("§8" + rp.translateString("Outdated profile", "Profil non à jour"));
		}
		double age = (double) ((long)(System.currentTimeMillis()-rpp.getBirthDate()))/Utils.MILLIS_IN_YEAR;
		lore.add("§8" + rp.translateString("Gender ", "Sexe ") + "§7" + rp.translateString(rpp.getSex().getEn().toLowerCase(), rpp.getSex().getFr().toLowerCase()));
		lore.add("§8" + rp.translateString("Age ", "Âge ") + "§7" + String.valueOf(Utils.round(age, 2)) +  rp.translateString("", " ans"));
		lore.add("§8" + rp.translateString("Birth date ", "Date de naissance ") + "§7" + new SimpleDateFormat("dd/MM/yyyy").format(rpp.getBirthDate()));
		lore.add("");
		long time = System.currentTimeMillis()-rpp.getLastDivorce();
		lore.addAll(Arrays.asList("§8" + rp.translateString("Kills ", "Meurtres ") + "§7" + rpp.getKills(), "§8" + rp.translateString("Gaming time ", "Temps de jeu ") + "§7" + TimeUnit.MILLISECONDS.toHours(rpp.getGamingTime()) + "h", "§8" + (rpp.getCouple() == null ? (time >= Settings.TIME_BEFORE_WEDDING_PROPOSAL ? rp.translateString("Single", "Célibataire") : rp.translateString("Divorced for ", "Divorcé depuis ") + "§7" + TimeUnit.MILLISECONDS.toHours(time) + "h") : rp.translateString("Married to ", "Marié à ") + "§7" + rpp.getCouple().getCompanion(rpp).getName())));
		GMember member = GMember.get(rpp);
		if(member.hasGuild()){
			Guild guild = member.getGuild();
			lore.addAll(Arrays.asList("", "§8" + rp.translateString("Guild ", "Guilde ") + "§7" + guild.getName(), "§8" + rp.translateString("Rank ", "Rang ") + "§7" + member.getRank().getName(), ""));
		}
		Date date = new Date(rpp.getLastConnectionDate());
		lore.addAll(Arrays.asList("§8" + rp.translateString("Last connected on ", "Dernière connexion le "), "§7" + new SimpleDateFormat("dd/MM/yyyy").format(date) + rp.translateString("§8,§7 ", " §8à§7 ") + new SimpleDateFormat("HH:mm").format(date)));
		meta.setLore(lore);
		meta.setOwner(rpp.getName());
		skull.setItemMeta(meta);
		return skull;
	}
	
	private ItemStack getCrcs(){
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		int n = 0;
		for(int i = 0;i < rpp.getSaves().length;i++){
			if(rpp.getSaves()[i] != null){
				n++;
			}
		}
		SPlayer sp = rpp.getSaves()[rpp.getLastLoadedSPlayerId()];
		double ratio = sp.getRExp()/LevelUtils.getRLevelTotalExp(sp.getRLevel());
		meta.setDisplayName("§6" + rp.translateString("Characters", "Personnages") + "§8(" + n + ")");
		meta.setLore(Arrays.asList("§8" + rp.translateString("Level ", "Niveau ") + "§7" + sp.getRLevel(), "§8" + rp.translateString("XP ", "XP ") + "§7" + sp.getRExp() + " (" + Utils.round(ratio, 2) + "%)", "§8" + rp.translateString("Mastery ", "Maîtrise ") + "§7" + rp.translateString(sp.getMastery().getNameEN(), sp.getMastery().getNameFR()), "§8" + rp.translateString("Job ", "Métier ") + "§7" + ChatColor.stripColor(rp.translateString(sp.getRJob().getNameEN(), sp.getRJob().getNameFR())), "§8" + rp.translateString("Skillpoints ", "Points de compétence ") + "§7" + sp.getSkp(), "§8" + rp.translateString("Distinction points ", "Points de distinction ") + "§7" + sp.getSkd(), "§8" + rp.translateString("Strength ", "Force ") + "§7" + sp.getStrength(), "§8" + rp.translateString("Endurance ", "Endurance ") + "§7" + sp.getEndurance(), "§8" + rp.translateString("Agility ", "Agilité ") + "§7" + sp.getAgility(), "§8" + rp.translateString("Intelligence ", "Intelligence ") + "§7" + sp.getIntelligence(), "§8" + rp.translateString("Perception ", "Perception ") + "§7" + sp.getPerception(), "§8" + rp.translateString("Kills ", "Meurtres ") + "§7" + sp.getKills(), "§8" + rp.translateString("Renom ", "Renom ") + "§7" + sp.getRenom()));
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

}
