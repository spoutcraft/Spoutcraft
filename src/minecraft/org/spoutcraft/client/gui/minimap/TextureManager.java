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
}
