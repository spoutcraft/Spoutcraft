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
package org.spoutcraft.client.entity;

import net.minecraft.src.EntityItem;

import org.spoutcraft.api.entity.Item;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.client.inventory.CraftItemStack;

public class CraftItem extends CraftEntity implements Item {
	private EntityItem item;

	public CraftItem(EntityItem entity) {
		super(entity);
		this.item = entity;
	}

	public ItemStack getItemStack() {
		return new CraftItemStack(item.item);
	}

	public void setItemStack(ItemStack stack) {
		item.item = new net.minecraft.src.ItemStack(stack.getTypeId(), stack.getAmount(), stack.getDurability());
	}

	public int getPickupDelay() {
		return item.delayBeforeCanPickup;
	}

	public void setPickupDelay(int delay) {
		item.delayBeforeCanPickup = delay;
	}

	@Override
	public String toString() {
		return "CraftItem";
	}
}
