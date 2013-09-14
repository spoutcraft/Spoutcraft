package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TileLoader;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

final class TileOverrideImpl$Repeat extends TileOverride {
	private final int width;
	private final int height;
	private final int symmetry;

	TileOverrideImpl$Repeat(ResourceLocation filePrefix, Properties properties, TileLoader tileLoader) {
		super(filePrefix, properties, tileLoader);
		this.width = MCPatcherUtils.getIntProperty(properties, "width", 0);
		this.height = MCPatcherUtils.getIntProperty(properties, "height", 0);

		if (this.width <= 0 || this.height <= 0) {
			this.error("invalid width and height (%dx%d)", new Object[] {Integer.valueOf(this.width), Integer.valueOf(this.height)});
		}

		String sym = properties.getProperty("symmetry", "none");

		if (sym.equals("opposite")) {
			this.symmetry = -2;
		} else {
			this.symmetry = -1;
		}
	}

	String getMethod() {
		return "repeat";
	}

	String checkTileMap() {
		return this.getNumberOfTiles() == this.width * this.height ? null : String.format("requires exactly %dx%d tiles", new Object[] {Integer.valueOf(this.width), Integer.valueOf(this.height)});
	}

	Icon getTileImpl(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (face < 0) {
			face = 0;
		}

		face &= this.symmetry;
		int x;
		int y;

		switch (face) {
			case 0:
			case 1:
				if (this.rotateTop) {
					x = k;
					y = i;
				} else {
					x = i;
					y = k;
				}

				break;

			case 2:
				x = -i - 1;
				y = -j;
				break;

			case 3:
				x = i;
				y = -j;
				break;

			case 4:
				x = k;
				y = -j;
				break;

			case 5:
				x = -k - 1;
				y = -j;
				break;

			default:
				return null;
		}

		x %= this.width;

		if (x < 0) {
			x += this.width;
		}

		y %= this.height;

		if (y < 0) {
			y += this.height;
		}

		return this.icons[this.width * y + x];
	}

	Icon getTileImpl(Block block, Icon origIcon, int face, int metadata) {
		return this.icons[0];
	}
}
