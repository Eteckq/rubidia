package me.pmilon.RubidiaCore.crafts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Crafts {

	public static final List<Material> CRAFTS_TO_REPLACE = Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
			Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
			Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET,
			Material.GOLDEN_BOOTS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET,
			Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,
			Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL,
			Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL,
			Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL,
			Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL);//does not contain leggings, which we modify entirely
	public static final List<String> LORE = Arrays.asList("§8Cet item peut être éveillé auprès d'un forgeron");
	
	public static void initialize(Plugin plugin) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		//first we remove crafts of every weapons
	    Iterator<Recipe> iterator = plugin.getServer().recipeIterator();
	    while(iterator.hasNext()) {
	    	Recipe recipe = iterator.next();
	    	if(recipe != null) {
	    		Material type = recipe.getResult().getType();
	    		if(type.toString().contains("_LEGGINGS") || Crafts.CRAFTS_TO_REPLACE.contains(type)) {
	    			if(Crafts.CRAFTS_TO_REPLACE.contains(type)) {
		    	    	recipes.add(recipe);
		    	    	System.out.println(recipe.getResult().getType().toString() + " :: " + (recipe instanceof ShapedRecipe) + " " + (recipe instanceof ShapelessRecipe));
		    		}
	    			iterator.remove();
	    		}
	    	}
	    }
	    
	    //we add them back, with modified lores
	    for(Recipe recipe : recipes) {
	    	ItemStack item = new ItemStack(recipe.getResult().getType(), 1);
	    	ItemMeta meta = item.getItemMeta();
	    	meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	meta.setLore(Crafts.LORE);
	    	item.setItemMeta(meta);
	    	if(recipe instanceof ShapedRecipe) {
	    		ShapedRecipe recipe2 = (ShapedRecipe)recipe;
		    	ShapedRecipe shapedRecipe = new ShapedRecipe(recipe2.getKey(), item).shape(recipe2.getShape());
		    	for(Character chr : recipe2.getChoiceMap().keySet()) {
		    		shapedRecipe.setIngredient(chr, recipe2.getChoiceMap().get(chr));
		    	}
		    	for(Character chr : recipe2.getIngredientMap().keySet()) {
		    		ItemStack stack = recipe2.getIngredientMap().get(chr);
		    		if(stack != null) {
			    		shapedRecipe.setIngredient(chr, stack.getType());
			    		shapedRecipe.setIngredient(chr, stack.getData());
		    		}
		    	}
		    	plugin.getServer().addRecipe(shapedRecipe);
	    	}else if(recipe instanceof ShapelessRecipe) {
	    		ShapelessRecipe recipe2 = (ShapelessRecipe)recipe;
	    		ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipe2.getKey(), item);
	    		for(RecipeChoice ingredient : recipe2.getChoiceList()) {
	    			shapelessRecipe.addIngredient(ingredient);
	    		}
	    		for(ItemStack ingredient : recipe2.getIngredientList()) {
	    			shapelessRecipe.addIngredient(ingredient.getType());
	    			shapelessRecipe.addIngredient(ingredient.getData());
	    		}
		    	plugin.getServer().addRecipe(shapelessRecipe);
	    	}
	    }
	    
	    //we add crafts for chainmail elements
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailHelmet"), new ItemStack(Material.CHAINMAIL_HELMET)).shape(new String[] { "#$#", "$ $", "   " }).setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailChestplate"), new ItemStack(Material.CHAINMAIL_CHESTPLATE)).shape(new String[] { "$ $", "#$#", "$#$" }).setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailBoots"), new ItemStack(Material.CHAINMAIL_BOOTS)).shape(new String[] { "   ", "# #", "$ $" }).setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		
		//we add crafts for gauntlets
		ItemStack leatherGauntlet = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemMeta leatherMeta = leatherGauntlet.getItemMeta();
		leatherMeta.setDisplayName("§fGants en cuir");
		leatherMeta.setLore(Crafts.LORE);
		leatherMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		leatherGauntlet.setItemMeta(leatherMeta);
		ItemStack chainmailGauntlet = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		ItemMeta chainmailMeta = chainmailGauntlet.getItemMeta();
		chainmailMeta.setDisplayName("§fGants de mailles");
		chainmailMeta.setLore(Crafts.LORE);
		chainmailMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		chainmailGauntlet.setItemMeta(chainmailMeta);
		ItemStack ironGauntlet = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemMeta ironMeta = ironGauntlet.getItemMeta();
		ironMeta.setDisplayName("§fGants en fer");
		ironMeta.setLore(Crafts.LORE);
		ironMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ironGauntlet.setItemMeta(ironMeta);
		ItemStack diamondGauntlet = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemMeta diamondMeta = diamondGauntlet.getItemMeta();
		diamondMeta.setDisplayName("§fGants en diamant");
		diamondMeta.setLore(Crafts.LORE);
		diamondMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		diamondGauntlet.setItemMeta(diamondMeta);
		ItemStack goldenGauntlet = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
		ItemMeta goldenMeta = goldenGauntlet.getItemMeta();
		goldenMeta.setDisplayName("§fGants en or");
		goldenMeta.setLore(Crafts.LORE);
		goldenMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		goldenGauntlet.setItemMeta(goldenMeta);
		
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "leatherGauntlet"), leatherGauntlet).shape(new String[] { " # ", "###", "## " }).setIngredient('#', Material.LEATHER));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailGauntlet"), chainmailGauntlet).shape(new String[] { " # ", "#$#", "$# " }).setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "ironGauntlet"), ironGauntlet).shape(new String[] { " # ", "###", "## " }).setIngredient('#', Material.IRON_INGOT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "diamondGauntlet"), diamondGauntlet).shape(new String[] { " # ", "###", "## " }).setIngredient('#', Material.DIAMOND));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "goldenGauntlet"), goldenGauntlet).shape(new String[] { " # ", "###", "## " }).setIngredient('#', Material.GOLD_INGOT));
	}
	
}
