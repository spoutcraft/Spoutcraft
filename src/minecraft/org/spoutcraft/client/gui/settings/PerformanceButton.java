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

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

public class PerformanceButton extends AutomatedButton{
	public PerformanceButton() {
		setTooltip("FPS Limit\nMax FPS - no limit (fastest)\nBalanced - limit 120 FPS (slower)\nPower saver - limit 40 FPS (slowest)\nVSync - limit to monitor framerate (60, 30, 20)\nBalanced and Power saver decrease the FPS even if\nthe limit value is not reached.");
	}

	@Override
	public String getText() {
		switch (ConfigReader.performance) {
			case 0: return "Performance: Max FPS";
			case 1: return "Performance: Balanced";
			case 2: return "Performance: Power Saver";
			case 3: return "Performance: Vsync";
		}
		return "Unknown State: " + ConfigReader.performance;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.performance += 1;
		ConfigReader.performance &= 3;
		ConfigReader.write();
		Minecraft.theMinecraft.gameSettings.limitFramerate = ConfigReader.performance;
		Display.setVSyncEnabled(ConfigReader.performance == 3);
	}
}
