package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockDetectorRail extends BlockRail {
	public BlockDetectorRail(int i, int j) {
		super(i, j, true);
		setTickOnLoad(true);
	}

	public int tickRate() {
		return 20;
	}

	public boolean canProvidePower() {
		return true;
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if (world.multiplayerWorld) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		if ((l & 8) != 0) {
			return;
		}
		else {
			setStateIfMinecartInteractsWithRail(world, i, j, k, l);
			return;
		}
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (world.multiplayerWorld) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		if ((l & 8) == 0) {
			return;
		}
		else {
			setStateIfMinecartInteractsWithRail(world, i, j, k, l);
			return;
		}
	}

	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return (iblockaccess.getBlockMetadata(i, j, k) & 8) != 0;
	}

	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		if ((world.getBlockMetadata(i, j, k) & 8) == 0) {
			return false;
		}
		else {
			return l == 1;
		}
	}

	private void setStateIfMinecartInteractsWithRail(World world, int i, int j, int k, int l) {
		boolean flag = (l & 8) != 0;
		boolean flag1 = false;
		float f = 0.125F;
		List list = world.getEntitiesWithinAABB(net.minecraft.src.EntityMinecart.class, AxisAlignedBB.getBoundingBoxFromPool((float)i + f, j, (float)k + f, (float)(i + 1) - f, (float)(j + 1) - f, (float)(k + 1) - f));
		if (list.size() > 0) {
			flag1 = true;
		}
		if (flag1 && !flag) {
			world.setBlockMetadataWithNotify(i, j, k, l | 8);
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
			world.markBlocksDirty(i, j, k, i, j, k);
		}
		if (!flag1 && flag) {
			world.setBlockMetadataWithNotify(i, j, k, l & 7);
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
			world.markBlocksDirty(i, j, k, i, j, k);
		}
		if (flag1) {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}
	}
}
