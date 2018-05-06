package me.pmilon.RubidiaQuests.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.QuestsPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {

	public static void reloadQuestsConfig() {
	    if (QuestsPlugin.questsConfigFile == null) {
	    	QuestsPlugin.questsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "quests.yml");
	    }
	    QuestsPlugin.questsConfig = YamlConfiguration.loadConfiguration(QuestsPlugin.questsConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(QuestsPlugin.instance.getResource("quests.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        QuestsPlugin.questsConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getQuestsConfig(){
	    if (QuestsPlugin.questsConfig == null) {
	        reloadQuestsConfig();
	    }
	    return QuestsPlugin.questsConfig;
	}
	
	public static void saveQuestsConfig() {
	    if (QuestsPlugin.questsConfig == null || QuestsPlugin.questsConfigFile == null) {
	        return;
	    }
	    try {
	        getQuestsConfig().save(QuestsPlugin.questsConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + QuestsPlugin.questsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultQuestsConfig() {
	    if (QuestsPlugin.questsConfigFile == null) {
	    	QuestsPlugin.questsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "quests.yml");
	    }
	    if (!QuestsPlugin.questsConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("quests.yml", false);
	    }
	}
	

	public static void reloadPNJConfig() {
	    if (QuestsPlugin.pnjConfigFile == null) {
	    	QuestsPlugin.pnjConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "pnjs.yml");
	    }
	    QuestsPlugin.pnjConfig = YamlConfiguration.loadConfiguration(QuestsPlugin.pnjConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(QuestsPlugin.instance.getResource("pnjs.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        QuestsPlugin.pnjConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getPNJConfig(){
	    if (QuestsPlugin.pnjConfig == null) {
	        reloadPNJConfig();
	    }
	    return QuestsPlugin.pnjConfig;
	}
	
	public static void savePNJConfig() {
	    if (QuestsPlugin.pnjConfig == null || QuestsPlugin.pnjConfigFile == null) {
	        return;
	    }
	    try {
	        getPNJConfig().save(QuestsPlugin.pnjConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + QuestsPlugin.pnjConfigFile, ex);
	    }
	}
	
	public static void saveDefaultPNJConfig() {
	    if (QuestsPlugin.pnjConfigFile == null) {
	    	QuestsPlugin.pnjConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "pnjs.yml");
	    }
	    if (!QuestsPlugin.pnjConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("pnjs.yml", false);
	    }
	}
	
	


	public static void reloadShopsConfig() {
	    if (QuestsPlugin.shopsConfigFile == null) {
	    	QuestsPlugin.shopsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "shops.yml");
	    }
	    QuestsPlugin.shopsConfig = YamlConfiguration.loadConfiguration(QuestsPlugin.shopsConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(QuestsPlugin.instance.getResource("shops.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        QuestsPlugin.shopsConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getShopsConfig(){
	    if (QuestsPlugin.shopsConfig == null) {
	        reloadShopsConfig();
	    }
	    return QuestsPlugin.shopsConfig;
	}
	
	public static void saveShopsConfig() {
	    if (QuestsPlugin.shopsConfig == null || QuestsPlugin.shopsConfigFile == null) {
	        return;
	    }
	    try {
	        getShopsConfig().save(QuestsPlugin.shopsConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + QuestsPlugin.shopsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultShopsConfig() {
	    if (QuestsPlugin.shopsConfigFile == null) {
	    	QuestsPlugin.shopsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "shops.yml");
	    }
	    if (!QuestsPlugin.shopsConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("shops.yml", false);
	    }
	}
	
	public static void saveConfigs(){
        try {
        	QuestsPlugin.instance.getConfig().save(new File(Core.getSavesFolder(), "qconfig.yml"));
        	Configs.getQuestsConfig().save(new File(Core.getSavesFolder(), "quests.yml"));
        	Configs.getPNJConfig().save(new File(Core.getSavesFolder(), "pnjs.yml"));
        	Configs.getShopsConfig().save(new File(Core.getSavesFolder(), "shops.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
