package me.pmilon.RubidiaQuests.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.BankPNJ;
import me.pmilon.RubidiaQuests.pnjs.InhabitantPNJ;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.pnjs.PasserPNJ;
import me.pmilon.RubidiaQuests.pnjs.PastorPNJ;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.pnjs.ShopPNJ;
import me.pmilon.RubidiaQuests.pnjs.SmithPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.entity.Player;

public class PNJCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			PNJType type = null;
			try{
				type = PNJType.valueOf(args[0].toUpperCase());
			}catch(Exception ex){
			}
			
			if(type != null){
				switch (type){
				case QUEST:
					List<Quest> quests = new ArrayList<Quest>();
					QuestsPlugin.pnjManager.spawn(new QuestPNJ(UUID.randomUUID().toString(), type.toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, quests, "No quest available", false));
					break;
				case INHABITANT:
					QuestsPlugin.pnjManager.spawn(new InhabitantPNJ(UUID.randomUUID().toString(), type.toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, new ArrayList<String>(), false));
					break;
				case SHOP:
					QuestsPlugin.pnjManager.spawn(new ShopPNJ(UUID.randomUUID().toString(), type.toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, false, QuestsPlugin.shopColl.addDefault(UUID.randomUUID().toString()), ""));
					break;
				case BANK:
					QuestsPlugin.pnjManager.spawn(new BankPNJ(UUID.randomUUID().toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, false, ""));
					break;
				case SMITH:
					QuestsPlugin.pnjManager.spawn(new SmithPNJ(UUID.randomUUID().toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, false, ""));
					break;
				case PASTOR:
					QuestsPlugin.pnjManager.spawn(new PastorPNJ(UUID.randomUUID().toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, false, player.getLocation(), player.getLocation()));
					break;
				case PASSER:
					QuestsPlugin.pnjManager.spawn(new PasserPNJ(UUID.randomUUID().toString(), "PNJ" + new Random().nextInt(), player.getLocation(), 0, new ArrayList<String>(), false, "ici", player.getLocation()));
					break;
				}
			}else rp.sendMessage("§cInvalid PNJ type.", "§cType de PNJ invalide.");
		}else rp.sendMessage("§cUse /pnj [TYPE]", "§cUtilisez /pnj [TYPE]");
	}

}
