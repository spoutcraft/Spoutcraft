package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TileLoader;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$BetterGrass implements ITileOverride {
	private static final int[][] OFFSET_MATRIX = new int[][] {{0, -1}, {0, 1}, { -1, 0}, {1, 0}};
	private final int blockID;
	private final String tileName;
	private final Icon fullTile;
	private final Icon fullSnowTile;

	TileOverrideImpl$BetterGrass(TileLoader var1, int var2, String var3) {
		this.blockID = var2;
		this.tileName = var3;
		this.fullSnowTile = var1.getIcon("snow");
		this.fullTile = var1.getIcon(var3 + "_top");
	}

	public String toString() {
		return "BetterGrass{" + this.tileName + "}";
	}

	public boolean isDisabled() {
		return false;
	}

	public void registerIcons() {}

	public Set getMatchingBlocks() {
		HashSet var1 = new HashSet();
		var1.add(Integer.valueOf(this.blockID));
		return var1;
	}

	public Set getMatchingTiles() {
		return null;
	}

	public int getRenderPass() {
		return 0;
	}

	public Icon getTile(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (var7 >= 2 && var7 <= 5) {
			int[] var8 = OFFSET_MATRIX[var7 - 2];

			if (var1.getBlockId(var4 + var8[0], var5 - 1, var6 + var8[1]) == this.blockID) {
				int var9 = var1.getBlockId(var4, var5 + 1, var6);
				return var9 != 78 && var9 != 80 ? this.fullTile : this.fullSnowTile;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	boolean isBetterGrass(IBlockAccess var1, Block var2, int var3, int var4, int var5, int var6) {
		return var2.blockID == this.blockID && this.getTile(var1, var2, (Icon)null, var3, var4, var5, var6) == this.fullTile;
	}

	public Icon getTile(Block var1, Icon var2, int var3, int var4) {
		return null;
	}
}