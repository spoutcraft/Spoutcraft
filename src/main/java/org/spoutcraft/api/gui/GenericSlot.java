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

import java.io.IOException;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class GenericSlot extends GenericControl implements Slot {
	private ItemStack stack = new ItemStack(0);
	private int depth = 16;

	public WidgetType getType() {
		return WidgetType.Slot;
	}

	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public ItemStack getItem() {
		if (stack == null) {
			stack = new ItemStack(0);
		}
		return stack.clone();
	}

	public Slot setItem(ItemStack item) {
		if (item == null) {
			stack = new ItemStack(0);
			setTooltip("");
			return this;
		}
		stack = item.clone();
		setTooltip(Spoutcraft.getMaterialManager().getToolTip(stack));
		return this;
	}

	public boolean onItemPut(ItemStack item) {
		return true;
	}

	public boolean onItemTake(ItemStack item) {
		return true;
	}

	public void onItemShiftClicked() {
	}

	public boolean onItemExchange(ItemStack current, ItemStack cursor) {
		return true;
	}

	public int getDepth() {
		return depth;
	}

	public Slot setDepth(int depth) {
		this.depth = depth;
		return this;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setItem(new ItemStack(input.readInt(), (int) input.readShort(), input.readShort()));
		depth = input.readInt();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(stack.getTypeId());
		output.writeShort((short)stack.getAmount());
		output.writeShort(stack.getDurability());
		output.writeInt(depth);
	}
}
