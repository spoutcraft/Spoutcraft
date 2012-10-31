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

import java.util.HashMap;

import net.minecraft.src.IInventory;

import org.spoutcraft.api.inventory.Inventory;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.material.Material;

public class CraftInventory implements Inventory {
	protected IInventory inventory;

	private static net.minecraft.src.ItemStack[] getContents(IInventory inventory) {
		net.minecraft.src.ItemStack[] contents = new net.minecraft.src.ItemStack[inventory.getSizeInventory()];
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			contents[i] = inventory.getStackInSlot(i);
		}
		return contents;
	}

	public CraftInventory(IInventory inventory) {
		this.inventory = inventory;
	}

	public IInventory getInventory() {
		return inventory;
	}

	public int getSize() {
		return getInventory().getSizeInventory();
	}

	public String getName() {
		return getInventory().getInvName();
	}

	public ItemStack getItem(int index) {
		return new CraftItemStack(getInventory().getStackInSlot(index));
	}

	public ItemStack[] getContents() {
		ItemStack[] items = new ItemStack[getSize()];
		net.minecraft.src.ItemStack[] mcItems = getContents(getInventory());

		for (int i = 0; i < mcItems.length; i++) {
			items[i] = mcItems[i] == null ? null : new CraftItemStack(mcItems[i]);
		}

		return items;
	}

	public void setContents(ItemStack[] items) {
		net.minecraft.src.ItemStack[] mcItems = getContents(getInventory());
		if (mcItems.length != items.length) {
			throw new IllegalArgumentException("Invalid inventory size; expected " + mcItems.length);
		}

		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (item == null || item.getTypeId() <= 0) {
				getInventory().setInventorySlotContents(i, null);
			} else {
				getInventory().setInventorySlotContents(i, new net.minecraft.src.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability()));
			}
		}
	}

	public void setItem(int index, ItemStack item) {
		getInventory().setInventorySlotContents(index, (item == null ? null : new net.minecraft.src.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability())));
	}

	public boolean contains(int materialId) {
		for (ItemStack item: getContents()) {
			if (item != null && item.getTypeId() == materialId) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Material material) {
		return contains(material.getRawId());
	}

	public boolean contains(ItemStack item) {
		if (item == null) {
			return false;
		}
		for (ItemStack i: getContents()) {
			if (item.equals(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(int materialId, int amount) {
		int amt = 0;
		for (ItemStack item: getContents()) {
			if (item != null && item.getTypeId() == materialId) {
				amt += item.getAmount();
			}
		}
		return amt >= amount;
	}

	public boolean contains(Material material, int amount) {
		return contains(material.getRawId(), amount);
	}

	public boolean contains(ItemStack item, int amount) {
		if (item == null) {
			return false;
		}
		int amt = 0;
		for (ItemStack i: getContents()) {
			if (item.equals(i)) {
				amt += item.getAmount();
			}
		}
		return amt >= amount;
	}

	public HashMap<Integer, ItemStack> all(int materialId) {
		HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

		ItemStack[] inventory = getContents();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack item = inventory[i];
			if (item != null && item.getTypeId() == materialId) {
				slots.put(i, item);
			}
		}
		return slots;
	}

	public HashMap<Integer, ItemStack> all(Material material) {
		return all(material.getRawId());
	}

	public HashMap<Integer, ItemStack> all(ItemStack item) {
		HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
		if (item != null) {
			ItemStack[] inventory = getContents();
			for (int i = 0; i < inventory.length; i++) {
				if (item.equals(inventory[i])) {
					slots.put(i, inventory[i]);
				}
			}
		}
		return slots;
	}

	public int first(int materialId) {
		ItemStack[] inventory = getContents();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack item = inventory[i];
			if (item != null && item.getTypeId() == materialId) {
				return i;
			}
		}
		return -1;
	}

	public int first(Material material) {
		return first(material.getRawId());
	}

	public int first(ItemStack item) {
		if (item == null) {
			return -1;
		}
		ItemStack[] inventory = getContents();
		for (int i = 0; i < inventory.length; i++) {
			if (item.equals(inventory[i])) {
				return i;
			}
		}
		return -1;
	}

	public int firstEmpty() {
		ItemStack[] inventory = getContents();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public int firstPartial(int materialId) {
		ItemStack[] inventory = getContents();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack item = inventory[i];
			if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
				return i;
			}
		}
		return -1;
	}

	public int firstPartial(Material material) {
		return firstPartial(material.getRawId());
	}

	public int firstPartial(ItemStack item) {
		ItemStack[] inventory = getContents();
		if (item == null) {
			return -1;
		}
		for (int i = 0; i < inventory.length; i++) {
			ItemStack cItem = inventory[i];
			if (cItem != null && cItem.getTypeId() == item.getTypeId() && cItem.getAmount() < cItem.getMaxStackSize() && cItem.getDurability() == item.getDurability()) {
				return i;
			}
		}
		return -1;
	}

	public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
		HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

		/* TODO some optimization
		 *  - Create a 'firstPartial' with a 'fromIndex'
		 *  - Record the lastPartial per Material
		 *  - Cache firstEmpty result
		 */

		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			while (true) {
				// Do we already have a stack of it?
				int firstPartial = firstPartial(item);

				// Drat! no partial stack
				if (firstPartial == -1) {
					// Find a free spot!
					int firstFree = firstEmpty();

					if (firstFree == -1) {
						// No space at all!
						leftover.put(i, item);
						break;
					} else {
						// More than a single stack!
						if (item.getAmount() > getMaxItemStack()) {
							setItem(firstFree, new CraftItemStack(item.getTypeId(), getMaxItemStack(), item.getDurability()));
							item.setAmount(item.getAmount() - getMaxItemStack());
						} else {
							// Just store it
							setItem(firstFree, item);
							break;
						}
					}
				} else {
					// So, apparently it might only partially fit, well lets do just that
					ItemStack partialItem = getItem(firstPartial);

					int amount = item.getAmount();
					int partialAmount = partialItem.getAmount();
					int maxAmount = partialItem.getMaxStackSize();

					// Check if it fully fits
					if (amount + partialAmount <= maxAmount) {
						partialItem.setAmount(amount + partialAmount);
						break;
					}

					// It fits partially
					partialItem.setAmount(maxAmount);
					item.setAmount(amount + partialAmount - maxAmount);
				}
			}
		}
		return leftover;
	}

	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
		HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

		// TODO Optimization

		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			int toDelete = item.getAmount();

			while (true) {
				int first = first(item.getType());

				// Drat! we don't have this type in the inventory
				if (first == -1) {
					item.setAmount(toDelete);
					leftover.put(i, item);
					break;
				} else {
					ItemStack itemStack = getItem(first);
					int amount = itemStack.getAmount();

					if (amount <= toDelete) {
						toDelete -= amount;
						// clear the slot, all used up
						clear(first);
					} else {
						// split the stack and store
						itemStack.setAmount(amount - toDelete);
						setItem(first, itemStack);
						toDelete = 0;
					}
				}

				// Bail when done
				if (toDelete <= 0) {
					break;
				}
			}
		}
		return leftover;
	}

	private int getMaxItemStack() {
		return getInventory().getInventoryStackLimit();
	}

	public void remove(int materialId) {
		ItemStack[] items = getContents();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getTypeId() == materialId) {
				clear(i);
			}
		}
	}

	public void remove(Material material) {
		remove(material.getRawId());
	}

	public void remove(ItemStack item) {
		ItemStack[] items = getContents();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].equals(item)) {
				clear(i);
			}
		}
	}

	public void clear(int index) {
		setItem(index, null);
	}

	public void clear() {
		for (int i = 0; i < getSize(); i++) {
			clear(i);
		}
	}
}
