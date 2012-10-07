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
package org.spoutcraft.client.gui.minimap;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.api.gui.GenericComboBox;

public class ZoomModeButton extends GenericComboBox {
	public ZoomModeButton() {
		setTooltip("Changes the zoom level of the minimap.");
		List<String> names = new ArrayList<String>(3);
		names.add("Zoom: 0");
		names.add("Zoom: 1");
		names.add("Zoom: 2");
		names.add("Zoom: 3");
		setItems(names);
		setSelection(MinimapConfig.getInstance().getZoom());
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		MinimapConfig.getInstance().setZoom(i);
	}
}
