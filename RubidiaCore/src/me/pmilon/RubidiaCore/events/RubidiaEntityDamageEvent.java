package me.pmilon.RubidiaCore.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class RubidiaEntityDamageEvent extends EntityEvent implements Cancellable {

	protected boolean cancelled = false;
	protected LivingEntity damager;
	protected double damages;
	protected Projectile projectile;
	public RubidiaEntityDamageEvent(LivingEntity damaged, LivingEntity damager, Projectile projectile, double damages) {
		super(damaged);
		this.damager = damager;
		this.damages = damages;
		this.projectile = projectile;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public LivingEntity getDamager() {
		return damager;
	}

	public void setDamager(LivingEntity damager) {
		this.damager = damager;
	}

	public double getDamages() {
		return damages;
	}

	public void setDamages(double damages) {
		this.damages = damages;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
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
