package me.pmilon.RubidiaManager;

import java.util.HashMap;
import java.util.Random;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaGuilds.guilds.GMember;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class WorldRegenHandler implements Listener {

	public HashMap<Player, Integer> fireworkFinish = new HashMap<Player, Integer>();
	
	private final RubidiaManagerPlugin plugin;
	public WorldRegenHandler(RubidiaManagerPlugin plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	private void onDragonDeath(EntityDeathEvent e){
		LivingEntity entity = e.getEntity();
		if(entity instanceof EnderDragon){
			EnderDragon dragon = (EnderDragon)entity;
			Player killer = dragon.getKiller();
			if(killer != null){
				if(dragon.getLocation().getWorld().getName().contains("the_end")){
					this.getPlugin().getConfig().set("regenEnd", true);
					e.setDroppedExp(0);
					e.getDrops().add(new ItemStack(Material.EMERALD, new Random().nextInt(64)+39));
					GMember member = GMember.get(killer);
					for(final Player player : Bukkit.getOnlinePlayers()){
						RPlayer rp = RPlayer.get(player);
						rp.sendTitle("", rp.translateString("§6§l" + (member.hasGuild() ? member.getGuild().getName() : member.getName()) + " §ahas ended the Ender Dragon !", "§6§l" + (member.hasGuild() ? member.getGuild().getName() : member.getName()) + " §een a fini avec l'Ender Dragon !"), 5, 120, 10);
						this.fireworkFinish.put(player, Bukkit.getScheduler().runTaskTimer(this.getPlugin(), new Runnable(){
							int time = 0;
							public void run(){
								if(time < 5){
									Core.playAnimEffect(ParticleEffect.VILLAGER_HAPPY, player.getLocation().add(0,1,0), .3F, .3F, .3F, 1, 50);
									LevelUtils.firework(player.getLocation());
								}else if(fireworkFinish.containsKey(player)){
									Bukkit.getScheduler().cancelTask(fireworkFinish.get(player));
									fireworkFinish.remove(player);
								}
								time += 1;
							}
						}, 0, 20).getTaskId());
					}
				}
			}
		}
	}

	public RubidiaManagerPlugin getPlugin() {
		return plugin;
	}
}
