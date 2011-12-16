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
package org.spoutcraft.client.gui.settings;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class PreloadedChunksButton extends GenericButton{
	public PreloadedChunksButton() {
		setTooltip("Defines an area in which no chunks will be loaded\n0 - after 5m new chunks will be loaded\n2 - after 32m  new chunks will be loaded\n8 - after 128m new chunks will be loaded\nHigher values need more time to load all the chunks");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.preloadedChunks += 2;
		if (ConfigReader.preloadedChunks > 8) {
			ConfigReader.preloadedChunks = 0;
		}
		ConfigReader.write();
		
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
	
	public String getText() {
		return "Preloaded Chunks: " + ConfigReader.preloadedChunks;
	}
}