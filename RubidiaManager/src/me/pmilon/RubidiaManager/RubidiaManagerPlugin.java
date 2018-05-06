package me.pmilon.RubidiaManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkHandler;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.RChunk;
import me.pmilon.RubidiaManager.commands.ChunkCommandExecutor;
import me.pmilon.RubidiaManager.commands.ChunksCommandExecutor;
import me.pmilon.RubidiaManager.tasks.EndRegenTask;
import me.pmilon.RubidiaManager.tasks.MapRegenTask;
import me.pmilon.RubidiaManager.tasks.NetherRegenTask;
import me.pmilon.RubidiaManager.tasks.ServerRestartTask;
import me.pmilon.RubidiaManager.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class RubidiaManagerPlugin extends JavaPlugin {

	public static ConsoleCommandSender console;
	private static WorldRegenHandler regenerator;
	private static ChunkColl chunkColl;
	private static ChunkHandler chunkHandler;
	
	public static MultiverseCore multiverseCore;
	
	private static File savesFolder;
	
	public static RubidiaManagerPlugin instance;
	public static File chunksConfigFile;
	public static FileConfiguration chunksConfig;
	
	public void onEnable(){
		instance = this;
		console = Bukkit.getConsoleSender();
		multiverseCore = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		
		this.getConfig().options().copyDefaults(true);
		Configs.getChunksConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultChunksConfig();
		
		savesFolder = new File(RubidiaManagerPlugin.instance.getDataFolder().getAbsolutePath() + "/saves");
		if(!savesFolder.exists())savesFolder.mkdirs();
		
		regenerator = new WorldRegenHandler(this);
		chunkColl = new ChunkColl();
		chunkHandler = new ChunkHandler(this);
		
		this.getCommand("chunk").setExecutor(new ChunkCommandExecutor());
		this.getCommand("chunks").setExecutor(new ChunksCommandExecutor());
		
		Timer timer = new Timer();
		this.scheduleRestart(timer);
		this.scheduleNetherRegen(timer);
		this.scheduleEndRegen(timer);
		this.scheduleMapRegen(timer);
	}
	
	public void onDisable(){
		RubidiaManagerPlugin.getChunkColl().save();
		this.saveConfig();
	}
	
	public static List<RChunk> getToRegen(World world){
		List<RChunk> toRegen = new ArrayList<RChunk>();
		for(Chunk chunk : ChunkColl.chunks){
			if(chunk.isRegenable().equals("true")){
				toRegen.add((RChunk)chunk);
			}
		}
		return toRegen;
	}
	
	public static void regen(List<RChunk> toRegen){
		for(final RChunk rchunk : toRegen){
			final ChunkManager manager = ChunkManager.getManager(rchunk);
			if(manager.isSaved()){
				final int id = toRegen.indexOf(rchunk);
				Bukkit.getScheduler().runTaskLater(RubidiaManagerPlugin.instance, new Runnable(){
					public void run(){
						manager.load();
						rchunk.setRegenerated(true);
						RubidiaManagerPlugin.console.sendMessage("§6Regenerated §echunk §6#" + id);
					}
				}, id);
			}
		}
	}
	
	public static WorldRegenHandler getRegenerator(){
		return regenerator;
	}

	public static ChunkColl getChunkColl() {
		return chunkColl;
	}

	public static ChunkHandler getChunkHandler() {
		return chunkHandler;
	}
	
	public static File getSavesFolder(){
		return savesFolder;
	}
	
	
	
	public void scheduleRestart(Timer timer){
		Calendar interval = Calendar.getInstance();
		interval.add(Calendar.DAY_OF_WEEK, 1);
		interval.set(Calendar.HOUR_OF_DAY, 2);
		interval.set(Calendar.MINUTE, 0);
		interval.set(Calendar.SECOND, 0);
		interval.set(Calendar.MILLISECOND, 0);
		timer.schedule(new ServerRestartTask(this), interval.getTime(), 24*60*60*1000);
	}
	
	private void scheduleNetherRegen(Timer timer){
		Calendar interval = Calendar.getInstance();
		interval.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		interval.set(Calendar.HOUR_OF_DAY, 2);
		interval.set(Calendar.MINUTE, 30);
		interval.set(Calendar.SECOND, 0);
		interval.set(Calendar.MILLISECOND, 0);
		if(interval.before(Calendar.getInstance()))interval.add(Calendar.WEEK_OF_MONTH, 1);
		timer.schedule(new NetherRegenTask(this), interval.getTime(), 7*24*60*60*1000);
	}
	
	private void scheduleEndRegen(Timer timer){
		Calendar interval = Calendar.getInstance();
		interval.set(Calendar.HOUR_OF_DAY, 3);
		interval.set(Calendar.MINUTE, 0);
		interval.set(Calendar.SECOND, 0);
		interval.set(Calendar.MILLISECOND, 0);
		if(interval.before(Calendar.getInstance()))interval.add(Calendar.DAY_OF_WEEK, 1);
		timer.schedule(new EndRegenTask(this), interval.getTime(), 24*60*60*1000);
	}
	
	private void scheduleMapRegen(Timer timer){
		Calendar interval = Calendar.getInstance();
		interval.set(Calendar.DAY_OF_MONTH, 1);
		interval.set(Calendar.HOUR_OF_DAY, 4);
		interval.set(Calendar.MINUTE, 0);
		interval.set(Calendar.SECOND, 0);
		interval.set(Calendar.MILLISECOND, 0);
		if(interval.before(Calendar.getInstance()))interval.add(Calendar.MONTH, 1);
		timer.schedule(new MapRegenTask(this), interval.getTime(), 31*7*24*60*60*1000);
	}
}
