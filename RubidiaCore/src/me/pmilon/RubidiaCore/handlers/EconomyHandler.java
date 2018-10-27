package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyHandler {
	
	public static void withdraw(Player player, int cost){
		RPlayer rp = RPlayer.get(player);
		rp.setBank(rp.getBank()-cost);
		rp.sendMessage("§4" + cost + "⟡  §cont été retirées de votre banque.");
		rp.sendMessage("§7Votre nouveau solde est de §f" + rp.getBank() + "⟡");
	}
	
	public static void deposit(Player player, int amount) {
		RPlayer rp = RPlayer.get(player);
		rp.setBank(rp.getBank()+amount);
		rp.sendMessage("§2" + amount + "⟡  §aont été ajoutées à votre banque.");
		rp.sendMessage("§7Votre nouveau solde est de §f" + rp.getBank() + "⟡");
	}

	public static boolean isQuestItem(ItemStack is){
		return me.pmilon.RubidiaQuests.utils.Utils.isQuestItem(is);
	}
}
