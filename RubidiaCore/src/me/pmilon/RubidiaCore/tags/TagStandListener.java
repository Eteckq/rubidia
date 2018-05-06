package me.pmilon.RubidiaCore.tags;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class TagStandListener implements Listener {

	private Core plugin;
	public TagStandListener(Core plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		final Chunk chunk = event.getChunk();
		for(Entity entity : chunk.getEntities()){
			if(entity instanceof ArmorStand){
				if(entity.hasMetadata("display")){
					event.setCancelled(true);
					new BukkitTask(this.getPlugin()){
						public void run(){
							chunk.load();
						}

						@Override
						public void onCancel() {
						}
					}.runTaskLater(1);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		if(entity instanceof ArmorStand){
			if(entity.hasMetadata("display")){
				Entity damager = event.getDamager();
				if(damager instanceof Projectile){
					if(((Projectile)damager).getShooter() instanceof Entity){
						damager = (Entity) ((Projectile)damager).getShooter();
					}
				}
				LivingEntity target = (LivingEntity) Utils.getEntityFromUUID(entity.getWorld(), entity.getMetadata("display").get(0).asString());
				if(target != null)target.damage(event.getDamage(), damager);
			}
		}
	}

	public Core getPlugin() {
		return plugin;
	}

	public void setPlugin(Core plugin) {
		this.plugin = plugin;
	}
}
