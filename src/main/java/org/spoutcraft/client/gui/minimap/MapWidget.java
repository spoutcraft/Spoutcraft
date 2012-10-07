/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
import java.awt.image.Raster;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.animation.PropertyAnimation;
import org.spoutcraft.api.gui.GenericScrollable;
import org.spoutcraft.api.gui.MinecraftTessellator;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.Point;
import org.spoutcraft.api.gui.RenderUtil;
import org.spoutcraft.api.gui.ScrollBarPolicy;
import org.spoutcraft.api.gui.WidgetType;
import org.spoutcraft.api.property.Property;
import org.spoutcraft.api.util.map.TIntPairObjectHashMap;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;

public class MapWidget extends GenericScrollable {
	static TIntObjectHashMap<TIntPairObjectHashMap<Map>> chunks = new TIntObjectHashMap<TIntPairObjectHashMap<Map>>(250);
	static int levelOfDetail = 1;
	static final int MIN_LOD = 1;
	static HeightMap heightMap;
	static Map blankMap = new Map(1); // Singleton instance used to indicate no pixels to draw in a chunk
	double scale = 1f;
	boolean dirty = true;
	GuiScreen parent = null;
	BufferedImage imageBuffer = null;

	private static MapWidgetRenderer renderer = null;

	private Point lastPlayerPos = new Point((int) Minecraft.theMinecraft.thePlayer.posX, (int) Minecraft.theMinecraft.thePlayer.posZ);
	private static Random random = new Random();

	public MapWidget(GuiScreen parent) {
		if (renderer == null) {
			renderer = new MapWidgetRenderer();
			renderer.start();
		}
		levelOfDetail = 1;
		this.parent = parent;
		HeightMap newheightMap = HeightMap.getHeightMap(MinimapUtils.getWorldName());
		if (newheightMap != heightMap) {
			chunks.clear();
			heightMap = newheightMap;
		}

		addProperty("scale", new Property() {
			@Override
			public void set(Object value) {
				setScale((double)(Double) value);
			}

			@Override
			public Object get() {
				return getScale();
			}
		});

		addProperty("scrollpos", new Property() {
			@Override
			public void set(Object value) {
				Point p = (Point) value;
				scrollTo(p, false, 0);
			}

			@Override
			public Object get() {
				return mapOutsideToCoords(new Point(0,0));
			}
		});

		setScrollBarPolicy(Orientation.HORIZONTAL, ScrollBarPolicy.SHOW_NEVER);
		setScrollBarPolicy(Orientation.VERTICAL, ScrollBarPolicy.SHOW_NEVER);
	}

	private void updateLOD() {
		int newlod = levelOfDetail;
		newlod = (int) (1 / scale);
		if (newlod < MIN_LOD) {
			newlod = MIN_LOD;
		}
		if (newlod != levelOfDetail) {
			renderer.renderQueue.clear();
		}
		levelOfDetail = newlod;
	}

	@Override
	public int getInnerSize(Orientation axis) {
		if (axis == Orientation.HORIZONTAL) {
			return (int) ((double) (heightMap.getMaxX() - heightMap.getMinX() + 1) * scale * 16d);
		} else {
			return (int) ((double) (heightMap.getMaxZ() - heightMap.getMinZ() + 1) * scale * 16d) + 30;
		}
	}

