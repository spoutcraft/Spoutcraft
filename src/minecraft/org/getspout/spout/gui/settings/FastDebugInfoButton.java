/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.gui.settings;

import org.getspout.spout.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class FastDebugInfoButton extends GenericButton{
	public FastDebugInfoButton() {
		setTooltip("Fast Debug Info\nOFF - default debug info screen, slower\nON - debug info screen without lagometer, faster\nRemoves the lagometer from the debug screen (F3).");
	}
	
	@Override
	public String getText() {
		switch (ConfigReader.fastDebug) {
			case 0: return "Debug Info: Fancy";
			case 1: return "Debug Info: Fast";
			case 2: return "Debug Info: FPS Only";
		}
		return "Unknown State: " + ConfigReader.fastDebug;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.fastDebug++;
		if (ConfigReader.fastDebug > 2) {
			ConfigReader.fastDebug = 0;
		}
		ConfigReader.write();
	}
}
