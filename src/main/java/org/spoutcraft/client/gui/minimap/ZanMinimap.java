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
package org.spoutcraft.client.gui.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiSleepMP;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.settings.GuiAdvancedOptions;
import org.spoutcraft.client.gui.settings.GuiSimpleOptions;

/**
 * Main Zanminimap class where everything happens
 */
public class ZanMinimap {
	/**
	 * MapCalculator instance, public for things that want to plug into the minimap
	 */
	public MapCalculator mapcalc;

	/**
	 * MapRenderer instance, public for things that want to plug into the minimap
	 */
	public MapRenderer renderer;

	public Map map;

	public TextureManager texman;

	/**
	 * Instance, mainly for things that want to plug into the minimap
	 */
	public static ZanMinimap instance;

	public ZanMinimap() {
		MinimapConfig.initialize();
		BlockColor.initDefaultColors();

		map = new Map();
		texman = new TextureManager();
		mapcalc = new MapCalculator(this);
		renderer = new MapRenderer(this);
		mapcalc.start();
		instance = this;
	}

	/**
	 * Heartbeat function called each render by whatever is managing the minimap.
	 * 
	 * @param mc Minecraft instance to initialize obfhub.game with
	 */
	public void onRenderTick() {
		if (Minecraft.theMinecraft.thePlayer == null || SpoutClient.getInstance().getRawWorld() == null) {
			return;
		}

		if (Minecraft.theMinecraft.currentScreen != null && !(isTransparentMenu() || isChatMenu())) {
			return;
		}

		if (!MinimapConfig.getInstance().isEnabled()) {
			return;
		}

		if (!Spoutcraft.hasPermission("spout.plugin.minimap")) {
			return;
		}

		int scWidth = Spoutcraft.getRenderDelegate().getScreenWidth();
		int scHeight = Spoutcraft.getRenderDelegate().getScreenHeight();

		mapcalc.onRenderTick();
		renderer.onRenderTick(scWidth, scHeight);
	}

	private boolean isTransparentMenu() {
		return Minecraft.theMinecraft.currentScreen instanceof GuiIngameMenu || Minecraft.theMinecraft.currentScreen instanceof GuiAdvancedOptions || Minecraft.theMinecraft.currentScreen instanceof GuiSimpleOptions || Minecraft.theMinecraft.currentScreen instanceof GuiMinimapMenu || Minecraft.theMinecraft.currentScreen instanceof GuiMoveMinimap;
	}

	private boolean isChatMenu() {
		return Minecraft.theMinecraft.currentScreen instanceof GuiChat || Minecraft.theMinecraft.currentScreen instanceof GuiSleepMP;
	}
}
