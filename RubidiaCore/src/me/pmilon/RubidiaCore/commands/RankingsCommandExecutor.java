package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.ranks.Ranks;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class RankingsCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("update")){
				Ranks.update();
				rp.sendMessage("§aRankings have been updated!", "§aLes classements ont été mis à jour !");
			}else if(args.length > 2){
				if(args[0].equalsIgnoreCase("set")){
					if(Utils.isInteger(args[2])){
						int i = Integer.valueOf(args[2]);
						if(i > 0 && i < 4){
							if(args[1].equalsIgnoreCase("level") || args[1].equalsIgnoreCase("renom") || args[1].equalsIgnoreCase("kills") || args[1].equalsIgnoreCase("money") || args[1].equalsIgnoreCase("claims") || args[1].equalsIgnoreCase("gamingtime")){
								Selection sel = Core.we.getSelection(player);
								if(sel != null){
									Location bottom = sel.getMinimumPoint();
									Location top = sel.getMaximumPoint();
									if(bottom.distanceSquared(top) == 0){
										Configs.getDatabase().set("rankings." + args[1] + "." + i + ".location", top);
										Configs.saveDatabase();
										rp.sendMessage("§aLocation for rank §2" + args[1] + " §a#§2" + i + " §ahas been set!", "§aLocation for rank §2" + args[1] + " §a#§2" + i + " §ahas been set!");
									}else rp.sendMessage("§cYour selection is invalid", "§cVotre sélection est invalide");
								}else rp.sendMessage("§cYour selection is invalid", "§cVotre sélection est invalide");
							}else rp.sendMessage("§cPlease use /rankings set [level|money|renom|kills|claims|gamingtime] " + args[2], "§cUtilisez /rankings set [level|money|renom|kills|claims|gamingtime] " + args[2]);
						}else rp.sendMessage("§cid must be between 1 & 3!", "§cLa place doit être entre 1 & 3 !");
					}else rp.sendMessage("§cPlease use /rankings set [level|money|renom|kills|claims|gamingtime] [#]", "§cUtilisez /rankings set [level|money|renom|kills|claims|gamingtime] [#]");
				}else rp.sendMessage("§cPlease use /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update", "§cUtilisez /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update");
			}else rp.sendMessage("§cPlease use /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update", "§cUtilisez /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update");
		}else rp.sendMessage("§cPlease use /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update", "§cUtilisez /rankings set [level|money|renom|kills|claims|gamingtime] [#]/update");
	}

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("update")){
				Ranks.update();
				sender.sendMessage("§aRankings have been updated!");
			}
		}else sender.sendMessage("§cPlease use /rankings update");
	}

}
