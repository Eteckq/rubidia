package me.pmilon.RubidiaPets.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaPets.PetsPlugin;

public class Configs {

	public static void reloadPetsConfig() {
	    if (PetsPlugin.petsConfigFile == null) {
	    	PetsPlugin.petsConfigFile = new File(PetsPlugin.instance.getDataFolder(), "pets.yml");
	    }
	    PetsPlugin.petsConfig = YamlConfiguration.loadConfiguration(PetsPlugin.petsConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(PetsPlugin.instance.getResource("pets.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        PetsPlugin.petsConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getPetsConfig(){
	    if (PetsPlugin.petsConfig == null) {
	        reloadPetsConfig();
	    }
	    return PetsPlugin.petsConfig;
	}
	
	public static void savePetsConfig() {
	    if (PetsPlugin.petsConfig == null || PetsPlugin.petsConfigFile == null) {
	        return;
	    }
	    try {
	        getPetsConfig().save(PetsPlugin.petsConfigFile);
	    } catch (IOException ex) {
	    	PetsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + PetsPlugin.petsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultPetsConfig() {
	    if (PetsPlugin.petsConfigFile == null) {
	    	PetsPlugin.petsConfigFile = new File(PetsPlugin.instance.getDataFolder(), "pets.yml");
	    }
	    if (!PetsPlugin.petsConfigFile.exists()) {            
	    	PetsPlugin.instance.saveResource("pets.yml", false);
	    }
	}
	

	
	public static void saveConfigs(){
        try {
        	PetsPlugin.instance.getConfig().save(new File(Core.getSavesFolder(), "pconfig.yml"));
        	Configs.getPetsConfig().save(new File(Core.getSavesFolder(), "pets.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
