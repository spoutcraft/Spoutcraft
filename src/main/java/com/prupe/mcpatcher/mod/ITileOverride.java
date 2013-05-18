package com.prupe.mcpatcher.mod;

import java.util.Map;
import java.util.Set;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Stitcher;
import net.minecraft.src.TextureMap;

interface ITileOverride {
	boolean isDisabled();

	int getTotalTextureSize();

	void registerIcons(TextureMap var1, Stitcher var2, Map var3);

	Set getMatchingBlocks();

	Set getMatchingTiles();

	int getRenderPass();

	Icon getTile(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7);

	Icon getTile(Block var1, Icon var2, int var3, int var4);
}