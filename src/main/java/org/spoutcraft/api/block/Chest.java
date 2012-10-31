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
package org.spoutcraft.api.block;

import org.spoutcraft.api.inventory.DoubleChestInventory;
import org.spoutcraft.api.inventory.Inventory;

public interface Chest extends BlockState, ContainerBlock {
	/**
	 * Is true if the chest is part of a larger double chest
	 * @return true if is double chest
	 */
	public boolean isDoubleChest();

	/**
	 * Gets the other side of the larger double chest, if a double chest, or null if it is a single chest
	 * @return other chest
	 */
	public Chest getOtherSide();

	/**
	 * Gets the full double inventory of the double chest, or null if a single chest
	 * @return full inventory
	 */
	public DoubleChestInventory getFullInventory();

	/**
	 * Gets the largest possible inventory associated with this block
	 * If this block is part of a double chest, it will return the double inventory
	 * otherwise it will return the single inventory for this chest block
	 * @return largest inventory
	 */
	public Inventory getLargestInventory();

	/**
	 * Gets the inventory for this single chest block
	 * @return inventory
	 */
	public Inventory getInventory();
}
