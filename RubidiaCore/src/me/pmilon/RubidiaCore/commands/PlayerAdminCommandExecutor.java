package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Player;

public abstract class PlayerAdminCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(rp.isOp()){
			this.onAdminCommand(player, rp, args);
		}else rp.sendMessage("§cYou really thought you could do this without being operator?", "§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
	}

	public abstract void onAdminCommand(Player player, RPlayer rp, String[] args);
	
}
