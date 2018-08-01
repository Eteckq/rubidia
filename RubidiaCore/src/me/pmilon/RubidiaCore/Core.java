package me.pmilon.RubidiaCore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import me.pmilon.RubidiaCore.RChat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RChat.RChatListener;
import me.pmilon.RubidiaCore.REvents.Event;
import me.pmilon.RubidiaCore.REvents.Events;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.RPlayerColl;
import me.pmilon.RubidiaCore.abilities.Abilities;
import me.pmilon.RubidiaCore.abilities.AbilitiesAPI;
import me.pmilon.RubidiaCore.aeroplane.AeroplaneListener;
import me.pmilon.RubidiaCore.chairs.ChairListener;
import me.pmilon.RubidiaCore.commands.BienvenueCommandExecutor;
import me.pmilon.RubidiaCore.commands.BoostersCommandExecutor;
import me.pmilon.RubidiaCore.commands.ChatCommandExecutor;
import me.pmilon.RubidiaCore.commands.ClassCommandExecutor;
import me.pmilon.RubidiaCore.commands.CoupleCommandExecutor;
import me.pmilon.RubidiaCore.commands.EventsCommandExecutor;
import me.pmilon.RubidiaCore.commands.HMPCommandExecutor;
import me.pmilon.RubidiaCore.commands.HelpCommandExecutor;
import me.pmilon.RubidiaCore.commands.ItemCommandExecutor;
import me.pmilon.RubidiaCore.commands.LevelCommandExecutor;
import me.pmilon.RubidiaCore.commands.MarryCommandExecutor;
import me.pmilon.RubidiaCore.commands.MoneyCommandExecutor;
import me.pmilon.RubidiaCore.commands.MuteCommandExecutor;
import me.pmilon.RubidiaCore.commands.ProfileCommandExecutor;
import me.pmilon.RubidiaCore.commands.RPlayersCommandExecutor;
import me.pmilon.RubidiaCore.commands.RankingsCommandExecutor;
import me.pmilon.RubidiaCore.commands.RebootCommandExecutor;
import me.pmilon.RubidiaCore.commands.SKDCommandExecutor;
import me.pmilon.RubidiaCore.commands.SKPCommandExecutor;
import me.pmilon.RubidiaCore.commands.ScrollCommandExecutor;
import me.pmilon.RubidiaCore.commands.StatisticsCommandExecutor;
import me.pmilon.RubidiaCore.commands.VIPCommandExecutor;
import me.pmilon.RubidiaCore.commands.VanishCommandExecutor;
import me.pmilon.RubidiaCore.commands.VoteCommandExecutor;
import me.pmilon.RubidiaCore.couples.Couple;
import me.pmilon.RubidiaCore.couples.Couples;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.duels.RDuelListener;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.handlers.EntityHandler;
import me.pmilon.RubidiaCore.handlers.GamePlayEffectsHandler;
import me.pmilon.RubidiaCore.handlers.HealthBarHandler;
import me.pmilon.RubidiaCore.handlers.JobsHandler;
import me.pmilon.RubidiaCore.handlers.PlaymodeHandler;
import me.pmilon.RubidiaCore.handlers.RLevelHandler;
import me.pmilon.RubidiaCore.handlers.ResourcePackHandler;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerPlayerListHeaderFooter;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerWindowItems;
import me.pmilon.RubidiaCore.ranks.Ranks;
import me.pmilon.RubidiaCore.ritems.backpacks.BackPacks;
import me.pmilon.RubidiaCore.ritems.general.ItemListener;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.general.RItemStack;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponsListener;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.scrolls.Scrolls;
import me.pmilon.RubidiaCore.tags.NameTags;
import me.pmilon.RubidiaCore.tags.TagStandListener;
import me.pmilon.RubidiaCore.tags.TagStandManager;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.AnvilUI;
import me.pmilon.RubidiaCore.ui.BackpackUI;
import me.pmilon.RubidiaCore.ui.DistinctionsMenu;
import me.pmilon.RubidiaCore.ui.EnchantmentUI;
import me.pmilon.RubidiaCore.ui.EnderChest;
import me.pmilon.RubidiaCore.ui.PlayerMenu;
import me.pmilon.RubidiaCore.ui.PrefsUI;
import me.pmilon.RubidiaCore.ui.SPlayerManager;
import me.pmilon.RubidiaCore.ui.SPlayerSelectionMenu;
import me.pmilon.RubidiaCore.ui.SkillTree;
import me.pmilon.RubidiaCore.ui.managers.UIManager;
import me.pmilon.RubidiaCore.ui.weapons.WeaponsUI;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.JSONUtils;
import me.pmilon.RubidiaCore.utils.LevelUtils;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaPets.PetsPlugin;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaWG.Flags;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;

public class Core extends JavaPlugin implements Listener {
	
	public static ConsoleCommandSender console;
	
	public static WorldEditPlugin we;
	public static WorldGuardPlugin wg;
	
	public static UIManager uiManager;
	public static HealthBarHandler barHandler;
	public static ItemMessage itemMessage;
	public static RPlayerColl rcoll;
	
	public static FileConfiguration playerConfig = null;
	public static File playerConfigFile = null;
	public static FileConfiguration backpackConfig = null;
	public static File backpackConfigFile = null;
	public static FileConfiguration cityConfig = null;
	public static File cityConfigFile = null;
	public static FileConfiguration database = null;
	public static File databaseFile = null;
	public static FileConfiguration pathConfig = null;
	public static File pathConfigFile = null;
	public static FileConfiguration weaponConfig = null;
	public static File weaponConfigFile = null;
	public static FileConfiguration coupleConfig = null;
	public static File coupleConfigFile = null;
	
	public static Core instance;
	public HashMap<Player, Block> anvil = new HashMap<Player, Block>();
	public static List<Player> drunk = new ArrayList<Player>();
	public static List<Player> glitch = new ArrayList<Player>();
	public static HashMap<Player, List<BukkitTask>> pesanteur = new HashMap<Player, List<BukkitTask>>();
	public static List<Material> pesTypes = Arrays.asList(Material.LADDER, Material.LAVA, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.WATER);
	public static boolean restarting = false;
	private static int playersMax = 1;

