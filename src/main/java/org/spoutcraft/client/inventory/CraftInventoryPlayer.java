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
package org.spoutcraft.client.inventory;

import net.minecraft.src.InventoryPlayer;

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.inventory.PlayerInventory;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory {
	public CraftInventoryPlayer(net.minecraft.src.InventoryPlayer inventory) {
		super(inventory);
	}

	public InventoryPlayer getInventory() {
		return (InventoryPlayer) inventory;
	}

	public int getSize() {
		return super.getSize() - 4;
	}

	public ItemStack getItemInHand() {
		return new CraftItemStack(getInventory().getCurrentItem());
	}

	public void setItemInHand(ItemStack stack) {
		setItem(getHeldItemSlot(), stack);
	}

	public int getHeldItemSlot() {
		return getInventory().currentItem;
	}

	public ItemStack getHelmet() {
		return getItem(getSize() + 3);
	}

	public ItemStack getChestplate() {
		return getItem(getSize() + 2);
	}

	public ItemStack getLeggings() {
		return getItem(getSize() + 1);
	}

	public ItemStack getBoots() {
		return getItem(getSize() + 0);
	}

	public void setHelmet(ItemStack helmet) {
		setItem(getSize() + 3, helmet);
	}

	public void setChestplate(ItemStack chestplate) {
		setItem(getSize() + 2, chestplate);
	}

	public void setLeggings(ItemStack leggings) {
		setItem(getSize() + 1, leggings);
	}

	public void setBoots(ItemStack boots) {
		setItem(getSize() + 0, boots);
	}

	public CraftItemStack[] getArmorContents() {
		net.minecraft.src.ItemStack[] mcItems = getInventory().armorInventory;
		CraftItemStack[] ret = new CraftItemStack[mcItems.length];

		for (int i = 0; i < mcItems.length; i++) {
			ret[i] = new CraftItemStack(mcItems[i]);
		}
		return ret;
	}

	public void setArmorContents(ItemStack[] items) {
		int cnt = getSize();

		if (items == null) {
			items = new ItemStack[4];
		}
		for (ItemStack item : items) {
			if (item == null || item.getTypeId() == 0) {
				clear(cnt++);
			} else {
				setItem(cnt++, item);
			}
		}
	}
}
