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
package org.spoutcraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.client.gui.singleplayer.WorldItem;

public abstract class FilterModel extends AbstractListModel {
	protected ArrayList<ListWidgetItem> filteredItems = new ArrayList<ListWidgetItem>();
	protected ArrayList<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	private ArrayList<FilterItem> filters = new ArrayList<FilterItem>();

	public ArrayList<FilterItem> getFilters() {
		return filters;
	}

	public void refresh() {
		items.clear();

		refreshContents();

		filteredItems.clear();

		for (ListWidgetItem item:items) {
			boolean matches = true;
			for (FilterItem filter:filters) {
				matches = filter.matches(item);
				if (!matches) {
					break;
				}
			}
			if (matches) {
				filteredItems.add(item);
			}
		}

		sizeChanged();
	}

	protected abstract void refreshContents();

	@Override
	public ListWidgetItem getItem(int row) {
		if (row >= 0 && row < filteredItems.size()) {
			return filteredItems.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return filteredItems.size();
	}
}
