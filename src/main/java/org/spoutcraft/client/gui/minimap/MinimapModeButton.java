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

public class MinimapModeButton extends GenericComboBox {
	private static final int LIGHTMAP = 0;
	private static final int CAVEMAP = 1;
	private static final int HEIGHTMAP = 2;
	public MinimapModeButton() {
		setTooltip("Changes the mode of the minimap.");
		List<String> names = new ArrayList<String>(3);
		names.add("Light Map");
		names.add("Cave Map");
		names.add("Height Map");
		setItems(names);
		int selected = HEIGHTMAP;
		if (MinimapConfig.getInstance().isCavemap()) {
			selected = CAVEMAP;
		}
		if (MinimapConfig.getInstance().isLightmap()) {
			selected = LIGHTMAP;
		}
		setSelection(selected);
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		switch(i) {
			case LIGHTMAP: 
				MinimapConfig.getInstance().setLightmap(true);
				MinimapConfig.getInstance().setCavemap(false);
				MinimapConfig.getInstance().setHeightmap(false);
				break;
			case CAVEMAP: 
				MinimapConfig.getInstance().setLightmap(false);
				MinimapConfig.getInstance().setCavemap(true);
				MinimapConfig.getInstance().setHeightmap(false);
				break;
			case HEIGHTMAP: 
				MinimapConfig.getInstance().setLightmap(false);
				MinimapConfig.getInstance().setCavemap(false);
				MinimapConfig.getInstance().setHeightmap(true);
				break;
		}
	}
}
