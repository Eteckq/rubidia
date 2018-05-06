package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;

public class DivorceConfirmationUI extends ConfirmationUI {

	public DivorceConfirmationUI(RPlayer rp) {
		super(rp.getPlayer(), "Divorce", new String[]{rp.translateString("§aDivorce with " + rp.getCouple().getCompanion(rp).getName() + " and lose all buffs", "§aDivorcer avec " + rp.getCouple().getCompanion(rp).getName() + " et perdre tous les buffs"),rp.translateString("§cKeep married to " + rp.getCouple().getCompanion(rp).getName(), "§cRester marié à " + rp.getCouple().getCompanion(rp).getName())},
				"§7§l" + rp.getCouple().getCompanion(rp).getName(), Arrays.asList(rp.translateString("§8Are you sure you want to divorce?","§8Êtes-vous sûr de vouloir divorcer ?")));
	}

	@Override
	protected void yes() {
		if(rp.getCouple() != null){
			rp.getCouple().divorce(rp);
		}
		this.close(false);
	}

	@Override
	protected void no() {
		this.close(false);
	}

}
