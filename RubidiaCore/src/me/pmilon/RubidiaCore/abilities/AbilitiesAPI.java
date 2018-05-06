package me.pmilon.RubidiaCore.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ItemMessage;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponUse;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class AbilitiesAPI {

	public static final int CLICK_TICKS = 17;
	
	private static List<Ability> abilities = new ArrayList<Ability>();
	
	public static void onEnable(Plugin plugin){
		Abilities.setPlugin(plugin);
	    Bukkit.getServer().getPluginManager().registerEvents(new AbilitiesListener(plugin), plugin);
		for(Player p : Bukkit.getOnlinePlayers()){
			for(int i = 1;i < 8;i++){
				p.setMetadata("activeskill" + i, new FixedMetadataValue(Abilities.getPlugin(), false));
			}
			
			if(RPlayer.get(p).getRClass().equals(RClass.MAGE)){
				p.setMetadata("mageAttack", new FixedMetadataValue(Abilities.getPlugin(), false));
			}
		}
		
		AbilitiesAPI.loadAbilities(plugin);
	}
	public static void loadAbilities(Plugin plugin){
		abilities.clear();
		if(plugin.getConfig().contains("abilities")){
			for(String rclass : plugin.getConfig().getConfigurationSection("abilities").getKeys(false)){
				RClass rClass = RClass.valueOf(rclass);
				for(String id : plugin.getConfig().getConfigurationSection("abilities." + rclass).getKeys(false)){
					int index = Integer.valueOf(id);
					String path = "abilities." + rclass + "." + id;
					String name = plugin.getConfig().getString(path + ".name");
					List<String> description = plugin.getConfig().getStringList(path + ".description");
					boolean passive = plugin.getConfig().getBoolean(path + ".passive");
					String sequence = plugin.getConfig().getString(path + ".sequence");
					double vigorMin = plugin.getConfig().getDouble(path + ".vigorMin");
					double vigorPerLevel = plugin.getConfig().getDouble(path + ".vigorPerLevel");
					double damagesMin = plugin.getConfig().getDouble(path + ".damagesMin");
					double damagesPerLevel = plugin.getConfig().getDouble(path + ".damagesPerLevel");
					int levelMax = plugin.getConfig().getInt(path + ".levelMax");
					Mastery mastery = Mastery.valueOf(plugin.getConfig().getString(path + ".mastery"));
					String suppInfo = plugin.getConfig().getString(path + ".suppInfo");
					String unit = plugin.getConfig().getString(path + ".unit");
					boolean toggleable = plugin.getConfig().getBoolean(path + ".toggleable");
					abilities.add(new Ability(name.split("-"), description, rClass, mastery, index, passive, sequence, levelMax, vigorMin, vigorPerLevel, damagesMin, damagesPerLevel, suppInfo != null ? suppInfo.split("-") : null, unit != null ? unit.split("-") : null, toggleable));
				}
			}
		}
	}
	
	public static List<Ability> getAvailable(RPlayer rp){
		List<Ability> abilities = new ArrayList<Ability>();
		for(Ability ability : AbilitiesAPI.getAvailable(rp.getRClass())){
			if(rp.getAbLevel(ability.getIndex()) > 0 && (!rp.isActiveAbility(ability.getIndex()) || ability.isToggleable())){
				abilities.add(ability);
			}
		}
		return abilities;
	}
	
	public static List<Ability> getAvailable(RClass rClass){
		List<Ability> abilities = new ArrayList<Ability>();
		for(Ability ability : AbilitiesAPI.abilities){
			if(ability.getRClass().equals(rClass)){
				abilities.add(ability);
			}
		}
		return abilities;
	}

	public static Ability getAbility(RClass rClass, int index){
		for(Ability ability : AbilitiesAPI.getAvailable(rClass)){
			if(ability.getIndex() == index)return ability;
		}
		return null;
	}
	
	public static String registerAbilityClick(final RPlayer rp, PlayerInteractEvent e){
		RClass rClass = rp.getRClass();
		RItem rItem = new RItem(e.getItem());
		if(rItem.isWeapon()){
			Weapon weapon = rItem.getWeapon();
			if(weapon.isAttack() && weapon.canUse(rp)){
				if((rClass.equals(RClass.ASSASSIN) || rClass.equals(RClass.PALADIN)) && !weapon.getWeaponUse().equals(WeaponUse.MELEE))return rp.getKeystroke();
				if(rClass.equals(RClass.MAGE) && !weapon.getWeaponUse().equals(WeaponUse.MAGIC))return rp.getKeystroke();
				if(rClass.equals(RClass.RANGER) && !weapon.getWeaponUse().toString().contains("RANGE"))return rp.getKeystroke();
				if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) && !rp.isInCombat()){
					List<Material> paladin = Arrays.asList(Material.LOG, Material.LOG_2, Material.WOOD, Material.WOOD_BUTTON, Material.WOOD_DOOR, Material.WOOD_DOUBLE_STEP, Material.WOOD_STAIRS, Material.WOOD_STEP, Material.WOODEN_DOOR, Material.CHEST, Material.FENCE, Material.FENCE_GATE, Material.JUKEBOX, Material.BOOKSHELF, Material.JACK_O_LANTERN, Material.PUMPKIN, Material.SIGN, Material.SIGN_POST, Material.WALL_SIGN, Material.WOOD_PLATE, Material.COCOA);
					if(rClass.equals(RClass.PALADIN)){
						if(paladin.contains(e.getClickedBlock().getType()))return rp.getKeystroke();
					}else if(rClass.equals(RClass.ASSASSIN)){
						if(e.getClickedBlock().getType().equals(Material.WEB))return rp.getKeystroke();
					}
				}else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					if(rClass.equals(RClass.MAGE) && weapon.getType().toString().contains("_HOE") && !rp.isInCombat()){
						if(e.getClickedBlock().getType().equals(Material.GRASS) || e.getClickedBlock().getType().equals(Material.DIRT))return rp.getKeystroke();
					}
				}
				
				List<Ability> available = AbilitiesAPI.getAvailable(rp);
				if(available.isEmpty())return rp.getKeystroke();
				Collections.sort(available, new Comparator<Ability>(){
					
			        public int compare(Ability ab1, Ability ab2) {
			            return ab1.getIndex()-ab2.getIndex();
			        }
			        
				});
				String click = e.getAction().toString().contains("LEFT_CLICK") ? "G" : (e.getAction().toString().contains("RIGHT_CLICK") ? "D" : "");
				rp.setKeystroke(rp.getKeystroke() + click);
				
				if(rp.getKeystrokeTaskId() != -1)BukkitTask.tasks.get(rp.getKeystrokeTaskId()).cancel();
				rp.setKeystrokeTaskId(new BukkitTask(Core.instance){

					@Override
					public void run() {
						rp.setKeystroke("");
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(CLICK_TICKS).getTaskId());
				
				for(Ability ability : available){
					if(!ability.isPassive()){
						String[] sequence = ability.getSequence().split(",");
						if(sequence[0].startsWith(rp.getKeystroke())){
							String[] seq = sequence[0].split("");
							if(sequence.length > 1){
								if(sequence[1].contains("!SN") && e.getPlayer().isSneaking())continue;
								else if(sequence[1].contains("SN") && !e.getPlayer().isSneaking())continue;
								
								if(sequence[1].contains("!SP") && e.getPlayer().isSprinting())continue;
								else if(sequence[1].contains("SP") && !e.getPlayer().isSprinting())continue;
							}
							
							int clicks = rp.getKeystroke().length();
							String keystroke = "§7";
							if(e.getPlayer().isSneaking())keystroke += "Sneak + ";
							if(e.getPlayer().isSprinting())keystroke += "Sprint + ";
							if(clicks > 0)keystroke += rp.translateString("", "Clic ");
							for(int i = 0;i < clicks;i++){
								if(i != 0)keystroke += "§f/§7";
								if(seq[i].equals("D"))keystroke += rp.translateString("Right", "Droit");
								else if(seq[i].equals("G"))keystroke += rp.translateString("Left", "Gauche");
							}
							for(int i = clicks;i < seq.length;i++){
								if(i != seq.length)keystroke += "§f/§8";
								if(seq[i].equals("D"))keystroke += rp.translateString("Right", "Droit");
								else if(seq[i].equals("G"))keystroke += rp.translateString("Left", "Gauche");
							}
							if(clicks > 0)keystroke += rp.translateString(" §7Click", "");
							
							ItemMessage.sendMessage(e.getPlayer(), keystroke, CLICK_TICKS);
							if(rp.getClickSound()){
								if(click.equals("G"))e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, .5F);
								else if(click.equals("D"))e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
							}
							break;
						}
					}
				}
			}
		}
		
		return rp.getKeystroke();
	}
}
