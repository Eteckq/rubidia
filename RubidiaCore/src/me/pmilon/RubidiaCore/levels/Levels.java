package me.pmilon.RubidiaCore.levels;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Levels {

	public static double getRLevelTotalExp(RPlayer rp){
		return Levels.getRLevelTotalExp(rp.getRLevel());
	}
	
	public static double getRLevelTotalExp(int level){
		return 100.+9900.*Math.pow(level/150.,2);
	}

	public static double getRExpFactorForBlock(Material type){
		if(type.equals(Material.COAL_ORE))return 1/500.;
		else if(type.equals(Material.IRON_ORE))return 1/350.;
		else if(type.equals(Material.GOLD_ORE))return 1/150.;
		else if(type.equals(Material.DIAMOND_ORE))return 1/150.;
		else if(type.equals(Material.REDSTONE_ORE))return 1/400.;
		else if(type.equals(Material.LAPIS_ORE))return 1/150.;
		else if(type.equals(Material.EMERALD_ORE))return 1/200.;
		else if(type.equals(Material.NETHER_QUARTZ_ORE))return 1/750.;
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