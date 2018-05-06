package me.pmilon.RubidiaPets;

import java.io.File;

import me.pmilon.RubidiaPets.commands.PetCommandExecutor;
import me.pmilon.RubidiaPets.commands.PetsCommandExecutor;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaPets.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class PetsPlugin extends JavaPlugin{
	
	public static PetsPlugin instance;
	public static EventsHandler eventsHandler;
	public static File petsConfigFile;
	public static FileConfiguration petsConfig;
	public static ConsoleCommandSender console;
	
  public void onEnable(){
	instance = this;
	console = Bukkit.getConsoleSender();
	Configs.saveDefaultPetsConfig();
	Configs.getPetsConfig().options().copyDefaults(true);
	console.sendMessage("§a   Loading Pets...");
	console.sendMessage("§2------------------------------------------------------");
	Pets.onEnable(true);
	console.sendMessage("§2------------------------------------------------------");
    eventsHandler = new EventsHandler();
    this.getCommand("pet").setExecutor(new PetCommandExecutor());
    this.getCommand("pets").setExecutor(new PetsCommandExecutor());
    getServer().getPluginManager().registerEvents(PetsPlugin.eventsHandler, this);
  }
  public void onEnd(){
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity instanceof LivingEntity){
					if(entity.hasMetadata("pet"))entity.remove();
				}
			}
		}
	  
		console.sendMessage("§a   Saving Pets...");
		console.sendMessage("§2------------------------------------------------------");
		Pets.save(true);
		console.sendMessage("§2------------------------------------------------------");
		this.saveConfig();
  }
  
}