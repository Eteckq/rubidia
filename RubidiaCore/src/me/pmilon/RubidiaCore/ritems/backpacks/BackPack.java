package me.pmilon.RubidiaCore.ritems.backpacks;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BackPack {

	private String BID;
	private HashMap<Integer, ItemStack> content;
	
	private boolean modified = false;
	public BackPack(String BID, HashMap<Integer, ItemStack> content){
		this.BID = BID;
		this.content = content;
	}
	
	public String getBID() {
		return BID;
	}
	
	public void setBID(String bID) {
		BID = bID;
	}

	public HashMap<Integer, ItemStack> getContent() {
		return content;
	}

	public void setContent(HashMap<Integer, ItemStack> content) {
		this.content = content;
	}
	
	public ItemStack getNewItemStack(){
		ItemStack stack = new ItemStack(Material.CHEST_MINECART,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§fSac à dos");
		meta.setLore(Arrays.asList("§7" + this.getBID()));
		stack.setItemMeta(meta);
		return stack;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
