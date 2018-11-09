package me.pmilon.RubidiaManager.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.RChunk;

public class WorldsRegenTask extends AbstractTask {

	public WorldsRegenTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		List<String> names = this.getPlugin().getConfig().getStringList("regenWorlds");
		if(names != null) {
			int delay = 0;
			for(int i = 0;i < names.size();i++) {
				final World world = Bukkit.getWorld(names.get(i));
				if(world != null) {
					final List<RChunk> toRegen = RubidiaManagerPlugin.getToRegen(world);
					new BukkitTask(this.getPlugin()) {
						
						@Override
						public void run(){
							RubidiaManagerPlugin.console.sendMessage("§eRegenerating §6" + world.getName() + "§e...");
							new BukkitTask(this.getPlugin()) {
								public void run(){
									RubidiaManagerPlugin.console.sendMessage("§6" + world.getName() + " §eregenerated!");
								}

								@Override
								public void onCancel() {
								}
							}.runTaskLater(toRegen.size());
							RubidiaManagerPlugin.regen(toRegen);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(delay + 20);
					delay += toRegen.size();
				} else RubidiaManagerPlugin.console.sendMessage("§cCouldn't regen world §4" + names.get(i) + "§c: unable to find it.");
			}
		} else RubidiaManagerPlugin.console.sendMessage("§cNow is the time to regen the worlds, but no worlds have been referenced in config.");
	}

}
