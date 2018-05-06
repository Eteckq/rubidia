package me.pmilon.RubidiaSecretPaths;

import org.bukkit.Location;

public class LocationUtils {

	public static boolean isInBox(Location location, Location bottom, Location top){
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		return ((x >= bottom.getBlockX()) && (x <= top.getBlockX()) && (y >= bottom.getBlockY()) && (y <= top.getBlockY()) && (z >= bottom.getBlockZ()) && (z <= top.getBlockZ()));
	}
	
}
