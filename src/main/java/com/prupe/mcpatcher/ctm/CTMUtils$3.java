package com.prupe.mcpatcher.ctm;

import net.minecraft.src.Block;
import net.minecraft.src.Icon;

final class CTMUtils$3 extends CTMUtils$TileOverrideIterator {
	final int val$face;

	final int val$metadata;

	CTMUtils$3(Block x0, Icon x1, int var3, int var4) {
		super(x0, x1);
		this.val$face = var3;
		this.val$metadata = var4;
	}

	Icon getTile(ITileOverride override, Block block, Icon currentIcon) {
		return override.getTile(block, currentIcon, this.val$face, this.val$metadata);
	}

	@Override
	public ITileOverride next() {
		// TODO Auto-generated method stub
		return null;
	}
}
