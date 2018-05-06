package me.pmilon.RubidiaCore.couples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;

public class Couples {

	public static final List<Couple> couples = new ArrayList<Couple>();
	public static final List<CBuff> buffs = new ArrayList<CBuff>();
	
	public static void onEnable(){
		if(Configs.getCouplesConfig().contains("couples")){
			for(String coupleUUID : Configs.getCouplesConfig().getConfigurationSection("couples").getKeys(false)){
				String path = "couples." + coupleUUID;
				Couple couple = new Couple(coupleUUID,
						Core.rcoll.get(Configs.getCouplesConfig().getString(path + ".companion1")),
						Core.rcoll.get(Configs.getCouplesConfig().getString(path + ".companion2")),
						Configs.getCouplesConfig().getLong(path + ".xpTime"),
						Configs.getCouplesConfig().getLong(path + ".weddingDate"));
				couples.add(couple);
			}
		}
		for(String buffId : Configs.getCouplesConfig().getConfigurationSection("buffs").getKeys(false)){
			if(Utils.isInteger(buffId)){
				int id = Integer.valueOf(buffId);
				String path = "buffs." + buffId;
				CBuff buff = new CBuff(id,
						Configs.getCouplesConfig().getString(path + ".name"),
						BuffType.valueOf(Configs.getCouplesConfig().getString(path + ".type")),
						Configs.getCouplesConfig().getInt(path + ".level"),
						Configs.getCouplesConfig().getLong(path + ".xpTime")*60*60*1000L);
				buffs.add(buff);
			}
		}
	}
	
	public static Couple get(String uuid){
		for(Couple couple : couples){
			if(couple.getUUID().equals(uuid)){
				return couple;
			}
		}
		return null;
	}
	
	public static Couple newDefault(RPlayer rp1, RPlayer rp2, long weddingDate){
		Couple couple = new Couple(UUID.randomUUID().toString(), rp1, rp2, 0L, weddingDate);
		couples.add(couple);
		return couple;
	}
	
	public static void save(boolean debug){
		for(Couple couple : couples){
			if(couple.isModified()){
				couple.setModified(false);
				String path = "couples." + couple.getUUID();
				Configs.getCouplesConfig().set(path + ".companion1", couple.getCompanion1().getUniqueId());
				Configs.getCouplesConfig().set(path + ".companion2", couple.getCompanion2().getUniqueId());
				Configs.getCouplesConfig().set(path + ".xpTime", couple.getXPTime());
				Configs.getCouplesConfig().set(path + ".weddingDate", couple.getWeddingDate());
				if(debug)Core.console.sendMessage("§6Saved §e" + couple.getUUID());
			}
		}
		Configs.saveCouplesConfig();
	}
	
	public static List<CBuff> getBuffs(){
		return buffs;
	}
	
	public static List<Couple> getCouples(){
		return couples;
	}
	
}
