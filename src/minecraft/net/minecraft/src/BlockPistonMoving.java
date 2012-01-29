package net.minecraft.src;

import java.util.Random;

public class BlockPistonMoving extends BlockContainer {
	public BlockPistonMoving(int i) {
		super(i, Material.piston);
		setHardness(-1F);
	}

	public TileEntity getBlockEntity() {
		return null;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if (tileentity != null && (tileentity instanceof TileEntityPiston)) {
			((TileEntityPiston)tileentity).clearPistonTileEntity();
		}
		else {
			super.onBlockRemoval(world, i, j, k);
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return false;
	}

	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
		return false;
	}

	public int getRenderType() {
		return -1;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (!world.multiplayerWorld && world.getBlockTileEntity(i, j, k) == null) {
			world.setBlockWithNotify(i, j, k, 0);
			return true;
		}
		else {
			return false;
		}
	}

	public int idDropped(int i, Random random, int j) {
		return 0;
	}

	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {
		if (world.multiplayerWorld) {
			return;
		}
		TileEntityPiston tileentitypiston = getTileEntityAtLocation(world, i, j, k);
		if (tileentitypiston == null) {
			return;
		}
		else {
			Block.blocksList[tileentitypiston.getStoredBlockID()].dropBlockAsItem(world, i, j, k, tileentitypiston.getBlockMetadata(), 0);
			return;
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!world.multiplayerWorld) {
			if (world.getBlockTileEntity(i, j, k) != null);
		}
	}

	public static TileEntity getNewTileEntity(int i, int j, int k, boolean flag, boolean flag1) {
		return new TileEntityPiston(i, j, k, flag, flag1);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		TileEntityPiston tileentitypiston = getTileEntityAtLocation(world, i, j, k);
		if (tileentitypiston == null) {
			return null;
		}
		float f = tileentitypiston.func_31008_a(0.0F);
		if (tileentitypiston.isExtending()) {
			f = 1.0F - f;
		}
		return getAxisAlignedBB(world, i, j, k, tileentitypiston.getStoredBlockID(), f, tileentitypiston.getPistonOrientation());
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		TileEntityPiston tileentitypiston = getTileEntityAtLocation(iblockaccess, i, j, k);
		if (tileentitypiston != null) {
			Block block = Block.blocksList[tileentitypiston.getStoredBlockID()];
			if (block == null || block == this) {
				return;
			}
			block.setBlockBoundsBasedOnState(iblockaccess, i, j, k);
			float f = tileentitypiston.func_31008_a(0.0F);
			if (tileentitypiston.isExtending()) {
				f = 1.0F - f;
			}
			int l = tileentitypiston.getPistonOrientation();
			minX = block.minX - (double)((float)Facing.offsetsXForSide[l] * f);
			minY = block.minY - (double)((float)Facing.offsetsYForSide[l] * f);
			minZ = block.minZ - (double)((float)Facing.offsetsZForSide[l] * f);
			maxX = block.maxX - (double)((float)Facing.offsetsXForSide[l] * f);
			maxY = block.maxY - (double)((float)Facing.offsetsYForSide[l] * f);
			maxZ = block.maxZ - (double)((float)Facing.offsetsZForSide[l] * f);
		}
	}

	public AxisAlignedBB getAxisAlignedBB(World world, int i, int j, int k, int l, float f, int i1) {
		if (l == 0 || l == blockID) {
			return null;
		}
		AxisAlignedBB axisalignedbb = Block.blocksList[l].getCollisionBoundingBoxFromPool(world, i, j, k);
		if (axisalignedbb == null) {
			return null;
		}
		else {
			axisalignedbb.minX -= (float)Facing.offsetsXForSide[i1] * f;
			axisalignedbb.maxX -= (float)Facing.offsetsXForSide[i1] * f;
			axisalignedbb.minY -= (float)Facing.offsetsYForSide[i1] * f;
			axisalignedbb.maxY -= (float)Facing.offsetsYForSide[i1] * f;
			axisalignedbb.minZ -= (float)Facing.offsetsZForSide[i1] * f;
			axisalignedbb.maxZ -= (float)Facing.offsetsZForSide[i1] * f;
			return axisalignedbb;
		}
	}

	private TileEntityPiston getTileEntityAtLocation(IBlockAccess iblockaccess, int i, int j, int k) {
		TileEntity tileentity = iblockaccess.getBlockTileEntity(i, j, k);
		if (tileentity != null && (tileentity instanceof TileEntityPiston)) {
			return (TileEntityPiston)tileentity;
		}
		else {
			return null;
		}
	}
}
