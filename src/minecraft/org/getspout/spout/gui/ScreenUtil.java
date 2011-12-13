package org.getspout.spout.gui;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.settings.VideoSettings;

import net.minecraft.src.GameSettings;
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
import net.minecraft.src.StatFileWriter;
import org.spoutcraft.spoutcraftapi.gui.*;

public class ScreenUtil {
	public static void open(ScreenType type){
		GuiScreen toOpen = null;
		GameSettings settings = SpoutClient.getHandle().gameSettings;
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
			toOpen = new GuiOptions(new GuiIngameMenu(), settings);
			break;
		case VIDEO_SETTINGS_MENU:
			toOpen = new VideoSettings(new GuiIngameMenu());
			break;
		case CONTROLS_MENU:
			toOpen = new GuiControls(new GuiOptions(new GuiIngameMenu(), settings), settings);
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
		if(gui == null) {
			screen = ScreenType.GAME_SCREEN;
		}
		if (gui instanceof GuiChat) {
			screen = ScreenType.CHAT_SCREEN;
		}
		else if (gui instanceof GuiSleepMP) {
			screen = ScreenType.SLEEP_SCREEN;
		}
		else if (gui instanceof CustomScreen) {
			screen = ScreenType.CUSTOM_SCREEN;
		}
		else if (gui instanceof GuiInventory) {
			screen = ScreenType.PLAYER_INVENTORY;
		}
		else if (gui instanceof GuiChest) {
			screen = ScreenType.CHEST_INVENTORY;
		}
		else if (gui instanceof GuiDispenser) {
			screen = ScreenType.DISPENSER_INVENTORY;
		}
		else if (gui instanceof GuiFurnace) {
			screen = ScreenType.FURNACE_INVENTORY;
		}
		else if (gui instanceof GuiIngameMenu) {
			screen = ScreenType.INGAME_MENU;
		}
		else if (gui instanceof GuiOptions) {
			screen = ScreenType.OPTIONS_MENU;
		}
		else if (gui instanceof VideoSettings) {
			screen = ScreenType.VIDEO_SETTINGS_MENU;
		}
		else if (gui instanceof GuiControls) {
			screen = ScreenType.CONTROLS_MENU;
		}
		else if (gui instanceof GuiAchievements) {
			screen = ScreenType.ACHIEVEMENTS_SCREEN;
		}
		else if (gui instanceof GuiCrafting) {
			screen = ScreenType.WORKBENCH_INVENTORY;
		}
		else if (gui instanceof GuiGameOver) {
			screen = ScreenType.GAME_OVER_SCREEN;
		}
		else if (gui instanceof GuiEditSign) {
			screen = ScreenType.SIGN_SCREEN;
		}
		else if (gui instanceof GuiStats) {
			screen = ScreenType.STATISTICS_SCREEN;
		}
		return screen;
	}
}
