package me.pmilon.RubidiaCore.ritems.backpacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;

public class BackPacks {

	public static int BID_LENGTH = 8;
	private static List<BackPack> backpacks = new ArrayList<BackPack>();
	
	public static void onEnable(){
		if(Configs.getBackpackConfig().contains("backpacks")){
			for(String bid : Configs.getBackpackConfig().getConfigurationSection("backpacks").getKeys(false)){
				String path = "backpacks." + bid;
				HashMap<Integer, ItemStack> content = new HashMap<Integer, ItemStack>();
				if(Configs.getBackpackConfig().contains(path + ".content")){
					for(String id : Configs.getBackpackConfig().getConfigurationSection(path + ".content").getKeys(false)){
						int slot = Integer.valueOf(id);
						content.put(slot, Configs.getBackpackConfig().getItemStack(path + ".content." + id));
					}
				}
				BackPack pack = new BackPack(bid, content);
				backpacks.add(pack);
			}
		}
	}
	
	public static void save(boolean debug){
		for(BackPack pack : backpacks){
			if(pack.isModified()){
				pack.setModified(false);
				String path = "backpacks." + pack.getBID();
				Configs.getBackpackConfig().set(path + ".content", null);
				for(Integer i : pack.getContent().keySet()){
					Configs.getBackpackConfig().set(path + ".content." + i, pack.getContent().get(i));
				}
				if(debug)Core.console.sendMessage("§6Saved §e" + pack.getBID());
			}
		}
		Configs.saveBackpackConfig();
	}
	
	public static BackPack get(String bid){
		for(BackPack pack : backpacks){
			if(pack.getBID().equals(bid)){
				return pack;
			}
		}
		return null;
	}
	
	public static String randomBID(){
		int bidr = Utils.random.nextInt((int) Math.pow(10, BID_LENGTH));
		String bid = String.valueOf(bidr);
		String plus = "";
		if(bid.length() < BID_LENGTH){
			for(int i = 0;i < BID_LENGTH-bid.length();i++){
				plus += "0";
			}
		}
		if(BackPacks.get(plus+bid) != null)return BackPacks.randomBID();
		return plus + bid;
	}
	
	public static ItemStack newBackPack(){
		return BackPacks.newBackPack(BackPacks.randomBID()).getNewItemStack();
	}
	
	public static BackPack newBackPack(String bid){
		BackPack pack = new BackPack(bid, new HashMap<Integer, ItemStack>());
		backpacks.add(pack);
		return pack;
	}
}
