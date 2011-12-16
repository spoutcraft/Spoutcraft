package org.spoutcraft.client.inventory;

import java.util.ArrayList;

import net.minecraft.src.CraftingManager;

import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.ShapelessRecipe;
import org.spoutcraft.spoutcraftapi.material.Material;

public class SimpleShapelessRecipe extends ShapelessRecipe implements SpoutcraftRecipe {
	
	public SimpleShapelessRecipe(ItemStack result) {
		super(result);
	}

	public static SimpleShapelessRecipe fromSpoutRecipe(ShapelessRecipe recipe) {
		if (recipe instanceof SimpleShapelessRecipe) {
			return (SimpleShapelessRecipe) recipe;
		}
		SimpleShapelessRecipe ret = new SimpleShapelessRecipe(recipe.getResult());
		for (Material ingred : recipe.getIngredientList()) {
			ret.addIngredient(ingred);
		}
		return ret;
	}

	public void addToCraftingManager() {
		ArrayList<Material> ingred = this.getIngredientList();
		Object[] data = new Object[ingred.size()];
		int i = 0;
		for (Material mdata : ingred) {
			int id = mdata.getRawId();
			int dmg = mdata.getRawData();
			data[i] = new net.minecraft.src.ItemStack(id, 1, dmg);
			i++;
		}
		int id = this.getResult().getTypeId();
		int amount = this.getResult().getAmount();
		short durability = this.getResult().getDurability();
		CraftingManager.getInstance().addShapelessRecipe(new net.minecraft.src.ItemStack(id, amount, durability), data);
	}
}
