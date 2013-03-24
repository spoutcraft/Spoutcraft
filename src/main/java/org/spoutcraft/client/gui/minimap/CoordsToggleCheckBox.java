/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.client.SpoutClient;

public class CoordsToggleCheckBox extends GenericCheckBox {
	public CoordsToggleCheckBox() {
		super("Show Coordinates");
		setChecked(MinimapConfig.getInstance().isCoords());
		setEnabled(SpoutClient.getInstance().isCoordsCheat());
		setTooltip("Minimap Enabled\nEnables or disables the minimap on the HUD.");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed by your server, it is considered cheating.\nContact your admins if you feel this is an error.";
		}
		return super.getTooltip();
	}

	@Override
	public void onButtonClick() {
		MinimapConfig.getInstance().setCoords(isChecked());
	}
}
