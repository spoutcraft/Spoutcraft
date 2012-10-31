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
import org.spoutcraft.client.SpoutClient;

public class SwitchViewSourceCombo extends GenericComboBox {
	private GuiFavorites gui;

	public SwitchViewSourceCombo(GuiFavorites gui) {
		List<String> items = new ArrayList<String>();
		items.add("Favorite Servers");
		items.add("LAN Servers");
		setItems(items);
		this.gui = gui;
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		switch(i) {
			case 0:
				gui.setModel(SpoutClient.getInstance().getServerManager().getFavorites());
				break;
			case 1:
				gui.setModel(SpoutClient.getInstance().getServerManager().getLANModel());
				break;
		}
		gui.updateButtons();
		super.onSelectionChanged(i, text);
	}
}
