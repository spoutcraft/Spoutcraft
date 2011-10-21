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

public class AutosaveButton extends GenericButton{
	public AutosaveButton() {
		setTooltip("Autosave interval\nDefault autosave interval (2s) is NOT RECOMMENDED.\nAutosave causes the famous Lag Spike of Death.");
	}
	
	@Override
	public String getText() {
		switch(ConfigReader.autosave) {
			case 0: return "Autosave: 30 min";
			case 1: return "Autosave: 3 min";
			case 2: return "Autosave: 1 min";
			case 3: return "Autosave: 30 sec";
			case 4: return "Autosave: 10 sec";
			case 5: return "Autosave: 2 sec";
		}
		return "Unknown State: " + ConfigReader.autosave;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.autosave++;
		if (ConfigReader.autosave > 5) {
			ConfigReader.autosave = 0;
		}
		ConfigReader.write();
	}
}
