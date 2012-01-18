/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.client.gui.database.AbstractAPIModel;
import org.spoutcraft.client.gui.database.UrlElement;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;

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
		return "accessType="+ids[getSelectedRow()];
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
		return "Access: "+super.getText();
	}
}
