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

import org.spoutcraft.api.player.RenderDistance;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;

public class RenderDistanceButton extends AutomatedButton {
	RenderDistance distance = RenderDistance.getRenderDistanceFromValue(Configuration.getRenderDistance());
	public RenderDistanceButton() {
		setTooltip("Visible distance\nFar - 256m (slower\nNormal - 128m\nShort - 64m (faster)\nTiny - 32m (fastest)");
	}

	@Override
	public String getText() {
		return "Render Distance: " + distance.toString();
	}

	@Override
	public void onButtonClick() {
		if (SpoutClient.getInstance().getActivePlayer() != null) {
			distance = SpoutClient.getInstance().getActivePlayer().getNextRenderDistance();
			Configuration.setRenderDistance(distance.getValue());
		} else {
			Configuration.setRenderDistance(Configuration.getRenderDistance() + 1);
			if (Configuration.getRenderDistance() > 3) {
				Configuration.setRenderDistance(0);
			}
			distance = RenderDistance.getRenderDistanceFromValue(Configuration.getRenderDistance());
		}
		Minecraft.theMinecraft.gameSettings.renderDistance = distance.getValue();
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}
}
