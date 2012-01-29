package net.minecraft.src;

import java.util.Random;

public class BlockNetherStalk extends BlockFlower {
	protected BlockNetherStalk(int i) {
		super(i, 226);
		setTickOnLoad(true);
		float f = 0.5F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i) {
		return i == Block.slowSand.blockID;
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		int l = world.getBlockMetadata(i, j, k);
		if (l < 3) {
			WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
			if (worldchunkmanager != null) {
				BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i, k);
				if ((biomegenbase instanceof BiomeGenHell) && random.nextInt(15) == 0) {
					l++;
					world.setBlockMetadataWithNotify(i, j, k, l);
				}
			}
		}
		super.updateTick(world, i, j, k, random);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (j >= 3) {
			return blockIndexInTexture + 2;
		}
		if (j > 0) {
			return blockIndexInTexture + 1;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public int getRenderType() {
		return 6;
	}

	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {
		if (world.multiplayerWorld) {
			return;
		}
		int j1 = 1;
		if (l >= 3) {
			j1 = 2 + world.rand.nextInt(3);
			if (i1 > 0) {
				j1 += world.rand.nextInt(i1 + 1);
			}
		}
		for (int k1 = 0; k1 < j1; k1++) {
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Item.netherStalkSeeds));
		}
	}

	public int idDropped(int i, Random random, int j) {
		return 0;
	}

	public int quantityDropped(Random random) {
		return 0;
	}
}
