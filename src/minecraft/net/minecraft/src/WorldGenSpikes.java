package net.minecraft.src;

import java.util.Random;

public class WorldGenSpikes extends WorldGenerator {
	private int field_40197_a;

	public WorldGenSpikes(int i) {
		field_40197_a = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		if (!world.isAirBlock(i, j, k) || world.getBlockId(i, j - 1, k) != field_40197_a) {
			return false;
		}
		int l = random.nextInt(32) + 6;
		int i1 = random.nextInt(4) + 1;
		for (int j1 = i - i1; j1 <= i + i1; j1++) {
			for (int l1 = k - i1; l1 <= k + i1; l1++) {
				int j2 = j1 - i;
				int l2 = l1 - k;
				if (j2 * j2 + l2 * l2 <= i1 * i1 + 1 && world.getBlockId(j1, j - 1, l1) != field_40197_a) {
					return false;
				}
			}
		}

		for (int k1 = j; k1 < j + l && k1 < world.worldHeight; k1++) {
			for (int i2 = i - i1; i2 <= i + i1; i2++) {
				for (int k2 = k - i1; k2 <= k + i1; k2++) {
					int i3 = i2 - i;
					int j3 = k2 - k;
					if (i3 * i3 + j3 * j3 <= i1 * i1 + 1) {
						world.setBlockWithNotify(i2, k1, k2, Block.obsidian.blockID);
					}
				}
			}
		}

		EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world);
		entityendercrystal.setLocationAndAngles((float)i + 0.5F, j + l, (float)k + 0.5F, random.nextFloat() * 360F, 0.0F);
		world.spawnEntityInWorld(entityendercrystal);
		world.setBlockWithNotify(i, j + l, k, Block.bedrock.blockID);
		return true;
	}
}
