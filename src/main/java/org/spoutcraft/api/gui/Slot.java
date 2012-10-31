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
package org.spoutcraft.api.gui;

import org.spoutcraft.api.inventory.ItemStack;

/**
 * Represents one item slot where items can be put in and out.
 */
public interface Slot extends Control {
	/**
	 * Gets the item of this slot
	 * @return the item of the slot
	 */
	public ItemStack getItem();

	/**
	 * Sets a new item to this slot
	 * @param item the new item
	 * @return the instance
	 */
	public Slot setItem(ItemStack item);

	/**
	 * Called when the user puts an item into the slot.
	 * @param item the stack that the user wants to put in. \
	 * The amount property in the stack will be calculated correctly, for example when the user right-clicks on the slot, it will only give one item.
	 * @return false if you want to cancel that.
	 */
	public boolean onItemPut(ItemStack item);

	/**
	 * Called when the user takes the item out of the slot
	 * @param item the stack that the user will get. \
	 * The amount property in the stack will be calculated correctly,for example when the user right-clicks on the slot, it would split the amount.
	 * @return false if you want to cancel that.
	 */
	public boolean onItemTake(ItemStack item);

	/**
	 * Called when the user shift-clicks on the slot.
	 * If the user holds shift while left-clicking, no other actions will be done.
	 */
	public void onItemShiftClicked();

	/**
	 * Called when the user wants to exchange the item on his cursor with that in the slot.
	 * @param current the item that is currently in the slot
	 * @param cursor the item that is on the cursor
	 * @return false, if you want to cancel the exchange
	 */
	public boolean onItemExchange(ItemStack current, ItemStack cursor);

	public Slot setDepth(int depth);
	public int getDepth();
}
