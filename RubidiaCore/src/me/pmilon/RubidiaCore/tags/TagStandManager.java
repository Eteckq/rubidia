package me.pmilon.RubidiaCore.tags;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class TagStandManager {

	public static HashMap<Entity, TagStand> tagStands = new HashMap<Entity,TagStand>();
	
	public static void registerTagStand(TagStand tag){
		tagStands.put(tag.getHolder(), tag);
	}
	
	public static void unregisterTagStand(TagStand tag){
		if(tagStands.containsKey(tag.getHolder()))tagStands.remove(tag.getHolder());
	}
	
	public static TagStand getTagStand(Entity holder){
		if(tagStands.containsKey(holder))return tagStands.get(holder);
		return null;
	}
	
	public static boolean hasTagStand(Entity holder){
		return tagStands.containsKey(holder);
	}
	
	public static void onDisable(){
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity instanceof ArmorStand && entity.hasMetadata("display")){
					entity.remove();
				}
			}
		}
		for(TagStand tagStand : tagStands.values()){
			for(ArmorStand stand : tagStand.displays){
				stand.remove();
			}
		}
	}
	
}
