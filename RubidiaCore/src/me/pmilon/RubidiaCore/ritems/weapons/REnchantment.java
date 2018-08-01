package me.pmilon.RubidiaCore.ritems.weapons;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.Core;

public class REnchantment {

	public static List<Enchantment> WEAPONS_ENCHANTMENTS = Arrays.asList(Enchantment.DAMAGE_ALL,
															Enchantment.DAMAGE_ARTHROPODS,
															Enchantment.DAMAGE_UNDEAD,
															Enchantment.FIRE_ASPECT,
															Enchantment.KNOCKBACK,
															Enchantment.LOOT_BONUS_MOBS,
															Enchantment.LUCK);
	
	public static Enchantment SOUL_BIND = new Enchantment(new NamespacedKey(Core.instance, "Soul Bind")){

		@Override
		public boolean canEnchantItem(ItemStack item) {
			return true;
		}

		@Override
		public boolean conflictsWith(Enchantment arg0) {
			return false;
		}

		@Override
		public EnchantmentTarget getItemTarget() {
			return EnchantmentTarget.ALL;
		}

		@Override
		public int getMaxLevel() {
			return 5;
		}

		@Override
		public String getName() {
			return "SOUL_BIND";
		}

		@Override
		public int getStartLevel() {
			return 1;
		}

		@Override
		public boolean isCursed() {
			return false;
		}

		@Override
		public boolean isTreasure() {
			return false;
		}
		
	};
	
	public static void registerEnchantments(){
		try {
		    Field f = Enchantment.class.getDeclaredField("acceptingNew");
		    f.setAccessible(true);
		    f.set(null, true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		Enchantment.registerEnchantment(REnchantment.SOUL_BIND);
	}
	
	public static Enchantment[] values(){
		return new Enchantment[]{Enchantment.ARROW_DAMAGE,
				Enchantment.ARROW_FIRE,
				Enchantment.ARROW_INFINITE,
				Enchantment.ARROW_KNOCKBACK,
				Enchantment.DAMAGE_ALL,
				Enchantment.DAMAGE_ARTHROPODS,
				Enchantment.DAMAGE_UNDEAD,
				Enchantment.DEPTH_STRIDER,
				Enchantment.DIG_SPEED,
				Enchantment.DURABILITY,
				Enchantment.FIRE_ASPECT,
				Enchantment.FROST_WALKER,
				Enchantment.KNOCKBACK,
				Enchantment.LOOT_BONUS_BLOCKS,
				Enchantment.LOOT_BONUS_MOBS,
				Enchantment.LUCK,
				Enchantment.LURE,
				Enchantment.MENDING,
				Enchantment.OXYGEN,
				Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.PROTECTION_EXPLOSIONS,
				Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,
				Enchantment.PROTECTION_PROJECTILE,
				Enchantment.SILK_TOUCH,
				Enchantment.THORNS,
				Enchantment.WATER_WORKER,
				REnchantment.SOUL_BIND};
	}

}
