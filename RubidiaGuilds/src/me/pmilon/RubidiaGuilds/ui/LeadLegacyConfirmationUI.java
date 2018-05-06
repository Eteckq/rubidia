package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.ConfirmationUI;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Rank;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class LeadLegacyConfirmationUI extends ConfirmationUI {

	public final Guild guild;
	public final GMember member;
	public LeadLegacyConfirmationUI(RPlayer rp, Guild guild, GMember leader) {
		super(rp.getPlayer(), rp.translateString("Leadership management", "Gestion de la direction"),
				new String[]{rp.translateString("�aGive leadership of your guild to " + leader.getName(), "�aL�guer la direction de votre guilde � " + leader.getName()),rp.translateString("�cKeep leadership of your guild", "�cConserver la direction de votre guilde")},
				leader.getName(), Arrays.asList("", rp.translateString("�7Are you sure you want to give your leadership?", "�7�tes-vous certain de vouloir l�guer la direction ?")));
		this.guild = guild;
		this.member = leader;
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}

	@Override
	protected void yes() {
		Rank rank = guild.getRanks()[6];
		for(int i = 1;i < 7;i++){
			Rank rk = guild.getRanks()[i];
			if(rk != null){
				rank = rk;
			}
		}
		guild.getLeader().setRank(rank);
		member.setRank(guild.getRanks()[0]);
		guild.broadcastAllMessage("�&d" + member.getName() + " �&cgave leadership to �&d" + member.getName() + "�&c!", "�&d" + member.getName() + " �&ca l�guer la direction � �&d" + member.getName() + " �&c!");
		guild.broadcastMessage(Relation.MEMBER, "�2" + member.getName() + " �agave leadership to �2" + member.getName() + "�c!", "�2" + member.getName() + " �aa l�guer la direction � �2" + member.getName() + " �a!");
		guild.broadcastAllyMessage("�5" + member.getName() + " �dgave leadership to �5" + member.getName() + "�d!", "�5" + member.getName() + " �da l�guer la direction � �5" + member.getName() + " �d!");
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}
}
