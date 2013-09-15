package com.prupe.mcpatcher.ctm;

import java.util.HashSet;
import java.util.Set;

import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

final class TileOverrideImpl$BetterGrass implements ITileOverride {
	private static final int[][] OFFSET_MATRIX = new int[][] {{0, -1}, {0, 1}, { -1, 0}, {1, 0}};
	private final int blockID;
	private final String tileName;
	private final Icon fullTile;
	private final Icon fullSnowTile;

	TileOverrideImpl$BetterGrass(TileLoader tileLoader, int blockID, String tileName) {
		this.blockID = blockID;
		this.tileName = tileName;
		this.fullSnowTile = tileLoader.getIcon("snow");
		this.fullTile = tileLoader.getIcon(tileName + "_top");
	}

	public String toString() {
		return "BetterGrass{" + this.tileName + "}";
	}

	public boolean isDisabled() {
		return false;
	}

	public void registerIcons() {}

	public Set<Integer> getMatchingBlocks() {
		HashSet ids = new HashSet();
		ids.add(Integer.valueOf(this.blockID));
		return ids;
	}

	public Set<String> getMatchingTiles() {
		return null;
	}

	public int getRenderPass() {
		return 0;
	}

	public int getWeight() {
		return -1;
	}

	public int compareTo(ITileOverride o) {
		return o.getWeight() - this.getWeight();
	}

	public Icon getTile(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (face >= 2 && face <= 5) {
			int[] offset = OFFSET_MATRIX[face - 2];

			if (blockAccess.getBlockId(i + offset[0], j - 1, k + offset[1]) == this.blockID) {
				int neighborBlock = blockAccess.getBlockId(i, j + 1, k);

				if (neighborBlock != 78 && neighborBlock != 80) {
					return this.fullTile;
				} else {
					neighborBlock = blockAccess.getBlockId(i + offset[0], j, k + offset[1]);
					return neighborBlock != 78 && neighborBlock != 80 ? null : this.fullSnowTile;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	boolean isBetterGrass(IBlockAccess blockAccess, Block block, int i, int j, int k, int face) {
		return block.blockID == this.blockID && this.getTile(blockAccess, block, (Icon)null, i, j, k, face) == this.fullTile;
	}

	public Icon getTile(Block block, Icon origIcon, int face, int metadata) {
		return null;
	}
	
}
