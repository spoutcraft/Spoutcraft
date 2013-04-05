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

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.gui.GenericSlider;

public class ScanRadiusSlider extends GenericSlider {
	private static final int MIN_RADIUS = 0, MAX_RADIUS = 7;

	public ScanRadiusSlider() {
		updateText();
		updateSliderPosition();
		setTooltip("Sets how far the overview-map scans the map when you move.\nHigher values will mean increased lag when you move");
	}

	private void updateText() {
		int radius = MinimapConfig.getInstance().getScanRadius();
		ChatColor color = ChatColor.WHITE;
		if (radius > 3) {
			color = ChatColor.YELLOW;
		}
		if (radius > 4) {
			color = ChatColor.GOLD;
		}
		if (radius > 5) {
			color = ChatColor.RED;
		}
		if (radius > 6) {
			color = ChatColor.DARK_RED;
		}
		setText(color + "Scan Radius: " + radius + " chunks");
	}

	private void updateSliderPosition() {
		setSliderPosition((float) (MinimapConfig.getInstance().getScanRadius() - MIN_RADIUS) / (float) (MAX_RADIUS - MIN_RADIUS));
	}

	@Override
	public void onSliderDrag(float oldPos, float newPos) {
		int newradius = (int) (newPos * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS);
		MinimapConfig.getInstance().setScanRadius(newradius);
		MinimapConfig.getInstance().save();
		updateText();
		updateSliderPosition();
	}
}
