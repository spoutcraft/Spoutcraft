package com.prupe.mcpatcher.ctm;

import java.util.Properties;

import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

final class TileOverrideImpl$HorizontalVertical extends TileOverrideImpl$Horizontal {
	private static final int[] neighborMap = new int[] {3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 4, 4, 5, 4, 4, 4, 4, 4, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 6, 3, 3, 3, 3, 3};

	TileOverrideImpl$HorizontalVertical(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
	}

	String getMethod() {
		return "horizontal+vertical";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 7 ? null : "requires exactly 7 tiles";
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		Icon icon = super.getTileImpl(blockAccess, block, origIcon, i, j, k, face);

		if (icon != this.icons[3]) {
			return icon;
		} else {
			int[][] offsets = NEIGHBOR_OFFSET[face];
			int neighborBits = 0;

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(1)])) {
				neighborBits |= 1;
			}

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(2)])) {
				neighborBits |= 2;
			}

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(3)])) {
				neighborBits |= 4;
			}

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(5)])) {
				neighborBits |= 8;
			}

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(6)])) {
				neighborBits |= 16;
			}

			if (this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(7)])) {
				neighborBits |= 32;
			}

			return this.icons[neighborMap[neighborBits]];
		}
	}
}
