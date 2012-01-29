package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockPistonBase extends Block {
	private boolean isSticky;
	private static boolean ignoreUpdates;

	public BlockPistonBase(int i, int j, boolean flag) {
		super(i, j, Material.piston);
		isSticky = flag;
		setStepSound(soundStoneFootstep);
		setHardness(0.5F);
	}

	public int func_31040_i() {
		return !isSticky ? 107 : 106;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		int k = getOrientation(j);
		if (k > 5) {
			return blockIndexInTexture;
		}
		if (i == k) {
			if (isExtended(j) || minX > 0.0D || minY > 0.0D || minZ > 0.0D || maxX < 1.0D || maxY < 1.0D || maxZ < 1.0D) {
				return 110;
			}
			else {
				return blockIndexInTexture;
			}
		}
		return i != Facing.field_31057_a[k] ? 108 : 109;
	}

	public int getRenderType() {
		return 16;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return false;
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = determineOrientation(world, i, j, k, (EntityPlayer)entityliving);
		world.setBlockMetadataWithNotify(i, j, k, l);
		if (!world.multiplayerWorld && !ignoreUpdates) {
			updatePistonState(world, i, j, k);
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!world.multiplayerWorld && !ignoreUpdates) {
			updatePistonState(world, i, j, k);
		}
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		if (!world.multiplayerWorld && world.getBlockTileEntity(i, j, k) == null && !ignoreUpdates) {
			updatePistonState(world, i, j, k);
		}
	}

	private void updatePistonState(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		int i1 = getOrientation(l);
		boolean flag = isIndirectlyPowered(world, i, j, k, i1);
		if (l == 7) {
			return;
		}
		if (flag && !isExtended(l)) {
			if (canExtend(world, i, j, k, i1)) {
				world.setBlockMetadata(i, j, k, i1 | 8);
				world.playNoteAt(i, j, k, 0, i1);
			}
		}
		else if (!flag && isExtended(l)) {
			world.setBlockMetadata(i, j, k, i1);
			world.playNoteAt(i, j, k, 1, i1);
		}
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k, int l) {
		if (l != 0 && world.isBlockIndirectlyProvidingPowerTo(i, j - 1, k, 0)) {
			return true;
		}
		if (l != 1 && world.isBlockIndirectlyProvidingPowerTo(i, j + 1, k, 1)) {
			return true;
		}
		if (l != 2 && world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2)) {
			return true;
		}
		if (l != 3 && world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3)) {
			return true;
		}
		if (l != 5 && world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5)) {
			return true;
		}
		if (l != 4 && world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4)) {
			return true;
		}
		if (world.isBlockIndirectlyProvidingPowerTo(i, j, k, 0)) {
			return true;
		}
		if (world.isBlockIndirectlyProvidingPowerTo(i, j + 2, k, 1)) {
			return true;
		}
		if (world.isBlockIndirectlyProvidingPowerTo(i, j + 1, k - 1, 2)) {
			return true;
		}
		if (world.isBlockIndirectlyProvidingPowerTo(i, j + 1, k + 1, 3)) {
			return true;
		}
		if (world.isBlockIndirectlyProvidingPowerTo(i - 1, j + 1, k, 4)) {
			return true;
		}
		return world.isBlockIndirectlyProvidingPowerTo(i + 1, j + 1, k, 5);
	}

	public void powerBlock(World world, int i, int j, int k, int l, int i1) {
		ignoreUpdates = true;
		int j1 = i1;
		if (l == 0) {
			if (tryExtend(world, i, j, k, j1)) {
				world.setBlockMetadataWithNotify(i, j, k, j1 | 8);
				world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
			}
			else {
				world.setBlockMetadata(i, j, k, j1);
			}
		}
		else if (l == 1) {
			TileEntity tileentity = world.getBlockTileEntity(i + Facing.offsetsXForSide[j1], j + Facing.offsetsYForSide[j1], k + Facing.offsetsZForSide[j1]);
			if (tileentity != null && (tileentity instanceof TileEntityPiston)) {
				((TileEntityPiston)tileentity).clearPistonTileEntity();
			}
			world.setBlockAndMetadata(i, j, k, Block.pistonMoving.blockID, j1);
			world.setBlockTileEntity(i, j, k, BlockPistonMoving.getNewTileEntity(blockID, j1, j1, false, true));
			if (isSticky) {
				int k1 = i + Facing.offsetsXForSide[j1] * 2;
				int l1 = j + Facing.offsetsYForSide[j1] * 2;
				int i2 = k + Facing.offsetsZForSide[j1] * 2;
				int j2 = world.getBlockId(k1, l1, i2);
				int k2 = world.getBlockMetadata(k1, l1, i2);
				boolean flag = false;
				if (j2 == Block.pistonMoving.blockID) {
					TileEntity tileentity1 = world.getBlockTileEntity(k1, l1, i2);
					if (tileentity1 != null && (tileentity1 instanceof TileEntityPiston)) {
						TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity1;
						if (tileentitypiston.getPistonOrientation() == j1 && tileentitypiston.isExtending()) {
							tileentitypiston.clearPistonTileEntity();
							j2 = tileentitypiston.getStoredBlockID();
							k2 = tileentitypiston.getBlockMetadata();
							flag = true;
						}
					}
				}
				if (!flag && j2 > 0 && canPushBlock(j2, world, k1, l1, i2, false) && (Block.blocksList[j2].getMobilityFlag() == 0 || j2 == Block.pistonBase.blockID || j2 == Block.pistonStickyBase.blockID)) {
					i += Facing.offsetsXForSide[j1];
					j += Facing.offsetsYForSide[j1];
					k += Facing.offsetsZForSide[j1];
					world.setBlockAndMetadata(i, j, k, Block.pistonMoving.blockID, k2);
					world.setBlockTileEntity(i, j, k, BlockPistonMoving.getNewTileEntity(j2, k2, j1, false, false));
					ignoreUpdates = false;
					world.setBlockWithNotify(k1, l1, i2, 0);
					ignoreUpdates = true;
				}
				else if (!flag) {
					ignoreUpdates = false;
					world.setBlockWithNotify(i + Facing.offsetsXForSide[j1], j + Facing.offsetsYForSide[j1], k + Facing.offsetsZForSide[j1], 0);
					ignoreUpdates = true;
				}
			}
			else {
				ignoreUpdates = false;
				world.setBlockWithNotify(i + Facing.offsetsXForSide[j1], j + Facing.offsetsYForSide[j1], k + Facing.offsetsZForSide[j1], 0);
				ignoreUpdates = true;
			}
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "tile.piston.in", 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
		}
		ignoreUpdates = false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		if (isExtended(l)) {
			switch (getOrientation(l)) {
				case 0:
					setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
					break;

				case 1:
					setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
					break;

				case 2:
					setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
					break;

				case 3:
					setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
					break;

				case 4:
					setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
					break;

				case 5:
					setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
					break;
			}
		}
		else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public static int getOrientation(int i) {
		return i & 7;
	}

	public static boolean isExtended(int i) {
		return (i & 8) != 0;
	}

	private static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (MathHelper.abs((float)entityplayer.posX - (float)i) < 2.0F && MathHelper.abs((float)entityplayer.posZ - (float)k) < 2.0F) {
			double d = (entityplayer.posY + 1.8200000000000001D) - (double)entityplayer.yOffset;
			if (d - (double)j > 2D) {
				return 1;
			}
			if ((double)j - d > 0.0D) {
				return 0;
			}
		}
		int l = MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if (l == 0) {
			return 2;
		}
		if (l == 1) {
			return 5;
		}
		if (l == 2) {
			return 3;
		}
		return l != 3 ? 0 : 4;
	}

	private static boolean canPushBlock(int i, World world, int j, int k, int l, boolean flag) {
		if (i == Block.obsidian.blockID) {
			return false;
		}
		if (i == Block.pistonBase.blockID || i == Block.pistonStickyBase.blockID) {
			if (isExtended(world.getBlockMetadata(j, k, l))) {
				return false;
			}
		}
		else {
			if (Block.blocksList[i].getHardness() == -1F) {
				return false;
			}
			if (Block.blocksList[i].getMobilityFlag() == 2) {
				return false;
			}
			if (!flag && Block.blocksList[i].getMobilityFlag() == 1) {
				return false;
			}
		}
		return !(Block.blocksList[i] instanceof BlockContainer);
	}

	private static boolean canExtend(World world, int i, int j, int k, int l) {
		int i1 = i + Facing.offsetsXForSide[l];
		int j1 = j + Facing.offsetsYForSide[l];
		int k1 = k + Facing.offsetsZForSide[l];
		int l1 = 0;
		do {
			if (l1 >= 13) {
				break;
			}
			if (j1 <= 0 || j1 >= world.worldHeight - 1) {
				return false;
			}
			int i2 = world.getBlockId(i1, j1, k1);
			if (i2 == 0) {
				break;
			}
			if (!canPushBlock(i2, world, i1, j1, k1, true)) {
				return false;
			}
			if (Block.blocksList[i2].getMobilityFlag() == 1) {
				break;
			}
			if (l1 == 12) {
				return false;
			}
			i1 += Facing.offsetsXForSide[l];
			j1 += Facing.offsetsYForSide[l];
			k1 += Facing.offsetsZForSide[l];
			l1++;
		}
		while (true);
		return true;
	}

	private boolean tryExtend(World world, int i, int j, int k, int l) {
		int i1 = i + Facing.offsetsXForSide[l];
		int j1 = j + Facing.offsetsYForSide[l];
		int k1 = k + Facing.offsetsZForSide[l];
		int l1 = 0;
		do {
			if (l1 >= 13) {
				break;
			}
			if (j1 <= 0 || j1 >= world.worldHeight - 1) {
				return false;
			}
			int j2 = world.getBlockId(i1, j1, k1);
			if (j2 == 0) {
				break;
			}
			if (!canPushBlock(j2, world, i1, j1, k1, true)) {
				return false;
			}
			if (Block.blocksList[j2].getMobilityFlag() == 1) {
				Block.blocksList[j2].dropBlockAsItem(world, i1, j1, k1, world.getBlockMetadata(i1, j1, k1), 0);
				world.setBlockWithNotify(i1, j1, k1, 0);
				break;
			}
			if (l1 == 12) {
				return false;
			}
			i1 += Facing.offsetsXForSide[l];
			j1 += Facing.offsetsYForSide[l];
			k1 += Facing.offsetsZForSide[l];
			l1++;
		}
		while (true);
		int l2;
		for (; i1 != i || j1 != j || k1 != k; k1 = l2) {
			int i2 = i1 - Facing.offsetsXForSide[l];
			int k2 = j1 - Facing.offsetsYForSide[l];
			l2 = k1 - Facing.offsetsZForSide[l];
			int i3 = world.getBlockId(i2, k2, l2);
			int j3 = world.getBlockMetadata(i2, k2, l2);
			if (i3 == blockID && i2 == i && k2 == j && l2 == k) {
				world.setBlockAndMetadata(i1, j1, k1, Block.pistonMoving.blockID, l | (isSticky ? 8 : 0));
				world.setBlockTileEntity(i1, j1, k1, BlockPistonMoving.getNewTileEntity(Block.pistonExtension.blockID, l | (isSticky ? 8 : 0), l, true, false));
			}
			else {
				world.setBlockAndMetadata(i1, j1, k1, Block.pistonMoving.blockID, j3);
				world.setBlockTileEntity(i1, j1, k1, BlockPistonMoving.getNewTileEntity(i3, j3, l, true, false));
			}
			i1 = i2;
			j1 = k2;
		}

		return true;
	}
}
