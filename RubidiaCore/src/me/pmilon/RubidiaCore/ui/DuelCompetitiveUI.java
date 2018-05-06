package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;

public class DuelCompetitiveUI extends ConfirmationUI {

	private RPlayer rpfrom;
	public DuelCompetitiveUI(RPlayer rp, RPlayer rpfrom) {
		super(rp.getPlayer(), rp.translateString("Competitive duel available", "Duel compétitif disponible"), new String[]{}, "§7§l" + rp.translateString("Competitive duel", "Duel compétitif"), Arrays.asList("§8" + rp.translateString("You have " + (Math.abs(rp.getRLevel()-rpfrom.getRLevel())) + " levels of deviation with", "Vous avez " + (Math.abs(rp.getRLevel()-rpfrom.getRLevel())) + " niveaux d'écart avec"), "§8" + rpfrom.getName() + ". " + rp.translateString("It is loyal to put your renown at stake.", "Il est loyal de mettre votre renom en jeu."), "§8" + rp.translateString("Would you like to request a competitive duel?", "Souhaitez-vous proposer un duel compétitif ?")));
		this.rpfrom = rpfrom;
		this.setDefaultNo(false);
	}

	@Override
	protected void yes() {
		rp.requestDuel(rpfrom,true);
		this.close(false);
	}

	@Override
	protected void no() {
		rp.requestDuel(rpfrom,false);
		this.close(false);
	}

}
