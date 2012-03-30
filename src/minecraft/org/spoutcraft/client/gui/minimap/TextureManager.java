/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import org.spoutcraft.client.io.CustomTextureManager;

/**
 * @author lahwran
 *
 */
public class TextureManager {

	private int glRoundmap = 0;
	private int glMMArrow = 0;
	private int glWaypoint = 0;
	private int glMarker = 0;
	private int glMinimap = 0;
	private int glWhiteRoundmap = 0;
	private int glWhiteMinimap = 0;
	/**
	 * @param zanMinimap
	 */
	public TextureManager() {}
	
	public void reset() {
		glRoundmap = 0;
		glMMArrow = 0;
		glWaypoint = 0;
		glMarker = 0;
		glMinimap = 0;
		glWhiteRoundmap = 0;
		glWhiteMinimap = 0;
	}

	public void loadRoundmap() {
		if (glRoundmap == 0)
			glRoundmap = CustomTextureManager.getTextureFromJar("/res/minimap/roundmap.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glRoundmap);
	}

	public void loadMMArrow() {
		if (glMMArrow == 0)
			glMMArrow = CustomTextureManager.getTextureFromJar("/res/minimap/mmarrow.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glMMArrow);
	}

	public void loadWaypoint() {
		if (glWaypoint == 0)
			glWaypoint = CustomTextureManager.getTextureFromJar("/res/minimap/waypoint.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glWaypoint);
	}

	public void loadMarker() {
		if (glMarker == 0)
			glMarker = CustomTextureManager.getTextureFromJar("/res/minimap/marker.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glMinimap);
	}

	public void loadMinimap() {
		if (glMinimap == 0)
			glMinimap = CustomTextureManager.getTextureFromJar("/res/minimap/minimap.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glMinimap);
	}
	
	public void loadWhiteMinimap() {
		if (glWhiteMinimap == 0)
			glWhiteMinimap = CustomTextureManager.getTextureFromJar("/res/minimap/squaremap_white.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glWhiteMinimap);
	}
	
	public void loadWhiteRoundmap() {
		if (glWhiteRoundmap == 0)
			glWhiteRoundmap = CustomTextureManager.getTextureFromJar("/res/minimap/roundmap_white.png").getTextureID();
		Minecraft.theMinecraft.renderEngine.bindTexture(glWhiteRoundmap);
	}
}