	public static Map drawChunk(int x, int z, boolean force) {
		synchronized(chunks) {
			Map map = chunks.get(levelOfDetail).get(x, z);
			if (map == null || (force && map == blankMap)) {
				map = new Map(16);
				map.originOffsetX = 0;
				map.originOffsetY = 0;
				map.renderSize = 16;
			} else if (!force) {
				return map;
			}
			boolean pixelSet = false;
			try {
				for (int cx = 0; cx < 16; cx++) {
					for (int cz = 0; cz < 16; cz++) {
						int aX = x * 16 + cx * levelOfDetail;
						int aZ = z * 16 + cz * levelOfDetail;

						short height = heightMap.getHeight(aX, aZ);
						int id = heightMap.getBlockId(aX, aZ);
						byte data = heightMap.getData(aX, aZ);
						if (id < 0) {
							id = 256 + id;
						}
						if (id == -1 || height == -1) {
							continue;
						} else {
							pixelSet = true;
						}

						if (levelOfDetail <= 2) {
							short reference = heightMap.getHeight(aX + levelOfDetail, aZ + levelOfDetail);
							int color = MapCalculator.getHeightColor(height, reference);
							map.heightimg.setARGB(cx, cz, color);
						}

						map.setColorPixel(cz, cx, BlockColor.getBlockColor(id, data).color | 0xff000000);
					}
				}
			}
			catch (Exception e) {
				pixelSet = false;
			}
			if (pixelSet) {
				getChunkMap(levelOfDetail).put(x, z, map);
			} else {
				getChunkMap(levelOfDetail).put(x, z, blankMap);
			}
			return map;
		}
	}

	public static TIntPairObjectHashMap<Map> getChunkMap(int levelOfDetail) {
		TIntPairObjectHashMap<Map> chunkmap = chunks.get(levelOfDetail);
		if (chunkmap == null) {
			chunkmap = new TIntPairObjectHashMap<Map>(500);
			chunks.put(levelOfDetail, chunkmap);
		}
		return chunkmap;
	}

	public Point mapOutsideToCoords(Point outside) {
		int x = outside.getX() + scrollX;
		int y = outside.getY() + scrollY;
		x /= scale;
		y /= scale;
		x += heightMap.getMinX() * 16;
		y += heightMap.getMinZ() * 16;
		return new Point(x,y);
	}

	public Point mapCoordsToOutside(Point coords) {
		int x = coords.getX();
		int y = coords.getY();
		x -= heightMap.getMinX() * 16;
		y -= heightMap.getMinZ() * 16;
		x *= scale;
		y *= scale;
		return new Point(x, y);
	}

	public void reset() {
		setScale(1f, true, 500);
		showPlayer(500);
	}

	public void showPlayer(int duration) {
		scrollTo(getPlayerPosition(), duration != 0, duration);
	}

	public void scrollTo(Point p, boolean animated, int duration) {
		scrollTo(p.getX(), p.getY(), animated, duration);
	}

	public void scrollTo(Point p) {
		scrollTo(p, false, 0);
	}

	public void scrollTo(int x, int z) {
		scrollTo(x,z, false, 0);
	}

	public void scrollTo(int x, int z, boolean animated, int duration) {
		if (!animated) {
			Point p = mapCoordsToOutside(new Point(x,z));
			int scrollX = p.getX(), scrollZ = p.getY();
			setScrollPosition(Orientation.HORIZONTAL, scrollX - (int) (getWidth() / 2));
			setScrollPosition(Orientation.VERTICAL, scrollZ - (int) (getHeight() / 2));
		} else {
			Point start = getCenterCoord();
			Point end = new Point(x, z);
			PropertyAnimation ani = new PropertyAnimation(this, "scrollpos");
			ani.setStartValue(start);
			ani.setEndValue(end);
			ani.setDuration(duration);
			ani.start();
		}
	}

	public Point getCenterCoord() {
		return mapOutsideToCoords(new Point((int) (getWidth() / 2), (int) (getHeight() / 2)));
	}

