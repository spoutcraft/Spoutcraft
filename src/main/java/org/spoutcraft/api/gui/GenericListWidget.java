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
import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class GenericListWidget extends GenericScrollable implements ListWidget {
	private List<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	private int selected = -1;
	protected int cachedTotalHeight = -1;

	public WidgetType getType() {
		return WidgetType.ListWidget;
	}

	public ListWidgetItem[] getItems() {
		ListWidgetItem[] sample = {};
		return items.toArray(sample);
	}

	public ListWidgetItem getItem(int i) {
		if (i == -1) {
			return null;
		}
		ListWidgetItem items [] = getItems();
		if (i>=items.length) {
			return null;
		}
		return items[i];
	}

	public ListWidget addItem(ListWidgetItem item) {
		items.add(item);
		item.setListWidget(this);
		cachedTotalHeight = -1;
		return this;
	}

	public ListWidget addItems(ListWidgetItem... items) {
		for (ListWidgetItem item : items) {
			this.addItem(item);
		}
		return this;
	}

	public boolean removeItem(ListWidgetItem item) {
		if (items.contains(item)) {
			items.remove(item);
			item.setListWidget(null);
			cachedTotalHeight = -1;
			return true;
		}
		return false;
	}

	public ListWidgetItem getSelectedItem() {
		return getItem(selected);
	}

	public int getSelectedRow() {
		return selected;
	}

	public ListWidget setSelection(int n) {
		selected = n;
		if (selected < -1) {
			selected = -1;
		}
		if (selected > items.size()-1) {
			selected = items.size()-1;
		}

		// Check if selection is visible
		ensureVisible(getItemRect(selected));
		return this;
	}

	public Rectangle getItemRect(int n) {
		ListWidgetItem item = getItem(n);
		Rectangle result = new Rectangle(0,0,0,0);
		if (item == null) {
			return result;
		}
		result.setX(0);
		result.setY(getItemYOnScreen(n));
		result.setHeight(item.getHeight());
		result.setWidth(getInnerSize(Orientation.VERTICAL));
		return result;
	}

	protected int getItemYOnScreen(int n) {
		int height = 0;
		for (int i = 0; i<n && i<getSize(); i++) {
			height += getItem(i).getHeight();
		}
		return height;
	}

	public int getSize() {
		return items.size();
	}

	public ListWidget clearSelection() {
		setSelection(-1);
		return this;
	}

	public boolean isSelected(int n) {
		return selected == n;
	}

	public ListWidget setScrollPosition(int position) {
		setScrollPosition(Orientation.VERTICAL, position);
		return this;
	}

	public int getScrollPosition() {
		return getScrollPosition(Orientation.VERTICAL);
	}

	@Override
	public int getInnerSize(Orientation axis) {
		if (axis == Orientation.HORIZONTAL) return getViewportSize(Orientation.HORIZONTAL);
		if (cachedTotalHeight == -1) {
			cachedTotalHeight = 0;
			for (ListWidgetItem item:getItems()) {
				cachedTotalHeight+=item.getHeight();
			}
		}
		return cachedTotalHeight + 10;
	}

	public int getTotalHeight() {
		return getInnerSize(Orientation.VERTICAL);
	}

	public int getMaxScrollPosition() {
		return getMaximumScrollPosition(Orientation.VERTICAL);
	}

	public boolean isSelected(ListWidgetItem item) {
		if (getSelectedItem() == null)
			return false;
		return getSelectedItem().equals(item);
	}

	public ListWidget shiftSelection(int n) {
		if (selected + n < 0) {
			setSelection(0);
		} else {
			setSelection(selected + n);
		}
		return this;
	}

	public void renderContents() {
		Spoutcraft.getRenderDelegate().renderContents(this);
	}

	public void onSelected(int item, boolean doubleClick) {
	}

	public void clear() {
		items.clear();
		cachedTotalHeight = -1;
		selected = -1;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setInnerSize(Orientation.HORIZONTAL, 0);
		setInnerSize(Orientation.VERTICAL, 0);
		clear();
		selected = input.readInt();
		int count = input.readInt();
		for (int i = 0; i < count; i++) {
			GenericListWidgetItem item = new GenericListWidgetItem(input.readString(), input.readString(), input.readString());
			addItem(item);
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(selected); // Write which item is selected.
		output.writeInt(0); // Write number of items first!
		// Don't transmit clientside items because of the interface ListWidgetItem
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
