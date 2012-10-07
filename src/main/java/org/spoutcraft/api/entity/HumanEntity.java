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
package org.spoutcraft.api.entity;

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.inventory.PlayerInventory;

public interface HumanEntity extends LivingEntity, AnimalTamer {
	/**
	 * Returns the name of this player
	 *
	 * @return Player name
	 */
	public String getName();

	/**
	 * Get the player's inventory.
	 *
	 * @return The inventory of the player, this also contains the armor slots.
	 */
	public PlayerInventory getInventory();

	/**
	 * Returns the ItemStack currently in your hand, can be empty.
	 *
	 * @return The ItemStack of the item you are currently holding.
	 */
	public ItemStack getItemInHand();

	/**
	 * Sets the item to the given ItemStack, this will replace whatever the
	 * user was holding.
	 *
	 * @param item The ItemStack which will end up in the hand
	 */
	public void setItemInHand(ItemStack item);

	/**
	 * Changes the item in hand to another of your 'action slots'.
	 *
	 * @param index The new index to use, only valid ones are 0-8.
	 *
	public void selectItemInHand(int index);
	 */

	/**
	 * Returns whether this player is slumbering.
	 *
	 * @return slumber state
	 */
	public boolean isSleeping();

	/**
	 * Get the sleep ticks of the player. This value may be capped.
	 *
	 * @return slumber ticks
	 */
	public int getSleepTicks();
}
