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

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.material.Material;

public class CraftItemStack extends ItemStack {
	protected net.minecraft.src.ItemStack item;

	public CraftItemStack(net.minecraft.src.ItemStack item) {
		super(
			item != null ? item.itemID: 0,
			item != null ? item.stackSize : 0,
			(short)(item != null ? item.getItemDamage() : 0)
		);
		this.item = item;
	}

	/* 'Overwritten' constructors from ItemStack, yay for Java sucking */
	public CraftItemStack(final int type) {
		this(type, 0);
	}

	public CraftItemStack(final Material type) {
		this(type, 0);
	}

	public CraftItemStack(final int type, final int amount) {
		this(type, amount, (byte) 0);
	}

	public CraftItemStack(final Material type, final int amount) {
		this(type.getRawId(), amount);
	}

	public CraftItemStack(final int type, final int amount, final short damage) {
		this(type, amount, damage, null);
	}

	public CraftItemStack(final Material type, final int amount, final short damage) {
		this(type.getRawId(), amount, damage);
	}

	public CraftItemStack(final Material type, final int amount, final short damage, final Byte data) {
		this(type.getRawId(), amount, damage, data);
	}

	public CraftItemStack(int type, int amount, short damage, Byte data) {
		this(new net.minecraft.src.ItemStack(type, amount, data != null ? data : damage));
	}

	/*
	 * Unsure if we have to sync before each of these calls the values in 'item'
	 * are all public.
	 */

	@Override
	public Material getType() {
		super.setTypeId(item != null ? item.itemID : 0); // sync, needed?
		return super.getType();
	}

	@Override
	public int getTypeId() {
		super.setTypeId(item != null ? item.itemID : 0); // sync, needed?
		return item != null ? item.itemID : 0;
	}

	@Override
	public void setTypeId(int type) {
		if (type == 0) {
			super.setTypeId(0);
			super.setAmount(0);
			item = null;
		} else {
			if (item == null) {
				item = new net.minecraft.src.ItemStack(type, 1, 0);
				super.setAmount(1);
			} else {
				item.itemID = type;
				super.setTypeId(item.itemID);
			}
		}
	}

	@Override
	public int getAmount() {
		super.setAmount(item != null ? item.stackSize : 0); // sync, needed?
		return (item != null ? item.stackSize : 0);
	}

	@Override
	public void setAmount(int amount) {
		if (amount == 0) {
			super.setTypeId(0);
			super.setAmount(0);
			item = null;
		} else {
			super.setAmount(amount);
			item.stackSize = amount;
		}
	}

	@Override
	public void setDurability(final short durability) {
		// Ignore damage if item is null
		if (item != null) {
			super.setDurability(durability);
			item.setItemDamage(durability);
		}
	}

	@Override
	public short getDurability() {
		if (item != null) {
			super.setDurability((short) item.getItemDamage()); // sync, needed?
			return (short) item.getItemDamage();
		} else {
			return -1;
		}
	}

	@Override
	public int getMaxStackSize() {
		return item.getItem().getItemStackLimit();
	}
}
