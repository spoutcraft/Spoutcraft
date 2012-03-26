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
package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class TextAlphaToggleButton extends GenericCheckBox{
	public TextAlphaToggleButton() {
		super("Text Alpha");
		setChecked(ConfigReader.alphaText);
		setEnabled(true);
		setTooltip("Text Alpha\nON - Text is colored in �4R�2G�1B�eA�f (Default)\nOFF  - Text is rendered in �4R�2G�1B�f.\nSwitching off trades transparent text for coloured text,\nrestoring colour to most machines with the white text bug.\n�00�11�22�33�44�55�66�77�88�99�aa�bb�cc�dd�ee�ff�ll�mm�nn�oo�rr�kk");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed, meow.";
		}
		return super.getTooltip();
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.alphaText = !ConfigReader.alphaText;
		ConfigReader.write();
	}
}