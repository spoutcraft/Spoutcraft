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
package org.spoutcraft.client.gui;

import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiStats;
import net.minecraft.src.StatFileWriter;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.controls.GuiControls;
import org.spoutcraft.client.gui.settings.GameSettingsScreen;
import org.spoutcraft.spoutcraftapi.gui.*;

public class ScreenUtil {
	public static void open(ScreenType type) {
		GuiScreen toOpen = null;
		StatFileWriter statfile = SpoutClient.getHandle().statFileWriter;
		switch(type)
		{
		case CHAT_SCREEN:
			toOpen = new GuiChat();
			break;
		case SLEEP_SCREEN:
			toOpen = new GuiSleepMP();
			break;
		case PLAYER_INVENTORY:
			toOpen = new GuiInventory(SpoutClient.getHandle().thePlayer);
			break;
		case INGAME_MENU:
			toOpen = new GuiIngameMenu();
			break;
		case OPTIONS_MENU:
			toOpen = new GameSettingsScreen(new GuiIngameMenu());
			break;
		case VIDEO_SETTINGS_MENU:
			toOpen = new GameSettingsScreen(new GuiIngameMenu());
			break;
		case CONTROLS_MENU:
			toOpen = new GuiControls(new GameSettingsScreen(new GuiIngameMenu()));
			break;
		case ACHIEVEMENTS_SCREEN:
			toOpen = new GuiAchievements(statfile);
			break;
		case STATISTICS_SCREEN:
			toOpen = new GuiStats(new GuiIngameMenu(), statfile);
			break;
		case GAME_OVER_SCREEN:
			toOpen = new GuiGameOver();
			break;
		}
		SpoutClient.getHandle().displayGuiScreen(toOpen);
	}

	public static ScreenType getType(GuiScreen gui) {
		ScreenType screen = ScreenType.UNKNOWN;
		if (gui == null) {
			screen = ScreenType.GAME_SCREEN;
		}
		if (gui instanceof GuiChat) {
			screen = ScreenType.CHAT_SCREEN;
		} else if (gui instanceof GuiSleepMP) {
			screen = ScreenType.SLEEP_SCREEN;
		} else if (gui instanceof CustomScreen) {
			screen = ScreenType.CUSTOM_SCREEN;
		} else if (gui instanceof GuiInventory) {
			screen = ScreenType.PLAYER_INVENTORY;
		} else if (gui instanceof GuiChest) {
			screen = ScreenType.CHEST_INVENTORY;
		} else if (gui instanceof GuiDispenser) {
			screen = ScreenType.DISPENSER_INVENTORY;
		} else if (gui instanceof GuiFurnace) {
			screen = ScreenType.FURNACE_INVENTORY;
		} else if (gui instanceof GuiIngameMenu) {
			screen = ScreenType.INGAME_MENU;
		} else if (gui instanceof GameSettingsScreen) {
			screen = ScreenType.OPTIONS_MENU;
		} else if (gui instanceof GameSettingsScreen) {
			screen = ScreenType.VIDEO_SETTINGS_MENU;
		} else if (gui instanceof GuiControls) {
			screen = ScreenType.CONTROLS_MENU;
		} else if (gui instanceof GuiAchievements) {
			screen = ScreenType.ACHIEVEMENTS_SCREEN;
		} else if (gui instanceof GuiCrafting) {
			screen = ScreenType.WORKBENCH_INVENTORY;
		} else if (gui instanceof GuiGameOver) {
			screen = ScreenType.GAME_OVER_SCREEN;
		} else if (gui instanceof GuiEditSign) {
			screen = ScreenType.SIGN_SCREEN;
		} else if (gui instanceof GuiStats) {
			screen = ScreenType.STATISTICS_SCREEN;
		}
		return screen;
	}
}
