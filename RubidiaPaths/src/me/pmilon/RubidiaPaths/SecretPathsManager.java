package me.pmilon.RubidiaPaths;

import java.util.HashSet;
import java.util.Set;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaCore.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SecretPathsManager extends JavaPlugin implements Listener{
	
	static WorldEditPlugin we;
	static SecretPathsManager instance;
	public SecretPathColl coll;
	
	public static final Set<RPlayer> teleported = new HashSet<RPlayer>();
	
	public class ZoneVector {
	}
	
	public void onEnable(){
		instance = this;
		coll = new SecretPathColl(this);
		we = (WorldEditPlugin) getServer().getPluginManager() .getPlugin("WorldEdit");
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
		this.coll.save();
	}
	
	@EventHandler
	public void onMove(RPlayerMoveEvent event){
		RPlayer rp = event.getRPlayer();
		Location location = event.getEvent().getTo();
		
		boolean insidePath = false;
		boolean beenTeleported = teleported.contains(rp);
		for(SecretPath path : SecretPathColl.paths){
			if(path.check(location)){
				if(beenTeleported){
					insidePath = true;
					break;
				} else {
					path.use(rp);
					return;
				}
			}
		}
		if(!insidePath)teleported.remove(rp);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player p = (Player)sender;
			RPlayer rp = RPlayer.get(p);
			if(cmd.getName().equalsIgnoreCase("portal")){
				if(p.isOp()){
					if(args.length > 1){
						if(args[0].equalsIgnoreCase("create")){
							if(args.length > 3){
								LocalSession session = we.getSession(p);
								if(session != null) {
									if(session.getSelectionWorld() != null) {
										if(session.isSelectionDefined(session.getSelectionWorld())){
											try {
												Vector b = session.getSelection(session.getSelectionWorld()).getMinimumPoint();
												Vector t = session.getSelection(session.getSelectionWorld()).getMaximumPoint();
												Location bottom = new Location(p.getWorld(), b.getBlockX(), b.getBlockY(), b.getBlockZ());
												Location top = new Location(p.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ());
												String title = "";
												String subtitle = "";
												boolean passed = false;
												for(int i = 3;i < args.length;i++){
													if(!args[i].equals("|")){
														if(!passed){
															if(i == 3)title += args[i];
															else title += " " + args[i];
														}else{
															if(i == (args.length-1))subtitle += args[i];
															else subtitle += args[i] + " ";
														}
													}else passed = true;
												}
												SecretPathColl.paths.add(new SecretPath(args[1], title, subtitle, args[2], p.getLocation(), bottom, top));
												rp.sendMessage("§aLe portail §2" + args[1] + " §aa été créé !");
											} catch (IncompleteRegionException e) {
												rp.sendMessage("§cSélectionnez une région complète");
											}
										}else rp.sendMessage("§cVous n'avez pas sélectionné de région !");
									}else rp.sendMessage("§cVous n'avez pas sélectionné de région !");
								}else rp.sendMessage("§cVous n'avez pas sélectionné de région !");
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
				}else{
					rp.sendMessage("§cVous avez vraiment cru pouvoir faire ça sans être Opérateur ?");
				}
			}
		}
		return false;
	}
	
}