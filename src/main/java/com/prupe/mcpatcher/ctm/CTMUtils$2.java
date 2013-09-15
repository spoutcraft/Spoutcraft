package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.ctm.CTMUtils$TileOverrideIterator;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class CTMUtils$2 extends CTMUtils$TileOverrideIterator {
	final IBlockAccess val$blockAccess;

	final int val$i;

	final int val$j;

	final int val$k;

	final int val$face;

	CTMUtils$2(Block x0, Icon x1, IBlockAccess var3, int var4, int var5, int var6, int var7) {
		super(x0, x1);
		this.val$blockAccess = var3;
		this.val$i = var4;
		this.val$j = var5;
		this.val$k = var6;
		this.val$face = var7;
	}

	Icon getTile(ITileOverride override, Block block, Icon currentIcon) {
		return override.getTile(this.val$blockAccess, block, currentIcon, this.val$i, this.val$j, this.val$k, this.val$face);
	}

	@Override
	public ITileOverride next() {
		// TODO Auto-generated method stub
		return null;
	}
}
