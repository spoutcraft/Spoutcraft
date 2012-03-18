package org.spoutcraft.client.gui.minimap;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.Material;
import net.minecraft.src.World;

/**
 * @author lahwran
 * 
 */
public class MapCalculator implements Runnable {

	/**
	 * Multiply two colors by each other. Treats 0xff as 1.0.
	 * 
	 * Yourself came up with the algorithm, I'm sure it makes sense to someone
	 * 
	 * @param x Color to multiply
	 * @param y Other color to multiply
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
		}
		catch (Exception e) {
			return false;
		}
	}

	private final int getBlockHeight(World world, int x, int z) {
		if (MinimapConfig.getInstance().isCavemap()) {
			Chunk chunk = world.getChunkFromBlockCoords(x, z);
			cmdist.setSeed((x & 0xffff) | ((z & 0xffff) << 16));
			float dist = distance((int)Minecraft.theMinecraft.thePlayer.posX, (int)Minecraft.theMinecraft.thePlayer.posZ, x, z);
			int y = (int) Minecraft.theMinecraft.thePlayer.posY;
			if (dist > 5)
				y -= (cmdist.nextInt((int) (dist)) - ((int) dist / 2));
			x &= 0xf;
			z &= 0xf;

			if (y < 0)
				y = 0;
			else if (y > 255)
				y = 255;

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
		li chunk = world.b(x, z);
		int y = (int)(game.h.aM); //starty;
		x &= 0xf;
		z &= 0xf;

		//while (y > 0)
		// {
			

			if (getBlockColor(id, meta).alpha == 0)
				return -1;//y--;
			else
				return y + 1; // what
		//}
		*/
		//return -1;
	}

	private int getBlockColor(World world, int x, int y, int z) {
		int color24 = 0;
			
		try {
			if (MinimapConfig.getInstance().isColor() && !MinimapConfig.getInstance().isCavemap()) {
				if (x == (int)map.getPlayerX() && z == (int)map.getPlayerZ())
					return 0xff0000;
				if ((world.getBlockMaterial(x, y + 1, z) == Material.ice) || (world.getBlockMaterial(x, y + 1, z) == Material.snow))
					color24 = 0xffffff;
				else {
					BlockColor col = BlockColor.getBlockColor(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
					color24 = col.color;
				}
			} else
				color24 = 0x808080;
		}
		catch (Exception e) {
			return 0xff0000;
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
		if (MinimapConfig.getInstance().isCavemap()) {
			if (MinimapConfig.getInstance().isLightmap()) {
				light *= 1.3f;
			} else {
				light *= 0.5;
				light += 64;
			}
		}

		if (light > 255)
			light = 255;
		else if (light < 0)
			light = 0;

		return light;
	}

	private void mapCalc() {
		if (Minecraft.theMinecraft.thePlayer == null || Minecraft.theMinecraft.theWorld == null)
			return;
		try {
			synchronized (map) {
				World data = Minecraft.theMinecraft.theWorld;
				map.zoom = MinimapConfig.getInstance().getZoom();
				map.update(Minecraft.theMinecraft.thePlayer.posX, Minecraft.theMinecraft.thePlayer.posZ);
				int startX = (int) (map.getPlayerX() - map.renderOff);
				int startZ = (int) (map.getPlayerZ() - map.renderOff);

				for (int worldX = startX; worldX < startX + map.renderSize; worldX++) {
					for (int worldZ = startZ; worldZ < startZ + map.renderSize; worldZ++) {
						if (Minecraft.theMinecraft.thePlayer == null)
							return;
						int worldY = getBlockHeight(data, worldX, worldZ);
						
						map.setColorPixel(worldX, worldZ, getBlockColor(data, worldX, worldY, worldZ));
						map.setHeightPixel(worldX, worldZ, getBlockHeightMap(data, worldX, worldY, worldZ));
						map.setLightPixel(worldX, worldZ, getBlockLight(data, worldX, worldY, worldZ));
					}
				}
			}
		} catch (Throwable whatever) {
			whatever.printStackTrace();
		}
	}

	/**
	 * Check if a render is necessary, and if so, do one.
	 */
	private void tryARender() {
		if (Minecraft.theMinecraft.thePlayer == null)
			return;
		try {
			if (MinimapConfig.getInstance().isEnabled() && map.isDirty(Minecraft.theMinecraft.thePlayer.posX, Minecraft.theMinecraft.thePlayer.posZ)) {
				mapCalc();
				map.timer = 1;
			}
		} catch (RuntimeException e) {
			throw e;
		} finally {
			map.timer++;
		}
	}

	/**
	 * the run() to implement runnable - the main function of the other thread.
	 * when threading is disabled, this simply idles and the actual work is
	 * done in onRenderTick().
	 */
	public void run() {
		while (true) {
			try {
				if (Minecraft.theMinecraft != null && Minecraft.theMinecraft.theWorld != null) {
					//can not multithread in SP
					if (!Minecraft.theMinecraft.theWorld.isRemote){
						Thread.sleep(1000);
					}
					else {
						tryARender();
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called each tick of the render.
	 */
	void onRenderTick() {
		if (zCalc == null || !zCalc.isAlive()) {
			zCalc = new Thread(this);
			zCalc.start();
		}
		if (Minecraft.theMinecraft != null && Minecraft.theMinecraft.theWorld != null) {
			if (!Minecraft.theMinecraft.theWorld.isRemote){
				tryARender();
			}
		}
	}

	/**
	 * Map calculation thread
	 */
	public Thread zCalc = new Thread(this);

	/**
	 * Random used to distort cave map
	 */
	public Random cmdist = new Random();

	private Map map;

	/**
	 * This constructor inits state, but does not start the thread.
	 * 
	 * @param minimap Minimap instance to initialize off
	 */
	public MapCalculator(ZanMinimap minimap) {
		map = minimap.map;
	}

	/**
	 * Start up the other thread. The thread may return early at this point,
	 * as there might not be a Minecraft instance available yet. if that occurs,
	 * the thread will be restarted by the keep-alive in onRenderTick().
	 */
	public void start() {
		zCalc.start();
	}
}
