package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.CTMUtils$TileOverrideIterator;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

final class CTMUtils$3 extends CTMUtils$TileOverrideIterator {
	final int val$face;

	final int val$metadata;

	CTMUtils$3(Block var1, Icon var2, int var3, int var4) {
		super(var1, var2);
		this.val$face = var3;
		this.val$metadata = var4;
	}

	Icon getTile(ITileOverride var1, Block var2, Icon var3) {
		return var1.getTile(var2, var3, this.val$face, this.val$metadata);
	}
}
