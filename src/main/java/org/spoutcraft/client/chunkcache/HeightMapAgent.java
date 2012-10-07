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
package org.spoutcraft.client.chunkcache;

import net.minecraft.src.Chunk;

import org.spoutcraft.client.chunkcache.HeightMap.HeightChunk;
import org.spoutcraft.client.gui.minimap.MinimapUtils;

public class HeightMapAgent {
	public static void scanChunk(Chunk chunk) {
		try {
			HeightMap map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
			synchronized (map) {
				HeightChunk hchunk = map.getChunk(chunk.xPosition, chunk.zPosition, true);
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						int h = getHighestBlock(chunk, x, z);
						if (h > -1) {
							byte id = (byte) chunk.getBlockID(x, h, z);
							byte data = (byte) chunk.getBlockMetadata(x, h, z);

							// Check if block above is snow
							if (chunk.getBlockID(x, h + 1, z) == 78) {
								id = 78;
							}
							hchunk.setHeight(x, z, (short) h);
							hchunk.setBlockId(x, z, id);
							hchunk.setData(x, z, data);
						}
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignore) {
		}
	}

	public static void save() {
		HeightMap.getHeightMap(MinimapUtils.getWorldName()).saveThreaded();
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
		return -1;
	}
}
