package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TileLoader;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$Top extends TileOverride {
	TileOverrideImpl$Top(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
	}

	String getMethod() {
		return "top";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
	}

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (var7 < 0) {
			var7 = 2;
		} else if (this.reorient(var7) <= 1) {
			return null;
		}

		int[][] var8 = NEIGHBOR_OFFSET[var7];
		return this.shouldConnect(var1, var2, var3, var4, var5, var6, var7, var8[this.rotateUV(6)]) ? this.icons[0] : null;
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
