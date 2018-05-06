package me.pmilon.RubidiaCore.tags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;

public class NameTags {

	@SuppressWarnings("deprecation")
	public static void update(){
		for(Player player : Bukkit.getOnlinePlayers()){
			for(Team team : player.getScoreboard().getTeams()){
				team.unregister();
			}
			
			Objective renom = player.getScoreboard().getObjective(player.getName());
			//Objective health = player.getScoreboard().getObjective("showhealth");
			if(renom != null)renom.unregister();
			/*if(health == null){
				health = player.getScoreboard().registerNewObjective("showhealth", "health");
				health.setDisplayName("§7HP");
				health.setDisplaySlot(DisplaySlot.BELOW_NAME);
			}*/
			renom = player.getScoreboard().registerNewObjective(player.getName(), "dummy");
			renom.setDisplayName("");
			renom.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			
			for(RPlayer rp : RPlayer.getOnlines()){
				Team team = player.getScoreboard().registerNewTeam(rp.getName());
				team.addPlayer(rp.getPlayer());
		        
				String colorcode = "§r";
				if(rp.getRClass().equals(RClass.PALADIN))colorcode = "§a";
				else if(rp.getRClass().equals(RClass.RANGER))colorcode = "§b";
				else if(rp.getRClass().equals(RClass.MAGE))colorcode = "§e";
				else if(rp.getRClass().equals(RClass.ASSASSIN))colorcode = "§c";
				else colorcode = "§7";
				if(rp.isOp())colorcode = "§4";
				team.setPrefix("§8[§7" + rp.getRLevel() + "§8] " + colorcode);
				
				renom.getScore(rp.getPlayer()).setScore(rp.getRenom());
			}
		}
	}
	
}
