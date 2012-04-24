/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.creative;

import org.lwjgl.input.Mouse;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.GenericSlot;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.Slot;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

public class CreativeSlot extends GenericSlot {

	private ItemStack backedItem;
	private boolean reset;

	public CreativeSlot() {
		super();
		reset = false;
	}

	@Override
	public void render() {
		if (reset) {
			setItem(backedItem.clone());
			reset = false;
		}
		super.render();
	}

	public Slot setSpecialItem(ItemStack item, String name) {
		setItem(item);
		setTooltip(name);
		backedItem = item.clone();
		return this;
	}

	@Override
	public boolean onItemExchange(ItemStack current, ItemStack cursor) {
		return false;
	}

	@Override
	public boolean onItemPut(ItemStack item) {
		if (item.getType() != getItem().getType()) {
			Spoutcraft.getActivePlayer().setItemStackOnCursor(null);
			return false;
		}
		ItemStack clone = item.clone();
		if (item.getAmount() == 1 && Mouse.isButtonDown(1)) {
			clone = null;
		} else {
			clone.setAmount(clone.getAmount() + 1);
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				clone.setAmount(64);
			}
		}
		Spoutcraft.getActivePlayer().setItemStackOnCursor(clone);
		return false;
	}

	@Override
	public void onItemShiftClicked() {
		ItemStack it = getItem().clone();
		it.setAmount(64);
		Spoutcraft.getActivePlayer().setItemStackOnCursor(it);
	}

	@Override
	public boolean onItemTake(ItemStack item) {
		if (Mouse.isButtonDown(1)) {
			return false;
		}
		ItemStack original = getItem().clone();
		Spoutcraft.getActivePlayer().setItemStackOnCursor(original);
		item = original;
		reset = true;
		return true;
	}
}
