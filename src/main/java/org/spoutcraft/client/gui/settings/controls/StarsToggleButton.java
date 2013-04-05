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

public class StarsToggleButton extends GenericCheckBox {
	public StarsToggleButton() {
		super("Stars");
		setChecked(Configuration.isStars());
		setEnabled(SpoutClient.getInstance().isStarsCheat());
		setTooltip("Stars\nON - stars are visible, slower\nOFF  - stars are not visible, faster");
	}

	@Override
	public void onButtonClick() {
		Configuration.setStars(!Configuration.isStars());
		Configuration.write();
	}
}
