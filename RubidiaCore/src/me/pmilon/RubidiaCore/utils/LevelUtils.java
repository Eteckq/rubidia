package me.pmilon.RubidiaCore.utils;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class LevelUtils {

	public static double getRLevelTotalExp(RPlayer rp){
		return LevelUtils.getRLevelTotalExp(rp.getRLevel());
	}
	
	public static double getRLevelTotalExp(int level){
		if(level < Mastery.ADVENTURER.getLevel())return (level+1)*9.6+19.2;
		else if(level < Mastery.ASPIRANT.getLevel())return (level+1)*63.1+116.6;
		else if(level < Mastery.MASTER.getLevel())return (level+1)*86.4+204.9;
		else if(level < Mastery.HERO.getLevel())return (level+1)*101.9+304.9;
		else return (level+1)*109.3+526.4;
	}

	public static double getRExpForBlock(Material type){
		if(type.equals(Material.COAL_ORE))return .45;
		else if(type.equals(Material.IRON_ORE))return .6;
		else if(type.equals(Material.GOLD_ORE))return 1.15;
		else if(type.equals(Material.DIAMOND_ORE))return 3.6;
		else if(type.equals(Material.REDSTONE_ORE))return 1.15;
		else if(type.equals(Material.LAPIS_ORE))return 2.1;
		else if(type.equals(Material.EMERALD_ORE))return 5.4;
		else if(type.equals(Material.NETHER_QUARTZ_ORE))return 1.23;
		else return 0.0;
	}
	
	public static void firework(Location location) {
		Firework f = location.getWorld().spawn(location, Firework.class);
		Firework f2 = location.getWorld().spawn(location, Firework.class);
		Firework f3 = location.getWorld().spawn(location, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		FireworkMeta fm1 = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder()
				.flicker(false)
				.trail(true)
				.with(Type.STAR)
				.withColor(Color.AQUA)
				.withColor(Color.BLUE)
				.withFade(Color.AQUA)
				.withFade(Color.BLUE)
				.withFade(Color.WHITE)
				.build());
		fm.setPower(0);
		fm1.addEffect(FireworkEffect.builder()
				.flicker(false)
				.trail(true)
				.with(Type.BALL)
				.withColor(Color.RED)
				.withColor(Color.ORANGE)
				.withColor(Color.YELLOW)
				.withFade(Color.RED)
				.withFade(Color.ORANGE)
				.withFade(Color.YELLOW)
				.withFade(Color.WHITE)
				.build());
		fm1.setPower(0);
		f.setFireworkMeta(fm);
		f2.setFireworkMeta(fm1);
		f3.setFireworkMeta(fm);
	}

}
