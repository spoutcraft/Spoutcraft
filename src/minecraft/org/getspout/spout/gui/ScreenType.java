/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.gui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiStats;
import net.minecraft.src.GuiVideoSettings;

public enum ScreenType {
	GAME_SCREEN(0),
	CHAT_SCREEN(1),
	CUSTOM_SCREEN(2),
	PLAYER_INVENTORY(3),
	CHEST_INVENTORY(4, true),
	DISPENSER_INVENTORY(5, true),
	FURNACE_INVENTORY(6, true),
	INGAME_MENU(7),
	OPTIONS_MENU(8),
	VIDEO_SETTINGS_MENU(9),
	CONTROLS_MENU(10),
	ACHIEVEMENTS_SCREEN(11),
	STATISTICS_SCREEN(12),
	WORKBENCH_INVENTORY(13, true),
	SIGN_SCREEN(14),
	GAME_OVER_SCREEN(15),
	SLEEP_SCREEN(16),
	UNKNOWN(-1);
	
	
	private final int code;
	private final boolean openInstant;
	private static Map<Integer, ScreenType> lookup = new HashMap<Integer, ScreenType>();
	private ScreenType(int code){
		this.code = code;
		openInstant = false;
	}
	
	private ScreenType(int code, boolean openInstant){
		this.code = code;
		this.openInstant = openInstant;
	}
	
	public int getCode(){
		return code;
	}
	
	public boolean isInstantOpen() {
		return openInstant;
	}
	
	public static ScreenType getType(int code){
		return lookup.get(code);
	}
	
		public static ScreenType getType(GuiScreen gui) {
			ScreenType screen = ScreenType.UNKNOWN;
			if (gui instanceof GuiChat) {
				screen = ScreenType.CHAT_SCREEN;
			}
			if (gui instanceof GuiSleepMP) {
				screen = ScreenType.SLEEP_SCREEN;
			}
			if (gui instanceof CustomScreen) {
				screen = ScreenType.CUSTOM_SCREEN;
			}
			if (gui instanceof GuiInventory) {
				screen = ScreenType.PLAYER_INVENTORY;
			}
			if (gui instanceof GuiChest) {
				screen = ScreenType.CHEST_INVENTORY;
			}
			if (gui instanceof GuiDispenser) {
				screen = ScreenType.DISPENSER_INVENTORY;
			}
			if (gui instanceof GuiFurnace) {
				screen = ScreenType.FURNACE_INVENTORY;
			}
			if (gui instanceof GuiIngameMenu) {
				screen = ScreenType.INGAME_MENU;
			}
			if (gui instanceof GuiOptions) {
				screen = ScreenType.OPTIONS_MENU;
			}
			if (gui instanceof GuiVideoSettings) {
				screen = ScreenType.VIDEO_SETTINGS_MENU;
			}
			if (gui instanceof GuiControls) {
				screen = ScreenType.CONTROLS_MENU;
			}
			if (gui instanceof GuiAchievements) {
				screen = ScreenType.ACHIEVEMENTS_SCREEN;
			}
			if (gui instanceof GuiCrafting) {
				screen = ScreenType.WORKBENCH_INVENTORY;
			}
			if (gui instanceof GuiGameOver) {
				screen = ScreenType.GAME_OVER_SCREEN;
			}
			if (gui instanceof GuiEditSign) {
				screen = ScreenType.SIGN_SCREEN;
			}
			if (gui instanceof GuiStats) {
				screen = ScreenType.STATISTICS_SCREEN;
			}
			return screen;
		}
		
	static {
		for(ScreenType type:values()){
			lookup.put(type.code, type);
		}
	}
}