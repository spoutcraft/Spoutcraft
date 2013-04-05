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

import org.spoutcraft.api.gui.GenericButton;

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
		switch(Minecraft.theMinecraft.gameSettings.difficulty) {
			case 0: difficulty = "Peaceful"; break;
			case 1: difficulty = "Easy"; break;
			case 2: difficulty = "Normal"; break;
			case 3: difficulty = "Hard"; break;
			default: difficulty = "Unknown"; break;
		}
		return "Difficulty: " + difficulty;
	}

	@Override
	public String getTooltip() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return "Can not change difficulty outside of the game";
		}
		if (!Minecraft.theMinecraft.isSingleplayer()) {
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
		if (!Minecraft.theMinecraft.isSingleplayer()) {
			return false;
		}
		if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return false;
		}
		return true;
	}

	@Override
	public void onButtonClick() {
		Minecraft.theMinecraft.gameSettings.difficulty++;
		if (Minecraft.theMinecraft.gameSettings.difficulty > 3) {
			Minecraft.theMinecraft.gameSettings.difficulty = 0;
		}
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}
}
