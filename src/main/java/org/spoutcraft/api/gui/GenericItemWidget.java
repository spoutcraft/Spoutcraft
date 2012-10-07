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
import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

@UnsafeClass
public class GenericItemWidget extends GenericWidget implements ItemWidget {
	protected int material = -1;
	protected short data = -1;
	protected int depth = 8;
	protected ItemStack toRender = null;

	public GenericItemWidget() {
		this(new ItemStack(0));
	}

	public GenericItemWidget(ItemStack item) {
		this.material = item.getTypeId();
		this.data = item.getDurability();
	}

	public int getVersion() {
		return super.getVersion() + 0;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.setTypeId(input.readInt());
		this.setData(input.readShort());
		this.setDepth(input.readInt());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getTypeId());
		output.writeShort(getData());
		output.writeInt(getDepth());
	}

	public ItemWidget setTypeId(int id) {
		this.material = id;
		return this;
	}

	public int getTypeId() {
		return material;
	}

	public ItemWidget setData(short data) {
		this.data = data;
		return this;
	}

	public short getData() {
		return data;
	}

	public ItemWidget setDepth(int depth) {
		this.depth = depth;
		return this;
	}

	public int getDepth() {
		return depth;
	}

	public ItemWidget setHeight(int height) {
		super.setHeight(height);
		return this;
	}

	public ItemWidget setWidth(int width) {
		super.setWidth(width);
		return this;
	}

	public WidgetType getType() {
		return WidgetType.ItemWidget;
	}

	@Override
	public ItemWidget copy() {
		return ((ItemWidget)super.copy()).setTypeId(getTypeId()).setData(getData()).setDepth(getDepth());
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}
}
