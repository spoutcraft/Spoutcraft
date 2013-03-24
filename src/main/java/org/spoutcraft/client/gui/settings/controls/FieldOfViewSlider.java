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

public class FieldOfViewSlider extends GenericSlider {
	public FieldOfViewSlider() {
		super("Field of View");
		setSliderPosition(Minecraft.theMinecraft.gameSettings.fovSetting);
		setTooltip("Field of View\nAdjusts the field of view in game.");
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Minecraft.theMinecraft.gameSettings.fovSetting = newPos;
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}

	public String getText() {
		String message = String.valueOf((70 + (int)(this.getSliderPosition() * 40)));
		if (this.getSliderPosition() == 0) {
			message = "Normal";
		}
		if (this.getSliderPosition() == 1) {
			message = "Quake Pro";
		}
		return "FOV: " + message;
	}
}
