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
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class DifficultyButton extends GenericButton {
	public DifficultyButton() {
		super("Difficulty");
		setTooltip("Difficulty\nControls the difficulty of the game.");
	}

	@Override
	public String getText() {
		if (Minecraft.theMinecraft.theWorld != null && Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return "Difficulty: Hardcore";
		}
		String difficulty;
		switch (Minecraft.theMinecraft.gameSettings.difficulty) {
			case 0:
				difficulty = "Peaceful";
				break;
			case 1:
				difficulty = "Easy";
				break;
			case 2:
				difficulty = "Normal";
				break;
			case 3:
				difficulty = "Hard;";
				break;
			default:
				difficulty = "Unknown";
				break;
		}
		return "Difficulty: " + difficulty;
	}

	@Override
	public String getTooltip() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return "Can not change difficulty outside of the game";
		}
		if (Minecraft.theMinecraft.isMultiplayerWorld()) {
			return "Can not change difficulty in multiplayer";
		}
		if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return "Can not change difficulty in hardcore mode";
		}
		return super.getTooltip();
	}

	@Override
	public boolean isEnabled() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return false;
		}
		if (Minecraft.theMinecraft.isMultiplayerWorld()) {
			return false;
		}
		if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return false;
		}
		return true;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.gameSettings.difficulty++;
		if (Minecraft.theMinecraft.gameSettings.difficulty > 3) {
			Minecraft.theMinecraft.gameSettings.difficulty = 0;
		}
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}
}
