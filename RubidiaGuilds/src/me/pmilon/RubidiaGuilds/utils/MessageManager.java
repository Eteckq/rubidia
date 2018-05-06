package me.pmilon.RubidiaGuilds.utils;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class MessageManager {
	
	public static String dcolorCode(Guild from, Guild to){
		return from.getRelationTo(to).getDColorCode();
	}
	public static String ccolorCode(Guild from, Guild to){
		return from.getRelationTo(to).getCColorCode();
	}

	public static void left(GMember member) {
		RPlayer rp = RPlayer.get(member);
		if(rp != null){
			rp.sendTitle(rp.translateString("§7§lWILDERNESS", "§7§lZONE SAUVAGE"), rp.translateString("§fUnclaimed territory", "§fTerritoire libre"), 5, 45, 10);
		}
	}
	
}
