package me.pmilon.RubidiaPaths.commands;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.sqlite.util.StringUtils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaPaths.SecretPath;
import me.pmilon.RubidiaPaths.SecretPathColl;
import me.pmilon.RubidiaPaths.SecretPathsManager;

public class PortalCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 1){
			if(args[0].equalsIgnoreCase("create")){
				if(args.length > 3){
					LocalSession session = SecretPathsManager.we.getSession(player);
					if(session != null) {
						if(session.getSelectionWorld() != null) {
							if(session.isSelectionDefined(session.getSelectionWorld())){
								try {
									Vector b = session.getSelection(session.getSelectionWorld()).getMinimumPoint();
									Vector t = session.getSelection(session.getSelectionWorld()).getMaximumPoint();
									Location bottom = new Location(player.getWorld(), b.getBlockX(), b.getBlockY(), b.getBlockZ());
									Location top = new Location(player.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ());
									String argtitles = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)), " ");
									String[] titles = argtitles.split(" | ");
									if(titles.length > 1) {
										SecretPathColl.paths.add(new SecretPath(args[1], titles[0], titles[1], args[2], player.getLocation(), bottom, top));
										rp.sendMessage("§aLe portail §2" + args[1] + " §aa été créé !");
									} else rp.sendMessage("§cSpécifiez un titre et un sous-titre : [title.../null + | + subtitle.../null]");
								} catch (IncompleteRegionException e) {
									rp.sendMessage("§cSélectionnez une région complète");
								}
							}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
						}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
					}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
				}else{
					rp.sendMessage("§cUtilisez /portal create [Nom] [NomCible/null] [title.../null + | + subtitle.../null]");
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				if(args.length == 2){
					SecretPath path = SecretPath.get(args[1]);
					if(path != null){
						SecretPathColl.paths.remove(path);
						Configs.getPathConfig().set("portals." + args[1], null);
						Configs.savePathConfig();
						rp.sendMessage("§2" + args[1] + " §aa été supprimé !");
					}else{
						rp.sendMessage("§4" + args[1] + "§c n'existe pas !");
					}
				}else{
					rp.sendMessage("§cUtilisez /portal remove [Nom]");
				}
			}else{
				rp.sendMessage("§cUtilisez /portal create [Nom] [NomCible/null] [title.../null + | + subtitle.../null]§l/§cremove [Nom]");
			}
		}else{
			rp.sendMessage("§cUtilisez /portal create [Nom] [NomCible/null] [title.../null + | + subtitle.../null]§l/§cremove [Nom]");
		}
	}

}
