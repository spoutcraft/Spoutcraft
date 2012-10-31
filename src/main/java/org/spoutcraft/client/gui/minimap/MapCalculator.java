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

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Chunk;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Material;
import net.minecraft.src.World;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;

public class MapCalculator implements Runnable {
	/**
	 * Multiply two colors by each other. Treats 0xff as 1.0.
	 * 
	 * Yourself came up with the algorithm, I'm sure it makes sense to someone
	 * 
	 * @param x
	 *    Color to multiply
	 * @param y
	 *    Other color to multiply
	 * @return multiplied color
	 */
	public int colorMult(int x, int y) {
		int res = 0;
		for (int octet = 0; octet < 3; ++octet) {
			res |= (((x & 0xff) * (y & 0xff)) / 0xff) << (octet << 3);
			x >>= 8;
			y >>= 8;
		}
		return res;
	}

	private final float distance(int x1, int y1, int x2, int y2) {
		return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	private final boolean blockIsSolid(Chunk chunk, int x, int y, int z) {
		if (y > 255)
			return false;
		if (y < 0)
			return true;
		try {
			int id = chunk.getBlockID(x, y, z);
			int meta = chunk.getBlockMetadata(x, y, z);
			return BlockColor.getBlockColor(id, meta).alpha > 0;
		} catch (Exception e) {
			return false;
		}
	}

	private final int getBlockHeight(World world, int x, int z) {
		if (MinimapConfig.getInstance().isCavemap()) {
			Chunk chunk = world.getChunkFromBlockCoords(x, z);
			cmdist.setSeed((x & 0xffff) | ((z & 0xffff) << 16));
			float dist = distance((int) Minecraft.theMinecraft.thePlayer.posX, (int) Minecraft.theMinecraft.thePlayer.posZ, x, z);
			int y = (int) Minecraft.theMinecraft.thePlayer.posY;
			if (dist > 5)
				y -= (cmdist.nextInt((int) (dist)) - ((int) dist / 2));
			x &= 0xf;
			z &= 0xf;

			if (y < 0) {
				y = 0;
			} else if (y > 255) {
				y = 255;
			}

			if (blockIsSolid(chunk, x, y, z)) {
				int itery = y;
				while (true) {
					itery++;
					if (itery > y + 10)
						return y + 10;
					if (!blockIsSolid(chunk, x, itery, z)) {
						return itery - 1;
					}
				}
			}
			while (y > -1) {
				y--;
				if (blockIsSolid(chunk, x, y, z)) {
					return y;
				}
			}
			return -1;
		}

		return world.getHeightValue(x, z) - 1;

		// return world.b(x, z).b(x & 0xf, z & 0xf);
		/*
		 * li chunk = world.b(x, z); int y = (int)(game.h.aM); //starty; x &=
		 * 0xf; z &= 0xf;
		 * 
		 * //while (y > 0) // {
		 * 
		 * 
		 * if (getBlockColor(id, meta).alpha == 0) return -1;//y--; else return
		 * y + 1; // what //}
		 */
		// return -1;
	}

	private int getBlockColor(World world, int x, int y, int z) {
		int color24 = 0;

		try {
			if (MinimapConfig.getInstance().isColor() && !MinimapConfig.getInstance().isCavemap()) {
				if (x == (int) map.getPlayerX() && z == (int) map.getPlayerZ())
					return 0xff0000;
				if ((world.getBlockMaterial(x, y + 1, z) == Material.ice) || (world.getBlockMaterial(x, y + 1, z) == Material.snow)) {
					color24 = 0xffffff;
				} else {
					BlockColor col = BlockColor.getBlockColor(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
					color24 = col.color;
				}
			}
		} catch (Exception e) {
			return 0;
		}

		return color24;
	}

	private int getBlockHeightMap(World world, int x, int y, int z) {
		int height = y - 128;
		double sc = Math.log10(Math.abs(height) / 8.0D + 1.0D) / 1.3D;
		int result = 0x80;

		if (height >= 0) {
			result = (int) (sc * (0xff - result)) + result;
		} else {
			height = Math.abs(height);
			result = result - (int) (sc * result);
		}
		return result;
	}

	private int getBlockLight(World world, int x, int y, int z) {
		int light = world.getBlockLightValue_do(x, y + 1, z, false) * 17;
		int min = 32;
		if (light < min) {
			light = min;
		}
		if (MinimapConfig.getInstance().isLightmap()) {
			light *= 1.3f;
		} else if (MinimapConfig.getInstance().isCavemap()) {
			light *= 0.5;
			light += 64;
		}

		if (light > 255) {
			light = 255;
		} else if (light < 0) {
			light = 0;
		}

		return light;
	}

	private void mapCalc() {
		if (Minecraft.theMinecraft.thePlayer == null || Minecraft.theMinecraft.theWorld == null)
			return;
		try {
			final boolean square = MinimapConfig.getInstance().isSquare();
			final World data = Minecraft.theMinecraft.theWorld;
			int renderSize;
			int startX;
			int startZ;
			synchronized (map) {
				if (map.zoom != MinimapConfig.getInstance().getZoom()) {
					map.zoom = MinimapConfig.getInstance().getZoom();
					switch (map.zoom) {
						case 0:
							map.renderSize = Map.ZOOM_0;
							break;
						case 1:
							map.renderSize = Map.ZOOM_1;
							break;
						case 2:
							map.renderSize = Map.ZOOM_2;
							break;
						case 3:
							map.renderSize = Map.ZOOM_3;
							break;
						default:
							map.renderSize = Map.ZOOM_2;
							break;
					}
					map.renderOff = map.renderSize / 2;
					map.clear();
				}
				map.square = square;

				map.update(Minecraft.theMinecraft.thePlayer.posX, Minecraft.theMinecraft.thePlayer.posZ);
				renderSize = map.renderSize;
				startX = (int) (map.getPlayerX() - map.renderOff);
				startZ = (int) (map.getPlayerZ() - map.renderOff);
			}

			for (int worldX = startX; worldX < startX + renderSize; worldX++) {
				for (int worldZ = startZ; worldZ < startZ + renderSize; worldZ++) {
					int worldY = getBlockHeight(data, worldX, worldZ);

					int pixelX = worldX - startX;
					if (pixelX >= renderSize) {
						pixelX -= renderSize;
					}

					int pixelZ = worldZ - startZ;
					pixelZ = renderSize - pixelZ;
					if (pixelZ >= renderSize) {
						pixelZ -= renderSize;
					}

					if (square || MinimapUtils.insideCircle(startX + renderSize / 2, startZ + renderSize / 2, renderSize / 2, worldX, worldZ)) {
						int color = getBlockColor(data, worldX, worldY, worldZ);
						if (color == 0) {
							map.clearColorPixel(pixelX, pixelZ);
						} else {
							map.setColorPixel(pixelX, pixelZ, color);
						}

						short height = (short) data.getHeightValue(worldX, worldZ);
						short reference = (short) data.getHeightValue(worldX + 1, worldZ + 1);
						map.heightimg.setARGB(pixelZ, pixelX, getHeightColor(height, reference));
						map.setLightPixel(pixelX, pixelZ, getBlockLight(data, worldX, worldY, worldZ));

					} else {
						map.clearColorPixel(pixelX, pixelZ);
						map.heightimg.setARGB(pixelX, pixelZ, 0);
						map.setLightPixel(pixelX, pixelZ, 255);
					}
				}
			}

			synchronized (map) {
				for (Waypoint pt : MinimapConfig.getInstance().getWaypoints(MinimapUtils.getWorldName())) {
					if (pt.enabled) {
						boolean render = false;
						if (square) {
							render = Math.abs(map.getPlayerX() - pt.x) < map.renderSize && Math.abs(map.getPlayerZ() - pt.z) < map.renderSize;
						} else {
							render = MinimapUtils.insideCircle(startX + map.renderSize / 2, startZ + map.renderSize / 2, map.renderSize / 2, pt.x, pt.z);
						}
						if (pt.deathpoint && !MinimapConfig.getInstance().isDeathpoints()) {
							render = false;
						}
						if (render) {
							int pixelX = pt.x - startX;

							int pixelZ = pt.z - startZ;
							pixelZ = map.renderSize - pixelZ;

							int scale = map.zoom + 2;
							if (map.zoom > 2) {
								scale += 2;
							}
							if (map.zoom > 1) {
								scale += 1;
							}

							int color = 0xEE2C2C;
							if (pt == MinimapConfig.getInstance().getFocussedWaypoint()) {
								color = 0xff00ffff;
							}
							drawCircle(pixelX, pixelZ, scale + map.zoom + 1, pt.deathpoint ? color : 0);
							drawCircle(pixelX, pixelZ, scale, pt.deathpoint ? 0 : color);
						}
					}
				}
				for (Waypoint pt : MinimapConfig.getInstance().getServerWaypoints()) {
					boolean render = false;
					if (square) {
						render = Math.abs(map.getPlayerX() - pt.x) < map.renderSize && Math.abs(map.getPlayerZ() - pt.z) < map.renderSize;
					} else {
						render = MinimapUtils.insideCircle(startX + map.renderSize / 2, startZ + map.renderSize / 2, map.renderSize / 2, pt.x, pt.z);
					}
					if (render) {
						int pixelX = pt.x - startX;

						int pixelZ = pt.z - startZ;
						pixelZ = map.renderSize - pixelZ;

						int scale = map.zoom + 2;
						if (map.zoom > 2) {
							scale += 2;
						}
						if (map.zoom > 1) {
							scale += 1;
						}

						int color = 0x3366CC;
						if (pt == MinimapConfig.getInstance().getFocussedWaypoint()) {
							color = 0xff00ffff;
						}
						drawCircle(pixelX, pixelZ, scale + map.zoom + 1, 0);
						drawCircle(pixelX, pixelZ, scale, color);
					}
				}
			}
		} catch (Throwable whatever) {	}
	}

	private void drawCircle(int x, int y, int radius, int color) {
		try {
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dy = -radius; dy <= radius; dy++) {
					if (x + dx < map.renderSize && x + dx > 0 && y + dy < map.renderSize && y + dy > 0) {
						if (MinimapUtils.insideCircle(x, y, radius, x + dx, y + dy)) {
							map.setColorPixel(x + dx, y + dy, color);
						}
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
		} // happens with fast movement
	}

	/**
	 * Check if a render is necessary, and if so, do one.
	 */
	private void tryARender() {
		if (Minecraft.theMinecraft.thePlayer == null || Minecraft.theMinecraft.theWorld == null)
			return;
		try {
			double x = Minecraft.theMinecraft.thePlayer.posX;
			double z = Minecraft.theMinecraft.thePlayer.posZ;
			if (MinimapConfig.getInstance().isEnabled() && map.isDirty(x, z)) {
//				long start = System.currentTimeMillis();
				if (MinimapConfig.getInstance().getScanRadius() > 0) {
					int radius = MinimapConfig.getInstance().getScanRadius() << 4;
					for (int cx = (int) (x - radius); cx <= (int) (x + radius); cx += 16) {
						for (int cz = (int) (z - radius); cz <= (int) (z + radius); cz += 16) {
							Chunk chunk = Minecraft.theMinecraft.theWorld.getChunkFromBlockCoords((int) cx, (int) cz);
							org.spoutcraft.client.chunkcache.HeightMapAgent.scanChunk(chunk);
						}
					}
				}
				//long dur = System.currentTimeMillis() - start;
				//int chunks = (int) Math.pow(MinimapConfig.getInstance().getScanRadius() * 2, 2);
				//System.out.println("Took " + dur + "ms to scan "+chunks+" chunks.\nThat is " + (float) (dur/(float)chunks) + " per chunk!");
				mapCalc();

				entityCalc();
				map.timer = 1;
			}
		} catch (RuntimeException e) {
			throw e;
		} finally {
			map.timer++;
		}
	}

	private void entityCalc() {
		synchronized(map.watchedEntities) {
			map.watchedEntities.clear();
			if (!Spoutcraft.hasPermission("spout.client.minimap.showentities")) {
				return;
			}
			if (!MinimapConfig.getInstance().isShowingEntities()) {
				return;
			}
			int radius = map.renderSize / 2;
			double playerX = map.getPlayerX();
			double playerZ = map.getPlayerZ();
			for (Object ob:SpoutClient.getHandle().theWorld.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(playerX - radius, 0, playerZ - radius, playerX + radius, 256, playerZ + radius))) {
				net.minecraft.src.Entity e = (net.minecraft.src.Entity) ob;
				if (!MinimapConfig.getInstance().isEntityVisible(e.getClass())) {
					continue;
				}
				WatchedEntity w = new WatchedEntity(e);
				if (w.getTexture() != null) {
					map.watchedEntities.add(w);
				}
			}
		}
	}

	/**
	 * the run() to implement runnable - the main function of the other thread.
	 * this simply idles and the actual work is done in onRenderTick().
	 */
	public void run() {
	}

	/**
	 * Called each tick of the render.
	 */
	void onRenderTick() {
		if (Minecraft.theMinecraft != null && Minecraft.theMinecraft.theWorld != null) {
			tryARender();
		}
	}

	/**
	 * Random used to distort cave map
	 */
	public Random cmdist = new Random();

	private Map map;

	/**
	 * This constructor inits state, but does not start the thread.
	 * 
	 * @param minimap
	 *			Minimap instance to initialize off
	 */
	public MapCalculator(ZanMinimap minimap) {
		map = minimap.map;
	}

	/**
	 * Start up the other thread. The thread may return early at this point, as
	 * there might not be a Minecraft instance available yet. if that occurs,
	 * the thread will be restarted by the keep-alive in onRenderTick().
	 */
	public void start() {
	}

	public static int getHeightColor(short height, short reference) {
		int hdiff = height - reference;
		int color = 0x00000000;
		if (hdiff != 0 && reference != -1) {
			boolean ascending = hdiff < 0;
			hdiff *= 2;
			hdiff += 256;
			if (hdiff > 255) {
				hdiff = 255;
			}
			if (hdiff < 0) {
				hdiff = 0;
			}
			if (!ascending) {
				hdiff = 255 - hdiff;
			}
			color = 0x55000000 | ((hdiff << 16) + (hdiff << 8) + hdiff);
		}
		return color;
	}

	public static int getHeightColor(int x, int z, HeightMap heightMap) {
		short height = heightMap.getHeight(x, z);
		short reference = heightMap.getHeight(x + 1, z + 1);

		return getHeightColor(height, reference);
	}
}
