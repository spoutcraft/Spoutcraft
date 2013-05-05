package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TileLoader;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$CTM extends TileOverride {
	private static final int[] neighborMap = new int[] {0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15, 1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14, 0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15, 1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14, 36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43, 16, 18, 16, 18, 6, 46, 6, 21, 16, 18, 16, 18, 28, 9, 28, 22, 36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43, 37, 40, 37, 40, 30, 8, 30, 34, 37, 40, 37, 40, 25, 23, 25, 45, 0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15, 1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14, 0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15, 1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14, 36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27, 16, 42, 16, 42, 6, 20, 6, 10, 16, 42, 16, 42, 28, 35, 28, 44, 36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27, 37, 38, 37, 38, 30, 11, 30, 32, 37, 38, 37, 38, 25, 33, 25, 26};

	TileOverrideImpl$CTM(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
	}

	String getMethod() {
		return "ctm";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() >= 47 ? null : "requires at least 47 tiles";
	}

	boolean requiresFace() {
		return true;
	}

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		int[][] var8 = NEIGHBOR_OFFSET[var7];
		int var9 = 0;

		for (int var10 = 0; var10 < 8; ++var10) {
			if (this.shouldConnect(var1, var2, var3, var4, var5, var6, var7, var8[var10])) {
				var9 |= 1 << var10;
			}
		}

		return this.icons[neighborMap[var9]];
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
