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

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import org.spoutcraft.client.SpoutClient;

public class ImageManager {
	protected final BufferedImage image;

	private int glImage = 0;

	private volatile boolean hasGLImage = false;

	private volatile boolean hasChanged = true;

	/**
	 * @param imageSize
	 * @param imageSize2
	 * @param type
	 */
	public ImageManager(int sizeX, int sizeY, int type) {
		image = new BufferedImage(sizeX, sizeY, type);
	}

	public void setRGB(int X, int Y, int color) {
		synchronized(image) {
			image.setRGB(X, Y, 0xff000000 | color);
			hasChanged = true;
		}
	}

	public void setARGB(int X, int Y, int color) {
		synchronized(image) {
			image.setRGB(X, Y, color);
			hasChanged = true;
		}
	}

	public void loadGLImage() {
		synchronized (image) {
			if (hasGLImage && hasChanged) {
				Minecraft.theMinecraft.renderEngine.deleteTexture(glImage);
				GL11.glPushMatrix();
				glImage = Minecraft.theMinecraft.renderEngine.allocateAndSetupTexture(image);
				GL11.glPopMatrix();
				hasChanged = false;
			} else if (!hasGLImage) {
				GL11.glPushMatrix();
				glImage = Minecraft.theMinecraft.renderEngine.allocateAndSetupTexture(image);
				GL11.glPopMatrix();
				hasGLImage = true;
				hasChanged = false;
			} else if (glImage > 0) {
				SpoutClient.getHandle().renderEngine.bindTexture(glImage);
			}
		}
	}
}
