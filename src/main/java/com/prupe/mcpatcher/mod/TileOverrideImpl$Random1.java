package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.WeightedIndex;
import java.util.Properties;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$Random1 extends TileOverride {
	private static final long P1 = 31024237183253L;
	private static final long P2 = 37916421967133L;
	private static final long P3 = 247193919306661L;
	private static final long P4 = 179199247101619L;
	private static final long MULTIPLIER = 25214903917L;
	private static final long ADDEND = 11L;
	private final int symmetry;
	private final WeightedIndex chooser;

	TileOverrideImpl$Random1(String var1, Properties var2, TileLoader var3) {
		super(var1, var2, var3);
		String var4 = var2.getProperty("symmetry", "none");

		if (var4.equals("all")) {
			this.symmetry = 6;
		} else if (var4.equals("opposite")) {
			this.symmetry = 2;
		} else {
			this.symmetry = 1;
		}

		this.chooser = WeightedIndex.create(this.getNumberOfTiles(), var2.getProperty("weights", ""));

		if (this.chooser == null) {
			this.error("invalid weights", new Object[0]);
		}
	}

	String getMethod() {
		return "random";
	}

	Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (this.getNumberOfTiles() == 1) {
			return this.icons[0];
		} else {
			if (var7 < 0) {
				var7 = 0;
			}

			var7 = this.reorient(var7) / this.symmetry;
			long var8 = 31024237183253L * (long)var4 * ((long)var4 + 11L) + 37916421967133L * (long)var5 * ((long)var5 + 11L) + 247193919306661L * (long)var6 * ((long)var6 + 11L) + 179199247101619L * (long)var7 * ((long)var7 + 11L);
			var8 = 25214903917L * (var8 + (long)var4 + (long)var5 + (long)var6 + (long)var7) + 11L;
			int var10 = this.chooser.choose(var8);
			return this.icons[var10];
		}
	}

	Icon getTileImpl(Block var1, Icon var2, int var3, int var4) {
		return this.icons[0];
	}
}
