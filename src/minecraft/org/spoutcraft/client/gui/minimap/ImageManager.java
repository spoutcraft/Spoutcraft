package org.spoutcraft.client.gui.minimap;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

/**
 * @author lahwran
 *
 */
public class ImageManager {

	private final BufferedImage image;
	
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
		image.setRGB(X, Y, 0xff000000 | color);
		hasChanged = true;
	}
	
	public void setARGB(int X, int Y, int color) {
		image.setRGB(X, Y, color);
		hasChanged = true;
	}

	public void loadGLImage() {
		if (hasGLImage && hasChanged) {
			Minecraft.theMinecraft.renderEngine.deleteTexture(glImage);
			GL11.glPushMatrix();
			glImage = Minecraft.theMinecraft.renderEngine.allocateAndSetupTexture(image);
			GL11.glPopMatrix();
			hasChanged = false;
		}
		else if (!hasGLImage) {
			GL11.glPushMatrix();
			glImage = Minecraft.theMinecraft.renderEngine.allocateAndSetupTexture(image);
			GL11.glPopMatrix();
			hasGLImage = true;
			hasChanged = false;
		}
		else if (glImage > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, glImage);
		}
	}
}
