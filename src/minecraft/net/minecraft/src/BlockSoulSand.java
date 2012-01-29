package net.minecraft.src;

public class BlockSoulSand extends Block {
	public BlockSoulSand(int i, int j) {
		super(i, j, Material.sand);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0.125F;
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, (float)(j + 1) - f, k + 1);
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.motionX *= 0.40000000000000002D;
		entity.motionZ *= 0.40000000000000002D;
	}
}
