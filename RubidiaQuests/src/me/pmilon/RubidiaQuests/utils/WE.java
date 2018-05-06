package me.pmilon.RubidiaQuests.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class WE {

	public static List<Block> getBlocks(Selection selection){
		List<Block> blocks = new ArrayList<Block>();
		
		Location min = selection.getMinimumPoint();
		Location max = selection.getMaximumPoint();
		int xMin = min.getBlockX();
		int yMin = min.getBlockY();
		int zMin = min.getBlockZ();
		
		int xMax = max.getBlockX();
		int yMax = max.getBlockY();
		int zMax = max.getBlockZ();
		
		for(int x = xMin;x <= xMax;x++){
			for(int y = yMin;y <= yMax;y++){
				for(int z = zMin;z <= zMax;z++){
					blocks.add(max.getWorld().getBlockAt(x, y, z));
				}
			}
		}
		return blocks;
	}
	
}
