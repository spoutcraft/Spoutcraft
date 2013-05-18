package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$Repeat extends TileOverride {
	private final int width;
	private final int height;
	private final int symmetry;

	TileOverrideImpl$Repeat(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
		this.width = MCPatcherUtils.getIntProperty(var2, "width", 0);
		this.height = MCPatcherUtils.getIntProperty(var2, "height", 0);

		if (this.width <= 0 || this.height <= 0) {
			this.error("invalid width and height (%dx%d)", new Object[] {Integer.valueOf(this.width), Integer.valueOf(this.height)});
		}

		String var4 = var2.getProperty("symmetry", "none");

		if (var4.equals("opposite")) {
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

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (var7 < 0) {
			var7 = 0;
		}

		var7 &= this.symmetry;
		int var8;
		int var9;

		switch (var7) {
			case 0:
			case 1:
				if (this.rotateTop) {
					var8 = var6;
					var9 = var4;
				} else {
					var8 = var4;
					var9 = var6;
				}

				break;

			case 2:
				var8 = -var4 - 1;
				var9 = -var5;
				break;

			case 3:
				var8 = var4;
				var9 = -var5;
				break;

			case 4:
				var8 = var6;
				var9 = -var5;
				break;

			case 5:
				var8 = -var6 - 1;
				var9 = -var5;
				break;

			default:
				return null;
		}

		var8 %= this.width;

		if (var8 < 0) {
			var8 += this.width;
		}

		var9 %= this.height;

		if (var9 < 0) {
			var9 += this.height;
		}

		return this.icons[this.width * var9 + var8];
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
