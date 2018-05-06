package me.pmilon.RubidiaWG;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RubidiaWGPlugin extends JavaPlugin {
    private WGRegionEventsListener listener;
    private WGFlagsListener flagsListener;
    public static WorldGuardPlugin wgPlugin;
    
	@Override
	public void onLoad(){
        wgPlugin = getWGPlugin();
        wgPlugin.getFlagRegistry().register(Flags.DUELS);
        wgPlugin.getFlagRegistry().register(Flags.SOIL_TRAMPLING);
        wgPlugin.getFlagRegistry().register(Flags.MUSIC);
        wgPlugin.getFlagRegistry().register(Flags.BLOCKS);
        wgPlugin.getFlagRegistry().register(Flags.SKILLS);
    	wgPlugin.getFlagRegistry().register(Flags.RIDE);
    	wgPlugin.getFlagRegistry().register(Flags.CLAIM);
    	wgPlugin.getFlagRegistry().register(Flags.LEAVE_COMMAND);
    	wgPlugin.getFlagRegistry().register(Flags.NATURAL_SPAWN);
    	wgPlugin.getFlagRegistry().register(Flags.ENTER_TITLE);
    	wgPlugin.getFlagRegistry().register(Flags.LEAVE_TITLE);
    	wgPlugin.getFlagRegistry().register(Flags.REGEN);
	}
	
    @Override
    public void onEnable()
    {
        
        listener = new WGRegionEventsListener(this, wgPlugin);
        flagsListener = new WGFlagsListener(wgPlugin);
        
        getServer().getPluginManager().registerEvents(listener, wgPlugin);
        getServer().getPluginManager().registerEvents(flagsListener, wgPlugin);
    }
    
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        
        return (WorldGuardPlugin) plugin;
    }
}