	////////////////////////////////////////
	//        MAIN PLUGIN BEHAVIOR        //
	////////////////////////////////////////
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent e){
		final Player p = e.getPlayer();
		final String s = p.getName();
		final boolean gamemode;
		
		if(Configs.getDatabase().getBoolean("maintenancemode")){
			if(!p.isOp()){
				p.kickPlayer(RPlayer.get(p).translateString("§4MAINTENANCE MODE: §cCome back later!", "§4MODE MAINTENANCE: §cRevenez plus tard !"));
				return;
			}
		}
		
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		
		final RPlayer rp;
		if(!rcoll.contains(p)){
			rp = rcoll.addDefault(p.getUniqueId().toString());
			if(Bukkit.getWorld("Tutorial") != null)rp.connectionLocation = Bukkit.getWorld("Tutorial").getSpawnLocation();
			new BukkitTask(this){
				public void run(){
					Bukkit.broadcastMessage("§2§lBIENVENUE §a§l" + s + " §2§lSUR RUBIDIA");
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(1);
			p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
			p.getInventory().addItem(new ItemStack(Material.EMERALD, 20));
			p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 4));
			Scrolls.newScroll(p, ScrollType.WILDTP, "");
			for(RPlayer rpp : rcoll.data()){
				if(!rpp.equals(rp)){
					rpp.lastWelcome = rp;
				}
			}
		}else rp = RPlayer.get(p);
		RPlayer.getOnlines().add(rp);
		rp.setPlayer(p);
		rp.setName(p.getName());
		gamemode = rp.isOp();
		
		new BukkitTask(this){
			public void run(){
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(rp.getMaxHealth());
				rp.refreshRLevelDisplay();
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);

    	WrapperPlayServerPlayerListHeaderFooter packet = new WrapperPlayServerPlayerListHeaderFooter();
    	packet.setHeader(WrappedChatComponent.fromText("§a§l§m-------------------------------§r\n§oBienvenue sur Rubidia !§r\n§a§l§m-------------------------------§r"));
    	packet.setFooter(WrappedChatComponent.fromText("§c§l§m-------------------------------§r\n§lhttp://www.rubidia.xyz§r\n§c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r"));
        packet.sendPacket(p);
		
		new BukkitTask(this){
			public void run(){
				PetsPlugin.eventsHandler.onPlayerJoin(e);
				for(RPlayer rpo : RPlayer.getOnlines()){
					if(rpo.knows(rp)){
						if(rpo.getNotifOnFriendJoin()){
							rpo.getPlayer().playSound(rpo.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10F);
							rpo.sendMessage("§e" + s + " §ahas just joined the game!", "§e" + s + " §avient de se connecter !");
						}
					}
				}

				NameTags.update();

	    		if(!rp.isOp() && rp.getBalance() > 120*(rp.getRLevel()+1)){
					for(RPlayer rpo : RPlayer.getOnlines()){
						if(rpo.isOp()){
							rpo.sendMessage("§4" + rp.getName() + " §chas too many emeralds for his level (§4" + rp.getBalance() + "§c)...","§4" + rp.getName() + " §ca trop d'émeraudes pour son niveau (§4" + rp.getBalance() + "§c)...");
						}
					}
	    		}
	    		
	    		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
				if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
					rp.sendTitle("§5§lSAMED'XP DE FOLIE", "§dXP ×2", 0, 100, 40);
				}else if(!Events.currentEvents.isEmpty()){
					for(Event event : Events.currentEvents){
						if(event.isActive()){
							rp.sendTitle("§5§lEVENEMENT EN COURS", "§d" + event.getSubtitle(), 0, 100, 40);
						}
					}
				}
				
				if(gamemode)p.setGameMode(GameMode.CREATIVE);
				
				new BukkitTask(this.getPlugin()){
					public void run(){
						if(rp.connectionLocation == null)rp.connectionLocation = p.getLocation();
						p.teleport(p.getWorld().getSpawnLocation());
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1, true, false), true);
						//rp.sendMessage("§eYou need to accept our resource pack in order to play.", "§eJouer nécessite l'installation de notre resource pack.");
						rp.updateResourcePack();
						if(!rp.isProfileUpdated())rp.sendMessage("§dPlease update your player profile: §l/profile", "§dMettez à jour votre profil de joueur : §l/profile");
						
						if(!rp.isVip() && rp.getLastLoadedSPlayerId() == 3){
							Core.uiManager.requestUI(new SPlayerSelectionMenu(p));
						}
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(20);
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(4);

		if(rp.isVanished()){
			rp.sendMessage("§7§oYou are vanished!","§7§oVous êtes invisible !");
			for(RPlayer rpp : RPlayer.getOnlines()){
				if(!rpp.isOp() && !rpp.equals(rp))rpp.getPlayer().hidePlayer(p);
			}
		}
		
		if(RPlayer.getOnlines().size() > playersMax){
			playersMax = RPlayer.getOnlines().size();
		}
		
		p.setWalkSpeed(1.0F);//to reset speed
		
		RChatListener.onPlayerJoin(e, rp);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);

		if(rp != null){
			if(Smiley.isSmileying(p))rp.smileyTask.run();
			rp.getLoadedSPlayer().setLoaded(false);
			rp.setLastConnectionDate(System.currentTimeMillis());
			RPlayer.getOnlines().remove(rp);
			rp.setPlayer(null);
		}
		
		if(p.getVehicle() != null){
			p.leaveVehicle();
		}

		for(Objective objective : p.getScoreboard().getObjectives()){
			objective.unregister();
		}
		for(Team team : p.getScoreboard().getTeams()){
			team.unregister();
		}
		
		RChatListener.onPlayerQuit(e, rp);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e){
		final Player p = e.getEntity();
		RPlayer rp = RPlayer.get(p);
		e.setKeepLevel(true);
		e.setKeepInventory(true);
		e.setDroppedExp(0);
		e.getDrops().clear();
		
		RPlayer killer = null;
		if(p.getKiller() != null)killer = RPlayer.get(p.getKiller());
		RPlayerDeathEvent event = new RPlayerDeathEvent(e, rp, killer, new ArrayList<ItemStack>(), p.getInventory().getContents(), p.getInventory().getArmorContents());
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			rp = event.getRPlayer();
			if(Smiley.isSmileying(p))rp.smileyTask.run();
			if(!rp.isInDuel()){
				if(!event.isKeepInventory()){
					ItemStack[] contents = event.getRPlayer().getPlayer().getInventory().getContents();
					for(int i = 0;i < contents.length;i++){
						if(i == 8 || i == 17 && event.getRPlayer().getRClass().equals(RClass.RANGER))continue;
						if(contents[i] != null){
							if(event.getInventoryDrops().length > i){
								if(contents[i].equals(event.getInventoryDrops()[i])){
									event.getDrops().add(contents[i]);
									contents[i] = null;
								}
							}
						}
					}
					event.getRPlayer().getPlayer().getInventory().setContents(contents);
					ItemStack[] contents1 = p.getInventory().getArmorContents();
					for(int i = 0;i < contents1.length;i++){
						if(contents1[i] != null){
							if(event.getArmorDrops().length > i){
								if(contents1[i].equals(event.getArmorDrops()[i])){
									event.getDrops().add(contents1[i]);
									contents1[i] = null;
								}
							}
						}
					}
					event.getRPlayer().getPlayer().getInventory().setArmorContents(contents1);
					for(ItemStack item : event.getDrops()){
						if(!item.getType().equals(Material.AIR)){
							event.getRPlayer().getPlayer().getWorld().dropItemNaturally(event.getRPlayer().getPlayer().getLocation(), item);
						}
					}
				}
				if(event.getKiller() != null)event.getKiller().setKills(event.getKiller().getKills()+1);
			}
			
			rp.setLastCombat(System.currentTimeMillis() - 4444*1000);
		}else{
			event.getRPlayer().getPlayer().setHealth(event.getRPlayer().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
		}

		event.getSuperEvent().setDeathMessage("");//to avoid custom chat bugs >> see RChatListener
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRPlayerDeath(RPlayerDeathEvent event){
		final RPlayer rp = event.getRPlayer();
		if(!event.isCancelled()){
			if(rp.isOnline()){
				final Player p = rp.getPlayer();
				
				if(!rp.isInDuel()){
					if(rp.getResurrectionTask() == null){
						for(ItemStack item : p.getInventory().getContents()){
							if(item != null){
								RItem rItem = new RItem(item);
								if(rItem.isScroll()){
									if(rItem.getScroll().getType().equals(ScrollType.RESURRECTION)){
										event.setCancelled(true);
										p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 180, 255, true, false), true);
										p.setHealth(.01);
										p.teleport(p.getLocation().add(0,.3,0));
										p.setAllowFlight(true);
										p.setFlying(true);
										p.setFlySpeed(0F);
										p.setWalkSpeed(0);
										p.setVelocity(new Vector(0,0,0));
										for(Player player : Bukkit.getOnlinePlayers()){
											player.hidePlayer(p);
										}
										p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 1);
										rp.setResurrectionTask(new BukkitTask(this){
											int step = 8;

											@Override
											public void run() {
												p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 5, .8F);
												rp.sendTitle(rp.translateString("§6Use your resurrection scroll!", "§6Utilisez votre parchemin de résurrection !"), (step > 3 ? "§e" : (step > 1 ? "§c" : "§4")) + step + "...", 0, 25, 0);
												step--;
											}

											@Override
											public void onCancel() {
												if(step == 0)p.damage(4444.0);
											}
											
										}.runTaskTimerCancelling(0, 20, 160));
										break;
									}
								}
							}
						}
					}else{
						rp.sendTitle("", "", 0, 1, 0);
						p.setFlying(false);
						p.setAllowFlight(false);
						p.setFlySpeed(.1F);
						p.setWalkSpeed(.2F);
						for(Player player : Bukkit.getOnlinePlayers()){
							player.showPlayer(p);
						}
						p.removePotionEffect(PotionEffectType.BLINDNESS);
						rp.getResurrectionTask().cancel();
						rp.setResurrectionTask(null);
						if(p.getKiller() != null){
							RPlayer rpp = RPlayer.get(p.getKiller());
							rpp.setKills(rpp.getKills()+1);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event){
		if(!event.isFlying()){
			if(RPlayer.get(event.getPlayer()).getResurrectionTask() != null){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		
		if(p.getInventory().contains(Material.STORAGE_MINECART)){
			int amount = 0;
			for(int slot = 0;slot < 36;slot++){
				if(p.getInventory().getItem(slot) != null){
					if(p.getInventory().getItem(slot).getType().equals(Material.STORAGE_MINECART)){
						if(p.getInventory().getItem(slot).hasItemMeta()){
							if(p.getInventory().getItem(slot).getItemMeta().getDisplayName().contains("§6BackPack") || p.getInventory().getItem(slot).getItemMeta().getDisplayName().contains("§6Sac à dos"))amount += 1;
							if(amount > 1){
								p.getWorld().dropItem(p.getLocation(), p.getInventory().getItem(slot));
								p.getInventory().remove(p.getInventory().getItem(slot));
								rp.sendMessage("§cYou cannot carry out more than one Backpack!", "§cVous ne pouvez transporter plus d'un Sac à dos !");
							}
						}
					}
				}
			}
		}
		
		if(drunk.contains(p)){
			Random r = new Random();
			Vector v = new Vector(r.nextDouble()*2-1, 0, r.nextDouble()*2-1);
			p.setVelocity(v.normalize().multiply(.23));
		}
		
		if(p.getWorld().getEnvironment().equals(Environment.NETHER)){
			if(!pesTypes.contains(p.getLocation().getBlock().getType()) && !pesTypes.contains(p.getEyeLocation().getBlock().getType()) && !p.isGliding() && !p.isFlying() && !((Entity)p).isOnGround()){
				if(e.getTo().getY() != e.getFrom().getY()){
					boolean walking = e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ();
					if(!pesanteur.containsKey(p)){
						int k = 0;
						final double factor = p.isSprinting() ? .3 : walking ? .225 : .15;
						List<BukkitTask> tasks = new ArrayList<BukkitTask>();
						final Vector base = p.getEyeLocation().getDirection().normalize().multiply(factor);
						if(e.getTo().getY() > e.getFrom().getY()){
							p.setVelocity(base.clone().setY(.72));
							for(double i = .25;i >= 0;i-=.01){
								final double j = i;
								tasks.add(new BukkitTask(this){

									@Override
									public void run() {
										if(!p.getLocation().subtract(0,.02,0).getBlock().getType().isSolid())p.setVelocity(base.add(p.getEyeLocation().getDirection().normalize().multiply(.02)).normalize().multiply(factor).setY(j));
										else{
											if(!pesanteur.containsKey(p)){
												for(BukkitTask task : pesanteur.get(p)){
													task.cancel();
												}
											}
										}
									}

									@Override
									public void onCancel() {
									}
									
								}.runTaskLater(k));
								k++;
							}
						}
						for(double i = 0;i >= -.25;i-=.01){
							final double j = i;
							tasks.add(new BukkitTask(this){

								@Override
								public void run() {
									if(!p.getLocation().subtract(0,.02,0).getBlock().getType().isSolid())p.setVelocity(base.add(p.getEyeLocation().getDirection().normalize().multiply(.03)).normalize().multiply(factor).setY(j));
									else{
										if(!pesanteur.containsKey(p)){
											for(BukkitTask task : pesanteur.get(p)){
												task.cancel();
											}
										}
									}
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(k));
							k++;
						}
						tasks.add(new BukkitTask(this){

							@Override
							public void run() {
								pesanteur.remove(p);
							}

							@Override
							public void onCancel() {
								pesanteur.remove(p);
							}
							
						}.runTaskLater(k));
						pesanteur.put(p, tasks);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ie){
		final Player p = (Player)ie.getWhoClicked();
		final RPlayer rp = RPlayer.get(p);

		int slot = ie.getSlot();//do not put raw slot
		ItemStack is = ie.getCurrentItem();
		if(is != null){
			RItem rItem = new RItem(is);
			if(ie.getClickedInventory().getType().equals(InventoryType.CRAFTING)){
				if(is.getType().equals(Material.BOOKSHELF)){
					ItemStack skt = ie.getCurrentItem();
					if(skt.hasItemMeta()){
						if(skt.getItemMeta().getDisplayName().contains("Character menu") || skt.getItemMeta().getDisplayName().contains("Menu du personnage")){
							ie.setCancelled(true);
							Core.uiManager.requestUI(new SPlayerManager(p));
						}
					}
				}
			}else if(p.getOpenInventory().getTopInventory().getType().equals(InventoryType.ANVIL)){
				if(rItem.isScroll() || rItem.isCustom() || rItem.isWeapon() || rItem.isBackPack()){
					ie.setCancelled(true);
					rp.sendMessage("§cYou cannot work this item!", "§cVous ne pouvez travailler cet item !");
				}
				
				if(ie.getClickedInventory().getType().equals(InventoryType.ANVIL)){
					if(ie.isShiftClick()){
						ie.setCancelled(true);
					}else{
						if(ie.getRawSlot() == 2){
							if(ie.getCurrentItem() != null){
								if(!ie.getCurrentItem().getType().equals(Material.AIR)){
									ie.setCancelled(true);
									ItemStack is_0 = ie.getClickedInventory().getItem(0);
									ItemStack is_1 = ie.getClickedInventory().getItem(1);
									ItemStack is_2 = ie.getClickedInventory().getItem(2);
									ie.getClickedInventory().setItem(0, new ItemStack(Material.AIR, 1));
									ie.getClickedInventory().setItem(1, new ItemStack(Material.AIR, 1));
									p.closeInventory();
									RItem rItem1 = new RItem(is_0);
									if(rItem1.isWeapon()){
										Weapon weapon = rItem1.getWeapon();
										ItemMeta meta = is_2.getItemMeta();
										meta.setDisplayName(weapon.getRarity().getPrefix() + "§l" + weapon.getName() + (weapon.getSuppLevel() > 0 ? " §7(+" + weapon.getSuppLevel() + ")" : ""));
										is_2.setItemMeta(meta);
									}
									uiManager.requestUI(new AnvilUI(p, anvil.get(p), is_0, is_1, is_2));
								}
							}
						}
					}
				}
			}
			
			if(ie.getSlotType().equals(SlotType.ARMOR) && ie.getCurrentItem().getType().equals(Material.SKULL_ITEM) && Smiley.isSmileying(p))ie.setCancelled(true);
		}
		if(!rp.isOp() && ie.getClickedInventory() != null){
			if(ie.getAction().equals(InventoryAction.HOTBAR_SWAP)){
				if(ie.getCursor() != null){
					ItemStack item = ie.getCursor();
					if(item.getType().equals(Material.STAINED_GLASS_PANE) && item.getDurability() == 15 && item.hasItemMeta()){
						ItemMeta meta = item.getItemMeta();
						if(meta.hasDisplayName()){
							if(meta.getDisplayName().equals("nrj")){
								ie.setCancelled(true);
							}
						}
					}
				}
			}else if(ie.getClickedInventory().equals(p.getOpenInventory().getBottomInventory())){
				if(rp.getRClass().equals(RClass.RANGER) && slot == 17 || slot == 8){
					ie.setCancelled(true);
				}
			}
		}
		if(!rp.isOp())Utils.updateInventory(p);
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event){
		Player player = (Player) event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		new BukkitTask(this){

			@Override
			public void run() {
				rp.updateNrj();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(0);
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		Player player = (Player) event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		new BukkitTask(this){

			@Override
			public void run() {
				rp.updateNrj();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(0);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("suggests")){
			List<String> suggestlist = Configs.getDatabase().getStringList("suggests");
			String[] suggests = new String[suggestlist.size()];
			suggestlist.toArray(suggests);
			sender.sendMessage("§6.---[ SUGGESTS ]---.");
			sender.sendMessage(suggests);
		}else if(cmd.getName().equalsIgnoreCase("rubis")){
			if(sender.isOp()){
				if(args.length > 1){
					RPlayer rp = RPlayer.getFromName(args[0]);
					if(rp != null){
						if(Utils.isInteger(args[1])){
							int amount = Integer.valueOf(args[1]);
							rp.setPendingRubis(rp.getPendingRubis()+amount);
							if(rp.isOnline()){
								rp.sendMessage("§aYou received §e" + amount + " §arubis spendable in the website's shop!", "§aVous avez reçu §e" + amount + " §arubis utilisables dans la boutique du site !");
								rp.getPlayer().sendMessage("§2§l>>>    §7http://www.rubidia.xyz/shop/");
								rp.sendMessage("§e§o(Please log in again to update your account)", "§e§o(Reconnectez-vous sur le site pour mettre à jour votre compte)");
							}
							sender.sendMessage("§2" + rp.getName() + " §areceived §e" + amount + " §arubis.");
						}else sender.sendMessage("§cPlease use /rubis " + args[0] + " [amount]");
					}else sender.sendMessage("§4" + args[0] + " §ccouldn't be found.");
				}else sender.sendMessage("§cPlease use /rubis [player] [amount]");
			}else sender.sendMessage("§cYou really thought you could do that without being Operator?");
		}else if(cmd.getName().equalsIgnoreCase("load")){
			if(sender.isOp()){
				if(args.length > 0){
					if(args[0].equals("abilities")){
						this.reloadConfig();
						AbilitiesAPI.loadAbilities(this);
						sender.sendMessage("§6Abilities reloaded!");
					}
				}else sender.sendMessage("§cPlease use /load [abilities]");
			}else sender.sendMessage("§cYou really thought you could do that without being Operator!");
		}else if(cmd.getName().equalsIgnoreCase("maintenance")){
			if(sender.isOp()){
				if(Configs.getDatabase().getBoolean("maintenancemode")){
					Configs.getDatabase().set("maintenancemode", false);
					Configs.getDatabase().set("maintenancemodemessage", null);
					sender.sendMessage("§4MAINTENACE MODE §cDISABLED");
				}else{
					Configs.getDatabase().set("maintenancemode", true);
					if(args.length > 0){
						String s = "";
						for(int i = 0;i < args.length;i++){
							if(i == (args.length-1))s += args[i];
							else s += args[i] + " ";
						}
						Configs.getDatabase().set("maintenancemodemessage", s);
					}
					sender.sendMessage("§4MAINTENACE MODE §aENABLED");
					for(Player p : Bukkit.getOnlinePlayers()){
						if(!p.isOp()){
							RPlayer rp = RPlayer.get(p);
							rp.sendTitle(rp.translateString("§4MAINTENANCE MODE", "§4MODE MAINTENANCE"), rp.translateString("§cYou will be kicked in 30 seconds", "§cVous serez expulsé dans 30 secondes"), 0, 150, 5);
						}
					}
					Bukkit.getScheduler().runTaskLater(this, new Runnable(){
						public void run(){
							if(Configs.getDatabase().getBoolean("maintenancemode")){
								for(Player p : Bukkit.getOnlinePlayers()){
									if(!p.isOp()){
										RPlayer rp = RPlayer.get(p);
										p.kickPlayer(rp.translateString("§4MAINTENANCE MODE: §cCome back later!", "§4MODE MAINTENANCE: §cRevenez plus tard !"));
									}
								}
							}
						}
					}, 30*20);
				}
				Bukkit.getServer().setWhitelist(Configs.getDatabase().getBoolean("maintenancemode"));
			}else sender.sendMessage("§cYou really thought you could do that without being Operator!");
		}else if(cmd.getName().equalsIgnoreCase("tp")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				if(args.length < 1){
					if(TeleportHandler.invoke_tasks.containsKey(player)){
						BukkitTask.tasks.get(TeleportHandler.invoke_tasks.get(player)).cancel();
						return true;
					}
					if(TeleportHandler.tp_tasks.containsKey(player)){
						BukkitTask.tasks.get(TeleportHandler.tp_tasks.get(player)).cancel();
						return true;
					}
				}
			}
			if(sender.isOp()){
				if(sender instanceof Player){
					Player p = (Player)sender;
					RPlayer rp = RPlayer.get(p);
					if(args.length == 1){
						if(Bukkit.getPlayer(args[0]) != null){
							TeleportHandler.teleport(p, Bukkit.getPlayer(args[0]).getLocation());
						}else{
							rp.sendMessage("§4" + args[0] + " §cmust be online!", "§4" + args[0] + " §cdoit être en ligne !");
						}
					}else if(args.length == 2){
						if(Bukkit.getPlayer(args[0]) != null){
							if(Bukkit.getPlayer(args[1]) != null){
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]).getLocation());
							}else{
								rp.sendMessage("§4" + args[1] + " §cmust be online!", "§4" + args[1] + " §cdoit être en ligne !");
							}
						}else{
							rp.sendMessage("§4" + args[0] + " §cmust be online!", "§4" + args[0] + " §cdoit être en ligne !");
						}
					}else if(args.length == 3){
						try{
							TeleportHandler.teleport(p, new Location(p.getWorld(), Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), p.getEyeLocation().getYaw(), p.getEyeLocation().getPitch()));
						}catch (Exception e){
							rp.sendMessage("§cYou did not give a valid location!", "§cVous n'avez pas donné une location exacte !");
						}
					}else if(args.length == 4){
						try{
							if(Bukkit.getPlayer(args[0]) != null){
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]), new Location(p.getWorld(), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Bukkit.getPlayer(args[0]).getEyeLocation().getYaw(), Bukkit.getPlayer(args[0]).getEyeLocation().getPitch()));
							}else{
								rp.sendMessage("§4" + args[0] + " §cmust be online!", "§4" + args[0] + " §cdoit être en ligne !");
							}
						}catch (Exception e){
							rp.sendMessage("§cYou did not give a valid location!", "§cVous n'avez pas donné une location exacte !");
						}
					}else{
						rp.sendMessage("§cPlease use /tp <Target> | <Player> <Target> | <x y z> | <Player> <x y z (Target)>", "§cUtilisez /tp <Cible> | <Joueur> <Cible> | <x y z> | <x y z (Cible)>");
					}
				}else{
					if(args.length == 2){
						if(Bukkit.getPlayer(args[0]) != null){
							if(Bukkit.getPlayer(args[1]) != null){
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]).getLocation());
							}else{
								sender.sendMessage("§4" + args[1] + " §cmust be online!");
							}
						}else{
							sender.sendMessage("§4" + args[0] + " §cmust be online!");
						}
					}else if(args.length == 4){
						try{
							if(Bukkit.getPlayer(args[0]) != null){
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]), new Location(Bukkit.getPlayer(args[0]).getWorld(), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Bukkit.getPlayer(args[0]).getEyeLocation().getYaw(), Bukkit.getPlayer(args[0]).getEyeLocation().getPitch()));
							}else{
								sender.sendMessage("§4" + args[0] + " §cmust be online!");
							}
						}catch (Exception e){
							sender.sendMessage("§cYou did not give a valid location!");
						}
					}else{
						sender.sendMessage("§cPlease use /tp <Target> | <Player> <Target> | <x y z> | <Player> <x y z (Target)>");
					}
				}
			}else{
				sender.sendMessage("§cYou really thought you could do that without being Operator!");
			}
		}else if(cmd.getName().equalsIgnoreCase("top")){
			sender.sendMessage("§6Allocated memory: §a" + Math.round((float)(Runtime.getRuntime().maxMemory())/1000) + " MB");
			sender.sendMessage("§6Used memory: §a" + Math.round((float)(Runtime.getRuntime().freeMemory())/1000) + " MB §e(" + Math.round(((float)(Runtime.getRuntime().freeMemory())/Runtime.getRuntime().maxMemory())*100) + "%)");
		}
		
		if(sender instanceof Player){
			final Player p = (Player)sender;
			final RPlayer rp = RPlayer.get(p);
			if(Core.uiManager.isInTempSession(p) && !rp.isOp()){
				rp.sendMessage("§cYou cannot do this while you are in edition mode!", "§cVous ne pouvez faire ça tant que vous êtes en mode d'édition !");
				return true;
			}
			if(rp.getResurrectionTask() != null && !rp.isOp()){
				rp.sendMessage("§cYou cannot do this while you are resurrecting!", "§cVous ne pouvez faire ça tant que vous vous ressuscitez !");
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("skilltree")){
				uiManager.requestUI(new SkillTree(p));
			}else if(cmd.getName().equalsIgnoreCase("spawn")){
				TeleportHandler.startTeleportation(p, Bukkit.getWorlds().get(0).getSpawnLocation(), new RTeleportCause(RTeleportType.DELAYED_TELEPORTATION, null, null,null));
			}else if(cmd.getName().equalsIgnoreCase("nremove")){
				p.setMetadata("removingEntity", new FixedMetadataValue(this, true));
				rp.sendMessage("§cYou will remove next hit entity.", "§cVous supprimerez la prochaine entité touchée.");
			}else if(cmd.getName().equalsIgnoreCase("playmode")){
				if(p.isOp()){
					if(p.getGameMode().equals(GameMode.SURVIVAL)){
						PlaymodeHandler.savePlaymodeSurvivalInventory(p);
						p.setGameMode(GameMode.CREATIVE);
						rp.sendMessage("§eYou are now in Admin mode!", "§eVous êtes désormais en mode Admin !");
						p.getInventory().clear();
						PlaymodeHandler.setPlaymodeCreativeInventory(p);
					}else if(p.getGameMode().equals(GameMode.CREATIVE)){
						PlaymodeHandler.savePlaymodeCreativeInventory(p);
						p.setGameMode(GameMode.SURVIVAL);
						rp.sendMessage("§eYou are now in Player mode!", "§eVous êtes désormais en mode Joueur !");
						p.getInventory().clear();
						PlaymodeHandler.setPlaymodeSurvivalInventory(p);
					}
				}else{
					rp.sendMessage("§cOnly OPs can do this command!", "§cCette commande est réservée aux admins !");
				}
			}else if(cmd.getName().equalsIgnoreCase("suggest")){
				if(args.length > 0){
					String suggest = "";
					for(int a = 0;a < args.length;a++){
						if(a != ((args.length)-1)){
							suggest += args[a] + " ";
						}else{
							suggest += args[a];
						}
					}
					if(Configs.getDatabase().contains("suggests")){
						List<String> suggests = Configs.getDatabase().getStringList("suggests");
						suggests.add(p.getName() + ": " + suggest);
						Configs.getDatabase().set("suggests", suggests);
						rp.sendMessage("§aYour suggest : \"§e" + suggest + "§a\" has been save successfully!", "§aVotre suggestion : \"§e" + suggest + "§a\" a bien été sauvegardée !");
					}else{
						Configs.getDatabase().set("suggests", new ArrayList<String>());
						List<String> suggests = Configs.getDatabase().getStringList("suggests");
						suggests.add(p.getName() + ": " + suggest);
						Configs.getDatabase().set("suggests", suggests);
						rp.sendMessage("§aYour suggest : \"§e" + suggest + "§a\" has been save successfully!", "§aVotre suggestion : \"§e" + suggest + "§a\" a bien été sauvegardée !");
					}
				}else rp.sendMessage("§cPlease use /suggest <Your Suggest>", "§cUtilisez /suggest <Votre Suggestion>");
			}else if(cmd.getName().equalsIgnoreCase("setcity")){
				if(p.isOp()){
					if(args.length == 1){
						Configs.getCitiesConfig().set("cities." + args[0] + ".location", p.getLocation());
					}else{
						rp.sendMessage("§cUse /setcity [name]", "§cUtilisez /setcity [nom]");
					}
				}else{
					rp.sendMessage("§cYou really thought you could do that without being Operator!", "§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
				}
			}else if(cmd.getName().equalsIgnoreCase("city")){
				if(p.isOp()){
					if(args.length == 1){
						if(Configs.getCitiesConfig().contains("cities." + args[0])){
							TeleportHandler.teleport(p, (Location) Configs.getCitiesConfig().get("cities." + args[0] + ".location", p.getLocation()));
						}else{
							rp.sendMessage("§cThere is no such city called §4" + args[0] + "§c!", "§cAucune cité ne se nomme §4" + args[0] + " §c!");
						}
					}else{
						rp.sendMessage("§cUse /city [name]", "§cUtilisez /city [nom]");
					}
				}else{
					rp.sendMessage("§cYou really thought you could do that without being Operator!", "§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
				}
			}else if(cmd.getName().equalsIgnoreCase("beer")){
				if(p.isOp()){
					ItemStack beer = new ItemStack(Material.POTION, 1, (short)8227);
					ItemMeta beerm = beer.getItemMeta();
					beerm.setDisplayName(rp.translateString("§rBeer","§rBière"));
					beerm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
					beer.setItemMeta(beerm);
					p.getInventory().addItem(beer);
				}else rp.sendMessage("§cYou really thought you could do that without being Operator!", "§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
			}else if(cmd.getName().equalsIgnoreCase("prefs")){
				Core.uiManager.requestUI(new PrefsUI(p));
			}else if(cmd.getName().equalsIgnoreCase("tutorial")){
				TeleportHandler.startTeleportation(p, Bukkit.getWorld("Tutorial").getSpawnLocation(), new RTeleportCause(RTeleportType.DELAYED_TELEPORTATION, null, null,null));
			}else if(cmd.getName().equalsIgnoreCase("weapons")){
				//if(p.isOp()){
					Core.uiManager.requestUI(new WeaponsUI(p));
				//}else rp.sendMessage("§cYou really thought you could do that without being Operator!", "§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
			}else if(cmd.getName().equalsIgnoreCase("character")){
				Core.uiManager.requestUI(new DistinctionsMenu(p));
			}else if(cmd.getName().equalsIgnoreCase("invsee")){
				if(rp.isOp()){
					if(args.length > 0){
						Player player = Bukkit.getPlayer(args[0]);
						if(player != null){
							p.openInventory(player.getInventory());
						}else rp.sendMessage("§cCouldn't find a player with name §4" + args[0], "§cImpossible de trouver un joueur avec le nom §4" + args[0]);
					}else rp.sendMessage("§cPlease use /invsee [player]", "§cUtilisez /invsee [joueur]");
				}else rp.sendMessage("§cYou really thought you could do that without being Operator?", "§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
			}else if(cmd.getName().equalsIgnoreCase("play")){
				Core.uiManager.requestUI(new SPlayerSelectionMenu(p));
			}else if(cmd.getName().equalsIgnoreCase("glitch")){
				if(args.length > 0){
					if(args[0].equalsIgnoreCase("tp")){
						if(glitch.contains(p)){
							glitch.remove(p);
							p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
							return true;
						}
					}
				}
				rp.getChat().addFixDisplay(new RChatFixDisplay(rp,-1,null).addLines(rp.translateString("§a§l          GLITCH HELP", "§a§l          AIDE GLITCH"),
				rp.translateString("      §6In a moment, you will receive another message of this type.", "     §6Dans un moment, vous recevrez un second message de ce type."),
				rp.translateString("   §eIt will allow you to get teleported instantly to Mearwood.", "   §eVous pourrez alors être instantanément téléporté à Mearwood.")));
				new BukkitTask(this){

					@Override
					public void run() {
						RChatFixDisplay fixDisplay = new RChatFixDisplay(rp,100,null).addLines(rp.translateString("§a§l          GLITCH HELP", "§a§l          AIDE GLITCH"),
						rp.translateString("   §eClick on the following button.", "   §eCliquez sur le bouton suivant."),
						rp.translateString("      §4WARNING! §cYou only have 5 seconds to use it!", "     §4ATTENTION! §cVous n'avez que 5 secondes pour l'utiliser !"), "");
					    TextComponent tp = new TextComponent(rp.translateString("§2[§aGET ME OUT OF THIS§2]", "§2[§aSORTEZ-MOI DE CE MACHIN§2]"));
					    ClickEvent tpEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/glitch tp");
					    tp.setClickEvent(tpEvent);
					    TextComponent text = new TextComponent("                  ");
					    text.addExtra(tp);
					    fixDisplay.addText(text);
					    fixDisplay.addLine("");
					   	rp.getChat().addFixDisplay(fixDisplay);
						glitch.add(p);
						new BukkitTask(this.getPlugin()){

							@Override
							public void run() {
								glitch.remove(p);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(5*20);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(Utils.random.nextInt(6*20)+9*20);
			}
			return true;
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				final Player p = e.getPlayer();
				final RPlayer rp = RPlayer.get(p);
				
				if(e.getAction().toString().contains("RIGHT_CLICK")){
					ItemStack item = p.getEquipment().getItemInMainHand();
					RItem rItem = new RItem(item);
					if(rItem.isBackPack()){
						e.setCancelled(true);
						uiManager.requestUI(new BackpackUI(p, rItem.getBackPack()));
					}else if(rItem.isScroll()){
						e.setCancelled(true);
						Scroll scroll = rItem.getScroll();
						if(scroll.use(p)){
							item.setAmount(item.getAmount()-1);
							if(item.getAmount() < 1)p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						}
						Utils.updateInventory(p);
					}else if(item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 1){
						e.setCancelled(true);
						rp.sendMessage("§cCheating golden apple are not permitted on Rubidia.", "§cLes pommes d'or aux pouvoirs surpuissants sont interdites.");
					}else{
						for(RItemStack stack : RItemStacks.ITEMS){
							if(stack.getItemStack().isSimilar(item)){
								e.setCancelled(true);
								break;
							}
						}
					}
					
					if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
						final Block clicked = e.getClickedBlock();
						if(clicked.getType().equals(Material.ENCHANTMENT_TABLE)){
							if(!e.isCancelled()){
								e.setCancelled(true);
								ApplicableRegionSet set = wg.getRegionManager(clicked.getWorld()).getApplicableRegions(clicked.getLocation());
								if(!set.testState(null, Flags.BLOCKS) && !p.isOp()){
									return;
								}
								
								if(!p.getEquipment().getItemInMainHand().getType().equals(Material.AIR))uiManager.requestUI(new EnchantmentUI(clicked.getLocation().add(.5,0,.5), p));
								else rp.sendMessage("§cPlease take an item in your hand to use the enchantment table.", "§cPrenez un item dans vos main pour utiliser la table d'enchantement.");
							}
						}else if(clicked.getType().toString().contains("SIGN")){
							final Sign s = (Sign) clicked.getState();
							if(s.getLine(0).contains("§8§l[§4§lA VENDRE§8§l]")){
								if(s.getLine(1).contains("§8§l[§6§lVIP§8§l]")){
									if(!rp.isVip()){
										rp.sendMessage("§cYou must be §8§l[§6§lVIP§8§l] §r§cto buy this region!", "§cVous devez être §8§l[§6§lVIP§8§l] §r§cpour acheter cette région !");
										return;
									}
								}
								if(!s.getLine(2).contains("no region here")){
									if(!s.getLine(3).contains("§aGratuit")){
										String[] price = s.getLine(3).split("§a");
										if(price.length == 2){
											if(rp.getBalance() >= Integer.valueOf(price[1])){
												EconomyHandler.withdrawBalanceITB(p, Integer.valueOf(price[1]));
											}
										}
									}
									DefaultDomain dd = new DefaultDomain();
									dd.addPlayer(p.getUniqueId());
									wg.getRegionManager(s.getWorld()).getRegion(s.getLine(2)).setOwners(dd);
									s.setLine(0, rp.translateString("§8§l[§4§lSOLD§8§l]", "§8§l[§4§lVENDU§8§l]"));
									s.setLine(2, rp.translateString("Congratulations!", "Félicitations !"));
									s.setLine(3, rp.translateString("§aLet's build this!", "§aEn avant !"));
									s.update();
									LevelUtils.firework(s.getBlock().getLocation().add(0, 1, 0));
									Bukkit.getScheduler().runTaskLater(this, new Runnable(){
										public void run(){
											s.getBlock().breakNaturally();
										}
									}, 80);
								}else{
									rp.sendMessage("§cPlease contact an Operator to buy this region.", "§cContactez un Opérateur afin d'acheter cette région.");
								}
							}
						}else if(clicked.getType().equals(Material.BOOKSHELF) && !(p.isSneaking())){
							e.setCancelled(true);
							uiManager.requestUI(new SPlayerManager(p));
						}else if(clicked.getType().equals(Material.ANVIL)){
							anvil.put(p, e.getClickedBlock());
						}else if(clicked.getType().equals(Material.ENDER_CHEST)){
							if(!e.isCancelled()){
								e.setCancelled(true);
								if(!p.isSneaking()){
									Core.uiManager.requestUI(new EnderChest(p, clicked));
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				Player player = e.getPlayer();
				RPlayer rp = RPlayer.get(player);
				Entity en = e.getRightClicked();
				if(en instanceof Player){
					if(!rp.isInCombat()){
						Player target = (Player)en;
						if(player.isSneaking() && !DialogManager.isInDialog(player)){
							Core.uiManager.requestUI(new PlayerMenu(player, target));
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		Entity ec = e.getDamager();
		Entity ed = e.getEntity();
		
		if(ec instanceof Player){
			Player pc = (Player)ec;
			if(!(ed instanceof Player)){
				if(pc.hasMetadata("removingEntity")){
					ed.remove();
					pc.removeMetadata("removingEntity", this);
				}
			}
		}else if(ec instanceof Firework){
			e.setCancelled(true);
		}
		
		if(ed instanceof Player){
			Player pd = (Player)ed;
			if(RPlayer.get(pd).getResurrectionTask() != null){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent e){
		Entity en = e.getEntity();
		if(en instanceof LivingEntity){
			if(en.getType().equals(EntityType.ZOMBIE) && en.getCustomName() != null && Monsters.get((LivingEntity)en) == null){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		ItemStack pickUp = e.getItem().getItemStack();
		RItem rItem = new RItem(pickUp);
		if(p.getInventory().contains(Material.STORAGE_MINECART)){
			int amount = 0;
			for(int slot = 0;slot < 36;slot++){
				ItemStack item = p.getInventory().getItem(slot);
				if(item != null){
					RItem rItem2 = new RItem(item);
					if(rItem2.isBackPack())amount++;
					if(amount > 1){
						p.getWorld().dropItem(p.getLocation(), p.getInventory().getItem(slot));
						p.getInventory().remove(item);
						rp.sendMessage("§cYou cannot carry more than one backpack!", "§cVous ne pouvez transporter plus d'un sac à dos !");
					}
				}
			}
			if(rItem.isBackPack() && amount > 1){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e){
		ItemStack is = e.getCurrentItem();
		HumanEntity he = e.getWhoClicked();
		if(he instanceof Player){
			Player p = (Player)he;
			RPlayer rp = RPlayer.get(p);
			
			if(is != null){
				if(is.getType().equals(Material.STORAGE_MINECART)){
					if(is.hasItemMeta()){
						ItemMeta im = is.getItemMeta();
						if(im.hasDisplayName()){
							if(im.getDisplayName().equals("§fSac à dos")){
								if(e.isShiftClick())e.setCancelled(true);
								else{
									e.setCurrentItem(BackPacks.newBackPack());
								}
							}
						}
					}
				}else if(is.getType().equals(Material.EMERALD_BLOCK)){
					e.setCancelled(true);
					rp.sendMessage("§cYou can only change money from a banker!", "§cSeul un banquier peut changer votre monnaie !");
				}else if(is.getType().equals(Material.EMERALD) && is.getAmount() == 9){
					e.setCancelled(true);
					rp.sendMessage("§cYou can only change money from a banker!", "§cSeul un banquier peut changer votre monnaie !");
				}else if(is.getType().equals(Material.SHIELD) && is.hasItemMeta()){
					e.setCancelled(true);
					rp.sendMessage("§cYou cannot customize your shield at the moment.", "§cVous ne pouvez pour le moment pas personnaliser votre bouclier.");
				}
			}
		}
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e){
		Player p = e.getPlayer();
		if(e.getBlock().getType().toString().contains("SIGN")){
			Sign s = (Sign) e.getBlock().getState();
			for(int i = 0;i < e.getLines().length;i++){
				e.setLine(i, ChatColor.translateAlternateColorCodes('&', e.getLines()[i]));
			}
			if(p.isOp()){
				if(e.getLine(0).contains("RGSELL")){
					ApplicableRegionSet rg = wg.getRegionManager(s.getWorld()).getApplicableRegions(s.getLocation());
					ProtectedRegion tosell = null;
					int priority = 999999999;
					for(ProtectedRegion region : rg){
						if(region.getPriority() < priority){
							tosell = region;
							priority = tosell.getPriority();
						}
					}
					e.setLine(0, "§8§l[§4§lA VENDRE§8§l]");
					e.setLine(1, e.getLine(1).isEmpty() ? "" : "§8§l[§6§lVIP§8§l]");
					e.setLine(2, tosell == null ? "no region here" : tosell.getId());
					try {
						e.setLine(3, e.getLine(3).isEmpty() ? "§aGratuit" : "§8Prix : §a" + Integer.valueOf(e.getLine(3)));
					} catch (Exception ex){
						e.setLine(3, "§aGratuit");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(!e.isCancelled()){
			if(p.getGameMode().equals(GameMode.SURVIVAL)){
				ItemStack inHand = p.getEquipment().getItemInMainHand();
				if(inHand.getType().equals(Material.GOLD_PICKAXE) || inHand.getType().equals(Material.IRON_PICKAXE) || inHand.getType().equals(Material.DIAMOND_PICKAXE)){
					if(!inHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
						if(b.getType().equals(Material.EMERALD_ORE)){
							Random r = new Random();
							int drops = r.nextInt(4);
							for(int i = 1;i <= drops;i++){
								b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.EMERALD, 1));
							}
						}
					}
				}
			}
		}
		e.setExpToDrop(0);
	}
	
	@EventHandler
	public void onBeerDrink(PlayerItemConsumeEvent e){
		final Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if(item.getType().equals(Material.POTION)){
			if(item.hasItemMeta()){
				ItemMeta meta = item.getItemMeta();
				if(meta.hasDisplayName()){
					if(meta.getDisplayName().contains("§rBeer") || meta.getDisplayName().contains("§rBière")){
						Bukkit.getScheduler().runTaskLater(this, new Runnable(){public void run(){p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);}}, 2);
						Utils.addSafeBuff(p, new PotionEffect(PotionEffectType.CONFUSION, 30*20, 1, true, true));
						Utils.addSafeBuff(p, new PotionEffect(PotionEffectType.SLOW, 30*20, 1, true, true));
						Core.drunk.add(p);
						Bukkit.getScheduler().runTaskLater(this, new Runnable(){
							public void run(){
								if(Core.drunk.contains(p))Core.drunk.remove(p);
							}
						}, 30*20);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(player.isSneaking()){
			RPlayer rp = RPlayer.get(player);
			Item item = event.getItemDrop();
			String material = item.getItemStack().getType().toString();
			boolean hasWeapon = (rp.getRClass().equals(RClass.PALADIN) && material.contains("_AXE")) || (rp.getRClass().equals(RClass.RANGER) && material.contains("BOW")) || (rp.getRClass().equals(RClass.MAGE) && material.contains("_HOE")) || (rp.getRClass().equals(RClass.ASSASSIN) && material.contains("_SWORD")) || (rp.getRClass().equals(RClass.VAGRANT) && (material.contains("_SWORD") || material.contains("_AXE") || material.contains("BOW")));
			if(hasWeapon){
				item.remove();
				Core.uiManager.requestUI(new DistinctionsMenu(player));
			}
		}
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent event){
		event.setMaxPlayers(playersMax+1);
		if(Utils.random.nextBoolean()){
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
				event.setMotd("             §5>>>  §d§lSAMED'XP DE FOLIE§5  <<<\n                             §dXP ×2");
				return;
			}else if(!Events.currentEvents.isEmpty()){
				for(Event e : Events.currentEvents){
					if(e.isActive()){
						event.setMotd("           §5>>>  §d§lEVENEMENT EN COURS§5  <<<");
						return;
					}
				}
			}
		}
		event.setMotd("§r                       §8mc.§9§lRubidia§8.xyz\n        §e§m  §e§l  §6§lO§e§lpen-§6§lW§e§lorld §6§lA§e§ldventure §6§lRPG  §e§m  ");
	}
	
	@EventHandler
	public void onManipulate(PlayerArmorStandManipulateEvent e){
		if(e.getRightClicked().isMarker() && !e.getPlayer().isOp())e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemChange(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		int slot = event.getNewSlot();
		if(!rp.isOp()){
			if(slot == 8){
				if(rp.isUsingCycle()){
					for(int i = 0;i < 8;i++){
						ItemStack hold = player.getInventory().getItem(i);
						for(int z = 3;z > 0;z--){
							int prev = i+z*9;
							int next = (prev+9)%36;
							player.getInventory().setItem(next, player.getInventory().getItem(prev));
						}
						player.getInventory().setItem(i+9, hold);
					}
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e){
		Entity entity = e.getEntity();
		Block block = e.getBlock();
		if(block != null){
			if(block.getType().equals(Material.SOIL)){
				if(entity instanceof LivingEntity){
					EntityEquipment equipment = ((LivingEntity)entity).getEquipment();
					ItemStack item = equipment.getBoots();
					if(item != null){
						if(item.hasItemMeta()){
							ItemMeta meta = item.getItemMeta();
							if(meta.hasEnchant(Enchantment.PROTECTION_FALL)){
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event){
		final Player player = event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		if(!player.getGameMode().equals(event.getNewGameMode())){
			if(event.getNewGameMode().equals(GameMode.SURVIVAL) || event.getNewGameMode().equals(GameMode.CREATIVE)){
				new BukkitTask(this){

					@Override
					public void run() {
						NameTags.update();
						if(rp.isOp()){
							for(RPlayer rpp : RPlayer.getOnlines()){
								if(rpp.isVanished() && !rpp.equals(rp)){
									player.showPlayer(rpp.getPlayer());
								}
							}
						}else{
							for(RPlayer rpp : RPlayer.getOnlines()){
								if(rpp.isVanished() && !rpp.equals(rp)){
									player.hidePlayer(rpp.getPlayer());
								}
							}
						}
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}
		}
	}
	
	////////////////////////////////////////
	//           CUSTOM METHODS           //
	////////////////////////////////////////
	
	public static void broadcast(String en, String fr, Player... noPlayers){
		List<Player> players = Arrays.asList(noPlayers);
		for(Player player : Bukkit.getOnlinePlayers()){
			if(!players.contains(player)){
				RPlayer rp = RPlayer.get(player);
				rp.sendMessage(en, fr);
			}
		}
	}
	
	public static List<LivingEntity> toLivingEntityList(List<Entity> near){
		List<LivingEntity> list = new ArrayList<LivingEntity>();
		for(Entity entity : near){
			if(entity instanceof LivingEntity){
				if(!(entity instanceof ArmorStand)/* && !Pet.isPet(entity)*/){
					if(entity instanceof Villager){
						if(PNJManager.isPNJ((Villager) entity)){
							continue;
						}
					}
					
					list.add((LivingEntity) entity);
				}
			}
		}
		return list;
	}
	
	public static List<LivingEntity> toDamageableLivingEntityList(Player player, List<Entity> entities, RDamageCause cause){
		List<LivingEntity> toDamageEntities = new ArrayList<LivingEntity>();
		for(LivingEntity entity : Core.toLivingEntityList(entities)){
			if(DamageManager.canDamage(player, entity, cause)){
				toDamageEntities.add(entity);
			}
		}
		return toDamageEntities;
	}
	
	public static List<Player> toPlayerList(List<Entity> near){
		List<Player> list = new ArrayList<Player>();
		for(Entity entity : near)if(entity instanceof Player)list.add((Player) entity);
		return list;
	}
	
	public static void playAnimEffect(ParticleEffect particle, Location location, float offSetX, float offSetY, float offSetZ, float speed, int amount, BlockData data){
		for(Player player : Bukkit.getOnlinePlayers()){
			Core.playAnimEffect(particle, player, location, offSetX, offSetY, offSetZ, speed, amount, data);
		}
	}
	
	public static void playAnimEffect(ParticleEffect particle, Player player, Location location, float offSetX, float offSetY, float offSetZ, float speed, int amount, BlockData data){
		if(player.getWorld().equals(location.getWorld()) && player.getLocation().distanceSquared(location) <= 2304/*range 48*/){
			if(RPlayer.get(player).getEffects()){
				if(particle.getRequiresData()){
					particle.display(data, offSetX, offSetY, offSetZ, speed, amount, location, player);
				}else particle.display(offSetX, offSetY, offSetZ, speed, amount, location, player);
			}
		}
	}
	
	public static void playAnimEffect(ParticleEffect particle, Location location, float offSetX, float offSetY, float offSetZ, float speed, int amount){
		for(Player player : Bukkit.getOnlinePlayers()){
			Core.playAnimEffect(particle, player, location, offSetX, offSetY, offSetZ, speed, amount);
		}
	}
	
	public static void playAnimEffect(ParticleEffect particle, Player player, Location location, float offSetX, float offSetY, float offSetZ, float speed, int amount){
		if(player.getWorld().equals(location.getWorld()) && player.getLocation().distanceSquared(location) <= 2304/*range 48*/){
			if(RPlayer.get(player).getEffects()){
				if(!particle.getRequiresData()){
					particle.display(offSetX, offSetY, offSetZ, speed, amount, location, player);
				}
			}
		}
	}
	
	public static File getSavesFolder(){
		File file = new File(instance.getDataFolder().getAbsolutePath().replace("RubidiaCore", "Rubidia.saves"));
		if(!file.exists())file.mkdirs();
		return file;
	}
	
	
	////////////////////////////////////////
	//            CITY SYSTEM             //
	////////////////////////////////////////
	
	public static void registerCity(Location loc, String name){
		Configs.getCitiesConfig().set("cities." + name + ".location", loc);
	}
	
	////////////////////////////////////////
	//          JSONAPI METHODS           //
	////////////////////////////////////////

	public String getAllWeapons(String rClass, String weaponUse){
		List<Weapon> available = new ArrayList<Weapon>(Weapons.weapons);
		if(rClass != null){
			for(Weapon weapon : Weapons.weapons){
				if(available.contains(weapon) && !weapon.getRClass().toString().contains(rClass)){
					available.remove(weapon);
				}
			}
		}
		if(weaponUse != null){
			for(Weapon weapon : Weapons.weapons){
				if(available.contains(weapon) && !weapon.getWeaponUse().toString().contains(weaponUse)){
					available.remove(weapon);
				}
			}
		}
		String json = available.size() > 1 ? "[" : "";
		for(Weapon weapon : available){
			json += "{";
			json += JSONUtils.toJSON("name") + ":" + JSONUtils.toJSON(weapon.getName()) + ",";
			json += JSONUtils.toJSON("level") + ":" + weapon.getLevel() + ",";
			json += JSONUtils.toJSON("weaponUse") + ":" + JSONUtils.toJSON(weapon.getWeaponUse().getDisplayFr()) + ",";
			String use = weapon.getWeaponUse().toString();
			json += JSONUtils.toJSON("dataWeaponUse") + ":" + JSONUtils.toJSON(weapon.isAttack() ? "ARMURE" : (use.equals("MELEE_RANGE") ? "polyvalente" : (use.equals("MAGIC") ? "magique" : use))) + ",";
			json += JSONUtils.toJSON("minDamages") + ":" + weapon.getMinDamages() + ",";
			json += JSONUtils.toJSON("maxDamages") + ":" + weapon.getMaxDamages() + ",";
			json += JSONUtils.toJSON("rarity") + ":" + JSONUtils.toJSON(weapon.getRarity().getDisplayFr()) + ",";
			json += JSONUtils.toJSON("dataRarity") + ":" + JSONUtils.toJSON(weapon.getRarity().getDisplayFr().replaceAll(" ", "#").replaceAll("é", "e").replaceAll("É", "E").replaceAll("ommun", "ommune")) + ",";
			json += JSONUtils.toJSON("dropChance") + ":" + JSONUtils.toJSON(String.valueOf(Utils.round(weapon.getDropChance()*weapon.getRarity().getFactor()*100,3))) + ",";
			json += JSONUtils.toJSON("attackSpeed") + ":" + JSONUtils.toJSON(String.valueOf(Utils.round(weapon.getAttackSpeed(),3))) + ",";
			json += JSONUtils.toJSON("skinId") + ":" + JSONUtils.toJSON(String.valueOf(weapon.getSkinId())) + ",";
			json += JSONUtils.toJSON("type") + ":" + JSONUtils.toJSON(weapon.getType().toString()) + ",";
			json += JSONUtils.toJSON("rclass") + ":" + JSONUtils.toJSON(weapon.getRClass().getDisplayFr()) + ",";
			String setName = "null";
			String setBuffs = "null";
			String setWeapons = "null";
			if(weapon.getSet() != null){
				setName = JSONUtils.toJSON(weapon.getSet().getName());
				List<String> state = weapon.getSet().getBuffState(null);
				String[] buffs = new String[state.size()];
				for(int i = 0;i < buffs.length;i++){
					buffs[i] = state.get(i);
				}
				setBuffs = JSONUtils.toJSON(buffs);
				List<Weapon> weapons = weapon.getSet().getWeapons();
				String[] names = new String[weapons.size()];
				for(int i = 0;i < names.length;i++){
					names[i] = weapons.get(i).getName();
				}
				setWeapons = JSONUtils.toJSON(names);
			}
			json += JSONUtils.toJSON("setName") + ":" + setName + ",";
			json += JSONUtils.toJSON("setBuffs") + ":" + setBuffs + ",";
			json += JSONUtils.toJSON("setWeapons") + ":" + setWeapons;
			json += "}";
			if(available.indexOf(weapon) != available.size()-1)json += ",";
		}
		json += available.size() > 1 ? "]" : "";
		return json;
	}
	
	public int getPendingRubis(String player){
		RPlayer rp = RPlayer.getFromName(player);
		if(rp != null){
			int amount = rp.getPendingRubis();
			rp.setPendingRubis(0);
			return amount;
		}
		return 0;
	}
	
	////////////////////////////////////////
	//        ON AND OFF BEHAVIOR         //
	////////////////////////////////////////


	public static void restart(){
		for(Player player : Bukkit.getOnlinePlayers()){
			RPlayer rp = RPlayer.get(player);
			rp.sendMessage("§e§l   RUBIDIA IS RESTARTING IN 20 SECONDS...", "§e§l   RUBIDIA REDEMARRE DANS 20 SECONDES...");
			rp.sendTitle("§aRubidia " + rp.translateString("is restarting", "redémarre") + "...", "§6>  §e20 second" + rp.translateString("", "e") + "s  §6<", 0, 140, 40);
		}
		new BukkitTask(Core.instance){

			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					RPlayer rp = RPlayer.get(player);
					rp.sendMessage("§e§l   RUBIDIA IS RESTARTING IN 5 SECONDS...", "§e§l   RUBIDIA REDEMARRE DANS 5 SECONDES...");
					rp.sendTitle("§aRubidia " + rp.translateString("is restarting", "redémarre") + "...", "§6>  §e5 second" + rp.translateString("", "e") + "s  §6<", 0, 200, 0);
				}
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						for(Player player : Bukkit.getOnlinePlayers()){
							player.kickPlayer("§6>>  §eRubidia redémarre et sera rapidement de nouveau disponible !");
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(5*20);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(15*20);
		restarting = true;
	}
	
	
	@SuppressWarnings("deprecation")
	public void onEnable(){
		console = Bukkit.getConsoleSender();
		REnchantment.registerEnchantments();
		
		wg = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
		we = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
		
		instance = this;
		uiManager = new UIManager(this);
    	barHandler = new HealthBarHandler(this);
		this.getConfig().options().copyDefaults(true);
		Configs.getPlayerConfig().options().copyDefaults(true);
		Configs.getBackpackConfig().options().copyDefaults(true);
		Configs.getCitiesConfig().options().copyDefaults(true);
		Configs.getDatabase().options().copyDefaults(true);
		Configs.getPathConfig().options().copyDefaults(true);
		Configs.getWeaponsConfig().options().copyDefaults(true);
		Configs.getCouplesConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultPlayerConfig();
		Configs.saveDefaultBackpackConfig();
		Configs.saveDefaultCitiesConfig();
		Configs.saveDefaultDatabase();
		Configs.saveDefaultPathConfig();
		Configs.saveDefaultWeaponsConfig();
		Configs.saveDefaultCouplesConfig();
		Bukkit.getServer().setWhitelist(Configs.getDatabase().getBoolean("maintenancemode"));
		playersMax = Configs.getDatabase().getInt("playersMax");
		console.sendMessage("§2------------------------------------------------------");
		GuildsPlugin.onStart();
		me.pmilon.RubidiaGuilds.utils.Configs.saveMembersConfig();
		me.pmilon.RubidiaGuilds.utils.Configs.saveGuildConfig();
		QuestsPlugin.onStart();
		RubidiaMonstersPlugin.onStart();
		console.sendMessage("§a   Loading RPlayers...");
		console.sendMessage("§2------------------------------------------------------");
    	rcoll = new RPlayerColl();
    	console.sendMessage("§2------------------------------------------------------");
		console.sendMessage("§a   Loading Couples...");
		console.sendMessage("§2------------------------------------------------------");
		Couples.onEnable();
		console.sendMessage("§2------------------------------------------------------");
		console.sendMessage("§a   Loading Weapons...");
		console.sendMessage("§2------------------------------------------------------");
    	Weapons.onEnable(true);
		Bukkit.getServer().getPluginManager().registerEvents(new WeaponsListener(), this);
		console.sendMessage("§2------------------------------------------------------");
		Bukkit.getPluginManager().registerEvents(this, this);
	    Bukkit.getPluginManager().registerEvents(new TagStandListener(this), this);
		Bukkit.getPluginManager().registerEvents(new TeleportHandler(this), this);
	    Bukkit.getPluginManager().registerEvents(new JobsHandler(this), this);
	    Bukkit.getPluginManager().registerEvents(new RLevelHandler(this), this);
	    Bukkit.getPluginManager().registerEvents(new GamePlayEffectsHandler(), this);
	    Bukkit.getPluginManager().registerEvents(new ChairListener(this), this);
	    Bukkit.getPluginManager().registerEvents(new AeroplaneListener(this), this);
	    Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ResourcePackHandler(), this);
		Bukkit.getPluginManager().registerEvents(new RChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new RDuelListener(), this);
		
	    this.getCommand("item").setExecutor(new ItemCommandExecutor());
	    this.getCommand("profile").setExecutor(new ProfileCommandExecutor());
	    this.getCommand("marry").setExecutor(new MarryCommandExecutor());
	    this.getCommand("couple").setExecutor(new CoupleCommandExecutor());
	    this.getCommand("events").setExecutor(new EventsCommandExecutor());
	    this.getCommand("class").setExecutor(new ClassCommandExecutor());
	    this.getCommand("vip").setExecutor(new VIPCommandExecutor());
	    this.getCommand("reboot").setExecutor(new RebootCommandExecutor());
	    this.getCommand("level").setExecutor(new LevelCommandExecutor());
	    this.getCommand("scroll").setExecutor(new ScrollCommandExecutor());
	    this.getCommand("howmanyplayers").setExecutor(new HMPCommandExecutor());
	    this.getCommand("money").setExecutor(new MoneyCommandExecutor());
	    this.getCommand("skd").setExecutor(new SKDCommandExecutor());
	    this.getCommand("skp").setExecutor(new SKPCommandExecutor());
	    this.getCommand("chat").setExecutor(new ChatCommandExecutor());
	    this.getCommand("vanish").setExecutor(new VanishCommandExecutor());
	    this.getCommand("mute").setExecutor(new MuteCommandExecutor());
	    this.getCommand("bienvenue").setExecutor(new BienvenueCommandExecutor());
	    this.getCommand("help").setExecutor(new HelpCommandExecutor());
	    this.getCommand("statistics").setExecutor(new StatisticsCommandExecutor());
	    this.getCommand("boosters").setExecutor(new BoostersCommandExecutor());
	    this.getCommand("rankings").setExecutor(new RankingsCommandExecutor());
	    this.getCommand("vote").setExecutor(new VoteCommandExecutor());
	    this.getCommand("rplayers").setExecutor(new RPlayersCommandExecutor());
	    
	    AbilitiesAPI.onEnable(this);
	    Events.onEnable(this);
	    EntityHandler.onEnable(this);

		ItemStack backpackis = new ItemStack(Material.STORAGE_MINECART, 1);
		ItemMeta bpmeta = backpackis.getItemMeta();
		bpmeta.setDisplayName("§fSac à dos");
		backpackis.setItemMeta(bpmeta);
		
		ShapedRecipe backpack = new ShapedRecipe(backpackis).shape(new String[] { "*#*", "§#§", "###" }).setIngredient('*', Material.STRING).setIngredient('#', Material.RABBIT_HIDE).setIngredient('§', Material.LEATHER);
		this.getServer().addRecipe(backpack);
		ShapedRecipe chainmailHelmet = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET)).shape(new String[] { "#§#", "§ §", "   " }).setIngredient('#', Material.IRON_INGOT).setIngredient('§', Material.FLINT);
		this.getServer().addRecipe(chainmailHelmet);
		ShapedRecipe chainmailChestplate = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE)).shape(new String[] { "§ §", "#§#", "§#§" }).setIngredient('#', Material.IRON_INGOT).setIngredient('§', Material.FLINT);
		this.getServer().addRecipe(chainmailChestplate);
		ShapedRecipe chainmailLeggings = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS)).shape(new String[] { "§#§", "# #", "§ §" }).setIngredient('#', Material.IRON_INGOT).setIngredient('§', Material.FLINT);
		this.getServer().addRecipe(chainmailLeggings);
		ShapedRecipe chainmailBoots = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_BOOTS)).shape(new String[] { "   ", "# #", "§ §" }).setIngredient('#', Material.IRON_INGOT).setIngredient('§', Material.FLINT);
		this.getServer().addRecipe(chainmailBoots);
		
		BackPacks.onEnable();
		RItemStacks.enable();
		console.sendMessage("§a   Rubidia Core Plugin Enabled");
		console.sendMessage("§2------------------------------------------------------");
		
		new BukkitTask(this){

			@Override
			public void run() {
				World world = Bukkit.getWorld("Tutorial");
				if(world != null){
					if(world.getTime() != 16000){
						world.setTime(16000);
						world.setGameRuleValue("doDaylightCycle", "false");
					}
				}
				
				for(RPlayer rp : RPlayer.getOnlines()){
					if(!rp.isOp()){
						rp.setLastMoneyAmount(rp.getBalance());
						rp.setRenom(rp.getRenom()+1);
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0, 60*20);
		//    1/min task
		new BukkitTask(this){
			
			@Override
			public void run(){
				for(RPlayer rp : RPlayer.getOnlines()){
					if(rp.getLoadedSPlayer() != null){
						rp.addNrj(rp.getNrjPerSecond());
						rp.setGamingTime(rp.getGamingTime()+1000L);
						rp.setLastConnectionDate(System.currentTimeMillis());
						
						if(rp.getPlayer().getWorld().getTime() > 12500 && rp.getPlayer().getWorld().getTime() < 12521){
							JobsHandler.getWage(rp.getPlayer(), true);
						}
						
						for(Pet pet : rp.getPets()){
							pet.update(rp.getPlayer());
						}
						
						if(rp.isVip()){
							if(rp.getVip() < System.currentTimeMillis()){
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vip " + rp.getName() + " 0");
							}
						}
					}
					
					Couple couple = rp.getCouple();
					if(couple != null){
						if(couple.isOnline()){
							couple.setXPTime(couple.getXPTime()+500L);
						}
					}
				}
				
				for(Event event : Events.currentEvents){
					if(!event.isStarted() && System.currentTimeMillis() >= event.getStartDate() && System.currentTimeMillis() <= event.getStartDate()+event.getDuration()){
						event.start();
					}else if(event.isStarted() && System.currentTimeMillis() > event.getStartDate()+event.getDuration()){
						event.finish();
					}
				}
			}
			
			@Override
			public void onCancel() {
			}
		}.runTaskTimer(0, 20);
		//    1/sec Task
		new BukkitTask(this){

			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					RPlayer rp = RPlayer.get(player);
					
					if(rp.getLoadedSPlayer() != null){
						float speedFactor = 0;
						if(rp.getRClass().equals(RClass.RANGER)){
							if(player.getInventory().getItem(17) != null){
								if(!(player.getInventory().getItem(17).getType().toString().contains("ARROW")) || player.getInventory().getItem(17).getAmount() < 3){
									player.getInventory().setItem(17, new ItemStack(Material.ARROW, 3));
								}
							}else{
								player.getInventory().setItem(17, new ItemStack(Material.ARROW, 3));
							}
						}else if(rp.getRClass().equals(RClass.ASSASSIN)){
							speedFactor += Abilities.doASSASSIN3(player);
						}
						
						if(player.getOpenInventory() != null){
							if(player.getOpenInventory().getTopInventory() != null){
								if(player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)){
									if(!Core.uiManager.hasActiveSession(player)){
										boolean recipe_empty = true;
										ItemStack skt = new ItemStack(Material.BOOKSHELF, 1);
										ItemMeta meta = skt.getItemMeta();
										meta.setDisplayName(rp.translateString("§6§lCharacter menu", "§6§lMenu du personnage"));
										meta.setLore(Arrays.asList(rp.translateString("§7Open your character menu", "§7Ouvrir le menu du personnage")));
										skt.setItemMeta(meta);
										for(int i = 0;i < 5;i++){
											if(player.getOpenInventory().getTopInventory().getItem(i) != null){
												recipe_empty = false;
												break;
											}
										}
										if(recipe_empty){
											player.getOpenInventory().getTopInventory().setItem(0, skt);
											player.updateInventory();
										}
									}
								}
							}
						}

						boolean elytra = false;
						List<Set> sets = new ArrayList<Set>();
						for(ItemStack item : player.getInventory().getArmorContents()){
							if(item != null){
								Material type = item.getType();
								if(type.equals(Material.ELYTRA))elytra = true;
								RItem rItem = new RItem(item);
								if(rItem.isWeapon()){
									Weapon weapon = rItem.getWeapon();
									if(!weapon.isAttack()){
										if(!weapon.canUse(rp)){
											String name = type.toString();
											if(name.contains("_HELMET"))player.getInventory().setHelmet(null);
											else if(name.contains("_CHESTPLATE"))player.getInventory().setChestplate(null);
											else if(name.contains("_LEGGINGS"))player.getInventory().setLeggings(null);
											else if(name.contains("_BOOTS"))player.getInventory().setBoots(null);
											player.getInventory().addItem(item);
											rp.sendMessage("§cYou cannot wear this piece of armor!", "§cVous ne pouvez porter cette pièce d'armure !");
											player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
											Utils.updateInventory(player);
										}else{
											if(weapon.isSetItem()){//for cpu reasons, we do not call Set.getFactor method
												Set set = weapon.getSet();
												if(!sets.contains(set)){
													for(Buff buff : weapon.getSet().getActiveBuffs(player)){
														if(buff.getType().equals(BuffType.WALK_SPEED)){
															speedFactor += buff.getFactor();
														}
													}
													sets.add(set);
												}
											}
										}
									}
								}
							}
						}
						
						if(elytra)player.setAllowFlight(!player.isGliding());
						else if((player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) && !DialogManager.isInDialog(player))player.setAllowFlight(false);
						
						if(player.getEquipment().getItemInOffHand() != null){
							ItemStack item = player.getEquipment().getItemInOffHand();
							Weapon weapon = null;
							RItem rItem = new RItem(item);
							if(rItem.isWeapon()){
								weapon = rItem.getWeapon();
								if(weapon.getType().equals(Material.SHIELD)){
									if(!weapon.canUse(rp)){
										player.getEquipment().setItemInOffHand(null);
										player.getInventory().addItem(item);
										rp.sendMessage("§cYou cannot hold this shield!", "§cVous ne pouvez porter ce bouclier !");
										player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
										Utils.updateInventory(player);
									}else{
										if(weapon.isSetItem()){//for cpu reasons, we do not call Set.getAdditionalFactor method
											Set set = weapon.getSet();
											if(!sets.contains(set)){
												for(Buff buff : weapon.getSet().getActiveBuffs(player)){
													if(buff.getType().equals(BuffType.WALK_SPEED)){
														speedFactor += buff.getFactor();
													}
												}
												sets.add(set);
											}
										}
									}
								}
							}//updated in next method
						}
						
						for(int i = 0;i < player.getInventory().getSize();i++){//updates every slot but the 8th
							if(i != 8){
								ItemStack item = player.getInventory().getItem(i);
								if(item != null){
									Weapon weapon;
									RItem rItem = new RItem(item);
									if(rItem.isWeapon()){
										weapon = rItem.getWeapon();
										if(!item.equals(player.getEquipment().getItemInMainHand()))weapon.updateState(rp, item);
										else if(weapon.isAttack() && weapon.canUse(rp) && weapon.isSetItem()){//shields in sets are only worn in left hand
											Set set = weapon.getSet();
											if(!sets.contains(set)){
												for(Buff buff : weapon.getSet().getActiveBuffs(player)){
													if(buff.getType().equals(BuffType.WALK_SPEED)){
														speedFactor += buff.getFactor();
													}
												}
												sets.add(set);
											}
										}
									}else{
										if(Weapons.types.contains(item.getType())){
											weapon = Weapons.craft(item.getType(), rp.getRClass(), rp.getRLevel(), Rarity.COMMON);
											if(weapon != null)player.getInventory().setItem(i, weapon.getNewItemStack(rp));
										}
									}
								}
							}
						}

						Couple couple = rp.getCouple();
						if(couple != null){
							for(Buff buff : couple.getAvailableBuffs()){
								if(buff.getType().equals(BuffType.WALK_SPEED)){
									speedFactor += buff.getFactor();
								}
							}
						}
						
						if(player.getWalkSpeed() > 0){
							float speed = .2F*(1+speedFactor);
							if(Math.abs(player.getWalkSpeed()-speed) >= .001){
								player.setWalkSpeed(speed);
								for(AttributeModifier modifier : new ArrayList<AttributeModifier>(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers())){
									player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
								}
							}
						}
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0, 5);
		//    4/sec Task
		new BukkitTask(this){

			@Override
			public void run() {
				for(RPlayer rp : RPlayer.getOnlines()){
					rp.sendTitle("§5SAUVEGARDE DU SERVEUR", "§dRalentissement attendu dans 5 secondes", 0, 100, 20);
				}
				new BukkitTask(Core.instance){
					
					@Override
					public void run(){
						Core.console.sendMessage("§eSaving configs...");
						
						Ranks.update();
						
						rcoll.saveAll(false);
						Couples.save(false);
						Weapons.onDisable();
						QuestsPlugin.questColl.saveAll(false);
						QuestsPlugin.shopColl.saveAll(false);
						PNJManager.save(false);
						BackPacks.save(false);
						Configs.saveCitiesConfig();
						Configs.saveDatabase();
						Configs.savePathConfig();
						GuildsPlugin.instance.saveConfig();
						GuildsPlugin.gcoll.saveAll(false);
						GuildsPlugin.gmembercoll.saveAll(false);
						GuildsPlugin.claimcoll.saveAll(false);
						GuildsPlugin.raidcoll.saveAll(false);
						Pets.save(false);
						
						if(getConfig().getBoolean("saveconfigs")){
							Core.console.sendMessage("§eSaving backup configs...");
							Configs.saveConfigs();
							me.pmilon.RubidiaGuilds.utils.Configs.saveConfigs();
							me.pmilon.RubidiaQuests.utils.Configs.saveConfigs();
							me.pmilon.RubidiaMonsters.utils.Configs.saveConfigs();
							me.pmilon.RubidiaPets.utils.Configs.saveConfigs();
							Core.console.sendMessage("§eSaved backup configs!");
						}
						Core.console.sendMessage("§eSaved configs!");
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(5*20);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(getConfig().getInt("saveinterval")*20, getConfig().getInt("saveinterval")*20);

		new BukkitTask(this){

			@Override
			public void run() {
				String[] announce = getConfig().getStringList("announcements").get(Utils.random.nextInt(getConfig().getStringList("announcements").size())).split("--");
				String message = "\n   §e" + announce[0];
				if(announce.length > 1){
					message += "\n            §8>> §7§l" + announce[1];
				}
				message += "\n ";
				for(RPlayer rp : RPlayer.getOnlines()){
					rp.getChat().addFixDisplay(new RChatFixDisplay(rp,200,null).addLine(message));
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(this.getConfig().getInt("announcementsInterval")*60*20, this.getConfig().getInt("announcementsInterval")*60*20);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Status.Server.OUT_SERVER_INFO){
	        @Override
	        public void onPacketSending(PacketEvent e) {
	        	try{
	                if(Configs.getDatabase().getBoolean("maintenancemode")){
		                WrappedServerPing ping = e.getPacket().getServerPings().read(0);
		                ping.setVersionProtocol(999);
	                	ping.setPlayersMaximum(0);
	                	ping.setVersionName("§cMAINTENANCE");
		        		String motd1 = ChatColor.translateAlternateColorCodes('&', Configs.getDatabase().getString("motd.1"));
		        		String motd2 = Configs.getDatabase().contains("maintenancemodemessage") ? "§d§l" + Configs.getDatabase().getString("maintenancemodemessage").toUpperCase().replaceAll("&", "§") : "§d§lREVENEZ UN PEU PLUS TARD";
		        		String motd2wc = ChatColor.stripColor(motd2);
		        		String[] parts = motd2wc.split("");
		        		int x = (35 - parts.length)/2;
		        		String space = "";
		        		for(int i = 0;i < (x*2)-1;i++)space += " ";
		        		ping.setMotD(motd1 + "\n" + "§d§l" + space + motd2wc);
		        		Iterable<WrappedGameProfile> players = Arrays.asList(new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), "§8_§7§m--§c§l§m[=======-§r§l        §2§l§oR§a§l§oUBIDIA        §c§l§m-=======]§7§m--§8_"), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), ""), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), "               §dNous travaillons dans le seul but"), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), "            §dd'améliorer votre expérience de jeu !"), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), ""), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), ""), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), "             §e- §6Open-World Adventure RPG §e-             "), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), ""), new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), "§8§m-§7§m--§c§l§m[==================================]§7§m--§8§m-"));
		        		ping.setPlayers(players);
	                }
	        	}catch(Exception ex){
	        		ex.printStackTrace();
	        	}
	        }
	    });
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.WINDOW_ITEMS){
	        @Override
	        public void onPacketSending(PacketEvent e) {
	        	WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(e.getPacket());
    			List<ItemStack> stacks = packet.getSlotData();
        		if(packet.getWindowId() == 0 && stacks.size() > 44){
        			RPlayer rp = RPlayer.get(e.getPlayer());
        			if(rp != null){
        				if(!rp.isOp()){
                			stacks.set(44, rp.getNrjItem());
                			packet.setSlotData(stacks);
        				}
        			}
        		}
	        }
	    });
	}
	public void onDisable(){
		for(RPlayer rp : RPlayer.getOnlines()){
			if(rp.isOnline())rp.getPlayer().kickPlayer(rp.translateString("§2§lRubidia §arestarts and will be available back soon!", "§2§lRubidia §aredémarre et sera très rapidement de nouveau disponible !"));
		}
		
		TagStandManager.onDisable();
		
		for(Team team : Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeams()){
			team.unregister();
		}

		console.sendMessage("§2------------------------------------------------------");
		console.sendMessage("§a   Saving RPlayers...");
		console.sendMessage("§2------------------------------------------------------");
		rcoll.saveAll(true);
		console.sendMessage("§a   Saving Couples...");
		console.sendMessage("§2------------------------------------------------------");
		Couples.save(true);
		console.sendMessage("§2------------------------------------------------------");
		console.sendMessage("§a   Saving Weapons...");
		console.sendMessage("§2------------------------------------------------------");
		Weapons.onDisable();
		console.sendMessage("§a   Saving Guilds...");
		console.sendMessage("§2------------------------------------------------------");
		GuildsPlugin.onEnd();
		console.sendMessage("§a   Saving Quests...");
		console.sendMessage("§2------------------------------------------------------");
		QuestsPlugin.onEnd();
		RubidiaMonstersPlugin.onEnd();
		PetsPlugin.instance.onEnd();
		console.sendMessage("§a   Saving Backpacks...");
		console.sendMessage("§2------------------------------------------------------");
		BackPacks.save(true);
		console.sendMessage("§a   Saving Cities...");
		console.sendMessage("§2------------------------------------------------------");
		Configs.saveCitiesConfig();
		console.sendMessage("§a   Saving Database...");
		console.sendMessage("§2------------------------------------------------------");
		Events.save();
		Configs.getDatabase().set("playersMax", playersMax);
		Configs.saveDatabase();
		console.sendMessage("§a   Rubidia Core Plugin Disabled");
		console.sendMessage("§2------------------------------------------------------");
		Weapons.onDisable();
		
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity instanceof LivingEntity && !(entity  instanceof ArmorStand)){
					entity.remove();
				}
			}
		}
	}
}
