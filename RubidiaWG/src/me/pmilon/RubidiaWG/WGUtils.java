package me.pmilon.RubidiaWG;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WGUtils {
	
	public static boolean testState(Player player, Location location, StateFlag flag) {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
		return set.testState(player == null ? null : WorldGuardPlugin.inst().wrapPlayer(player), flag);
	}
	
	public static boolean testState(Player player, World world, Vector corner1, Vector corner2, StateFlag flag) {
		ProtectedCuboidRegion region = new ProtectedCuboidRegion("temp", new BlockVector(corner1.getX(), corner1.getY(), corner1.getZ()), new BlockVector(corner2.getX(), corner2.getY(), corner2.getZ()));
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ApplicableRegionSet set = container.get(BukkitAdapter.adapt(world)).getApplicableRegions(region);
		return set.testState(player == null ? null : WorldGuardPlugin.inst().wrapPlayer(player), flag);
	}
	
}
