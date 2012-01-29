package net.minecraft.src;

import java.util.Random;

public class BlockWeb extends Block {
	public BlockWeb(int i, int j) {
		super(i, j, Material.web);
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.setInWeb();
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	public int getRenderType() {
		return 1;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.silk.shiftedIndex;
	}
}
