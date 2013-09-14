package com.prupe.mcpatcher.ctm;

import java.util.Set;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

interface ITileOverride extends Comparable<ITileOverride> {
	boolean isDisabled();

	void registerIcons();

	Set<Integer> getMatchingBlocks();

	Set<String> getMatchingTiles();

	int getRenderPass();

	int getWeight();

	Icon getTile(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7);

	Icon getTile(Block var1, Icon var2, int var3, int var4);
}
