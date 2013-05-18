package com.prupe.mcpatcher.mod;

import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$Horizontal extends TileOverride {
	private static final int[] neighborMap = new int[] {3, 2, 0, 1};

	TileOverrideImpl$Horizontal(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
	}

	String getMethod() {
		return "horizontal";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 4 ? null : "requires exactly 4 tiles";
	}

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (var7 < 0) {
			var7 = 2;
		} else if (this.reorient(var7) <= 1) {
			return null;
		}

		int[][] var8 = NEIGHBOR_OFFSET[var7];
		int var9 = 0;

		if (this.shouldConnect(var1, var2, var3, var4, var5, var6, var7, var8[this.rotateUV(0)])) {
			var9 |= 1;
		}

		if (this.shouldConnect(var1, var2, var3, var4, var5, var6, var7, var8[this.rotateUV(4)])) {
			var9 |= 2;
		}

		return this.icons[neighborMap[var9]];
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
