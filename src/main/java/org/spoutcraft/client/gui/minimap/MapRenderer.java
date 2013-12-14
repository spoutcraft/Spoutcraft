/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org//>
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

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.RenderUtil;

public class MapRenderer {
	/** Direction you're facing */
	public float direction = 0.0f;

	/** Last direction you were facing */
	public float oldDir = 0.0f;

	private Map map;
	private TextureManager texman;

	private double distanceToFocus;

	/**
	 * @param minimap
	 *			minimap instance to init with
	 */
	public MapRenderer(ZanMinimap minimap) {
		map = minimap.map;
		texman = minimap.texman;
	}

	/**
	 * Do rendering
	 *
	 * @param scWidth
	 *			screen width
	 * @param scHeight
	 *			screen height
	 */
	public void onRenderTick(int scWidth, int scHeight) {
		if (this.oldDir != Minecraft.getMinecraft().thePlayer.rotationYaw) {
			this.direction += this.oldDir - Minecraft.getMinecraft().thePlayer.rotationYaw;
			this.oldDir = Minecraft.getMinecraft().thePlayer.rotationYaw;
		}

		if (this.direction >= 360.0f)
			this.direction %= 360.0f;

		if (this.direction < 0.0f) {
			while (this.direction < 0.0f)
				this.direction += 360.0f;
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(scWidth, 0.0f, 0.0f);
		GL11.glTranslatef(MinimapConfig.getInstance().getAdjustX(), MinimapConfig.getInstance().getAdjustY(), 0);
		GL11.glScalef(MinimapConfig.getInstance().getSizeAdjust(), MinimapConfig.getInstance().getSizeAdjust(), 1F);
		if (!MinimapConfig.getInstance().isScale()) {
			float scaleFactor = Math.max(scWidth / 427, scHeight / 240F);
			GL11.glScalef(scaleFactor, scaleFactor, 1);
		}
		renderMap();
		if (MinimapConfig.getInstance().isCavemap())
			renderMapFull(0, scHeight);

		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		showCoords(0);
		GL11.glPopMatrix();
	}

	private void renderMap() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (MinimapConfig.getInstance().isEnabled()) {
			if (MinimapConfig.getInstance().isSquare()) {
				// Scale
				GL11.glPushMatrix();
				GL11.glTranslatef(-34.0f, 30.0F, 0.0F);
				GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
				switch (MinimapConfig.getInstance().getZoom()) {
				case 0:
					GL11.glScalef(8F, 8F, 1F);
					GL11.glTranslatef(56, 0, 0F);
					break;
				case 1:
					GL11.glScalef(4F, 4F, 1F);
					GL11.glTranslatef(48, 0, 0F);
					break;
				case 2:
					GL11.glScalef(2F, 2F, 1F);
					GL11.glTranslatef(32, 0, 0F);
					break;
				}

				map.loadColorImage();

				drawOnMap();

				if (MinimapConfig.getInstance().isHeightmap()) {
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_COLOR);

					map.loadHeightImage();

					drawOnMap();
					GL11.glRotatef(90f, 0.0f, 0.0f, 1.0f);
				}
				GL11.glPopMatrix();

				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				renderEntities();

				try {
					GL11.glPushMatrix();
					GL11.glScalef(1.8f, 1.8f, 1.0f);
					GL11.glTranslatef(27, -1, 0F); // don't ask
					if (MinimapConfig.getInstance().isShowBackground()) {
						texman.loadMinimap();
					} else {
						GL11.glScalef(138F / 256F, 138F / 256F, 1F);
						GL11.glTranslatef(-55, 3, 0);
						texman.loadWhiteMinimap();
					}
					drawOnMap();
				} catch (Exception e) {
//					System.err.println("error: minimap overlay not found!");
//					e.printStackTrace();
				} finally {
					GL11.glPopMatrix();
				}

				renderWaypoints();

				try {
					GL11.glPushMatrix();
					texman.loadMMArrow();
					if (MinimapConfig.getInstance().isShowBackground()) {
						GL11.glTranslatef(-34.0F, 30.0F, 0.0F);
					} else {
						GL11.glTranslatef(-36.0F, 32.0F, 0.0F);
					}
					GL11.glRotatef(-this.direction, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
					drawOnMap();
				} catch (Exception e) {
//					System.err.println("Error: minimap arrow not found!");
					e.printStackTrace();
				} finally {
					GL11.glPopMatrix();
				}

				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GL11.glPushMatrix();
				GL11.glTranslatef(-2, 2, 0.0F); // Lines up compass directions when rendering on square map
				drawDirections();
				drawFocusSquare();
				GL11.glPopMatrix();

			} else {
				GL11.glPushMatrix();

				map.loadColorImage();

				GL11.glTranslatef(-32.0f, 32.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(32.0F, -(32.0F), 0.0F);

				switch (MinimapConfig.getInstance().getZoom()) {
				case 0:
					GL11.glScalef(8F, 8F, 1F);
					GL11.glTranslatef(56.25F, 0.25F, 0F);
					break;
				case 1:
					GL11.glScalef(4F, 4F, 1F);
					GL11.glTranslatef(48.5F, 0.5F, 0F);
					break;
				case 2:
					GL11.glScalef(2F, 2F, 1F);
					GL11.glTranslatef(33F, 1F, 0F);
					break;
				}

				drawOnMap();

				if (MinimapConfig.getInstance().isHeightmap()) {
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_COLOR);

					map.loadHeightImage();

					drawOnMap();
				}

				GL11.glPopMatrix();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				renderWaypoints();
				renderEntities();
				drawRound();

				try {
					GL11.glPushMatrix();
					texman.loadMMArrow();
					if (MinimapConfig.getInstance().isShowBackground()) {
						GL11.glTranslatef(-32.0f, 32.0F, 0.0F);
					} else {
						GL11.glTranslatef(-31.0f, 31.0F, 0.0F);
					}
					GL11.glRotatef(-this.direction, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
					drawOnMap();
				} catch (Exception e) {
//					System.err.println("Error: minimap arrow not found!");
					e.printStackTrace();
				} finally {
					GL11.glPopMatrix();
				}

				drawDirections();
				GL11.glPushMatrix();
				drawFocusRound();
				GL11.glPopMatrix();
			}
		}
	}

	private void renderEntities() {
		if (!Spoutcraft.hasPermission("spout.plugin.minimap.showentities")) {
			return;
		}
		if (!MinimapConfig.getInstance().isShowingEntities()) {
			return;
		}
		boolean isSquare = MinimapConfig.getInstance().isSquare();
		boolean isShowBackground = MinimapConfig.getInstance().isShowBackground();
		double playerX = map.getPlayerX();
		double playerZ = map.getPlayerZ();

		synchronized (map.watchedEntities) {
			for (WatchedEntity w:map.watchedEntities) {
				Entity e = w.entity;
				double entityX = e.posX - playerX;
				double entityZ = e.posZ - playerZ;
				boolean render = false;

				int circleX = MathHelper.floor_double(playerX);
				int circleY = MathHelper.floor_double(playerZ);

				if (isSquare) {
					render = Math.abs(playerX - (int) e.posX) < (map.renderSize / 2) - 6 && Math.abs(playerZ - (int) e.posZ) < (map.renderSize / 2) - 6;
				} else {
					render = MinimapUtils.insideCircle(circleX, circleY, (map.renderSize / 2) - 4, (int) e.posX, (int) e.posZ);
				}
				Texture tex = w.getTexture();
				if (render && tex != null) {
					GL11.glPushMatrix();
					if (isSquare && isShowBackground) { // Square Map with ancient-style texture
						GL11.glTranslatef(-34.0f, 30.0f, 0.0f);
					} else if (isSquare) { // Square Map with basic texture
						GL11.glTranslatef(-36.0f, 32.0f, 0.0f);
					} else if (isShowBackground) { // Round Map with ancient-style texture
						GL11.glTranslatef(-32.0f, 32.0f, 0.0f);
					} else { // Round Map with basic texture
						GL11.glTranslatef(-31.0f, 31.0f, 0.0f);
					}
					GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
					switch (MinimapConfig.getInstance().getZoom()) {
					case 0:
						GL11.glTranslated(-entityZ, entityX, 0F);
						break;
					case 1:
						GL11.glTranslated(-entityZ * 0.45F, entityX * 0.45F, 0F);
						break;
					case 2:
						GL11.glTranslated(-entityZ * 0.25F, entityX * 0.28F, 0F);
						break;
					}
					GL11.glScaled(0.05, 0.05, 0.05);
					if (MinimapConfig.getInstance().isSquare()) {
						GL11.glTranslated(-34f, -30f, 0); // Handles entity face offset for the square map
					} else {
						GL11.glTranslated(-32f, -32f, 0); // Handles entity face offset for the round map
					}
					GL11.glRotatef(-90, 0, 0, 1); // Handles entity face orientation
					tex.bind();
					drawOnMap();
					GL11.glPopMatrix();
				}
			}
		}
	}

	private void drawFocusRound() {
		Waypoint focus = MinimapConfig.getInstance().getFocussedWaypoint();
		if (focus != null) {
			GL11.glTranslated(-map.renderSize / 4d, map.renderSize / 4d, 0);
			GL11.glPushMatrix();
			GL11.glRotatef(this.direction - 180f, 0f, 0f, 1f);
			double px = Minecraft.getMinecraft().thePlayer.posX;
			double pz = Minecraft.getMinecraft().thePlayer.posZ;
			int x = focus.x;
			int z = focus.z;

			int radius = map.renderSize / 4 - 4;
			double dx = x - px;
			double dz = z - pz;
			double l = Math.sqrt(dx * dx + dz * dz);
			this.distanceToFocus = l;
			if (l > radius * 2) {
				double f = radius / l;
				int fx = (int) (f * dx);
				int fz = (int) (f * dz);
				RenderUtil.drawRectangle(fx - 1, fz - 1, fx + 1, fz + 1, 0xff00ffff);
			}
			GL11.glPopMatrix();
		}
	}

	private void drawFocusSquare() {
		Waypoint focus = MinimapConfig.getInstance().getFocussedWaypoint();
		if (focus != null) {
			GL11.glTranslated(-map.renderSize / 4d, map.renderSize / 4d - 4, 0);
			GL11.glRotatef(90f, 0, 0, 1f);
			RenderUtil.drawRectangle(map.renderSize / 4, 0, map.renderSize / 4 + 2, 2, 0xff0000);
			double px = Minecraft.getMinecraft().thePlayer.posX;
			double pz = Minecraft.getMinecraft().thePlayer.posZ;
			double x = focus.x;
			double z = focus.z;

			double dx = x - px;
			double dz = z - pz;

			double alpha = 0;
			if (dx == 0) {
				alpha = 0d;
			} else {
				alpha = Math.atan(dz / dx);
			}
			double l = Math.sqrt(dx * dx + dz * dz);
			this.distanceToFocus = l;
			if (true) {
				int fx;
				int fz;
				if (Math.abs(dx) > Math.abs(dz)) {
					if (dx > 0) {
						fx = map.renderSize / 4;
					} else {
						fx = -map.renderSize / 4;
					}
					fz = (int) (Math.tan(alpha) * fx);
				} else {
					if (dz > 0) {
						fz = map.renderSize / 4;
					} else {
						fz = -map.renderSize / 4;
					}
					fx = (int) (1 / Math.tan(alpha) * fz);
				}
				if (Math.abs(dx) >= (map.renderSize - 5) / 2 || Math.abs(dz) >= (map.renderSize - 5) / 2) {
					RenderUtil.drawRectangle(fx - 1, fz - 1, fx + 1, fz + 1, 0xff00ffff);
				}
			}
		}
	}

	private void renderWaypoints() {
		double playerX = Minecraft.getMinecraft().thePlayer.posX;
		double playerZ = Minecraft.getMinecraft().thePlayer.posZ;
		for (Waypoint pt : MinimapConfig.getInstance().getWaypoints(MinimapUtils.getWorldName())) {
			if (pt.enabled) {
				double wayX = playerX - pt.x;
				double wayY = playerZ - pt.z;

				int circleX = MathHelper.floor_double(playerX);
				int circleY = MathHelper.floor_double(playerZ);

				boolean render = false;
				if (MinimapConfig.getInstance().isSquare()) {
					render = Math.abs(playerX - pt.x) < map.renderSize && Math.abs(playerZ - pt.z) < map.renderSize;
				} else {
					render = MinimapUtils.insideCircle(circleX, circleY, map.renderSize / 2, pt.x, pt.z);
				}

				if (render) {
					GL11.glPushMatrix();

					GL11.glTranslatef(32.0f, -32.0F, 0.0F);
					GL11.glRotatef(-(this.direction + 90.0F), 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(-32.0F, -32.0F, 0.0F);

					GL11.glTranslatef(-33F, 29F, 0F);
					switch (MinimapConfig.getInstance().getZoom()) {
					case 0:
						GL11.glTranslated(wayY, -wayX, 0F);
						break;
					case 1:
						GL11.glTranslated(wayY * 0.45F, -wayX * 0.45F, 0F);
						break;
					case 2:
						GL11.glTranslated(wayY * 0.25F, -wayX * 0.28F, 0F);
						break;
					}
					GL11.glScalef(0.25F, 0.25F, 1F);

					GL11.glPopMatrix();
				}
			}
		}
		GL11.glColor3f(1f, 1f, 1f);
	}

	private void renderMapFull(int scWidth, int scHeight) {
		map.loadColorImage();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV((scWidth) / 2 - 128, (scHeight) / 2 + 128, 1.0D, 0.0D, 1.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 + 128, (scHeight) / 2 + 128, 1.0D, 1.0D, 1.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 + 128, (scHeight) / 2 - 128, 1.0D, 1.0D, 0.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 - 128, (scHeight) / 2 - 128, 1.0D, 0.0D, 0.0D);
		Tessellator.instance.draw();

		try {
			GL11.glPushMatrix();
			texman.loadMMArrow();
			GL11.glTranslatef((scWidth) / 2, (scHeight) / 2, 0.0F);
			GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-((scWidth) / 2), -((scHeight) / 2), 0.0F);
			Tessellator.instance.startDrawingQuads();
			Tessellator.instance.addVertexWithUV((scWidth) / 2 - 32, (scHeight) / 2 + 32, 1.0D, 0.0D, 1.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 + 32, (scHeight) / 2 + 32, 1.0D, 1.0D, 1.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 + 32, (scHeight) / 2 - 32, 1.0D, 1.0D, 0.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 - 32, (scHeight) / 2 - 32, 1.0D, 0.0D, 0.0D);
			Tessellator.instance.draw();
		} catch (Exception e) {
//			System.err.println("Error: minimap arrow not found!");
			e.printStackTrace();
		} finally {
			GL11.glPopMatrix();
		}
	}

	private void showCoords(int scWidth) {
		if (MinimapConfig.getInstance().isCoords()) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, 0.5f, 1.0f);
			String xy = ((int) Minecraft.getMinecraft().thePlayer.posX) + ", " + ((int) Minecraft.getMinecraft().thePlayer.posZ);
			int m = Minecraft.getMinecraft().fontRenderer.getStringWidth(xy) / 2;
			Minecraft.getMinecraft().fontRenderer.drawString(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
			xy = Integer.toString((int) (Minecraft.getMinecraft().thePlayer.posY - 1.620d)); // Substract eyes pos
			m = Minecraft.getMinecraft().fontRenderer.getStringWidth(xy) / 2;
			Minecraft.getMinecraft().fontRenderer.drawString(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
			if (MinimapConfig.getInstance().getFocussedWaypoint() != null) {
				String text = (int) distanceToFocus + "m";
				int y = MinimapConfig.getInstance().getFocussedWaypoint().y;
				if (distanceToFocus < 10) {
					double py = Minecraft.getMinecraft().thePlayer.posY;
					if (y < py - 3) {
						text = "\\/ " + text;
					}
					if (y > py + 3) {
						text = "/\\ " + text;
					}
				}
				m = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
				Minecraft.getMinecraft().fontRenderer.drawString(text, scWidth * 2 - 32 * 2 - m / 2, 166, 0xffffff);
			}
			GL11.glPopMatrix();
		}
	}

	private void drawRound() {
		try {
			GL11.glPushMatrix();
			if (MinimapConfig.getInstance().isShowBackground()) {
				texman.loadRoundmap();
			} else {
				GL11.glScaled(0.97F, 0.97F, 1F);
				GL11.glTranslatef(-0.50F, 0.50F, 0);
				texman.loadWhiteRoundmap();
			}
			drawOnMap();
		} catch (Exception localException) {
//			System.err.println("Error: minimap overlay not found!");
		} finally {
			GL11.glPopMatrix();
		}
	}

	private void drawOnMap() {
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(-64.0D, 64.0D, 1.0D, 0.0D, 1.0D);
		Tessellator.instance.addVertexWithUV(0, 64.0D, 1.0D, 1.0D, 1.0D);
		Tessellator.instance.addVertexWithUV(0, 0, 1.0D, 1.0D, 0.0D);
		Tessellator.instance.addVertexWithUV(-64.0D, 0, 1.0D, 0.0D, 0.0D);
		Tessellator.instance.draw();
	}

	private void drawDirections() {
		if (!MinimapConfig.getInstance().isDirections()) {
			return;
		}
		float dir = -180;
		int xPosition;
		int yPosition;
		boolean isSquare = MinimapConfig.getInstance().isSquare();
		boolean isShowBackground = MinimapConfig.getInstance().isShowBackground();

		if (isSquare && isShowBackground) { // Square Map with ancient-style texture
			xPosition = -66;
			yPosition = 54;
		} else if (isSquare) { // Square Map with basic texture
			xPosition = -70;
			yPosition = 57;
		} else { // Round Map
			xPosition = -66;
			yPosition = 60;
		}
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.getMinecraft().fontRenderer.drawString("N", xPosition, yPosition, 0xffffff);
		GL11.glPopMatrix();

		dir += 90;

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.getMinecraft().fontRenderer.drawString("E", xPosition, yPosition, 0xffffff);
		GL11.glPopMatrix();

		dir += 90;

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.getMinecraft().fontRenderer.drawString("S", xPosition, yPosition, 0xffffff);
		GL11.glPopMatrix();

		dir += 90;

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.getMinecraft().fontRenderer.drawString("W", xPosition, yPosition, 0xffffff);
		GL11.glPopMatrix();
	}
}
