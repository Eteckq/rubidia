package me.pmilon.RubidiaManager.tasks;

import com.onarandombox.MultiverseCore.utils.WorldManager;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class NetherRegenTask extends AbstractTask {

	public NetherRegenTask(RubidiaManagerPlugin plugin){
		super(plugin);
	}
	
	@Override
	public void runTaskSynchronously() {
		RubidiaManagerPlugin.console.sendMessage("§eRegenerating nether...");
		WorldManager worldManager = new WorldManager(RubidiaManagerPlugin.multiverseCore);
		if(worldManager.regenWorld("Rubidia_nether", true, true, null)) {
			RubidiaManagerPlugin.console.sendMessage("§eNether regenerated!");
		} else RubidiaManagerPlugin.console.sendMessage("§cUnable to regen nether.");
	}

}
