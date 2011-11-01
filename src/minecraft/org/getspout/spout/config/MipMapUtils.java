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
package org.getspout.spout.config;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

public class MipMapUtils {
	public static int mipmapLevels = 8;
	public static int mode = 0;
	public static boolean updateTerrain = true;
	public static float targetFade = 1F;
	public static float currentFade = 1F;
	
	
	public static void initializeMipMaps() {
		GL11.glPushMatrix();
		int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
		GL11.glBindTexture(3553, terrain);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
		
		int textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int tileWidth = textureWidth / 16;
		
		MipMapUtils.mipmapLevels = (int)Math.round(Math.log((double)tileWidth)/Math.log(2D));

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, MipMapUtils.mipmapLevels);
		
		ContextCapabilities capabilities = GLContext.getCapabilities();
		if (capabilities.OpenGL30) {
			MipMapUtils.mode = 1;
		}
		else if (capabilities.GL_EXT_framebuffer_object) {
			MipMapUtils.mode = 2;
		}
		else if (capabilities.OpenGL14) {
			MipMapUtils.mode = 3;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		}
		MipMapUtils.targetFade = ConfigReader.mipmapsPercent;
		GL11.glPopMatrix();
	}
	
	public static void update() {
		MipMapUtils.targetFade = ConfigReader.mipmapsPercent;
		GL11.glPushMatrix();
		int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
		if (MipMapUtils.mode == 3) {
			MipMapUtils.updateTerrain = ConfigReader.mipmapsPercent > 0F;
			GL11.glBindTexture(3553, terrain);
			if (ConfigReader.mipmapsPercent > 0F) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			}
			else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			}
			GL11.glPopMatrix();
			return;
		}

		if (ConfigReader.mipmapsPercent > 0F) {
			MipMapUtils.updateTerrain = true;

			GL11.glBindTexture(3553, terrain);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
		}
		GL11.glPopMatrix();
	}
	
	public static void onTick() {
		if (updateTerrain) {
			GL11.glPushMatrix();
			int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(3553, terrain);
			
			if (targetFade != currentFade) {
				if (targetFade < currentFade) {
					currentFade -= 0.01f;
					if (currentFade <= targetFade) {
						currentFade = targetFade;
					}
				}
				else {
					currentFade += 0.01f;
					if (currentFade >= targetFade) {
						currentFade = targetFade;
					}
				}

				if (currentFade <= 0.0f) {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F); //default blend state

					updateTerrain = false;
					GL11.glPopMatrix();
					return;
				}
				else {
					GL11.glTexEnvf(GL14.GL_TEXTURE_FILTER_CONTROL, GL14.GL_TEXTURE_LOD_BIAS, mipmapLevels*(currentFade-1.0f));
				}
			}
			
			switch (mode) {
				case 1:
					GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
					break;
	
				case 2:
					EXTFramebufferObject.glGenerateMipmapEXT(GL11.GL_TEXTURE_2D);
					break;
			}
			GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.3F); //more strict blend state
			GL11.glPopMatrix();
		}
	}
}
