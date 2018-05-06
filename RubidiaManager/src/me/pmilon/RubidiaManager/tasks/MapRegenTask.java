package me.pmilon.RubidiaManager.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.RChunk;

public class MapRegenTask extends AbstractTask{

	public MapRegenTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		String worldName = this.getPlugin().getConfig().getString("regenWorld");
		final World world = Bukkit.getWorld(worldName);
		if(world != null){
			RubidiaManagerPlugin.console.sendMessage("§eREGENERATING §6" + world.getName() + "§e...");
			List<RChunk> toRegen = RubidiaManagerPlugin.getToRegen(world);
			Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable(){
				public void run(){
					RubidiaManagerPlugin.console.sendMessage("§e" + world.getName() + " §6REGENERATED!");
				}
			}, toRegen.size());
			RubidiaManagerPlugin.regen(toRegen);
		}else RubidiaManagerPlugin.console.sendMessage("§cCOULDNT REGEN WORLD §4" + worldName + "§c: CANNOT FIND IT.");
	}

}
