package me.pmilon.RubidiaCore.ranks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class Ranks {

	public static final long TIMEOUT = 1*7*24*60*60*1000L;
	
	public static void update(){
		Core.console.sendMessage("§eUpdating ranks...");
		List<RPlayer> rps = new ArrayList<RPlayer>();
		for(RPlayer rp : Core.rcoll.data()){
			if(!Bukkit.getServer().getOperators().contains(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId()))) && System.currentTimeMillis()-rp.getLastConnectionDate() <= Ranks.TIMEOUT){
				rps.add(rp);
			}
		}
		Collections.sort(rps, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	        	int level1 = 0;
	        	for(SPlayer sp : rp1.getSaves()){
	        		if(sp != null){
		        		if(sp.getRLevel() >= level1){
		        			level1 = sp.getRLevel();
		        		}
	        		}
	        	}
	        	int level2 = 0;
	        	for(SPlayer sp : rp2.getSaves()){
	        		if(sp != null){
		        		if(sp.getRLevel() >= level1){
		        			level2 = sp.getRLevel();
		        		}
	        		}
	        	}
	            return level2-level1;
	        }
	    });
		for(int i = 1;i < 6 && i < rps.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.level." + i + ".location");
			if(location != null){
				RPlayer rp = rps.get(i-1);
				if(rp != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + rp.getName());
							sign.setLine(2, "§3N. §9§l" + rp.getRLevel());
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		Collections.sort(rps, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	        	int renom1 = 0;
	        	for(SPlayer sp : rp1.getSaves()){
	        		if(sp != null){
		        		if(sp.getRenom() >= renom1){
		        			renom1 = sp.getRenom();
		        		}
	        		}
	        	}
	        	int renom2 = 0;
	        	for(SPlayer sp : rp2.getSaves()){
	        		if(sp != null){
		        		if(sp.getRenom() >= renom2){
		        			renom2 = sp.getRenom();
		        		}
	        		}
	        	}
	            return renom2-renom1;
	        }
	    });
		for(int i = 1;i < 6 && i < rps.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.renom." + i + ".location");
			if(location != null){
				RPlayer rp = rps.get(i-1);
				if(rp != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + rp.getName());
							sign.setLine(2, "§8[§7" + rp.getRenom() + "§8]");
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		Collections.sort(rps, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	        	int money1 = 0;
	        	for(SPlayer sp : rp1.getSaves()){
	        		if(sp != null){
		        		if(sp.getLastmoneyamount() >= money1){
		        			money1 = sp.getLastmoneyamount();
		        		}
	        		}
	        	}
	        	int money2 = 0;
	        	for(SPlayer sp : rp2.getSaves()){
	        		if(sp != null){
		        		if(sp.getLastmoneyamount() >= money2){
		        			money2 = sp.getLastmoneyamount();
		        		}
	        		}
	        	}
	            return money2-money1;
	        }
	    });
		for(int i = 1;i < 6 && i < rps.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.money." + i + ".location");
			if(location != null){
				RPlayer rp = rps.get(i-1);
				if(rp != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + rp.getName());
							sign.setLine(2, "§2§l" + rp.getLastMoneyAmount() + " §aémd.");
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		Collections.sort(rps, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	        	int kills1 = 0;
	        	for(SPlayer sp : rp1.getSaves()){
	        		if(sp != null){
		        		if(sp.getKills() >= kills1){
		        			kills1 = sp.getKills();
		        		}
	        		}
	        	}
	        	int kills2 = 0;
	        	for(SPlayer sp : rp2.getSaves()){
	        		if(sp != null){
		        		if(sp.getKills() >= kills2){
		        			kills2 = sp.getKills();
		        		}
	        		}
	        	}
	            return kills2-kills1;
	        }
	    });
		for(int i = 1;i < 6 && i < rps.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.kills." + i + ".location");
			if(location != null){
				RPlayer rp = rps.get(i-1);
				if(rp != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + rp.getName());
							sign.setLine(2, "§4§l" + rp.getKills() + " §cmeurtre" + (rp.getKills() > 1 ? "s" : ""));
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		Collections.sort(rps, new Comparator<RPlayer>() {
	        public int compare(RPlayer rp1, RPlayer rp2) {
	            return (int) (rp2.getGamingTime()-rp1.getGamingTime());
	        }
	    });
		for(int i = 1;i < 6 && i < rps.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.gamingtime." + i + ".location");
			if(location != null){
				RPlayer rp = rps.get(i-1);
				if(rp != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							long hours = TimeUnit.MILLISECONDS.toHours(rp.getGamingTime());
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + rp.getName());
							sign.setLine(2, "§4§l" + hours + " §cheure" + (hours > 1 ? "s" : ""));
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		
		List<Guild> guilds = new ArrayList<Guild>();
		for(Guild guild : GuildsPlugin.gcoll.data()){
			if(!guild.getName().equalsIgnoreCase("none") && guild.isActive())guilds.add(guild);
		}
		Collections.sort(guilds, new Comparator<Guild>() {
	        public int compare(Guild g1, Guild g2) {
	            return g2.getClaims().size()-g1.getClaims().size();
	        }
	    });
		for(int i = 1;i < 6 && i < guilds.size()+1;i++){
			Location location = (Location) Configs.getDatabase().get("rankings.claims." + i + ".location");
			if(location != null){
				Guild guild = guilds.get(i-1);
				if(guild != null){
					Block block = location.getBlock();
					for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
						if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
							Sign sign = (Sign)block.getRelative(fc).getState();
							sign.setLine(0, "§2§m---§2> §8#" + i + "§2 <§m---");
							sign.setLine(1, "§7§l" + guild.getName());
							sign.setLine(2, "§6§l" + guild.getClaims().size() + "§e terre" + (guild.getClaims().size() > 1 ? "s" : ""));
							sign.setLine(3, "§2§m----------");
							sign.update(true);
							Block blockUp = block.getRelative(BlockFace.UP);
							if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
								Skull skull = (Skull)blockUp.getState();
								skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(guild.getLeader().getUniqueId())));
								skull.update(true);
							}
						}
					}
				}
			}
		}
		Core.console.sendMessage("§eRanks updated!");
	}
	
}
