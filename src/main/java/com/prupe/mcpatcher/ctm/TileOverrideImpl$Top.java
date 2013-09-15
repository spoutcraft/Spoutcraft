package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.TileLoader;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

final class TileOverrideImpl$Top extends TileOverride {
	TileOverrideImpl$Top(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
	}

	String getMethod() {
		return "top";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (face < 0) {
			face = 2;
		} else if (this.reorient(face) <= 1) {
			return null;
		}

		int[][] offsets = NEIGHBOR_OFFSET[face];
		return this.shouldConnect(blockAccess, block, origIcon, i, j, k, face, offsets[this.rotateUV(6)]) ? this.icons[0] : null;
	}

	Icon getTileImpl(Block block, Icon origIcon, int face, int metadata) {
		return null;
	}
}
