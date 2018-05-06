package me.pmilon.RubidiaManager.tasks;

import com.onarandombox.MultiverseCore.utils.WorldManager;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class NetherRegenTask extends AbstractTask {

	public NetherRegenTask(RubidiaManagerPlugin plugin){
		super(plugin);
	}
	
	@Override
	public void runTaskSynchronously() {
		RubidiaManagerPlugin.console.sendMessage("§eREGENERATING §6NETHER...");
		WorldManager worldManager = new WorldManager(Core.multiverseCore);
		if(worldManager.regenWorld("Rubidia_nether", true, true, null))RubidiaManagerPlugin.console.sendMessage("§6NETHER §eREGENERATED");
		else RubidiaManagerPlugin.console.sendMessage("§cCOULDNT REGENERATE §4NETHER");
	}

}
