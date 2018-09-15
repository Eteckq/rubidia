package me.pmilon.RubidiaQuests.pnjs;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.ui.bank.BankPNJUI;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class BankPNJ extends ActivePNJ {

	public BankPNJ(String uuid, String name, Location loc, int age,
			boolean fix, String dialog) {
		super(uuid, "BANQUIER", "§9§l", name, "§b", PNJType.BANK, loc, age, fix, dialog);
	}

	@Override
	protected void onDelete() {
	}

	@Override
	protected void onTalk(Player player) {
		Core.uiManager.requestUI(new BankPNJUI(player, this));
	}

	@Override
	protected void onSubSave() {
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

}
