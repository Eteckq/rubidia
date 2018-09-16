package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class HomeRemovalConfirmationUI extends ConfirmationUI {

	private final Guild guild;
	private final int home;
	public HomeRemovalConfirmationUI(RPlayer rp, Guild guild, int home) {
		super(rp.getPlayer(), rp.translateString("Home removal", "Suppression de PR"),
				new String[]{rp.translateString("�aRemove home �2" + guild.getHomes()[home].getName(), "�aSupprimer le PR �2" + guild.getHomes()[home].getName()),
						rp.translateString("�cKeep home " + guild.getHomes()[home].getName(), "�cConserver le PR " + guild.getHomes()[home].getName())},
				"�7�l" + guild.getHomes()[home].getName(), Arrays.asList(rp.translateString("�8Are you sure you want to remove this home?","�8�tes-vous s�r de vouloir supprimer ce PR ?")));
		this.guild = guild;
		this.home = home;
	}

	@Override
	protected void yes() {
		this.getGuild().getHomes()[this.getHome()] = null;
		rp.sendMessage("�cHome successfully deleted.", "�cPoint de rassemblement supprim�.");
		Core.uiManager.requestUI(new GHomeListUI(this.getHolder(), this.getGuild()));
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GHomeListUI(this.getHolder(), this.getGuild()));
	}

	public Guild getGuild() {
		return guild;
	}

	public int getHome() {
		return home;
	}

}