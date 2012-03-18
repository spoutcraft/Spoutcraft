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
package org.spoutcraft.client.gui.singleplayer;

import org.spoutcraft.client.gui.FilterItem;
import org.spoutcraft.client.gui.FilterModel;
import org.spoutcraft.spoutcraftapi.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class WorldSearchField extends GenericTextField implements FilterItem{
	private FilterModel model;
	
	public WorldSearchField(FilterModel model) {
		this.model = model;
		model.getFilters().add(this);
		setPlaceholder(ChatColor.GRAY + "Search ...");
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
