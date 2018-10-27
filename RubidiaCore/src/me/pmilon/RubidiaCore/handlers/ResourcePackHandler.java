package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.potion.PotionEffectType;

public class ResourcePackHandler implements Listener {
	
	@EventHandler
	public void onResourcePackStatus(PlayerResourcePackStatusEvent event){
		final Player player = event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		Status status = event.getStatus();
		if(!player.isOp()){
			if(status.equals(Status.FAILED_DOWNLOAD)){
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.kickPlayer(("§cLe téléchargement de notre resource pack a échoué. Tentez de vous reconnecter.\nSi cela persiste, supprimez votre dossier .minecraft/server-resource-packs/ et reconnectez-vous."));
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}else if(status.equals(Status.DECLINED)){
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.kickPlayer(("§cNotre resource pack est §4§lindispensable§c.\nAutorisez-le dans le menu multijoueur > Rubidia > Modifier > Packs de ressources."));
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}else if(status.equals(Status.SUCCESSFULLY_LOADED))reset(player, rp);
		}else{
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					reset(player, rp);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(30);
		}
	}
	
	public void reset(final Player player, final RPlayer rp){
		if(rp.connectionLocation != null){
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					player.teleport(rp.connectionLocation);
					rp.connectionLocation = null;
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(0);
		}
		player.removePotionEffect(PotionEffectType.BLINDNESS);
	}
}