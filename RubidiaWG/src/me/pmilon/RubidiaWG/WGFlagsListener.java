package me.pmilon.RubidiaWG;

import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerAbilityEvent;
import me.pmilon.RubidiaCore.events.RPlayerRequestDuelEvent;
import me.pmilon.RubidiaGuilds.events.GMemberClaimEvent;
import me.pmilon.RubidiaMonsters.events.MonsterSpawnEvent;
import me.pmilon.RubidiaMusics.MusicsPlugin;
import me.pmilon.RubidiaMusics.SongManager;
import me.pmilon.RubidiaMusics.SongManager.Song;
import me.pmilon.RubidiaMusics.WGUtils;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaWG.events.RegionEnteredEvent;
import me.pmilon.RubidiaWG.events.RegionLeftEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGFlagsListener implements Listener {

	private final WorldGuardPlugin wgPlugin;
	
	public WGFlagsListener(WorldGuardPlugin wgPlugin) {
		this.wgPlugin = wgPlugin;
	}
	
	@EventHandler
	public void onPVPDisallowed(DisallowedPVPEvent e){
		Player attacker = e.getAttacker();
		Player defender = e.getDefender();
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(defender.getWorld()).getApplicableRegions(defender.getLocation());

		RPlayer opponent = RPlayer.get(defender).getDuelOpponent();
		if(opponent != null){
			if(opponent.equals(RPlayer.get(attacker))){
				if(set.testState(null, Flags.DUELS)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDuelRequest(RPlayerRequestDuelEvent e){
		RPlayer rp = e.getRPlayer();
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(rp.getPlayer().getWorld()).getApplicableRegions(rp.getPlayer().getLocation());

		if(!set.testState(null, Flags.DUELS)){
			e.setCancelled(true);
			rp.sendMessage("§cYou cannot request a duel here.", "§cVous ne pouvez demander un duel ici.");
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Block block = e.getClickedBlock();
		
		if(block != null){
			ApplicableRegionSet set = wgPlugin.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
			List<Material> blocks = Arrays.asList(Material.CHEST, Material.HOPPER, Material.FURNACE, Material.ANVIL, Material.ENCHANTMENT_TABLE, Material.CAULDRON, Material.WORKBENCH, Material.DROPPER, Material.ENDER_PORTAL, Material.BEACON, Material.BREWING_STAND, Material.DISPENSER);
			if(blocks.contains(block.getType()) && !rp.isOp()){
				if(!set.testState(null, Flags.BLOCKS)){
					e.setCancelled(true);
					rp.sendMessage("§cYou can't interact with blocks here.", "§cVous ne pouvez intéragir avec les blocs ici.");
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e){
		Entity entity = e.getRightClicked();
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(player);

		if(!rp.isOp()){
			if(entity instanceof Vehicle){
				Pet pet = Pets.get(entity);
				ApplicableRegionSet set = wgPlugin.getRegionManager(entity.getWorld()).getApplicableRegions(entity.getLocation());
				if(pet != null){
					if(!pet.getOwner().equals(player)){
						if(!set.testState(null, Flags.RIDE)){
							e.setCancelled(true);
							rp.sendMessage("§cYou can only mount your pets here.", "§cVous ne pouvez monter que vos compagnons ici.");
						}
					}
				}else{
					if(!set.testState(null, Flags.RIDE)){
						e.setCancelled(true);
						rp.sendMessage("§cYou can only mount your pets here.", "§cVous ne pouvez monter que vos compagnons ici.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e){
		Block block = e.getBlock();
		if(block != null){
			if(block.getType().equals(Material.SOIL)){
				ApplicableRegionSet set = this.wgPlugin.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
				if(!set.testState(null, Flags.SOIL_TRAMPLING)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent e){
		Block block = e.getBlock();
		if(block != null){
			if(block.getType().equals(Material.SOIL)){
				ApplicableRegionSet set = this.wgPlugin.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
				if(!set.testState(null, Flags.SOIL_TRAMPLING)){
                    e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onRegionLeft(RegionLeftEvent e){
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(e.getPlayer());
		State claimFlag = e.getRegion().getFlag(Flags.CLAIM);
		String leaveFlag = e.getRegion().getFlag(Flags.LEAVE_COMMAND);
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());

		if(claimFlag != null){
			if(claimFlag.equals(State.DENY) && set.testState(null, Flags.CLAIM)){
				if(rp != null){
					rp.sendTitle(rp.translateString("§7§lWILDERNESS", "§7§lZONE SAUVAGE"), rp.translateString("§fUnclaimed territory", "§fTerritoire libre"), 5, 45, 10);
				}
			}
		}
		
		if(leaveFlag != null){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), leaveFlag.replaceAll("%player", player.getName()).replaceAll("%level", String.valueOf(rp.getRLevel())));
		}

		Location from = null;
		ApplicableRegionSet lastSet = null;
		switch(e.getMovementWay()){
		case MOVE:
			PlayerMoveEvent event = (PlayerMoveEvent)e.getParentEvent();
			from = event.getFrom();
			lastSet = this.wgPlugin.getRegionManager(from.getWorld()).getApplicableRegions(from);
			ProtectedRegion enterTitleRegion = WGUtils.getHighestPriorityWithFlag(set, Flags.ENTER_TITLE);
			if(enterTitleRegion != null){
				String enterTitle = enterTitleRegion.getFlag(Flags.ENTER_TITLE);
				ProtectedRegion lastEnterTitleRegion = WGUtils.getHighestPriorityWithFlag(lastSet, Flags.ENTER_TITLE);
				if(lastEnterTitleRegion != null){
					if(e.getRegion().equals(lastEnterTitleRegion)){
						String lastEnterTitle = lastEnterTitleRegion.getFlag(Flags.ENTER_TITLE);
						if(!lastEnterTitle.equals(enterTitle)){
							if(!enterTitle.isEmpty()){
								String[] titles = enterTitle.split("//");
								rp.sendTitle(titles[0], titles[1], 25, 60, 25);
							}
						}
					}
				}else{
					if(!enterTitle.isEmpty()){
						String[] titles = enterTitle.split("//");
						rp.sendTitle(titles[0], titles[1], 25, 60, 25);
					}
				}
			}else{
				ProtectedRegion lastLeaveTitleRegion = WGUtils.getHighestPriorityWithFlag(lastSet, Flags.LEAVE_TITLE);
				if(lastLeaveTitleRegion != null){
					if(e.getRegion().equals(lastLeaveTitleRegion)){
						String leaveTitle = lastLeaveTitleRegion.getFlag(Flags.LEAVE_TITLE);
						if(!leaveTitle.isEmpty()){
							String[] titles = leaveTitle.split("//");
							rp.sendTitle(titles[0], titles[1], 25, 60, 25);
						}
					}
				}
			}
			break;
		case TELEPORT:
			PlayerTeleportEvent event1 = (PlayerTeleportEvent)e.getParentEvent();
			from = event1.getFrom();
			lastSet = this.wgPlugin.getRegionManager(from.getWorld()).getApplicableRegions(from);
			break;
		default:
			break;
		}
		
		if(from != null){
			ProtectedRegion enterMusicRegion = WGUtils.getHighestPriorityWithFlag(set, Flags.MUSIC);
			if(enterMusicRegion != null){
				String enterMusic = enterMusicRegion.getFlag(Flags.MUSIC);
				Song enterSong = Song.getByName(enterMusic);
				if(enterSong != null){
					ProtectedRegion lastEnterMusicRegion = WGUtils.getHighestPriorityWithFlag(lastSet, Flags.MUSIC);
					if(lastEnterMusicRegion != null){
						if(e.getRegion().equals(lastEnterMusicRegion)){
							String lastEnterMusic = lastEnterMusicRegion.getFlag(Flags.MUSIC);
							Song lastEnterSong = Song.getByName(lastEnterMusic);
							if(lastEnterSong != null){
								if(!lastEnterSong.equals(enterSong)){
									SongManager.playSong(player, enterSong, (Location) MusicsPlugin.instance.getConfig().get(enterMusicRegion.getId()));
								}
							}
						}
					}else{
						SongManager.playSong(player, enterSong, (Location) MusicsPlugin.instance.getConfig().get(enterMusicRegion.getId()));
					}
				}
			}//else stop song ??
		}
	}
	
	@EventHandler
	public void onRegionEntered(RegionEnteredEvent e){
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(player);
		State claimFlag = e.getRegion().getFlag(Flags.CLAIM);
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		
		if(claimFlag != null){
			if(claimFlag.equals(State.DENY) && set.testState(null, Flags.CLAIM)){
				if(rp != null){
					rp.sendTitle(rp.translateString("§6§lPROTECTED ZONE", "§6§lREGION PROTEGEE"), rp.translateString("§eClaimed territory", "§eTerritoire conquis"), 5, 45, 10);
				}
			}
		}
		
		Location from = null;
		ApplicableRegionSet lastSet = null;
		switch(e.getMovementWay()){
		case MOVE:
			PlayerMoveEvent event = (PlayerMoveEvent)e.getParentEvent();
			from = event.getFrom();
			ProtectedRegion enterTitleRegion = WGUtils.getHighestPriorityWithFlag(set, Flags.ENTER_TITLE);
			lastSet = this.wgPlugin.getRegionManager(from.getWorld()).getApplicableRegions(from);
			
			if(enterTitleRegion != null){
				if(e.getRegion().equals(enterTitleRegion)){
					String enterTitle = enterTitleRegion.getFlag(Flags.ENTER_TITLE);
					ProtectedRegion lastEnterTitleRegion = WGUtils.getHighestPriorityWithFlag(lastSet, Flags.ENTER_TITLE);
					if(lastEnterTitleRegion != null){
						String lastEnterTitle = lastEnterTitleRegion.getFlag(Flags.ENTER_TITLE);
						if(!lastEnterTitle.equals(enterTitle)){
							if(!enterTitle.isEmpty()){
								String[] titles = enterTitle.split("//");
								rp.sendTitle(titles[0], titles[1], 25, 60, 25);
							}
						}
					}else{
						if(!enterTitle.isEmpty()){
							String[] titles = enterTitle.split("//");
							rp.sendTitle(titles[0], titles[1], 25, 60, 25);
						}
					}
				}
			}
			break;
		case TELEPORT:
			PlayerTeleportEvent event1 = (PlayerTeleportEvent)e.getParentEvent();
			from = event1.getFrom();
			lastSet = this.wgPlugin.getRegionManager(from.getWorld()).getApplicableRegions(from);
			break;
		case SPAWN:
			ProtectedRegion enterMusicRegion = WGUtils.getHighestPriorityWithFlag(set, Flags.MUSIC);
			if(enterMusicRegion != null){
				if(e.getRegion().equals(enterMusicRegion)){
					String enterMusic = enterMusicRegion.getFlag(Flags.MUSIC);
					Song enterSong = Song.getByName(enterMusic);
					if(enterSong != null){
						SongManager.playSong(player, enterSong, (Location) MusicsPlugin.instance.getConfig().get(enterMusicRegion.getId()));
					}
				}
			}
			break;
		default:
			break;
		}
		
		if(from != null){
			ProtectedRegion enterMusicRegion = WGUtils.getHighestPriorityWithFlag(set, Flags.MUSIC);
			if(enterMusicRegion != null){
				if(e.getRegion().equals(enterMusicRegion)){
					String enterMusic = enterMusicRegion.getFlag(Flags.MUSIC);
					Song enterSong = Song.getByName(enterMusic);
					if(enterSong != null){
						ProtectedRegion lastEnterMusicRegion = WGUtils.getHighestPriorityWithFlag(lastSet, Flags.MUSIC);
						if(lastEnterMusicRegion != null){
							String lastEnterMusic = lastEnterMusicRegion.getFlag(Flags.MUSIC);
							Song lastEnterSong = Song.getByName(lastEnterMusic);
							if(lastEnterSong != null){
								if(!lastEnterSong.equals(enterSong)){
									SongManager.playSong(player, enterSong, (Location) MusicsPlugin.instance.getConfig().get(enterMusicRegion.getId()));
								}
							}
						}else{
							SongManager.playSong(player, enterSong, (Location) MusicsPlugin.instance.getConfig().get(enterMusicRegion.getId()));
						}
					}
				}
			}//else stop song ??
		}
	}

	@EventHandler
	public void onGuildClaim(GMemberClaimEvent e){
		Player p = e.getGMember().getPlayer();
		int x = e.getClaim().getX() << 4;
		int z = e.getClaim().getZ() << 4;
		ProtectedCuboidRegion region = new ProtectedCuboidRegion("temp", new BlockVector(x, 0, z), new BlockVector(x + 15, 256, z + 15));
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(p.getWorld()).getApplicableRegions(region);
		if(!set.testState(null, Flags.CLAIM)){
			e.setCancelled(true);
			RPlayer.get(p).sendMessage("§cYou cannot claim here.", "§cVous ne pouvez conquérir ce territoire.");
		}
	}

	@EventHandler
	public void onMonsterSpawn(MonsterSpawnEvent event){
		ApplicableRegionSet set = this.wgPlugin.getRegionManager(event.getLocation().getWorld()).getApplicableRegions(event.getLocation());
		if(!set.testState(null, Flags.NATURAL_SPAWN)){
  	    	event.setCancelled(true);
		}
	}

	@EventHandler
	public void onAbility(RPlayerAbilityEvent event){
		RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			if(!rp.isInDuel()){
				Player player = rp.getPlayer();
				ApplicableRegionSet set = RubidiaWGPlugin.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
				if(!set.testState(null, Flags.SKILLS) && !rp.isOp()){
					event.setCancelled(true);
					rp.sendActionBar("§4§lHey! §cYou cannot cast skills here!", "§4§lHey ! §cVous ne pouvez utiliser de compétences ici !");
				}
			}
		}
	}
}
