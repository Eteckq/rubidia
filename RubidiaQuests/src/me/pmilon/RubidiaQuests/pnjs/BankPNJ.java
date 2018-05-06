package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaQuests.ui.bank.BankPNJUI;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class BankPNJ extends ActivePNJ {

	public BankPNJ(String uuid, String name, Location loc, int age,
			boolean fix, String dialog) {
		super(uuid, "BANQUIER", "§9§l", name, "§b", PNJType.BANK, loc, age, fix, dialog);
	}

	@Override
	protected void onDelete() {
	}

	@Override
	protected void onTalk(Player player) {
		Core.uiManager.requestUI(new BankPNJUI(player, this));
	}

	@Override
	protected void onSubSave() {
	}

	@Override
	protected void onSpawn(Villager villager) {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		MerchantRecipe recipe1 = new MerchantRecipe(new ItemStack(Material.EMERALD_BLOCK,1), 0, Integer.MAX_VALUE, false);
		recipe1.setIngredients(Arrays.asList(new ItemStack(Material.EMERALD,EconomyHandler.EMERALD_BLOCK_VALUE),new ItemStack(Material.EMERALD,2)));
		MerchantRecipe recipe2 = new MerchantRecipe(new ItemStack(Material.EMERALD,EconomyHandler.EMERALD_BLOCK_VALUE), 0, Integer.MAX_VALUE, false);
		recipe2.setIngredients(Arrays.asList(new ItemStack(Material.EMERALD_BLOCK,1)));
		recipes.addAll(Arrays.asList(recipe1,recipe2));
		villager.setRecipes(recipes);
	}

}
