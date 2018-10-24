package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaCore.utils.Configs;
import net.md_5.bungee.api.ChatColor;

public class SPlayerDeletionConfirmationUI extends ConfirmationUI {

	private int id;
	public SPlayerDeletionConfirmationUI(RPlayer rp, SPlayer sp, int id) {
		super(rp.getPlayer(), rp.translateString("Character deletion", "Suppression de personnage"), new String[]{rp.translateString("§aDelete character and lose all progression", "§aSupprimer le personnage et perdre toute progression"),
				rp.translateString("§cKeep character and all progression", "§cGarder le personnage et conserver la progression")},
				"§7§l" + rp.translateString(sp.getRClass().getDisplayEn(), sp.getRClass().getDisplayFr()),
				Arrays.asList("§8" + rp.translateString("Level: ", "Niveau : ") + "§7§o" + sp.getRLevel(), "§8" + rp.translateString("XP: ", "XP : ") + "§7§o" + sp.getRExp(), "§8" + rp.translateString("Mastery: ", "Maîtrise : ") + "§7§o" + rp.translateString(sp.getMastery().getNameEN(), sp.getMastery().getNameFR()), "§8" + rp.translateString("Job: ", "Métier : ") + "§7§o" + ChatColor.stripColor(rp.translateString(sp.getRJob().getNameEN(), sp.getRJob().getNameFR())), "§8" + rp.translateString("Skillpoints: ", "Points de compétence : ") + "§7§o" + sp.getSkp(), "§8" + rp.translateString("Distinction points: ", "Points de distinction : ") + "§7§o" + sp.getSkd(), "§8" + rp.translateString("Strength: ", "Force : ") + "§7§o" + sp.getStrength(), "§8" + rp.translateString("Endurance: ", "Endurance : ") + "§7§o" + sp.getEndurance(), "§8" + rp.translateString("Agility: ", "Agilité : ") + "§7§o" + sp.getAgility(), "§8" + rp.translateString("Intelligence: ", "Intelligence : ") + "§7§o" + sp.getIntelligence(), "§8" + rp.translateString("Perception: ", "Perception : ") + "§7§o" + sp.getPerception(), "§8" + rp.translateString("Kills: ", "Meurtres : ") + "§7§o" + sp.getKills(), "§8" + rp.translateString("Renom: ", "Renom : ") + "§7§o" + sp.getRenom(), "", rp.translateString("§e§oDo you really want to delete this character?", "§e§oSouhaitez-vous vraiment supprimer ce personnage ?"), "", "§f§m----------------------------------------"));
		this.id = id;
	}
	
	@Override
	protected void yes() {
		rp.getSaves()[id] = null;
		Configs.getPlayerConfig().set("players." + rp.getUniqueId() + ".saves." + id, null);
		rp.sendMessage("§eYour character has been deleted.", "§eVotre personnage a bien été supprimé.");
		this.close(false);
	}
	
	@Override
	protected void no() {
		Core.uiManager.requestUI(new SPlayerSelectionMenu(this.getHolder()));
	}

}
