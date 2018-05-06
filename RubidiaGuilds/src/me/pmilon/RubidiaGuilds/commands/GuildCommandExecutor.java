package me.pmilon.RubidiaGuilds.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RChat.ChatType;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.PlayerCommandExecutor;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.ui.GCreateMenuUI;
import me.pmilon.RubidiaGuilds.ui.GMenuUI;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class GuildCommandExecutor extends PlayerCommandExecutor {

	public static int HELP_LENGTH = 0;

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		GMember member = GMember.get(player);
		if(args.length == 0){
			if(member.hasGuild()){
				Core.uiManager.requestUI(new GMenuUI(player, member.getGuild()));
			}else Core.uiManager.requestUI(new GCreateMenuUI(player));
		}else if(member.hasGuild()){
			rp.prechat(StringUtils.join(args, " "), ChatType.GUILD);
		}else Core.uiManager.requestUI(new GCreateMenuUI(player));
	}

}
