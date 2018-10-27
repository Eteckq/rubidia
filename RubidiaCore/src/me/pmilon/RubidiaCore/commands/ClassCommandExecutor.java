package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;

import org.bukkit.entity.Player;

public class ClassCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length >= 1){
			switch (args[0].toLowerCase()) {
			case "v":
				rp.setRClass(RClass.VAGRANT);
				rp.sendMessage("Vous �tes d�sormais un �7Vagabond �f!");
				break;
			case "p":
				rp.setRClass(RClass.PALADIN);
				rp.sendMessage("Vous �tes d�sormais un �aPaladin�f !");
				break;
			case "r":
				rp.setRClass(RClass.RANGER);
				rp.sendMessage("Vous �tes d�sormais un �bRanger�f !");
				break;
			case "m":
				rp.setRClass(RClass.MAGE);
				rp.sendMessage("Vous �tes d�sormais un �eMage�f !");
				break;
			case "a":
				rp.setRClass(RClass.ASSASSIN);
				rp.sendMessage("Vous �tes d�sormais un �cAssassin�f !");
				break;
			}
		}else rp.sendMessage("�cUtilisez /class [V/P/R/M/A]");
	}

}
