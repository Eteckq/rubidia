package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;

import org.bukkit.command.CommandSender;

public class RebootCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		Core.restart();
	}

}
