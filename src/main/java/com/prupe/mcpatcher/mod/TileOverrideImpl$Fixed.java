package com.prupe.mcpatcher.mod;

import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$Fixed extends TileOverride {
	TileOverrideImpl$Fixed(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
	}

	String getMethod() {
		return "fixed";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
	}

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		return this.icons[0];
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
