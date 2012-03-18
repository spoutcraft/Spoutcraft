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

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class FlightSpeedSlider extends GenericSlider{
	public FlightSpeedSlider() {
		super("Flight Speed");
		this.setSliderPosition(ConfigReader.flightSpeedMultiplier / 10);
		setTooltip("Flight Speed Multiplier\nAlters how fast you fly in creative. 1X is vanilla speed.");
	}

	@Override
	public String getText() {
		double pos = this.getSliderPosition() * 10;
		String text = Integer.toString((int)pos);
		text += "." + (int)((pos - (int)pos) * 100);
		return "Flight Speed: " + text + "X";
	}

	@Override
	public void onSliderDrag(SliderDragEvent event) {
		ConfigReader.flightSpeedMultiplier = event.getNewPosition() * 10;
		ConfigReader.write();
	}
}
