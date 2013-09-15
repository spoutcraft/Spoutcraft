package com.prupe.mcpatcher.ctm;

import java.util.Properties;

import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

class TileOverrideImpl$Vertical extends TileOverride {
	private static final int[] neighborMap = new int[] {3, 2, 0, 1};

	TileOverrideImpl$Vertical(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
	}

	String getMethod() {
		return "vertical";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 4 ? null : "requires exactly 4 tiles";
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (face < 0) {
			face = 2;
		} else if (this.reorient(face) <= 1) {
			return null;
		}

		int[][] offsets = NEIGHBOR_OFFSET[face];
		int neighborBits = 0;

		if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(2)])) {
			neighborBits |= 1;
		}

		if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(6)])) {
			neighborBits |= 2;
		}

		return this.icons[neighborMap[neighborBits]];
	}

	Icon getTileImpl(Block block, Icon origIcon, int face, int metadata) {
		return this.icons[3];
	}
}
