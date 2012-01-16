/*
 * This file is part of Spoutcraft (http://spout.org).
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

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class StarsToggleButton extends GenericCheckBox{

	public StarsToggleButton() {
		super("Stars");
		setChecked(ConfigReader.stars);
		setEnabled(SpoutClient.getInstance().isStarsCheat());
		setTooltip("Stars\nON - stars are visible, slower\nOFF  - stars are not visible, faster");
	}
	
	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed by your server, it is considered cheating.";
		}
		return super.getTooltip();
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.stars = !ConfigReader.stars;
		ConfigReader.write();
	}
}
