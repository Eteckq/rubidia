package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RTeleportCancelEvent extends Event implements Cancellable {

		private boolean cancelled = false;
		private Player player;
		private Location to;
		private RTeleportCause cause;
		public RTeleportCancelEvent(Player player, Location to, RTeleportCause cause){
			this.setPlayer(player);
			this.setTo(to);
			this.setCause(cause);
		}
		
		public Player getPlayer() {
			return player;
		}

		public void setPlayer(Player player) {
			this.player = player;
		}

		public Location getTo() {
			return to;
		}

		public void setTo(Location to) {
			this.to = to;
		}

		public RTeleportCause getCause() {
			return cause;
		}

		public void setCause(RTeleportCause cause) {
			this.cause = cause;
		}

		@Override
		public boolean isCancelled() {
			return cancelled;
		}

		@Override
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}

		private static final HandlerList handlers = new HandlerList();
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList(){
			return handlers;
		}
}
