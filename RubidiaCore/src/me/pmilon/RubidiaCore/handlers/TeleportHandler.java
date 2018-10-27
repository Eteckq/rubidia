package me.pmilon.RubidiaCore.handlers;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RTeleportCancelEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaPets.pets.Pet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportHandler implements Listener{

	public static HashMap<Player, Integer> invoke_tasks = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> tp_tasks = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> teleportationtask = new HashMap<Player, Integer>();
	
	static Core plugin;
	public TeleportHandler(Core core){
		plugin = core;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(final PlayerTeleportEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Location to = e.getTo();
		if(to.getWorld().getEnvironment().equals(Environment.NETHER)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false), true);
			rp.sendTitle("§7Nether", "", 5, 30, 20);
		}else if(to.getWorld().getEnvironment().equals(Environment.THE_END)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false), true);
			rp.sendTitle(("§7Le Néant"), "", 5, 30, 20);
		}
		
		if(!e.getFrom().getWorld().equals(to.getWorld())){
			if(p.getGameMode().equals(GameMode.CREATIVE)){
				new BukkitTask(TeleportHandler.plugin){

					@Override
					public void run() {
						p.setGameMode(GameMode.CREATIVE);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}
		}
		
		if(!p.isInsideVehicle()){//avoid infinite loop while player mounts a horse (because mount == teleport)
			for(Pet pet : rp.getPets()){
				if(pet.isActive()){
					if(pet.getEntity() != null){
						if(pet.getEntity().getWorld().equals(p.getWorld())){
							if(pet.getEntity().getLocation().distanceSquared(p.getLocation()) > 4*4){
								teleport(pet.getEntity(), e.getTo());
							}
						}
					}
				}
			}
		}
	}
		
	@EventHandler
	public void onRTeleportCancel(RTeleportCancelEvent event){
		RTeleportCause cause = event.getCause();
		Scroll scroll = cause.getScroll();
		if(scroll != null){
			Player player = event.getPlayer();
			Player teleporter = cause.getTeleporter();
			Player teleported = cause.getTeleported();
			if(teleporter != null){
				if(scroll.getType().equals(ScrollType.FRDCALL)){
					scroll.cancel(teleporter);
					RPlayer.get(teleporter).sendMessage("§4" + player.getName() + "§c moved during invocation establishment!", "§4" + player.getName() + "§c a bougé durant l'établissement de l'invocation !");
				}else if(scroll.getType().equals(ScrollType.FRDTP)){
					scroll.cancel(teleported);
					RPlayer.get(teleported).sendMessage("§4" + player.getName() + "§c moved during teleportation establishment!", "§4" + player.getName() + "§c a bougé durant l'établissement de la téléportation !");
				}
			}else scroll.cancel(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		if(teleportationtask.containsKey(p) && e.getFrom().distanceSquared(e.getTo()) > .04){
			BukkitTask.tasks.get(teleportationtask.get(p)).cancel();
			rp.sendMessage("§cVous avez bougé ! Téléportation annulée !");
		}
	}
	
	public static void startTeleportation(final Player player, final Location location, final RTeleportCause cause){
		RPlayer rp = RPlayer.get(player);
		rp.sendMessage("§eNe bougez pas, ou la téléportation sera annulée !");
		teleportationtask.put(player, new BukkitTask(Core.instance){

			@Override
			public void run() {
				teleportationtask.remove(player);
				RTeleportEvent event = new RTeleportEvent(player, player.getLocation(), location, cause);
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					TeleportHandler.teleport(event.getPlayer(), event.getTo());
				}else this.cancel();
			}

			@Override
			public void onCancel() {
				RTeleportCancelEvent ev = new RTeleportCancelEvent(player, location, cause);
				Bukkit.getPluginManager().callEvent(ev);
				teleportationtask.remove(player);
			}
			
		}.runTaskLater(rp.isOp() ? 0 : 65).getTaskId());
	}
	
	public static void teleport(final Entity e, Location location) {
		if(e != null){
	    	Core.playAnimEffect(Particle.PORTAL, e.getLocation(), .5F, .5F, .5F, 1, 100);
			handleVehicleTeleportation(e, location);
			e.teleport(location);
	    	Core.playAnimEffect(Particle.PORTAL, location, .5F, .5F, .5F, 1, 100);
		}
	}

	public static void handleVehicleTeleportation(Entity e, Location location){
		if(e != null){
			Entity vehicle = e;
			Entity passenger = e;
			for(int i = e.getPassengers().size();i > 0;i--){
				passenger = e;
				while(!passenger.getPassengers().isEmpty()){
					passenger = passenger.getPassengers().get(0);
				}
				vehicle = passenger.getVehicle();
				passenger.eject();
				passenger.teleport(location);
				final Entity v = vehicle;
				final Entity p = passenger;
				new BukkitTask(Core.instance){
					public void run(){
						v.addPassenger(p);
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				}.runTaskLater(i);
			}
		}
	}
	
	
	
	public static void requestInvocation(final Player invoked, final Player invocator, final Scroll scroll) {
		TeleportHandler.invoke_tasks.put(invoked, new BukkitTask(Core.instance){
			@Override
			public void run(){
				RPlayer.get(invocator).sendMessage("§4" + invoked.getName() + " §chas not answered your invocation request.", "§4" + invoked.getName() + " §cn'a pas répondu à votre invocation.");
				TeleportHandler.invoke_tasks.remove(invoked);
			}

			@Override
			public void onCancel() {
				TeleportHandler.startTeleportation(invoked, invocator.getLocation(), new RTeleportCause(RTeleportType.FRIEND_INVOCATION,scroll,invocator,invoked));
				TeleportHandler.invoke_tasks.remove(invoked);
			}
		}.runTaskLater(15*20).getTaskId());
		invocator.sendMessage("§eInvocation...");
		RPlayer.get(invoked).sendMessage("§6" + invocator.getName() + " §ehas invoked you! Type §6/tp §eto accept : you have §615 §eseconds.", "§6" + invocator.getName() + " §evous a invoqué ! Tapez §6/tp §epour accepter : vous avez §615 §esecondes.");
	}
	
	public static void requestTeleportation(final Player teleported, final Player teleporter, final Scroll scroll) {
		TeleportHandler.tp_tasks.put(teleporter, new BukkitTask(Core.instance){
			@Override
			public void run(){
				RPlayer.get(teleported).sendMessage("§4" + teleporter.getName() + " §chas not answered your teleportation request.", "§4" + teleporter.getName() + " §cn'a pas répondu à votre demande de téléportation.");
				TeleportHandler.tp_tasks.remove(teleporter);
			}

			@Override
			public void onCancel() {
				TeleportHandler.startTeleportation(teleported, teleporter.getLocation(), new RTeleportCause(RTeleportType.FRIEND_TELEPORTATION, scroll,teleporter,teleported));
				TeleportHandler.tp_tasks.remove(teleporter);
			}
		}.runTaskLater(15*20).getTaskId());
		teleported.sendMessage("§eTéléportation...");
		RPlayer.get(teleporter).sendMessage("§6" + teleported.getName() + " §einvoked god powers to teleport to you! Type §6/tp §eto accept : you have §615 §eseconds.", "§6" + teleported.getName() + " §ea invoqué les puissances divines pour se téléporter vers vous ! Tapez §6/tp §epour accepter : vous avez §615 §esecondes.");
	}
}
