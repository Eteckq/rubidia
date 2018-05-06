package me.pmilon.RubidiaWG;

import java.util.Set;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGUtils {

	public static ProtectedRegion getHighestPriority(Set<ProtectedRegion> set){
		ProtectedRegion region = null;
		int priority = -10000000;
		for(ProtectedRegion rg : set){
			if(rg.getPriority() > priority){
				priority = rg.getPriority();
				region = rg;
			}
		}
		return region;
	}
	
}
