package me.pmilon.RubidiaCore.commands;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RChat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridCommandExecutor;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoneyCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(args.length == 0){
			int invbalance = 0;
			int invbalancee = 0;
			int bankbalance = 0;
			int bankbalancee = 0;
			for(int slot = 0;slot < 36;slot++){
				if(player.getInventory().getItem(slot) != null){
					if(player.getInventory().getItem(slot).getType().equals(Material.EMERALD)){
						invbalance += player.getInventory().getItem(slot).getAmount();
					}else if(player.getInventory().getItem(slot).getType().equals(Material.EMERALD_BLOCK)){
						invbalancee += player.getInventory().getItem(slot).getAmount();
					}
				}
			}
			for(ItemStack is : rp.getBank().values()){
				if(is != null){
					if(is.getType().equals(Material.EMERALD))bankbalance += is.getAmount();
					else if(is.getType().equals(Material.EMERALD_BLOCK))bankbalancee += is.getAmount();
				}
			}
			rp.getChat().addFixDisplay(new RChatFixDisplay(rp,80,null).addLines(Arrays.asList("   �6Total : �e" + rp.getBalance() + rp.translateString(" �aemeralds", " �a�meraudes"),
					"      �6Inventaire : �e" + invbalance + rp.translateString(" �aemeralds", " �a�meraudes") + (invbalancee != 0 ? " �c+ �e" + invbalancee + rp.translateString(" �aemerald blocks", " �ablocs d'�meraudes") : ""),
					"      �6Banque : �e" + bankbalance + rp.translateString(" �aemeralds", " �a�meraudes") + (bankbalancee != 0 ? " �c+ �e" + bankbalancee + rp.translateString(" �aemerald blocks", " �ablocs d'�meraudes") : ""))));
		}else if(args.length == 1){
			RPlayer rpo = RPlayer.getFromName(args[0]);
			if(rpo != null){
				rp.getChat().addFixDisplay(new RChatFixDisplay(rp, 80, null).addLines(Arrays.asList("     �6" + args[0].toUpperCase() + " : �e" + rpo.getBalance() + rp.translateString(" �aemeralds", " �a�meraudes"))));
			}else rp.sendMessage("�4" + args[0] + " �chas never been on this server! Use: " + (rp.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"), "�4" + args[0] + " �cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("pay")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						int amount = Integer.valueOf(args[2]);
						if(amount > 0){
							RPlayer rpo = RPlayer.get(po);
							if(rp.getBalance() >= amount){
								EconomyHandler.withdrawBalanceITB(player, amount);
								rp.sendMessage("�aYou paid �2" + args[1] + " �aof �e" + amount + " �aemeralds.", "�aVous avez pay� �2" + args[1] + " �ade �e" + amount + " �a�meraudes !");
								rpo.sendMessage("�2" + rp.getName() + " �apaid you �e" + amount + " �aemeralds !", "�2" + rp.getName() + " �avous a pay� de �e" + amount + " �a�meraudes !");
								EconomyHandler.addBalanceBTI(po, amount);
							}
						}
					}
				}else rp.sendMessage("�4" + args[1] + " �chas never been on this server! Use: " + (rp.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"), "�4" + args[1] + " �cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
			}else if(args[0].equalsIgnoreCase("take")){
				if(rp.isOp()){
					if(Bukkit.getPlayer(args[1]) != null){
						Player po = Bukkit.getPlayer(args[1]);
						if(Utils.isInteger(args[2])){
							RPlayer rpo = RPlayer.get(po);
							int amount = Integer.valueOf(args[2]);
							if(rpo.getBalance() < amount)rp.sendMessage("�4" + args[1] + " �cn'avait pas autant d'argent! Toutes ses �meraudes ont �t� pr�lev�es.", "�4" + args[1] + " �cdid not have that much money! All of his emeralds have been taken.");
							if(EconomyHandler.withdrawBalanceITB(po, amount)){
								rp.sendMessage("�aYou withdrawed �e" + amount + " �aemeralds from �2" + args[1] + "�a's account.", "�aVous avez retir� �e" + amount + " �a�meraudes du compte de �2" + args[1] + "�a.");
								rpo.sendMessage("�4" + rp.getName() + " �ctook you �e" + amount + " �cemeralds !", "�4" + rp.getName() + " �cvous a pris �e" + amount + " �c�meraudes !");
							}
						}
					}else rp.sendMessage("�4" + args[1] + " �chas never been on this server! Use: " + (rp.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"), "�4" + args[1] + " �cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
				}else rp.sendMessage("�cYou really thought you could do that without being operator?", "�cVous avez vraiment cru pouvoir faire �a sans �tre op�rateur ?");
			}else rp.sendMessage("�cPlease use: " + (rp.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"), "�cUtilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
		}else rp.sendMessage("�cPlease use: " + (rp.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"), "�cUtilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage("�cOnly players can check their money.");
		}else if(args.length == 1){
			RPlayer rpo = RPlayer.getFromName(args[0]);
			if(rpo != null){
				sender.sendMessage("�e�m---------------------------------------------------");
				sender.sendMessage("�e|     �6" + args[0].toUpperCase() + " : �e" + rpo.getBalance() + " �aemeralds");
				sender.sendMessage("�e�m---------------------------------------------------");
			}else sender.sendMessage("�4" + args[0] + " �chas never been on this server! Use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("pay")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						int amount = Integer.valueOf(args[2]);
						if(amount > 0){
							RPlayer rpo = RPlayer.get(po);
							sender.sendMessage("�aYou paid �2" + args[1] + " �aof �e" + amount + " �aemeralds.");
							rpo.sendMessage("�2�lRubidia�a paid you �e" + amount + " �aemeralds !", "�2�lRubidia�a vous a pay� de �e" + amount + " �a�meraudes !");
							EconomyHandler.addBalanceBTI(po, amount);
						}
					}
				}else sender.sendMessage("�cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
			}else if(args[0].equalsIgnoreCase("take")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						RPlayer rpo = RPlayer.get(po);
						int amount = Integer.valueOf(args[2]);
						if(rpo.getBalance() < amount)sender.sendMessage("�4" + args[1] + " �cdoes not have that much money! All of his emeralds have been taken.");
						if(EconomyHandler.withdrawBalanceITB(po, amount)){
							sender.sendMessage("�aYou withdrawed �e" + amount + " �aemeralds from �2" + args[2] + "�a's account.");
							rpo.sendMessage("�4�lRubidia�c took you �e" + amount + " �cemeralds !", "�4�lRubidia�c vous a pris �e" + amount + " �c�meraudes !");
						}
					}
				}else sender.sendMessage("�cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
			}else sender.sendMessage("�cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
		}else sender.sendMessage("�cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
	}
	
}
