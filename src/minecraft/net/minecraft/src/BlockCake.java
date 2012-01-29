package net.minecraft.src;

import java.util.Random;

public class BlockCake extends Block {
	protected BlockCake(int i, int j) {
		super(i, j, Material.cake);
		setTickOnLoad(true);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		float f = 0.0625F;
		float f1 = (float)(1 + l * 2) / 16F;
		float f2 = 0.5F;
		setBlockBounds(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
	}

	public void setBlockBoundsForItemRender() {
		float f = 0.0625F;
		float f1 = 0.5F;
		setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.0625F;
		float f1 = (float)(1 + l * 2) / 16F;
		float f2 = 0.5F;
		return AxisAlignedBB.getBoundingBoxFromPool((float)i + f1, j, (float)k + f, (float)(i + 1) - f, ((float)j + f2) - f, (float)(k + 1) - f);
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.0625F;
		float f1 = (float)(1 + l * 2) / 16F;
		float f2 = 0.5F;
		return AxisAlignedBB.getBoundingBoxFromPool((float)i + f1, j, (float)k + f, (float)(i + 1) - f, (float)j + f2, (float)(k + 1) - f);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 1) {
			return blockIndexInTexture;
		}
		if (i == 0) {
			return blockIndexInTexture + 3;
		}
		if (j > 0 && i == 4) {
			return blockIndexInTexture + 2;
		}
		else {
			return blockIndexInTexture + 1;
		}
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return blockIndexInTexture;
		}
		if (i == 0) {
			return blockIndexInTexture + 3;
		}
		else {
			return blockIndexInTexture + 1;
		}
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		eatCakeSlice(world, i, j, k, entityplayer);
		return true;
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		eatCakeSlice(world, i, j, k, entityplayer);
	}

	private void eatCakeSlice(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (entityplayer.canEat(false)) {
			entityplayer.getFoodStats().addStats(2, 0.1F);
			int l = world.getBlockMetadata(i, j, k) + 1;
			if (l >= 6) {
				world.setBlockWithNotify(i, j, k, 0);
			}
			else {
				world.setBlockMetadataWithNotify(i, j, k, l);
				world.markBlockAsNeedsUpdate(i, j, k);
			}
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (!super.canPlaceBlockAt(world, i, j, k)) {
			return false;
		}
		else {
			return canBlockStay(world, i, j, k);
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	public boolean canBlockStay(World world, int i, int j, int k) {
		return world.getBlockMaterial(i, j - 1, k).isSolid();
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int idDropped(int i, Random random, int j) {
		return 0;
	}
}
