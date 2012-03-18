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

import net.minecraft.client.Minecraft;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;

public class RenderDistanceButton extends AutomatedButton {
	RenderDistance distance = RenderDistance.getRenderDistanceFromValue(ConfigReader.renderDistance);

	public RenderDistanceButton() {
		setTooltip("Visible distance\nFar - 256m (slower\nNormal - 128m\nShort - 64m (faster)\nTiny - 32m (fastest)");
	}

	@Override
	public String getText() {
		return "Render Distance: " + distance.toString();
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (SpoutClient.getInstance().getActivePlayer() != null) {
			distance = SpoutClient.getInstance().getActivePlayer().getNextRenderDistance();
			ConfigReader.renderDistance = distance.getValue();
		} else {
			ConfigReader.renderDistance++;
			if (ConfigReader.renderDistance > 3) {
				ConfigReader.renderDistance = 0;
			}
			distance = RenderDistance.getRenderDistanceFromValue(ConfigReader.renderDistance);
		}
		Minecraft.theMinecraft.gameSettings.renderDistance = distance.getValue();
		ConfigReader.write();
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}
}
