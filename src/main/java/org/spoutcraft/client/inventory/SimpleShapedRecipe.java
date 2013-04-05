/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.inventory;

import java.util.HashMap;

import net.minecraft.src.CraftingManager;

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.inventory.ShapedRecipe;
import org.spoutcraft.api.material.Material;

public class SimpleShapedRecipe extends ShapedRecipe implements SpoutcraftRecipe {
	public SimpleShapedRecipe(ItemStack result) {
		super(result);
	}

	public static SimpleShapedRecipe fromSpoutRecipe(ShapedRecipe recipe) {
		if (recipe instanceof SimpleShapedRecipe) {
			return (SimpleShapedRecipe) recipe;
		}
		SimpleShapedRecipe ret = new SimpleShapedRecipe(recipe.getResult());
		String[] shape = recipe.getShape();
		ret.shape(shape);
		for (char c : recipe.getIngredientMap().keySet()) {
			ret.setIngredient(c, recipe.getIngredientMap().get(c));
		}
		return ret;
	}

	public void addToCraftingManager() {
		Object[] data;
		String[] shape = this.getShape();
		HashMap<Character, Material> ingred = this.getIngredientMap();
		int datalen = shape.length;
		datalen += ingred.size() * 2;
		int i = 0;
		data = new Object[datalen];
		for (; i < shape.length; i++) {
			data[i] = shape[i];
		}
		for (char c : ingred.keySet()) {
			data[i] = c;
			i++;
			Material mdata = ingred.get(c);

			int id = mdata.getRawId();
			int dmg = mdata.getRawData();

			data[i] = new net.minecraft.src.ItemStack(id, 1, dmg);
			i++;
		}
		int id = this.getResult().getTypeId();
		int amount = this.getResult().getAmount();
		short durability = this.getResult().getDurability();
		CraftingManager.getInstance().addRecipe(new net.minecraft.src.ItemStack(id, amount, durability), data);
	}
}
