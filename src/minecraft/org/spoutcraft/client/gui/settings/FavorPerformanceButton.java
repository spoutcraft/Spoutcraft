/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericRadioButton;
import org.spoutcraft.spoutcraftapi.gui.Label;

public class FavorPerformanceButton extends GenericRadioButton {
	Label label;
	public FavorPerformanceButton(String title, Label label) {
		super(title);
		this.label = label;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.automatePerformance = true;
		ConfigReader.automateMode = 0;
		ConfigReader.write();
		label.setTextColor(new Color(1F, 1F, 1F, 1F));
	}
}
