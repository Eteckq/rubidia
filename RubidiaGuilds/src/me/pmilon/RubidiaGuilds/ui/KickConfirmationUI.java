package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.ConfirmationUI;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class KickConfirmationUI extends ConfirmationUI {

	public final Guild guild;
	public final GMember member;
	public KickConfirmationUI(RPlayer rp, Guild guild, GMember kicked) {
		super(rp.getPlayer(), rp.translateString("Kick ", "Ejection de ") + kicked.getName(),
				new String[]{rp.translateString("�aKick " + kicked.getName() + " from your guild", "�aEjecter " + kicked.getName() + " de votre guilde"),rp.translateString("�cKeep " + kicked.getName() + " as a member of your guild", "�cGarder " + kicked.getName() + " parmis les rangs de votre guilde")},
				"�7�l" + kicked.getName(), Arrays.asList("", rp.translateString("�8Are you sure you want to kick this player?", "�8�tes-vous certain de vouloir �jecter ce joueur ?")));
		this.guild = guild;
		this.member = kicked;
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}

	@Override
	protected void yes() {
		if(member.isOnline()){
			RPlayer.get(member).sendMessage("�cYou've been kicked out of the guild!", "�cVous avez �t� �ject� de la guilde !");
		}
		guild.removeMember(member);
		guild.broadcastMessage(Relation.MEMBER, "�4" + member.getName() + " �chas been kicked out of the guild!", "�4" + member.getName() + " �ca �t� �ject� de la guilde !");
		this.getUIManager().requestUI(new GMembersUI(this.getHolder(), guild));
	}

}
