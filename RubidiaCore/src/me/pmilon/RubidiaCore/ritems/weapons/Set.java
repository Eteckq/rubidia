package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Set {

	private String UUID;
	private String name;
	private List<Buff> buffs;
	private List<Weapon> weapons;
	
	private boolean modified = false;
	public Set(String UUID, String name, List<Buff> buffs, List<Weapon> weapons){
		this.UUID = UUID;
		this.name = name;
		this.buffs = buffs;
		this.weapons = weapons;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
		this.setModified(true);
	}

	public List<Buff> getBuffs() {
		return buffs;
	}

	public void setBuffs(List<Buff> buffs) {
		this.buffs = buffs;
		this.setModified(true);
	}

	public List<Weapon> getWeapons() {
		weapons.removeAll(Collections.singleton(null));
		return weapons;
	}

	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
		this.setModified(true);
	}
	
	public List<Buff> getActiveBuffs(LivingEntity entity){
		List<Buff> available = new ArrayList<Buff>();
		if(!this.getBuffs().isEmpty() && !this.getWeapons().isEmpty()){
			EntityEquipment equipment = entity.getEquipment();
			double count = 0;
			for(Weapon weapon : this.getWeapons()){
				if(weapon.getType().toString().contains("_HELMET")){
					ItemStack stack = equipment.getHelmet();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon helmet = rItem.getWeapon();
							if(helmet.isSimilar(weapon))count++;
						}
					}
				}else if(weapon.getType().toString().contains("_CHESTPLATE")){
					ItemStack stack = equipment.getChestplate();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon chestplate = rItem.getWeapon();
							if(chestplate.isSimilar(weapon))count++;
						}
					}
				}else if(weapon.getType().toString().contains("_LEGGINGS")){
					ItemStack stack = equipment.getLeggings();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon leggings = rItem.getWeapon();
							if(leggings.isSimilar(weapon))count++;
						}
					}
				}else if(weapon.getType().toString().contains("_BOOTS")){
					ItemStack stack = equipment.getBoots();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon boots = rItem.getWeapon();
							if(boots.isSimilar(weapon))count++;
						}
					}
				}else if(weapon.getType().equals(Material.SHIELD)){
					ItemStack stack = equipment.getItemInOffHand();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon shield = rItem.getWeapon();
							if(shield.isSimilar(weapon))count++;
						}
					}
				}else if(weapon.isAttack()){
					ItemStack stack = equipment.getItemInMainHand();
					if(stack != null){
						RItem rItem = new RItem(stack);
						if(rItem.isWeapon()){
							Weapon arme = rItem.getWeapon();
							if(arme.isSimilar(weapon))count++;
						}
					}
				}
			}
			if(count > 1 || this.getWeapons().size() == 1){
				double proportion = count/this.getWeapons().size();
				int buffCount = (int) (this.getBuffs().size()*proportion);
				for(int i = 0;i < buffCount;i++){
					available.add(this.getBuffs().get(i));
				}
			}
		}
		return available;
	}
	
	public List<String> getWeaponState(Player player){
		List<String> state = new ArrayList<String>();
		if(!this.getWeapons().isEmpty()){
			EntityEquipment equipment = null;
			if(player != null)equipment = player.getEquipment();
			for(Weapon weapon : this.getWeapons()){
				boolean equipped = false;
				if(player != null){
					if(weapon.getType().toString().contains("_HELMET")){
						ItemStack stack = equipment.getHelmet();
						if(stack != null){
							RItem rItem = new RItem(stack);
							if(rItem.isWeapon()){
								Weapon helmet = rItem.getWeapon();
								if(helmet.isSimilar(weapon))equipped = true;
							}
						}
					}else if(weapon.getType().toString().contains("_CHESTPLATE")){
						ItemStack stack = equipment.getChestplate();
						if(stack != null){
							RItem rItem = new RItem(stack);
							if(rItem.isWeapon()){
								Weapon chestplate = rItem.getWeapon();
								if(chestplate.isSimilar(weapon))equipped = true;
							}
						}
					}else if(weapon.getType().toString().contains("_LEGGINGS")){
						ItemStack stack = equipment.getLeggings();
						if(stack != null){
							RItem rItem = new RItem(stack);
							if(rItem.isWeapon()){
								Weapon leggings = rItem.getWeapon();
								if(leggings.isSimilar(weapon))equipped = true;
							}
						}
					}else if(weapon.getType().toString().contains("_BOOTS")){
						ItemStack stack = equipment.getBoots();
						if(stack != null){
							RItem rItem = new RItem(stack);
							if(rItem.isWeapon()){
								Weapon boots = rItem.getWeapon();
								if(boots.isSimilar(weapon))equipped = true;
							}
						}
					}
				}
				state.add("    " + (equipped ? "§a" : "§8") + weapon.getName());
			}
		}
		return state;
	}
	
	public List<String> getBuffState(Player player){
		RPlayer rp = player != null ? RPlayer.get(player) : null;
		List<String> states = new ArrayList<String>();
		List<Buff> available = player != null ? this.getActiveBuffs(player) : new ArrayList<Buff>();
		for(Buff buff : this.getBuffs()){
			String state = (player != null ? "    " : "") + (available.contains(buff) ? "§e" : "§8") + (buff.getFactor() >= 0 ? "+" : "") + (Utils.round(buff.getFactor()*100, 1)) + "% " + (player != null ? (buff.getType().getDisplayFr()) : buff.getType().getDisplayFr());
			if(player == null)state = ChatColor.stripColor(state);
			states.add(state);
		}
		return states;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setModified(true);
	}
	
	public void delete(Buff buff){
		this.getBuffs().remove(buff);
		Configs.getWeaponsConfig().set("sets." + this.getUUID() + ".buffs." + buff.getId(), null);
		this.setModified(true);
	}
	
	public void saveWeapons(){
		List<String> save = new ArrayList<String>();
		for(Weapon weapon : this.getWeapons()){
			save.add(weapon.getUUID());
		}
		Configs.getWeaponsConfig().set("sets." + this.getUUID() + ".weaponsUUIDs", save);
	}
	
	public void saveBuffs(){
		for(Buff buff : this.getBuffs()){
			String path = "sets." + this.getUUID() + ".buffs." + buff.getId();
			Configs.getWeaponsConfig().set(path + ".type", buff.getType().toString());
			Configs.getWeaponsConfig().set(path + ".level", buff.getLevel());
		}
	}

	public static double getAdditionalFactor(LivingEntity entity, BuffType... types){
		double factor = 0;
		if(entity != null){
			List<Set> sets = new ArrayList<Set>();
			for(ItemStack armor : entity.getEquipment().getArmorContents()){
				if(armor != null){
					RItem rItem = new RItem(armor);
					if(rItem.isWeapon()){
						Weapon weapon = rItem.getWeapon();
						if(weapon != null){
							if(weapon.isSetItem()){
								Set set = weapon.getSet();
								if(!sets.contains(set)){
									for(Buff buff : weapon.getSet().getActiveBuffs(entity)){
										for(BuffType type : types){
											if(buff.getType().equals(type)){
												factor += buff.getFactor();
											}
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
		return factor;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
