package me.pmilon.RubidiaQuests.pnjs;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.ui.smith.SmithMenu;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class SmithPNJ extends ActivePNJ{

	public SmithPNJ(String uuid, String name, Location loc, int age, boolean fix, String dialog) {
		super(uuid, "FORGERON", "§8§l", name, "§7", PNJType.SMITH, loc, age, fix, dialog);
	}

	@Override
	protected void onDelete() {
	}

	@Override
	protected void onTalk(Player player) {
		Core.uiManager.requestUI(new SmithMenu(player));
	}

	@Override
	protected void onSubSave() {
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

}
