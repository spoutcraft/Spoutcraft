/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui.inventory;

import java.util.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.CustomBlock;

public class CreativeTabCustom extends CreativeTabs{
	public CreativeTabCustom(int inty, String string) {
		super(inty, string);
	}

	@Override
	public Item getTabIconItem() {
		return Item.flint;
	}

	@Override
	public String getTranslatedTabLabel() {
		return "Custom Items";
	}

	@Override
	public void displayAllReleventItems(List par1List) {
		CustomItem[] iteml = MaterialData.getCustomItems();
		List<Integer> rawDatas = new ArrayList<Integer>();

		for (CustomItem ci : iteml) {
			if (rawDatas.contains(ci.getRawData())) {
				continue;
			}
			ItemStack is = new ItemStack(ci.getRawId(), 1, ci.getRawData());
			rawDatas.add(ci.getRawData());
			par1List.add(is);
		}
	}
}
