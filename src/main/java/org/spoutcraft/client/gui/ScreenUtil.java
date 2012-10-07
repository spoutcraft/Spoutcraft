/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui;

import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiBrewingStand;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiEnchantment;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiLanguage;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiStats;
import net.minecraft.src.GuiWinGame;
import net.minecraft.src.StatFileWriter;

import org.spoutcraft.api.gui.*;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.GuiAmbigousInput;
import org.spoutcraft.client.gui.chat.GuiChatSettings;
import org.spoutcraft.client.gui.chat.GuiListEdit;
import org.spoutcraft.client.gui.chat.GuiURLConfirm;
import org.spoutcraft.client.gui.controls.GuiControls;
import org.spoutcraft.client.gui.controls.GuiEditShortcut;
import org.spoutcraft.client.gui.minimap.GuiAddWaypoint;
import org.spoutcraft.client.gui.minimap.GuiMinimapMenu;
import org.spoutcraft.client.gui.minimap.GuiMoveMinimap;
import org.spoutcraft.client.gui.minimap.GuiOverviewMap;
import org.spoutcraft.client.gui.settings.GuiAdvancedOptions;
import org.spoutcraft.client.gui.settings.GuiSimpleOptions;

public class ScreenUtil {
	public static void open(ScreenType type) {
		GuiScreen toOpen = null;
		StatFileWriter statfile = SpoutClient.getHandle().statFileWriter;
		switch(type) {
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
				toOpen = GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu());
				break;
			case VIDEO_SETTINGS_MENU:
				toOpen = GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu());
				break;
			case CONTROLS_MENU:
				toOpen = new GuiControls(GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu()));
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
			case CHANGE_LANGUAGE:
				toOpen = new GuiLanguage(GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu()), SpoutClient.getHandle().gameSettings);
				break;
			case MINIMAP_SETTINGS:
				toOpen = new GuiMinimapMenu(GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu()));
				break;
			case CHAT_SETTINGS:
				toOpen = new GuiChatSettings(GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu()));
				break;
			case MOVE_MINIMAP:
				toOpen = new GuiMoveMinimap(new GuiMinimapMenu(GuiSimpleOptions.constructOptionsScreen(new GuiIngameMenu())));
				break;
			case OVERVIEW_MAP:
				toOpen = new GuiOverviewMap();
				break;
			case WIN_GAME:
				toOpen = new GuiWinGame();
				break;
		}
		SpoutClient.getHandle().displayGuiScreen(toOpen);
	}

	public static ScreenType getType(GuiScreen gui) {
		ScreenType screen = ScreenType.UNKNOWN;
		if (gui == null) {
			screen = ScreenType.GAME_SCREEN;
		}
		if (gui instanceof CustomScreen) {
			screen = ScreenType.CUSTOM_SCREEN;
		} else if (gui instanceof GuiAdvancedOptions) {
			screen = ScreenType.VIDEO_SETTINGS_MENU;
		} else if (gui instanceof GuiAchievements) {
			screen = ScreenType.ACHIEVEMENTS_SCREEN;
		} else if (gui instanceof GuiAddWaypoint) {
			screen = ScreenType.ADD_WAYPOINT;
		} else if (gui instanceof GuiSleepMP) {
			screen = ScreenType.SLEEP_SCREEN;
		} else if (gui instanceof GuiChat) {
			screen = ScreenType.CHAT_SCREEN;
		} else if (gui instanceof GuiBrewingStand) {
			screen = ScreenType.BREWING_STAND_INVENTORY;
		} else if (gui instanceof GuiChest) {
			screen = ScreenType.CHEST_INVENTORY;
		} else if (gui instanceof GuiContainerCreative) {
			screen = ScreenType.PLAYER_INVENTORY_CREATIVE;
		} else if (gui instanceof GuiCrafting) {
			screen = ScreenType.WORKBENCH_INVENTORY;
		} else if (gui instanceof GuiDispenser) {
			screen = ScreenType.DISPENSER_INVENTORY;
		} else if (gui instanceof GuiEnchantment) {
			screen = ScreenType.ENCHANTMENT_INVENTORY;
		} else if (gui instanceof GuiFurnace) {
			screen = ScreenType.FURNACE_INVENTORY;
		} else if (gui instanceof GuiInventory) {
			screen = ScreenType.PLAYER_INVENTORY;
		} else if (gui instanceof GuiEditShortcut) {
			screen = ScreenType.EDIT_SHORTCUT;
		} else if (gui instanceof GuiEditSign) {
			screen = ScreenType.SIGN_SCREEN;
		} else if (gui instanceof GuiGameOver) {
			screen = ScreenType.GAME_OVER_SCREEN;
		} else if (gui instanceof GuiIngameMenu) {
			screen = ScreenType.INGAME_MENU;
		} else if (gui instanceof GuiLanguage) {
			screen = ScreenType.CHANGE_LANGUAGE;
		} else if (gui instanceof GuiMinimapMenu) {
			screen = ScreenType.MINIMAP_SETTINGS;
		} else if (gui instanceof GuiAmbigousInput) {
			screen = ScreenType.AMBIGUOUS_SHORTCUT;
		} else if (gui instanceof GuiChatSettings) {
			screen = ScreenType.CHAT_SETTINGS;
		} else if (gui instanceof GuiControls) {
			screen = ScreenType.CONTROLS_MENU;
		} else if (gui instanceof GuiListEdit) {
			screen = ScreenType.LIST_EDIT;
		} else if (gui instanceof GuiMoveMinimap) {
			screen = ScreenType.MOVE_MINIMAP;
		} else if (gui instanceof GuiOverviewMap) {
			screen = ScreenType.OVERVIEW_MAP;
		} else if (gui instanceof GuiStats) {
			screen = ScreenType.STATISTICS_SCREEN;
		} else if (gui instanceof GuiWinGame) {
			screen = ScreenType.WIN_GAME;
		} else if (gui instanceof GuiURLConfirm) {
			screen = ScreenType.CONFIRM_URL;
		}

		return screen;
	}
}
