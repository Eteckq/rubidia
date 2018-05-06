package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.scrolls.Scroll;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RTeleportEvent extends Event implements Cancellable{

	public static class RTeleportCause {

		public static enum RTeleportType {
			DELAYED_TELEPORTATION, FRIEND_INVOCATION, CITY_TELEPORTATION, WILD_TELEPORTATION, FRIEND_TELEPORTATION, GUILD_HOME, RAID_CENTER;
		}
		
		private RTeleportType type;
		private Scroll scroll;
		private Player teleporter;
		private Player teleported;
		public RTeleportCause(RTeleportType type, Scroll scroll, Player teleporter, Player teleported){
			this.type = type;
			this.scroll = scroll;
			this.teleporter = teleporter;
			this.teleported = teleported;
		}
		
		public RTeleportType getType() {
			return type;
		}
		
		public void setType(RTeleportType type) {
			this.type = type;
		}

		public Scroll getScroll() {
			return scroll;
		}

		public void setScroll(Scroll scroll) {
			this.scroll = scroll;
		}

		public Player getTeleporter() {
			return teleporter;
		}

		public void setTeleporter(Player teleporter) {
			this.teleporter = teleporter;
		}

		public Player getTeleported() {
			return teleported;
		}

		public void setTeleported(Player teleported) {
			this.teleported = teleported;
		}
		
	}
	
	private boolean cancelled = false;
	private final Player player;
	private Location from;
	private Location to;
	private RTeleportCause cause;
	public RTeleportEvent(Player player, Location from, Location to, RTeleportCause cause){
		this.player = player;
		this.setFrom(from);
		this.setTo(to);
		this.setCause(cause);
	}
	
	
	public Player getPlayer() {
		return player;
	}


	public Location getFrom() {
		return from;
	}


	public void setFrom(Location from) {
		this.from = from;
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
