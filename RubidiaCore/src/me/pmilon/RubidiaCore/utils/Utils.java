package me.pmilon.RubidiaCore.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class Utils {

	public static final long MILLIS_IN_YEAR = 31556952000L;

	public static int defineEnchantmentCost(Player p, ItemStack item, Map<Enchantment, Integer> enchantms, int level){
		String m = item.getType().toString();
		RPlayer rp = RPlayer.get(p);
		int DCOST = (rp.isVip() ? 47 : 59)+level*8;
		if(m.contains("WOODEN_")){
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += level*(enchantms.get(enchant));
					else DCOST += (level+1)*(enchantms.get(enchant));
				}
			}
		}else if(m.contains("STONE_")){
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += level*2*(enchantms.get(enchant));
					else DCOST += (level*2+2)*(enchantms.get(enchant));
				}
			}
		}else if(m.contains("IRON_")){
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += (level*4+1)*(enchantms.get(enchant));
					else DCOST += (level*5+1)*(enchantms.get(enchant));
				}
			}
		}else if(m.contains("DIAMOND_")){
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += (level*6-1)*(enchantms.get(enchant));
					else DCOST += (level*7-1)*(enchantms.get(enchant));
				}
			}
		}else if(m.contains("GOLD_")){
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += (level*8+2)*(enchantms.get(enchant));
					else DCOST += (level*9+3)*(enchantms.get(enchant));
				}
			}
		}else{
			for(Enchantment enchant : REnchantment.values()){
				if(enchantms.containsKey(enchant)){
					if(rp.isVip())DCOST += level*3*(enchantms.get(enchant));
					else DCOST += (level*4-1)*(enchantms.get(enchant));
				}
			}
		}
		return (DCOST < 0 ? 0 : DCOST)*item.getAmount();
	}

	public static int defineAnvilCost(Player p, ItemStack is) {
		RPlayer rp = RPlayer.get(p);
		int cost = 0;
		ItemMeta im = is.getItemMeta();
		if(im.hasDisplayName()){
			if(rp.isVip())cost += 7;
			else cost += 10;
		}
		if(im.hasEnchants()){
			cost += Utils.defineEnchantmentCost(p, is, im.getEnchants(),2);
		}
		return cost;
	}
	
	public static void updateInventory(final Player p){
		new BukkitTask(Core.instance){
			public void run(){
				p.updateInventory();
				RPlayer.get(p).updateNrj();
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(0);
	}
	
	public static Entity getEntityFromUUID(World w, String uuid){
		for(Entity entity : w.getEntities()){
			if(entity.getUniqueId().toString().equals(uuid))return entity;
		}
		return null;
	}
	
	
	public static List<LivingEntity> getInSightEntities(Player player, RDamageCause cause){
		List<LivingEntity> entities = Core.toDamageableLivingEntityList(player, player.getWorld().getEntities(), cause);
		List<LivingEntity> inSight = new ArrayList<LivingEntity>();
		for(LivingEntity entity : entities){
			if(!entity.equals(player))if(player.hasLineOfSight(entity))inSight.add(entity);
		}
		return inSight;
	}
	
	public static LivingEntity getInSightDamageableEntity(Player player, RDamageCause cause, double range){
		Vector direction = player.getEyeLocation().getDirection().normalize();
		Location check = player.getEyeLocation().add(direction);
		for(int i = 0;i < range/.5;i++){
			for(LivingEntity entity : Core.toDamageableLivingEntityList(player, Arrays.asList(check.getChunk().getEntities()), cause)){
				if(!entity.isDead() && (check.distanceSquared(entity.getLocation()) < .5 || check.distanceSquared(entity.getEyeLocation()) < .5)){
					return entity;
				}
			}
			check.add(direction);
		}
		return null;
	}

	public static ItemStack setGlowingWithoutAttributes(ItemStack is){
        /*net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);*/
		ItemMeta meta = is.getItemMeta();
		meta.addEnchant(Enchantment.LUCK, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);
		return is;
	}

	public static boolean addSafeBuff(Player p, PotionEffect buff){
		if(p.hasPotionEffect(buff.getType())){
			for(PotionEffect active : p.getActivePotionEffects()){
				if(active.getType().equals(buff.getType())){
					if(active.getAmplifier() <= buff.getAmplifier()){
						p.addPotionEffect(buff, true);
						return true;
					}
				}
			}
			return false;
		}else{
			p.addPotionEffect(buff);
			return true;
		}
	}
	/*public static void removeSafeBuff(Player p, PotionEffectType type, int amplifier){
		if(p.hasPotionEffect(type)){
			for(PotionEffect active : p.getActivePotionEffects()){
				if(active.getType().equals(type)){
					if(active.getAmplifier() == amplifier){
						p.removePotionEffect(type);
						return;
					}
				}
			}
		}
	}*/
	
	public static Object hashMapKeyOf(HashMap<?,?> map, Object value){
		for(Object k : map.keySet()){
			if(map.get(k).equals(value))return k;
		}
		return null;
	}
	public static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(Exception ex){return false;}
	}
	public static boolean isDouble(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(Exception ex){return false;}
	}

	public static String valuesOf(Class<?> enum1){
		String result = "";
		Object[] list = enum1.getEnumConstants();
		for(int i = 0;i < list.length;i++){
			result += list[i];
			if(i != list.length-1)result += ", ";
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> mergeLists(List<T>... lists){
		List<T> finalList = new ArrayList<T>();
		if(lists.length > 0){
			List<T> first = lists[0];
			for(T object : first){
				boolean ok = true;
				for(int i = 1;i < lists.length;i++){
					if(!lists[i].contains(object))ok = false;
					
					if(!ok)break;
				}
				if(ok)finalList.add(object);
			}
		}
		return finalList;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	public static <E> List<E> getModifiableCopy(List<E> list){
		List<E> newList = new ArrayList<E>();
		newList.addAll(list);
		return newList;
	}

	public static <E> List<E> toList(E[] es){
		List<E> newEs = new ArrayList<E>();
		newEs.addAll(Arrays.asList(es));
		return newEs;
	}
	
	public static int getAmountCanHold(Player player, ItemStack itemStack){
		int amount = 0;
		for(int slot = 0;slot < 36;slot++){
			if(slot != 8){
				ItemStack item = player.getInventory().getItem(slot);
				if(item == null || item.getType().equals(Material.AIR))amount += 64;
				else if(item.isSimilar(itemStack))amount += 64-item.getAmount();
			}
		}
		return amount;
	}

}
