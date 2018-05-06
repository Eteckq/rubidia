package me.pmilon.RubidiaCore.ritems.general;

import java.util.Arrays;

import me.pmilon.RubidiaCore.ritems.backpacks.BackPack;
import me.pmilon.RubidiaCore.ritems.backpacks.BackPacks;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.Scrolls;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RItem {
	
	private ItemStack item;
	private Weapon weaponReference;
	private BackPack backpackReference;
	private Scroll scrollReference;
	public RItem(ItemStack item){
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public boolean isWeapon(){
		if(this.weaponReference != null){
			if(this.weaponReference.getRarity() != null){
				return true;
			}
		}
		
		if(this.getItem().hasItemMeta()){
			ItemMeta meta = this.getItem().getItemMeta();
			if(meta.getDisplayName() != null && meta.getLore() != null){
				this.weaponReference = new Weapon(this);
				return this.weaponReference.getRarity() != null;
			}
		}
		return false;
	}

	public boolean isBackPack(){
		if(this.backpackReference != null){
			return true;
		}
		
		if(this.getItem().getType().equals(Material.STORAGE_MINECART)){
			if(this.getItem().hasItemMeta()){
				ItemMeta meta = this.getItem().getItemMeta();
				if(meta.hasDisplayName()){
					if(meta.getDisplayName().contains("Sac à dos")){
						if(meta.getLore() != null){
							String bid = ChatColor.stripColor(meta.getLore().get(0));
							this.backpackReference = BackPacks.get(bid);
						}else{
							String bid = BackPacks.randomBID();
							meta.setLore(Arrays.asList("§7" + bid));
							this.getItem().setItemMeta(meta);
							this.backpackReference = BackPacks.newBackPack(bid);
						}
						return this.backpackReference != null;
					}
				}
			}
		}
		return false;
	}

	public boolean isScroll(){
		if(this.scrollReference != null){
			return true;
		}

		if(this.getItem().getType().equals(Material.EMPTY_MAP)){
			if(this.getItem().hasItemMeta()){
				ItemMeta meta = this.getItem().getItemMeta();
				if(meta.hasDisplayName()){
					if(meta.getDisplayName().contains("scroll") || meta.getDisplayName().contains("Parchemin")){
						this.scrollReference = Scrolls.get(this.getItem());
						return this.scrollReference != null;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isCustom(){
		for(RItemStack item : RItemStacks.ITEMS){
			if(item.getItemStack().isSimilar(this.getItem())){
				return true;
			}
		}
		return false;
	}
	
	public BackPack getBackPack(){
		return this.backpackReference;
	}
	
	public Weapon getWeapon(){
		return this.weaponReference;
	}
	
	public Scroll getScroll(){
		return this.scrollReference;
	}
}
