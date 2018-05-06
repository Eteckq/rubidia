package me.pmilon.RubidiaManager.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {
	
	public static void reloadChunksConfig() {
	    if (RubidiaManagerPlugin.chunksConfigFile == null) {
	    	RubidiaManagerPlugin.chunksConfigFile = new File(RubidiaManagerPlugin.instance.getDataFolder(), "saves.yml");
	    }
	    RubidiaManagerPlugin.chunksConfig = YamlConfiguration.loadConfiguration(RubidiaManagerPlugin.chunksConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(RubidiaManagerPlugin.instance.getResource("saves.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        RubidiaManagerPlugin.chunksConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getChunksConfig(){
	    if (RubidiaManagerPlugin.chunksConfig == null) {
	        reloadChunksConfig();
	    }
	    return RubidiaManagerPlugin.chunksConfig;
	}
	
	public static void saveChunksConfig() {
	    if (RubidiaManagerPlugin.chunksConfig == null || RubidiaManagerPlugin.chunksConfigFile == null) {
	        return;
	    }
	    try {
	        getChunksConfig().save(RubidiaManagerPlugin.chunksConfigFile);
	    } catch (IOException ex) {
	    	RubidiaManagerPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + RubidiaManagerPlugin.chunksConfigFile, ex);
	    }
	}
	
	public static void saveDefaultChunksConfig() {
	    if (RubidiaManagerPlugin.chunksConfigFile == null) {
	        RubidiaManagerPlugin.chunksConfigFile = new File(RubidiaManagerPlugin.instance.getDataFolder(), "saves.yml");
	    }
	    if (!RubidiaManagerPlugin.chunksConfigFile.exists()) {            
	    	RubidiaManagerPlugin.instance.saveResource("saves.yml", false);
	    }
	}
	
}
