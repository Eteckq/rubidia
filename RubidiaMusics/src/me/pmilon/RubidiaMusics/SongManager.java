package me.pmilon.RubidiaMusics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SongManager {
	
	public enum Song {
		
		TAVERN(Material.RECORD_3, "tavern"),
		FOREST_2(Material.RECORD_4, "forest2"),
		MEARWOODEN_EXIT(Material.RECORD_5, "mearwood_exit"),
		FANTASTIC(Material.RECORD_6, "fantastic"),
		FIELDS(Material.RECORD_7, "fields"),
		NULL(Material.RECORD_8, "null"),//empty song
		DUNGEON(Material.RECORD_9, "dungeon"),
		AERO_TRAVEL(Material.RECORD_10, "aeroplane"),
		RUBIDIA_THEME(Material.RECORD_11, "principal"),
		BOAT_TRAVEL(Material.RECORD_12, "boat"),
		MOUNTAIN(Material.GOLD_RECORD, "mountain"),
		FOREST(Material.GREEN_RECORD, "forest");
		
		private final Material material;
		private final String name;
		private Song(Material material, String name){
			this.material = material;
			this.name = name;
		}
		public Material getMaterial() {
			return material;
		}
		public String getName() {
			return name;
		}
		public static Song getByName(String name){
			for(Song song : values()){
				if(song.getName().equals(name)){
					return song;
				}
			}
			return null;
		}
	}

	public static HashMap<Player, Song> attemptingPlaying = new HashMap<Player, Song>();
	public static HashMap<Player, Location> attemptingPlayingLocations = new HashMap<Player, Location>();
	public static HashMap<Player, Location> locations = new HashMap<Player, Location>();
	public static HashMap<Player, Song> songs = new HashMap<Player, Song>();
	public static HashMap<Player, List<Location>> attemptingErasing = new HashMap<Player, List<Location>>();
	
	public static void playSong(final Player player, Song song, Location location){
		SongManager.stopSong(player);
		if(song != null){
			RPlayer rp = RPlayer.get(player);
			if(rp.getMusic()){
				if(location == null)location = player.getLocation();
				if(!location.getWorld().equals(player.getWorld()))return;
				if(location.distanceSquared(player.getLocation()) < 3844){
					player.playEffect(location, Effect.RECORD_PLAY, song.getMaterial());
					locations.put(player, location);
					songs.put(player, song);
				}else SongManager.addAttemptingPlaying(player, song, location);
			}
		}
	}
	
	public static void stopSong(final Player player){
		if(SongManager.isListeningSong(player)){
			final Location location = SongManager.getSongLocations(player);
			locations.remove(player);
			songs.remove(player);
			if(!location.getWorld().equals(player.getWorld()))return;
			if(location.distanceSquared(player.getLocation()) < 3844){
				player.playEffect(location, Effect.RECORD_PLAY, Song.NULL.getMaterial());
			}else SongManager.addAttemptingErasingLocation(player, location);
		}
	}
	
	public static void addAttemptingErasingLocation(Player player, Location location) {
		List<Location> locations = SongManager.attemptingErasing.containsKey(player) ? SongManager.attemptingErasing.get(player) : new ArrayList<Location>();
		locations.add(location);
		SongManager.attemptingErasing.put(player, locations);
	}
	
	public static void eraseAttemptingLocation(Player player, Location location) {
		player.playEffect(location, Effect.RECORD_PLAY, Song.NULL.getMaterial());
		List<Location> locations = SongManager.attemptingErasing.containsKey(player) ? SongManager.attemptingErasing.get(player) : new ArrayList<Location>();
		locations.remove(location);
		if(!locations.isEmpty())SongManager.attemptingErasing.put(player, locations);
		else SongManager.attemptingErasing.remove(player);
	}
	
	public static void addAttemptingPlaying(Player player, Song song, Location location) {
		attemptingPlayingLocations.put(player, location);
		attemptingPlaying.put(player, song);
	}
	
	public static void playAttemptingPlaying(Player player, Location location) {
		Song song = attemptingPlaying.get(player);
		SongManager.playSong(player, song, location);
		attemptingPlayingLocations.remove(player);
		attemptingPlaying.remove(player);
	}

	public static Location getSongLocations(Player player){
		return locations.get(player);
	}
	
	public static boolean isListeningSong(Player player){
		return songs.containsKey(player);
	}
	
	public static Song getSong(Player player){
		return songs.get(player);
	}
}
