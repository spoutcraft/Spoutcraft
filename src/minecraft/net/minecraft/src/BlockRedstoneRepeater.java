package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneRepeater extends Block {
	public static final double repeaterTorchOffset[] = {
		-0.0625D, 0.0625D, 0.1875D, 0.3125D
	};
	private static final int repeaterState[] = {
		1, 2, 3, 4
	};
	private final boolean isRepeaterPowered;

	protected BlockRedstoneRepeater(int i, boolean flag) {
		super(i, 6, Material.circuits);
		isRepeaterPowered = flag;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (!world.isBlockNormalCube(i, j - 1, k)) {
			return false;
		}
		else {
			return super.canPlaceBlockAt(world, i, j, k);
		}
	}

	public boolean canBlockStay(World world, int i, int j, int k) {
		if (!world.isBlockNormalCube(i, j - 1, k)) {
			return false;
		}
		else {
			return super.canBlockStay(world, i, j, k);
		}
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		int l = world.getBlockMetadata(i, j, k);
		boolean flag = ignoreTick(world, i, j, k, l);
		if (isRepeaterPowered && !flag) {
			world.setBlockAndMetadataWithNotify(i, j, k, Block.redstoneRepeaterIdle.blockID, l);
		}
		else if (!isRepeaterPowered) {
			world.setBlockAndMetadataWithNotify(i, j, k, Block.redstoneRepeaterActive.blockID, l);
			if (!flag) {
				int i1 = (l & 0xc) >> 2;
				world.scheduleBlockUpdate(i, j, k, Block.redstoneRepeaterActive.blockID, repeaterState[i1] * 2);
			}
		}
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) {
			return !isRepeaterPowered ? 115 : 99;
		}
		if (i == 1) {
			return !isRepeaterPowered ? 131 : 147;
		}
		else {
			return 5;
		}
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l != 0 && l != 1;
	}

	public int getRenderType() {
		return 15;
	}

	public int getBlockTextureFromSide(int i) {
		return getBlockTextureFromSideAndMetadata(i, 0);
	}

	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (!isRepeaterPowered) {
			return false;
		}
		int i1 = iblockaccess.getBlockMetadata(i, j, k) & 3;
		if (i1 == 0 && l == 3) {
			return true;
		}
		if (i1 == 1 && l == 4) {
			return true;
		}
		if (i1 == 2 && l == 2) {
			return true;
		}
		return i1 == 3 && l == 5;
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = ignoreTick(world, i, j, k, i1);
		int j1 = (i1 & 0xc) >> 2;
		if (isRepeaterPowered && !flag) {
			world.scheduleBlockUpdate(i, j, k, blockID, repeaterState[j1] * 2);
		}
		else if (!isRepeaterPowered && flag) {
			world.scheduleBlockUpdate(i, j, k, blockID, repeaterState[j1] * 2);
		}
	}

	private boolean ignoreTick(World world, int i, int j, int k, int l) {
		int i1 = l & 3;
		switch (i1) {
			case 0:
				return world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3) || world.getBlockId(i, j, k + 1) == Block.redstoneWire.blockID && world.getBlockMetadata(i, j, k + 1) > 0;

			case 2:
				return world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2) || world.getBlockId(i, j, k - 1) == Block.redstoneWire.blockID && world.getBlockMetadata(i, j, k - 1) > 0;

			case 3:
				return world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5) || world.getBlockId(i + 1, j, k) == Block.redstoneWire.blockID && world.getBlockMetadata(i + 1, j, k) > 0;

			case 1:
				return world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4) || world.getBlockId(i - 1, j, k) == Block.redstoneWire.blockID && world.getBlockMetadata(i - 1, j, k) > 0;
		}
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		int l = world.getBlockMetadata(i, j, k);
		int i1 = (l & 0xc) >> 2;
		i1 = i1 + 1 << 2 & 0xc;
		world.setBlockMetadataWithNotify(i, j, k, i1 | l & 3);
		return true;
	}

	public boolean canProvidePower() {
		return true;
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = ((MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
		world.setBlockMetadataWithNotify(i, j, k, l);
		boolean flag = ignoreTick(world, i, j, k, l);
		if (flag) {
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
		}
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
	}

	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l) {
		if (isRepeaterPowered) {
			world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		}
		super.onBlockDestroyedByPlayer(world, i, j, k, l);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.redstoneRepeater.shiftedIndex;
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (!isRepeaterPowered) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		double d = (double)((float)i + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d1 = (double)((float)j + 0.4F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d2 = (double)((float)k + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		if (random.nextInt(2) == 0) {
			switch (l & 3) {
				case 0:
					d4 = -0.3125D;
					break;

				case 2:
					d4 = 0.3125D;
					break;

				case 3:
					d3 = -0.3125D;
					break;

				case 1:
					d3 = 0.3125D;
					break;
			}
		}
		else {
			int i1 = (l & 0xc) >> 2;
			switch (l & 3) {
				case 0:
					d4 = repeaterTorchOffset[i1];
					break;

				case 2:
					d4 = -repeaterTorchOffset[i1];
					break;

				case 3:
					d3 = repeaterTorchOffset[i1];
					break;

				case 1:
					d3 = -repeaterTorchOffset[i1];
					break;
			}
		}
		world.spawnParticle("reddust", d + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
	}
}
