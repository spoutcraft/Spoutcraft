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
import org.spoutcraft.client.gui.database.UrlElement;

public class CountryButton extends GenericComboBox implements UrlElement {
	ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();

	public CountryButton() {
		List<String> countries = new ArrayList<String>();
		countries.add("All");
		countries.addAll(model.getCountries());
		setItems(countries);
		setText("Country");
	}

	public boolean isActive() {
		return getSelectedRow() != 0;
	}

	public String getUrlPart() {
		return "country=" + getSelectedItem();
	}

	@Override
	public void onSelectionChanged(int row, String text) {
		model.updateUrl();
	}

	public void setCurrentCountry(int i) {
		setSelection(i + 1);
	}

	public void clear() {
		setSelection(0);
	}
}
