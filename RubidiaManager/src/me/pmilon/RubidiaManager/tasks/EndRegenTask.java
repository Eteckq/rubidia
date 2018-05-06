package me.pmilon.RubidiaManager.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.onarandombox.MultiverseCore.utils.WorldManager;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class EndRegenTask extends AbstractTask {
	
	public EndRegenTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		if(this.getPlugin().getConfig().contains("regenEnd")){
			if(this.getPlugin().getConfig().getBoolean("regenEnd")){
				this.getPlugin().getConfig().set("regenEnd", false);
				WorldManager worldManager = new WorldManager(Core.multiverseCore);
				if(worldManager.regenWorld("Rubidia_the_end", true, true, null)){
					World world = Bukkit.getWorld("Rubidia_the_end");
					if(world != null){
						for(Entity entity : world.getEntities()){
							if(entity.getType().equals(EntityType.ENDER_DRAGON)){
								((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4444.0);
							}
						}
					}
					RubidiaManagerPlugin.console.sendMessage("�6END WORLD REGENERATED");
				}
			}
		}else this.getPlugin().getConfig().set("regenEnd", false);
	}

}
