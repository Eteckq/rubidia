package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Player;

public class ClassCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length >= 1){
			switch (args[0].toLowerCase()) {
			case "v":
				rp.setRClass(RClass.VAGRANT);
				rp.sendMessage("You are now a §7Vagrant§f!", "Vous êtes désormais un §7Vagabond §f!");
				break;
			case "p":
				rp.setRClass(RClass.PALADIN);
				rp.sendMessage("You are now a §aPaladin§f!", "Vous êtes désormais un §aPaladin§f !");
				break;
			case "r":
				rp.setRClass(RClass.RANGER);
				rp.sendMessage("You are now a §bRanger§f!", "Vous êtes désormais un §bRanger§f !");
				break;
			case "m":
				rp.setRClass(RClass.MAGE);
				rp.sendMessage("You are now a §eMage§f!", "Vous êtes désormais un §eMage§f !");
				break;
			case "a":
				rp.setRClass(RClass.ASSASSIN);
				rp.sendMessage("You are now a §cAssassin§f!", "Vous êtes désormais un §cAssassin§f !");
				break;
			}
		}else rp.sendMessage("§cPlease use /class [V/P/R/M/A]", "§cUtilisez /class [V/P/R/M/A]");
	}

}
