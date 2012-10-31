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
package org.spoutcraft.client.gui.singleplayer;

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.event.screen.TextFieldChangeEvent;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.client.gui.FilterItem;
import org.spoutcraft.client.gui.FilterModel;

public class WorldSearchField extends GenericTextField implements FilterItem {
	private FilterModel model;

	public WorldSearchField(FilterModel model) {
		this.model = model;
		model.getFilters().add(this);
		setPlaceholder(ChatColor.GRAY + "Search...");
	}

	@Override
	public void onTextFieldChange(TextFieldChangeEvent event) {
		model.refresh();
	}

	public boolean matches(Object obj) {
		if (getText().trim().isEmpty()) {
			return true;
		}
		if (obj instanceof WorldItem) {
			WorldItem world = (WorldItem) obj;
			if (world.getWorld().getWorldName().toLowerCase().contains(getText().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
