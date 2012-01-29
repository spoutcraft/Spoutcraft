package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneOre extends Block {
	private boolean glowing;

	public BlockRedstoneOre(int i, int j, boolean flag) {
		super(i, j, Material.rock);
		if (flag) {
			setTickOnLoad(true);
		}
		glowing = flag;
	}

	public int tickRate() {
		return 30;
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		glow(world, i, j, k);
		super.onBlockClicked(world, i, j, k, entityplayer);
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		glow(world, i, j, k);
		super.onEntityWalking(world, i, j, k, entity);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		glow(world, i, j, k);
		return super.blockActivated(world, i, j, k, entityplayer);
	}

	private void glow(World world, int i, int j, int k) {
		sparkle(world, i, j, k);
		if (blockID == Block.oreRedstone.blockID) {
			world.setBlockWithNotify(i, j, k, Block.oreRedstoneGlowing.blockID);
		}
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (blockID == Block.oreRedstoneGlowing.blockID) {
			world.setBlockWithNotify(i, j, k, Block.oreRedstone.blockID);
		}
	}

	public int idDropped(int i, Random random, int j) {
		return Item.redstone.shiftedIndex;
	}

	public int quantityDroppedWithBonus(int i, Random random) {
		return quantityDropped(random) + random.nextInt(i + 1);
	}

	public int quantityDropped(Random random) {
		return 4 + random.nextInt(2);
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (glowing) {
			sparkle(world, i, j, k);
		}
	}

	private void sparkle(World world, int i, int j, int k) {
		Random random = world.rand;
		double d = 0.0625D;
		for (int l = 0; l < 6; l++) {
			double d1 = (float)i + random.nextFloat();
			double d2 = (float)j + random.nextFloat();
			double d3 = (float)k + random.nextFloat();
			if (l == 0 && !world.isBlockOpaqueCube(i, j + 1, k)) {
				d2 = (double)(j + 1) + d;
			}
			if (l == 1 && !world.isBlockOpaqueCube(i, j - 1, k)) {
				d2 = (double)(j + 0) - d;
			}
			if (l == 2 && !world.isBlockOpaqueCube(i, j, k + 1)) {
				d3 = (double)(k + 1) + d;
			}
			if (l == 3 && !world.isBlockOpaqueCube(i, j, k - 1)) {
				d3 = (double)(k + 0) - d;
			}
			if (l == 4 && !world.isBlockOpaqueCube(i + 1, j, k)) {
				d1 = (double)(i + 1) + d;
			}
			if (l == 5 && !world.isBlockOpaqueCube(i - 1, j, k)) {
				d1 = (double)(i + 0) - d;
			}
			if (d1 < (double)i || d1 > (double)(i + 1) || d2 < 0.0D || d2 > (double)(j + 1) || d3 < (double)k || d3 > (double)(k + 1)) {
				world.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	protected ItemStack createStackedBlock(int i) {
		return new ItemStack(Block.oreRedstone);
	}
}