	public BufferedImage renderFullImage() {
		int scrollX = (int) (getScrollPosition(Orientation.HORIZONTAL) / scale);
		int scrollY = (int) (getScrollPosition(Orientation.VERTICAL) / scale);

		int 	minChunkX = heightMap.getMinX() + scrollX / 16,
				minChunkZ = heightMap.getMinZ() + scrollY / 16,
				maxChunkX = 0,
				maxChunkZ = 0;
		int horiz = (int) (getWidth() / 16 / scale) + 1;
		int vert = (int) (getHeight() / 16 / scale) + 1;
		maxChunkX = minChunkX + horiz;
		maxChunkZ = minChunkZ + vert;

		minChunkX++;
		minChunkZ++;
		BufferedImage fullImage = new BufferedImage((maxChunkX - minChunkX) * 16 + 32, (maxChunkZ - minChunkZ) * 16 + 32, BufferedImage.TYPE_INT_ARGB);
		for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
				Map map = drawChunk(chunkX, chunkZ, dirty);
				if (map != null && map != blankMap) {
					Raster raster = map.getColorRaster();
					int startX = (chunkX - minChunkX) * 16;
					int startZ = (chunkZ - minChunkZ) * 16;
					java.awt.image.DataBufferInt buf = (java.awt.image.DataBufferInt)raster.getDataBuffer();
					int[] srcbuf = buf.getData();
					fullImage.setRGB(startX, startZ, 16, 16, srcbuf, 0, 16);
				}
			}
		}
		return fullImage;
	}

	public boolean saveToDesktop() {
		try {
			BufferedImage fullImage = renderFullImage();

			// Creates a file named 'minimap 3-29-2012.png' in the desktop, if possible
			// Otherwise saves to screenshots. Appends "(1)", etc as needed to avoid overwriting existing files
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String fileName = "minimap " + df.format(new Date());
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			if (!desktop.exists()) {
				desktop = new File(Minecraft.getMinecraftDir(), "screenshots");
			}
			String fullFileName = fileName;
			int duplicate = 0;
			while (true) {
				if (!fileExists(desktop, fullFileName, ".png")) {
					break;
				}
				duplicate++;
				fullFileName = fileName + " (" + duplicate + ")";
			}
			ImageIO.write(fullImage, "png", new File(desktop, fullFileName + ".png"));
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean fileExists(File dir, String name, String ext) {
		name += ext;
		for (File f : dir.listFiles()) {
			if (f.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void renderContents() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int scrollX = (int) (getScrollPosition(Orientation.HORIZONTAL) / scale);
		int scrollY = (int) (getScrollPosition(Orientation.VERTICAL) / scale);

		GL11.glScaled(scale, scale, scale);
		GL11.glTranslatef(-heightMap.getMinX() * 16, -heightMap.getMinZ() * 16, 0);

		int 	minChunkX = heightMap.getMinX() + scrollX / 16,
				minChunkZ = heightMap.getMinZ() + scrollY / 16,
				maxChunkX = 0,
				maxChunkZ = 0;
		int horiz = (int) (getWidth() / 16 / scale) + 1;
		int vert = (int) (getHeight() / 16 / scale) + 1;
		maxChunkX = minChunkX + horiz;
		maxChunkZ = minChunkZ + vert;

		minChunkX = Math.max(minChunkX, heightMap.getMinX());
		minChunkZ = Math.max(minChunkZ, heightMap.getMinZ());
		maxChunkX = Math.min(maxChunkX, heightMap.getMaxX());
		maxChunkZ = Math.min(maxChunkZ, heightMap.getMaxZ());

		GL11.glPushMatrix();
		synchronized(chunks) {
			for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX+=levelOfDetail) {
				for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ+=levelOfDetail) {
					Map map = getChunkMap(levelOfDetail).get(chunkX, chunkZ);
					if (dirty || map == null || random .nextInt(10000) == 0) {
						renderer.renderQueue.add(new Point(chunkX, chunkZ));
					}
					if (map != null && map != blankMap) {
						GL11.glPushMatrix();
						int x = chunkX * 16;
						int y = chunkZ * 16;
						int width = x + 16 * levelOfDetail;
						int height = y + 16 * levelOfDetail;
						map.loadColorImage();
						MinecraftTessellator tessellator = Spoutcraft.getTessellator();
						tessellator.startDrawingQuads();
						tessellator.addVertexWithUV((double) width, (double) height, -90, 1, 1);
						tessellator.addVertexWithUV((double) width, (double) y, -90, 1, 0);
						tessellator.addVertexWithUV((double) x, (double) y, -90, 0, 0);
						tessellator.addVertexWithUV((double) x, (double) height, -90, 0, 1);
						tessellator.draw();
	//					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	//					RenderUtil.drawRectangle(x, y, width, height, 0x88ffffff);
						if (MinimapConfig.getInstance().isHeightmap()) {
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_COLOR);
							map.loadHeightImage();
							tessellator.startDrawingQuads();
							tessellator.addVertexWithUV((double) width, (double) height, -90, 1, 1);
							tessellator.addVertexWithUV((double) width, (double) y, -90, 1, 0);
							tessellator.addVertexWithUV((double) x, (double) y, -90, 0, 0);
							tessellator.addVertexWithUV((double) x, (double) height, -90, 0, 1);
							tessellator.draw();
						}
						GL11.glPopMatrix();
					}
				}
			}
		}
		int x = (int) SpoutClient.getHandle().thePlayer.posX;
		int z = (int) SpoutClient.getHandle().thePlayer.posZ;

		drawPOI("You", x, z, 0xffff0000);

		for (Waypoint waypoint : MinimapConfig.getInstance().getAllWaypoints(MinimapUtils.getWorldName())) {
			if (!waypoint.deathpoint || MinimapConfig.getInstance().isDeathpoints()) {
				drawPOI(waypoint.name, waypoint.x, waypoint.z, 0xff00ff00);
			}
		}

		if (MinimapConfig.getInstance().getFocussedWaypoint() != null) {
			Waypoint pos = MinimapConfig.getInstance().getFocussedWaypoint();
			drawPOI("Marker", pos.x, pos.z, 0xff00ffff);
		}

		GL11.glPopMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(2929);
		GL11.glDisable(3042);
		dirty = false;

		Point newpos = getPlayerPosition();
		if (lastPlayerPos.getX() != newpos.getX() || lastPlayerPos.getY() != newpos.getY()) {
			showPlayer(0);
			lastPlayerPos = newpos;
		}
	}

	private void drawPOI(String name, int x, int z, int color) {
		int mouseX = (int) ((getScreen().getMouseX() - getX() + scrollX) / scale + heightMap.getMinX() * 16);
		int mouseY = (int) ((getScreen().getMouseY() - getY() + scrollY) / scale + heightMap.getMinZ() * 16);
		int radius = (int) (2f / scale);
		if (radius <= 0) {
			radius = 2;
		}
		int mouseRadius = radius * 2;
		if (parent.isInBoundingRect(x - mouseRadius, z - mouseRadius, mouseRadius * 2, mouseRadius * 2, mouseX, mouseY)) {
			color = 0xff0000ff;
			parent.drawTooltip(name, x, z);
		}
		RenderUtil.drawRectangle(x - radius, z - radius, x + radius, z + radius, color);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.ScrollArea;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double value) {
		setScale(value, false, 0);
	}

	public void setScale(double newscale, boolean animated, int duration) {
		if (!animated) {
			Point center = getCenterCoord();
			this.scale = newscale;
			scrollTo(center);
			updateLOD();
		} else {
			PropertyAnimation ani = new PropertyAnimation(this, "scale");
			ani.setStartNumber(this.scale);
			ani.setEndNumber(newscale);
			ani.setDuration(duration);
			ani.start();
		}
	}

	public void zoomBy(double d) {
		double newscale = scale * d;
		if (newscale <= 0) {
			newscale = 0.0000001;
		}
		setScale(newscale, true, 100);
	}

	public Point getPlayerPosition() {
		int x = (int) SpoutClient.getHandle().thePlayer.posX;
		int z = (int) SpoutClient.getHandle().thePlayer.posZ;
		return new Point(x, z);
	}
}
