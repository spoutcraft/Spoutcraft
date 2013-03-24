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

public class SensitivitySlider extends GenericSlider {
	public SensitivitySlider() {
		super("Sensitivity");
		setSliderPosition(Minecraft.theMinecraft.gameSettings.mouseSensitivity);
		setTooltip("Sensitivity\nAdjusts the sensitivity of the mouse in game.");
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Minecraft.theMinecraft.gameSettings.mouseSensitivity = newPos;
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}

	public String getText() {
		return "Sensitivity: " + (int)(this.getSliderPosition() * 200) + "%";
	}
}
