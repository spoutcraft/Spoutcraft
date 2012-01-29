package net.minecraft.src;

import java.util.Random;

public class BlockReed extends Block {
	protected BlockReed(int i, int j) {
		super(i, Material.plants);
		blockIndexInTexture = j;
		float f = 0.375F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
		setTickOnLoad(true);
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (world.isAirBlock(i, j + 1, k)) {
			int l;
			for (l = 1; world.getBlockId(i, j - l, k) == blockID; l++) { }
			if (l < 3) {
				int i1 = world.getBlockMetadata(i, j, k);
				if (i1 == 15) {
					world.setBlockWithNotify(i, j + 1, k, blockID);
					world.setBlockMetadataWithNotify(i, j, k, 0);
				}
				else {
					world.setBlockMetadataWithNotify(i, j, k, i1 + 1);
				}
			}
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		int l = world.getBlockId(i, j - 1, k);
		if (l == blockID) {
			return true;
		}
		if (l != Block.grass.blockID && l != Block.dirt.blockID && l != Block.sand.blockID) {
			return false;
		}
		if (world.getBlockMaterial(i - 1, j - 1, k) == Material.water) {
			return true;
		}
		if (world.getBlockMaterial(i + 1, j - 1, k) == Material.water) {
			return true;
		}
		if (world.getBlockMaterial(i, j - 1, k - 1) == Material.water) {
			return true;
		}
		return world.getBlockMaterial(i, j - 1, k + 1) == Material.water;
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		checkBlockCoordValid(world, i, j, k);
	}

	protected final void checkBlockCoordValid(World world, int i, int j, int k) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	public boolean canBlockStay(World world, int i, int j, int k) {
		return canPlaceBlockAt(world, i, j, k);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.reed.shiftedIndex;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
