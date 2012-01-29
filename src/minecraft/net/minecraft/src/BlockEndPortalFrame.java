package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockEndPortalFrame extends Block {
	public BlockEndPortalFrame(int i) {
		super(i, 159, Material.glass);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 1) {
			return blockIndexInTexture - 1;
		}
		if (i == 0) {
			return blockIndexInTexture + 16;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 26;
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		int l = world.getBlockMetadata(i, j, k);
		if (isEnderEyeInserted(l)) {
			setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		setBlockBoundsForItemRender();
	}

	public static boolean isEnderEyeInserted(int i) {
		return (i & 4) != 0;
	}

	public int idDropped(int i, Random random, int j) {
		return 0;
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = ((MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
		world.setBlockMetadataWithNotify(i, j, k, l);
	}
}
