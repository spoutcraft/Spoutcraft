package net.minecraft.src;

import java.util.Random;

public class BlockLockedChest extends Block {
	protected BlockLockedChest(int i) {
		super(i, Material.wood);
		blockIndexInTexture = 26;
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return blockIndexInTexture - 1;
		}
		if (l == 0) {
			return blockIndexInTexture - 1;
		}
		int i1 = iblockaccess.getBlockId(i, j, k - 1);
		int j1 = iblockaccess.getBlockId(i, j, k + 1);
		int k1 = iblockaccess.getBlockId(i - 1, j, k);
		int l1 = iblockaccess.getBlockId(i + 1, j, k);
		byte byte0 = 3;
		if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[j1]) {
			byte0 = 3;
		}
		if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[i1]) {
			byte0 = 2;
		}
		if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[l1]) {
			byte0 = 5;
		}
		if (Block.opaqueCubeLookup[l1] && !Block.opaqueCubeLookup[k1]) {
			byte0 = 4;
		}
		return l != byte0 ? blockIndexInTexture : blockIndexInTexture + 1;
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return blockIndexInTexture - 1;
		}
		if (i == 0) {
			return blockIndexInTexture - 1;
		}
		if (i == 3) {
			return blockIndexInTexture + 1;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return true;
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		world.setBlockWithNotify(i, j, k, 0);
	}
}
