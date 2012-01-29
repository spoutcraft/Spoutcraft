package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockPane extends Block {
	private int sideTextureIndex;
	private final boolean canDropItself;

	protected BlockPane(int i, int j, int k, Material material, boolean flag) {
		super(i, j, material);
		sideTextureIndex = k;
		canDropItself = flag;
	}

	public int idDropped(int i, Random random, int j) {
		if (!canDropItself) {
			return 0;
		}
		else {
			return super.idDropped(i, random, j);
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 18;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		int i1 = iblockaccess.getBlockId(i, j, k);
		if (i1 == blockID) {
			return false;
		}
		else {
			return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
		}
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		boolean flag = func_35298_d(world.getBlockId(i, j, k - 1));
		boolean flag1 = func_35298_d(world.getBlockId(i, j, k + 1));
		boolean flag2 = func_35298_d(world.getBlockId(i - 1, j, k));
		boolean flag3 = func_35298_d(world.getBlockId(i + 1, j, k));
		if (flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1) {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (flag2 && !flag3) {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (!flag2 && flag3) {
			setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		if (flag && flag1 || !flag2 && !flag3 && !flag && !flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (flag && !flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (!flag && flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		boolean flag = func_35298_d(iblockaccess.getBlockId(i, j, k - 1));
		boolean flag1 = func_35298_d(iblockaccess.getBlockId(i, j, k + 1));
		boolean flag2 = func_35298_d(iblockaccess.getBlockId(i - 1, j, k));
		boolean flag3 = func_35298_d(iblockaccess.getBlockId(i + 1, j, k));
		if (flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1) {
			f = 0.0F;
			f1 = 1.0F;
		}
		else if (flag2 && !flag3) {
			f = 0.0F;
		}
		else if (!flag2 && flag3) {
			f1 = 1.0F;
		}
		if (flag && flag1 || !flag2 && !flag3 && !flag && !flag1) {
			f2 = 0.0F;
			f3 = 1.0F;
		}
		else if (flag && !flag1) {
			f2 = 0.0F;
		}
		else if (!flag && flag1) {
			f3 = 1.0F;
		}
		setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	public int getSideTextureIndex() {
		return sideTextureIndex;
	}

	public final boolean func_35298_d(int i) {
		return Block.opaqueCubeLookup[i] || i == blockID || i == Block.glass.blockID;
	}
}
