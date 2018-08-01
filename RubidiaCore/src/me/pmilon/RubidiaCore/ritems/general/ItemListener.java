package me.pmilon.RubidiaCore.ritems.general;

import java.util.Collections;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.regions.Monster;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ItemListener implements Listener {

	private Plugin plugin;
	public ItemListener(Plugin plugin){
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onKill(MonsterKillEvent event){
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		Monster monster = event.getMonster();
		LivingEntity entity = monster.getEntity();
		
		int lootFactor = 1;
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.hasItemMeta()){
			ItemMeta meta = item.getItemMeta();
			if(meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)){
				lootFactor += meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS)*Settings.ENCHANTMENT_LOOT_BONUS_FACTOR;
			}
		}
		
		for(RItemStack drop : RItemStacks.ITEMS){
			if(Utils.random.nextInt(drop.getDropRarity()) < 1){
				ItemStack dropItem = drop.getItemStack();
				dropItem.setAmount(dropItem.getAmount()*lootFactor);
				entity.getWorld().dropItemNaturally(entity.getLocation(), dropItem);
				//break;//we only want one drop aaahaahahaaah
			}
		}
		
		int rarityProb = Utils.random.nextInt(1000000);
		Rarity rarity = null;
		for(int i = Rarity.values().length-1;i >= 0;i--){
			rarity = Rarity.values()[i];
			if(rarity.getFactor()*(1+rp.getLootBonusChanceFactor())*1000000 > rarityProb){
				break;
			}
		}
		
		int probability = Utils.random.nextInt(1000000);
		List<Weapon> weaponsLevel = Weapons.getByLevel(monster.getLevel(), (int) (6+(monster.getLevel()/30.0)));
		List<Weapon> weaponsRarity = Weapons.getByRarity(rarity);
		List<Weapon> available = Utils.mergeLists(weaponsLevel, weaponsRarity);
		Collections.shuffle(available);
		for(Weapon weapon : available){
			if(weapon.getDropChance()*1000000/Settings.GLOBAL_WEAPON_DROP_REDUCTION > probability){
				int supp = Utils.random.nextInt(100);
				int suppLevel = 0;
				if(supp >= 49)suppLevel++;
				if(supp >= 74)suppLevel++;
				if(supp >= 88)suppLevel++;
				Weapon toDrop = weapon.getNewInstance();
				toDrop.setSuppLevel(suppLevel);
				entity.getWorld().dropItemNaturally(entity.getLocation(), toDrop.getNewItemStack(rp));
				break;
			}
		}
	}
	
	@EventHandler
	public void onPickUp(EntityPickupItemEvent event){
		if(event.getItem().hasMetadata("unpickable")){
			event.setCancelled(true);
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
}
