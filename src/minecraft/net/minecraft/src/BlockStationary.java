package net.minecraft.src;

import java.util.Random;

public class BlockStationary extends BlockFluid {
	protected BlockStationary(int i, Material material) {
		super(i, material);
		setTickOnLoad(false);
		if (material == Material.lava) {
			setTickOnLoad(true);
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		super.onNeighborBlockChange(world, i, j, k, l);
		if (world.getBlockId(i, j, k) == blockID) {
			setNotStationary(world, i, j, k);
		}
	}

	private void setNotStationary(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		world.editingBlocks = true;
		world.setBlockAndMetadata(i, j, k, blockID - 1, l);
		world.markBlocksDirty(i, j, k, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID - 1, tickRate());
		world.editingBlocks = false;
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (blockMaterial == Material.lava) {
			int l = random.nextInt(3);
			for (int i1 = 0; i1 < l; i1++) {
				i += random.nextInt(3) - 1;
				j++;
				k += random.nextInt(3) - 1;
				int j1 = world.getBlockId(i, j, k);
				if (j1 == 0) {
					if (isFlammable(world, i - 1, j, k) || isFlammable(world, i + 1, j, k) || isFlammable(world, i, j, k - 1) || isFlammable(world, i, j, k + 1) || isFlammable(world, i, j - 1, k) || isFlammable(world, i, j + 1, k)) {
						world.setBlockWithNotify(i, j, k, Block.fire.blockID);
						return;
					}
					continue;
				}
				if (Block.blocksList[j1].blockMaterial.getIsSolid()) {
					return;
				}
			}
		}
	}

	private boolean isFlammable(World world, int i, int j, int k) {
		return world.getBlockMaterial(i, j, k).getCanBurn();
	}
}
