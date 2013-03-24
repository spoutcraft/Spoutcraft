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

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.Configuration;

public class PerformanceButton extends AutomatedButton {
	public PerformanceButton() {
		setTooltip("FPS Limit\nMax FPS - no limit (fastest)\nBalanced - limit 120 FPS (slower)\nPower saver - limit 40 FPS (slowest)\nVSync - limit to monitor framerate (60, 30, 20)\nBalanced and Power saver decrease the FPS even if\nthe limit value is not reached.");
	}

	@Override
	public String getText() {
		switch (Configuration.getPerformance()) {
			case 0: return "Performance: Max FPS";
			case 1: return "Performance: Balanced";
			case 2: return "Performance: Power Saver";
			case 3: return "Performance: Vsync";
		}
		return "Unknown State: " + Configuration.getPerformance();
	}

	@Override
	public void onButtonClick() {
		Configuration.setPerformance(Configuration.getPerformance() + 1);
		Configuration.setPerformance(Configuration.getPerformance() & 3);
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.limitFramerate = Configuration.getPerformance();
		Display.setVSyncEnabled(Configuration.getPerformance() == 3);
	}
}
