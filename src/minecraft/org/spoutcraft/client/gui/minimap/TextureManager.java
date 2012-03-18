package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.client.io.CustomTextureManager;

/**
 * @author lahwran
 *
 */
public class TextureManager {

	/**
	 * @param zanMinimap
	 */
	public TextureManager() {}

	public void loadRoundmap() {
		CustomTextureManager.getTextureFromJar("/res/minimap/roundmap.png").bind();
	}

	public void loadMMArrow() {
		CustomTextureManager.getTextureFromJar("/res/minimap/mmarrow.png").bind();
	}

	public void loadWaypoint() {
		CustomTextureManager.getTextureFromJar("/res/minimap/waypoint.png").bind();
	}

	public void loadMarker() {
		CustomTextureManager.getTextureFromJar("/res/minimap/marker.png").bind();
	}

	public void loadMinimap() {
		CustomTextureManager.getTextureFromJar("/res/minimap/minimap.png").bind();
	}
}
