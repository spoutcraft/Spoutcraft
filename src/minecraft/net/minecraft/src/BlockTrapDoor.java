package net.minecraft.src;

public class BlockTrapDoor extends Block {
	protected BlockTrapDoor(int i, Material material) {
		super(i, material);
		blockIndexInTexture = 84;
		if (material == Material.iron) {
			blockIndexInTexture++;
		}
		float f = 0.5F;
		float f1 = 1.0F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 0;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		setBlockBoundsBasedOnState(world, i, j, k);
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		setBlockBoundsBasedOnState(world, i, j, k);
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBoundsForBlockRender(iblockaccess.getBlockMetadata(i, j, k));
	}

	public void setBlockBoundsForItemRender() {
		float f = 0.1875F;
		setBlockBounds(0.0F, 0.5F - f / 2.0F, 0.0F, 1.0F, 0.5F + f / 2.0F, 1.0F);
	}

	public void setBlockBoundsForBlockRender(int i) {
		float f = 0.1875F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
		if (isTrapdoorOpen(i)) {
			if ((i & 3) == 0) {
				setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
			}
			if ((i & 3) == 1) {
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
			}
			if ((i & 3) == 2) {
				setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
			if ((i & 3) == 3) {
				setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
			}
		}
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		blockActivated(world, i, j, k, entityplayer);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (blockMaterial == Material.iron) {
			return true;
		}
		else {
			int l = world.getBlockMetadata(i, j, k);
			world.setBlockMetadataWithNotify(i, j, k, l ^ 4);
			world.playAuxSFXAtEntity(entityplayer, 1003, i, j, k, 0);
			return true;
		}
	}

	public void onPoweredBlockChange(World world, int i, int j, int k, boolean flag) {
		int l = world.getBlockMetadata(i, j, k);
		boolean flag1 = (l & 4) > 0;
		if (flag1 == flag) {
			return;
		}
		else {
			world.setBlockMetadataWithNotify(i, j, k, l ^ 4);
			world.playAuxSFXAtEntity(null, 1003, i, j, k, 0);
			return;
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (world.multiplayerWorld) {
			return;
		}
		int i1 = world.getBlockMetadata(i, j, k);
		int j1 = i;
		int k1 = k;
		if ((i1 & 3) == 0) {
			k1++;
		}
		if ((i1 & 3) == 1) {
			k1--;
		}
		if ((i1 & 3) == 2) {
			j1++;
		}
		if ((i1 & 3) == 3) {
			j1--;
		}
		if (!func_41052_f(world.getBlockId(j1, j, k1))) {
			world.setBlockWithNotify(i, j, k, 0);
			dropBlockAsItem(world, i, j, k, i1, 0);
		}
		boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
		if (flag || l > 0 && Block.blocksList[l].canProvidePower() || l == 0) {
			onPoweredBlockChange(world, i, j, k, flag);
		}
	}

	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
		setBlockBoundsBasedOnState(world, i, j, k);
		return super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
	}

	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		byte byte0 = 0;
		if (l == 2) {
			byte0 = 0;
		}
		if (l == 3) {
			byte0 = 1;
		}
		if (l == 4) {
			byte0 = 2;
		}
		if (l == 5) {
			byte0 = 3;
		}
		world.setBlockMetadataWithNotify(i, j, k, byte0);
	}

	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
		if (l == 0) {
			return false;
		}
		if (l == 1) {
			return false;
		}
		if (l == 2) {
			k++;
		}
		if (l == 3) {
			k--;
		}
		if (l == 4) {
			i++;
		}
		if (l == 5) {
			i--;
		}
		return func_41052_f(world.getBlockId(i, j, k));
	}

	public static boolean isTrapdoorOpen(int i) {
		return (i & 4) != 0;
	}

	private static boolean func_41052_f(int i) {
		if (i <= 0) {
			return false;
		}
		else {
			Block block = Block.blocksList[i];
			return block != null && block.blockMaterial.getIsOpaque() && block.renderAsNormalBlock() || block == Block.glowStone;
		}
	}
}
