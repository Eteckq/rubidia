package me.pmilon.RubidiaCore.utils;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Locations {

	public static Location getSafeLocation(Location location){
		Block block = location.getBlock();
		if(block.getType().isTransparent()){
			while(location.subtract(0,1,0).getBlock().getType().isTransparent()){
				if(location.getY() < 0)break;
			}
			return location.add(0,1,0);
		}else{
			while(!location.getBlock().getType().isTransparent()){
				location.add(0,1,0);
			}
			return location;
		}
	}
	
	public static LivingEntity getInSightEntity(Location location, Vector direction){
		for(int i = 0;i < 150;i++){
			direction.normalize();
			Location loc = location.toVector().add(direction.multiply(i)).toLocation(location.getWorld());
			for(LivingEntity entity : Core.toLivingEntityList(location.getWorld().getEntities())){
				if(entity.getLocation().distanceSquared(loc) < i*i)return entity;
			}
		}
		return null;
	}
	
	public static LivingEntity getClosestEntity(Location location, List<LivingEntity> entities){
		double distanceSquared = Double.MAX_VALUE;
		LivingEntity closestEntity = null;
		if(entities == null)entities = Core.toLivingEntityList(location.getWorld().getEntities());
		for(LivingEntity entity : entities){
			double thisDistance = entity.getLocation().distanceSquared(location);
			if(thisDistance < distanceSquared){
				distanceSquared = thisDistance;
				closestEntity = entity;
			}
		}
		return closestEntity;
	}
    
    public static List<Entity> getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        List<Entity> radiusEntities = new ArrayList<Entity>();
            for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
                for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                    int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                    for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                        if(e.getWorld().equals(l.getWorld()))if (e.getLocation().distanceSquared(l) <= (radius*radius)) radiusEntities.add(e);
                    }
                }
            }
        return radiusEntities;
    }
	
    public static List<Block> getRoundBlocks(Location center, int radius, boolean getInside){
    	List<Block> blocks = new ArrayList<Block>();
		for(int x = 0;x <= radius;x++){
			int z = (int)Math.round(Math.sqrt(radius*radius-x*x-1+2*x));
			if(getInside){
				for(int zu = 0;zu <= z;zu++){
					Block block1 = center.clone().add(x, 0, zu).getBlock();
					if(!blocks.contains(block1)){
						blocks.add(block1);
					}
					Block block2 = center.clone().add(x, 0, -zu).getBlock();
					if(!blocks.contains(block2)){
						blocks.add(block2);
					}
					Block block3 = center.clone().add(-x, 0, zu).getBlock();
					if(!blocks.contains(block3)){
						blocks.add(block3);
					}
					Block block4 = center.clone().add(-x, 0, -zu).getBlock();
					if(!blocks.contains(block4)){
						blocks.add(block4);
					}
				}
			}else{
				Block block1 = center.clone().add(x, 0, z).getBlock();
				if(!blocks.contains(block1)){
					blocks.add(block1);
				}
				Block block2 = center.clone().add(x, 0, -z).getBlock();
				if(!blocks.contains(block2)){
					blocks.add(block2);
				}
				Block block3 = center.clone().add(-x, 0, z).getBlock();
				if(!blocks.contains(block3)){
					blocks.add(block3);
				}
				Block block4 = center.clone().add(-x, 0, -z).getBlock();
				if(!blocks.contains(block4)){
					blocks.add(block4);
				}
			}
		}
    	return blocks;
    }
    
    public static Location getCenter(Location l){
    	return l.getBlock().getLocation().add(.5,0,.5);
    }
}
