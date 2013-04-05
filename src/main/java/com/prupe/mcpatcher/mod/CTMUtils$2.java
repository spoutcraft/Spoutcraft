package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.CTMUtils$TileOverrideIterator;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class CTMUtils$2 extends CTMUtils$TileOverrideIterator {
	final IBlockAccess val$blockAccess;

	final int val$i;

	final int val$j;

	final int val$k;

	final int val$face;

	CTMUtils$2(Block var1, Icon var2, IBlockAccess var3, int var4, int var5, int var6, int var7) {
		super(var1, var2);
		this.val$blockAccess = var3;
		this.val$i = var4;
		this.val$j = var5;
		this.val$k = var6;
		this.val$face = var7;
	}

	Icon getTile(ITileOverride var1, Block var2, Icon var3) {
		return var1.getTile(this.val$blockAccess, var2, var3, this.val$i, this.val$j, this.val$k, this.val$face);
	}
}
