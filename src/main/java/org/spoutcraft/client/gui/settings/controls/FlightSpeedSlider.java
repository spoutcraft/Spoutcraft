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

import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;

public class FlightSpeedSlider extends GenericSlider {
	public FlightSpeedSlider() {
		super("Flight Speed");
		setEnabled(SpoutClient.getInstance().isFlySpeedCheat());
		if (SpoutClient.getInstance().isFlySpeedCheat()) {
			this.setSliderPosition(Configuration.getFlightSpeedFactor() / 10);
		} else {
			this.setSliderPosition(1.0F / 10);
		}
		setTooltip("Flight Speed Multiplier\nAlters how fast you fly in creative. 1X is vanilla speed.");
	}

	@Override
	public String getText() {
		double pos = this.getSliderPosition() * 10;
		return "Flight Speed: " + (Math.round(pos * 100D)  / 100D) + "X";
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Configuration.setFlightSpeedFactor(newPos * 10);
		Configuration.write();
	}
}
