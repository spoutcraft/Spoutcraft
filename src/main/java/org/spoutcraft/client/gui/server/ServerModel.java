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
package org.spoutcraft.client.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.api.gui.ListWidgetItem;

public class ServerModel extends AbstractListModel {
	protected List<ServerItem> items = new ArrayList<ServerItem>();
	protected GuiFavorites gui;
	private boolean polling = false;

	@Override
	public ListWidgetItem getItem(int row) {
		if (row >= 0 && row < getSize()) {
			return items.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}

	public synchronized boolean isPolling() {
		return polling;
	}

	public synchronized void setPolling(boolean poll) {
		polling = poll;
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void setCurrentGUI(GuiFavorites gui) {
		this.gui = gui;
	}

	public GuiFavorites getCurrentGui() {
		return gui;
	}
}
