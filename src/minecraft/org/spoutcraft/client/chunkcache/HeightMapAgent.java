package org.spoutcraft.client.chunkcache;

import org.spoutcraft.client.gui.minimap.MinimapUtils;

import net.minecraft.src.Chunk;

public class HeightMapAgent {
	public static void scanChunk(Chunk chunk) {
		HeightMap map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				int h = chunk.getHeightValue(x, z) - 1;
				if (h > -1) {
					int actualX = chunk.xPosition * 16 + x;
					int actualZ = chunk.zPosition * 16 + z;
					byte id = (byte) chunk.getBlockID(x, h, z);
					if(chunk.getBlockID(x, h + 1, z) == 78) {
						id = 78;
					}
					map.setHighestBlock(actualX, actualZ, (short) h, id);
				}
			}
		}
	}

	public static void save() {
		HeightMap map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
		map.save();
	}
}
