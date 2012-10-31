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

import org.spoutcraft.api.gui.GenericComboBox;
import org.spoutcraft.client.gui.database.AbstractAPIModel;
import org.spoutcraft.client.gui.database.UrlElement;

public class AccessTypeFilter extends GenericComboBox implements UrlElement {
	private String[] strings = { "All", "Open", "Whitelisted", "Graylisted", "Blacklisted"};
	private int [] ids = {-1, 0, 1, 2, 3};
	AbstractAPIModel model;

	public AccessTypeFilter(AbstractAPIModel model) {
		this.model = model;
		List<String> l = new ArrayList<String>();
		for (String type:strings) {
			l.add(type);
		}
		setItems(l);
		setSelection(0);
	}

	public boolean isActive() {
		return ids[getSelectedRow()] != -1;
	}

	public String getUrlPart() {
		return "accessType=" + ids[getSelectedRow()];
	}

	public void clear() {
		setSelection(0);
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		model.updateUrl();
	}

	@Override
	public String getText() {
		return "Access: " + super.getText();
	}
}
