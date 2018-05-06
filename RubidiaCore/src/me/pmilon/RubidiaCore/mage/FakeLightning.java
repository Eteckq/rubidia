package me.pmilon.RubidiaCore.mage;

import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSpawnEntityWeather;
import me.pmilon.RubidiaCore.utils.Locations;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FakeLightning {

	private boolean fire;
	public FakeLightning(boolean fire){
		this.fire = fire;
	}
	
	public boolean isFire() {
		return fire;
	}
	
	public void setFire(boolean fire) {
		this.fire = fire;
	}
	
	public void display(Location location, List<Entity> entities){
		WrapperPlayServerSpawnEntityWeather packet = new WrapperPlayServerSpawnEntityWeather();
		packet.setEntityID(Utils.random.nextInt(200));
		packet.setType(1);
		packet.setX(location.getBlockX());
		packet.setY(location.getBlockY());
		packet.setZ(location.getBlockZ());
		if(!Core.toPlayerList(entities).isEmpty()){
			for(Player player : Core.toPlayerList(entities)){
				packet.sendPacket(player);
			}
		}
		location.getWorld().playSound(location, Sound.ENTITY_LIGHTNING_IMPACT, 1, 1);
		if(this.isFire())location.getBlock().setType(Material.FIRE);
	}
	
	public void display(Entity entity){
		display(entity.getLocation(), entity.getNearbyEntities(16, 16, 16));
	}
	
	public void display(Location location){
		display(location, Locations.getNearbyEntities(location, 16));
	}
}
