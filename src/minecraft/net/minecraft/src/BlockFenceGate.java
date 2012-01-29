package net.minecraft.src;

public class BlockFenceGate extends Block {
	public BlockFenceGate(int i, int j) {
		super(i, j, Material.wood);
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (!world.getBlockMaterial(i, j - 1, k).isSolid()) {
			return false;
		}
		else {
			return super.canPlaceBlockAt(world, i, j, k);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		if (isFenceGateOpen(l)) {
			return null;
		}
		if (l == 2 || l == 0) {
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, (float)k + 0.375F, i + 1, (float)j + 1.5F, (float)k + 0.625F);
		}
		else {
			return AxisAlignedBB.getBoundingBoxFromPool((float)i + 0.375F, j, k, (float)i + 0.625F, (float)j + 1.5F, k + 1);
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = func_35290_f(iblockaccess.getBlockMetadata(i, j, k));
		if (l == 2 || l == 0) {
			setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}
		else {
			setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 21;
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = (MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) % 4;
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		int l = world.getBlockMetadata(i, j, k);
		if (isFenceGateOpen(l)) {
			world.setBlockMetadataWithNotify(i, j, k, l & -5);
		}
		else {
			int i1 = (MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) % 4;
			int j1 = func_35290_f(l);
			if (j1 == (i1 + 2) % 4) {
				l = i1;
			}
			world.setBlockMetadataWithNotify(i, j, k, l | 4);
		}
		world.playAuxSFXAtEntity(entityplayer, 1003, i, j, k, 0);
		return true;
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (world.multiplayerWorld) {
			return;
		}
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
		if (flag || l > 0 && Block.blocksList[l].canProvidePower() || l == 0) {
			if (flag && !isFenceGateOpen(i1)) {
				world.setBlockMetadataWithNotify(i, j, k, i1 | 4);
				world.playAuxSFXAtEntity(null, 1003, i, j, k, 0);
			}
			else if (!flag && isFenceGateOpen(i1)) {
				world.setBlockMetadataWithNotify(i, j, k, i1 & -5);
				world.playAuxSFXAtEntity(null, 1003, i, j, k, 0);
			}
		}
	}

	public static boolean isFenceGateOpen(int i) {
		return (i & 4) != 0;
	}

	public static int func_35290_f(int i) {
		return i & 3;
	}
}
