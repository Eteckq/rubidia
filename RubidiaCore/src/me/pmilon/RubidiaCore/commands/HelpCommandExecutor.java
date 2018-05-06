package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RChat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.executors.HybridCommandExecutor;
import me.pmilon.RubidiaCore.utils.Settings;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		rp.getChat().addFixDisplay(new RChatFixDisplay(rp, -1, null).addLines(rp.getLanguage().contains("fr") ? Settings.HELP_FR : Settings.HELP_EN));
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		for(String help : Settings.HELP_FR){
			sender.sendMessage(help);
		}
	}

}
