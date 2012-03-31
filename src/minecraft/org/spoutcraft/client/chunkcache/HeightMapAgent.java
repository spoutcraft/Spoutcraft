/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.chunkcache;

import org.spoutcraft.client.gui.minimap.MinimapUtils;

import net.minecraft.src.Chunk;

public class HeightMapAgent {
	public static void scanChunk(Chunk chunk) {
		try {
			HeightMap map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int h = getHighestBlock(chunk, x, z);
					if (h > -1) {
						int actualX = chunk.xPosition * 16 + x;
						int actualZ = chunk.zPosition * 16 + z;
						byte id = (byte) chunk.getBlockID(x, h, z);
						if (chunk.getBlockID(x, h + 1, z) == 78) {
							id = 78;
						}
						map.setHighestBlock(actualX, actualZ, (short) h, id);
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignore) { }
	}

	public static void save() {
		(new Thread(new HeightMapSave())).start();
	}

	public static short getHighestBlock(Chunk chunk, int x, int z) {
		boolean lastWater = false;
		for (short y = 255; y > 0; y--) {
			byte id = (byte) chunk.getBlockID(x, y, z);
			if (id != 0 && id != 8 && id != 9) {
				return (short) (lastWater ? y + 1 : y);
			} else if (id == 8 || id == 9) {
				lastWater = true;
			}
		}
		return 0;
	}
}

class HeightMapSave implements Runnable {
	HeightMap map;
	public HeightMapSave() {
		map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
	}
	
	public void run() {
		map.save();
	}
}
