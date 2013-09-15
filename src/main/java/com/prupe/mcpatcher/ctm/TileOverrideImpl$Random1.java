package com.prupe.mcpatcher.ctm;

import java.util.Properties;

import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.WeightedIndex;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

final class TileOverrideImpl$Random1 extends TileOverride {
	private final int symmetry;
	private final WeightedIndex chooser;

	TileOverrideImpl$Random1(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
		String sym = properties.getProperty("symmetry", "none");

		if (sym.equals("all")) {
			this.symmetry = 6;
		} else if (sym.equals("opposite")) {
			this.symmetry = 2;
		} else {
			this.symmetry = 1;
		}

		this.chooser = WeightedIndex.create(this.getNumberOfTiles(), properties.getProperty("weights", ""));

		if (this.chooser == null) {
			this.error("invalid weights", new Object[0]);
		}
	}

	String getMethod() {
		return "random";
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (face < 0) {
			face = 0;
		}

		long hash = WeightedIndex.hash128To64(i, j, k, this.reorient(face) / this.symmetry);
		int index = this.chooser.choose(hash);
		return this.icons[index];
	}

	Icon getTileImpl(Block block, Icon origIcon, int face, int metadata) {
		return this.icons[0];
	}
}
