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
package org.spoutcraft.client.gui.settings.controls;

import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;

public class VoidFogButton extends GenericCheckBox {
	public VoidFogButton() {
		super("Void Fog");
		setChecked(Configuration.isVoidFog());
		setEnabled(SpoutClient.getInstance().isVoidFogCheat());
		setTooltip("Void Fog\nON - A dark fog that obscures vision appears at low\nlevels of the map.\nOFF - normal view distance at all height levels.");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed by your server, it is considered cheating.";
		}

		return super.getTooltip();
	}

	@Override
	public void onButtonClick() {
		Configuration.setVoidFog(!Configuration.isVoidFog());
		Configuration.write();
	}
}
