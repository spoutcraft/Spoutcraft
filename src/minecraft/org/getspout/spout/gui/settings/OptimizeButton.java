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
package org.getspout.spout.gui.settings;

import net.minecraft.client.Minecraft;
import org.getspout.spout.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class OptimizeButton extends GenericButton{
	public OptimizeButton() {
		super("Optimize Video Settings");
		setEnabled(Minecraft.theMinecraft.theWorld != null);
	}
	
	@Override
	public String getTooltip() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return "This can only be used in game";
		}
		return "Attempts to configure your video settings to achieve ~60 fps.\nMay be more or less, depending on hardware.";
	}
	
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		int cores = Runtime.getRuntime().availableProcessors();
		int fps = Math.max(1, net.minecraft.client.Minecraft.framesPerSecond);
		System.out.println("Optimizing Settings, Cores: " + cores + " FPS: " + fps);
		if (fps > 150) {
			ConfigReader.fancyFog = true;
			Minecraft.theMinecraft.gameSettings.fancyGraphics = true;
			ConfigReader.fancyGraphics = true;
			ConfigReader.preloadedChunks = 6;
			ConfigReader.advancedOpenGL = 2;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			ConfigReader.chunkUpdates = 4;
			if (cores > 1) {
				ConfigReader.smoothFPS = true;
				ConfigReader.preloadedChunks = 8;
			}
			ConfigReader.renderDistance = 0;
			Minecraft.theMinecraft.gameSettings.renderDistance = 0;
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
			ConfigReader.signDistance = Integer.MAX_VALUE;
		}
		else if (fps > 100) {
			ConfigReader.chunkUpdates = 2;
			ConfigReader.fancyFog = true;
			Minecraft.theMinecraft.gameSettings.fancyGraphics = true;
			ConfigReader.fancyGraphics = true;
			ConfigReader.preloadedChunks = 2;
			ConfigReader.advancedOpenGL = 2;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			ConfigReader.renderDistance = 0;
			Minecraft.theMinecraft.gameSettings.renderDistance = 0;
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
			ConfigReader.signDistance = 128;
		}
		else if (fps > 60) {
			ConfigReader.preloadedChunks = 0;
			ConfigReader.advancedOpenGL = 1;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			ConfigReader.signDistance = 64;
			//Ideal range
		}
		else if (fps > 30) {
			ConfigReader.farView = false;
			ConfigReader.preloadedChunks = 0;
			ConfigReader.advancedOpenGL = 1;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			ConfigReader.smoothFPS = false;
			ConfigReader.clearWater = false;
			ConfigReader.fancyGraphics = false;
			ConfigReader.renderDistance = Math.max(1, ConfigReader.renderDistance);
			Minecraft.theMinecraft.gameSettings.renderDistance = Math.max(1, ConfigReader.renderDistance);
			ConfigReader.performance = 0;
			Minecraft.theMinecraft.renderGlobal.loadRenderers();
			ConfigReader.signDistance = 32;
		}
		else if (fps > 20) {
			ConfigReader.fancyFog = false;
			ConfigReader.farView = false;
			ConfigReader.preloadedChunks = 0;
			ConfigReader.advancedOpenGL = 0;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = false;
			ConfigReader.smoothFPS = false;
			ConfigReader.clearWater = false;
			ConfigReader.fancyGraphics = false;
			Minecraft.theMinecraft.gameSettings.fancyGraphics = false;
			ConfigReader.chunkUpdates = 1;
			ConfigReader.renderDistance = Math.max(2, ConfigReader.renderDistance);
			Minecraft.theMinecraft.gameSettings.renderDistance = Math.max(2, ConfigReader.renderDistance);
			ConfigReader.performance = 0;
			Minecraft.theMinecraft.renderGlobal.loadRenderers();
			ConfigReader.signDistance = 16;
		}
		else {
			ConfigReader.fancyFog = false;
			ConfigReader.farView = false;
			ConfigReader.preloadedChunks = 0;
			ConfigReader.smoothFPS = false;
			ConfigReader.advancedOpenGL = 0;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = false;
			ConfigReader.clearWater = false;
			ConfigReader.fancyGraphics = false;
			Minecraft.theMinecraft.gameSettings.fancyGraphics = false;
			ConfigReader.chunkUpdates = 1;
			ConfigReader.renderDistance = 3;
			Minecraft.theMinecraft.gameSettings.renderDistance = 3;
			ConfigReader.performance = 0;
			Minecraft.theMinecraft.renderGlobal.loadRenderers();
			ConfigReader.signDistance = 8;
		}
	}
}
