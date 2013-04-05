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

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.config.Configuration;

public class BrightnessSlider extends GenericSlider {
	public BrightnessSlider() {
		super("Brightness");
		setSliderPosition(Configuration.getBrightnessSlider());
		setTooltip("Increases the brightness of darker objects\nOFF - standard brightness\n100% - maximum brightness for darker objects\nThis options does not change the brightness of\nfully black objects");
	}

	@Override
	public void onSliderDrag(float oldPos, float newPos) {
		Configuration.setBrightnessSlider(newPos);
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.gammaSetting = Configuration.getBrightnessSlider();
	}

	public String getText() {
		if (getSliderPosition() == 0F) {
			return "Brightness: Moody";
		}
		if (getSliderPosition() == 1F) {
			return "Brightness: Bright";
		}
		return "Brightness: " + (int)(this.getSliderPosition() * 100) + "%";
	}
}
