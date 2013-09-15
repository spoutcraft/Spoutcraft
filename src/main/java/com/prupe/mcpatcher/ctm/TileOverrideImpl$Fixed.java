package com.prupe.mcpatcher.ctm;

import java.util.Properties;

import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

final class TileOverrideImpl$Fixed extends TileOverride {
	TileOverrideImpl$Fixed(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
	}

	String getMethod() {
		return "fixed";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		return this.icons[0];
	}

	Icon getTileImpl(Block block, Icon origIcon, int face, int metadata) {
		return this.icons[0];
	}
}
