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
package org.spoutcraft.client.config;

import gnu.trove.map.hash.TIntIntHashMap;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.SpoutClient;

public class MipMapUtils {
	private static TIntIntHashMap mipmapLevels = new TIntIntHashMap();
	public static int mode = 0;
	public static boolean updateTerrain = true;
	public static float targetFade = 1F;
	public static float currentFade = 1F;
	private static boolean initialized = false;

	public static void initializeMipMaps() {
		initialized = false;
		GL11.glPushMatrix();
		if (Configuration.getMipmapsPercent() > 0F) {
			int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
			initalizeTexture(terrain);

			for (CustomBlock block : MaterialData.getCustomBlocks()) {
				if (block.getBlockDesign() != null) {
					String texture = block.getBlockDesign().getTexureURL();
					String textureAddon = block.getBlockDesign().getTextureAddon();
					if (texture != null && textureAddon != null) {
						Texture tex = CustomTextureManager.getTextureFromUrl(textureAddon, texture);
						if (tex != null) {
							initalizeTexture(tex.getTextureID());
						}
					}
				}
			}

			MipMapUtils.targetFade = Configuration.getMipmapsPercent();
			initialized = true;
		}
		GL11.glPopMatrix();
	}

	public static int getMipmapLevels(int texture) {
		int levels = mipmapLevels.get(texture);
		if (levels == 0) {
			levels = 8;
		}
		return levels;
	}

	public static void setMipmapLevels(int texture, int levels) {
		mipmapLevels.put(texture, levels);
	}

	public static void initalizeTexture(int textureId) {
		SpoutClient.getHandle().renderEngine.bindTexture(textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);

		int textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int tileWidth = textureWidth / 16;

		setMipmapLevels(textureId, (int)Math.round(Math.log((double)tileWidth)/Math.log(2D)));

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, getMipmapLevels(textureId));

		ContextCapabilities capabilities = GLContext.getCapabilities();
		if (capabilities.OpenGL30) {
			MipMapUtils.mode = 1;
		} else if (capabilities.GL_EXT_framebuffer_object) {
			MipMapUtils.mode = 2;
		} else if (capabilities.OpenGL14) {
			MipMapUtils.mode = 3;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		}
	}

	public static void update() {
		if (!initialized && Configuration.getMipmapsPercent() > 0F) {
			initializeMipMaps();
		}
		MipMapUtils.targetFade = Configuration.getMipmapsPercent();
		int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
		update(terrain);

		for (CustomBlock block : MaterialData.getCustomBlocks()) {
			if (block.getBlockDesign() != null) {
				String texture = block.getBlockDesign().getTexureURL();
				String textureAddon = block.getBlockDesign().getTextureAddon();
				if (texture != null && textureAddon != null) {
					Texture tex = CustomTextureManager.getTextureFromUrl(textureAddon, texture);
					if (tex != null) {
						update(tex.getTextureID());
					}
				}
			}
		}

	}

	public static void update(int texture) {
		GL11.glPushMatrix();
		if (MipMapUtils.mode == 3) {
			MipMapUtils.updateTerrain = Configuration.getMipmapsPercent() > 0F;
			SpoutClient.getHandle().renderEngine.bindTexture(texture);
			if (Configuration.getMipmapsPercent() > 0F) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			}
			GL11.glPopMatrix();
			return;
		}

		if (Configuration.getMipmapsPercent() > 0F) {
			MipMapUtils.updateTerrain = true;
			SpoutClient.getHandle().renderEngine.bindTexture(texture);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
		}
		GL11.glPopMatrix();
	}

	public static void onTick() {
		if (updateTerrain) {
			int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");

			onTick(terrain, targetFade, currentFade);

			if (targetFade != currentFade) {
				for (CustomBlock block : MaterialData.getCustomBlocks()) {
					if (block.getBlockDesign() != null) {
						String texture = block.getBlockDesign().getTexureURL();
						String textureAddon = block.getBlockDesign().getTextureAddon();
						if (texture != null && textureAddon != null) {
							Texture tex = CustomTextureManager.getTextureFromUrl(textureAddon, texture);
							if (tex != null) {
								onTick(tex.getTextureID(), targetFade, currentFade);
							}
						}
					}
				}
			}

			if (targetFade != currentFade) {
				if (targetFade < currentFade) {
					currentFade -= 0.01f;
					if (currentFade <= targetFade) {
						currentFade = targetFade;
					}
				} else {
					currentFade += 0.01f;
					if (currentFade >= targetFade) {
						currentFade = targetFade;
					}
				}
			}
		}
	}

	public static void onTick(int texture, float targetFade, float currentFade) {
		GL11.glPushMatrix();
		SpoutClient.getHandle().renderEngine.bindTexture(texture);

		if (targetFade != currentFade) {
			if (targetFade < currentFade) {
				currentFade -= 0.01f;
				if (currentFade <= targetFade) {
					currentFade = targetFade;
				}
			} else {
				currentFade += 0.01f;
				if (currentFade >= targetFade) {
					currentFade = targetFade;
				}
			}

			if (currentFade <= 0.0f) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F); // Default blend state

				updateTerrain = false;
				GL11.glPopMatrix();
				return;
			} else {
				GL11.glTexEnvf(GL14.GL_TEXTURE_FILTER_CONTROL, GL14.GL_TEXTURE_LOD_BIAS, getMipmapLevels(texture)*(currentFade-1.0f));
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
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.3F); // More strict blend state
		GL11.glPopMatrix();
	}
}
