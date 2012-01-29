package net.minecraft.src;

import java.util.Random;

public class WorldGenGlowStone2 extends WorldGenerator {
	public WorldGenGlowStone2() {
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		if (!world.isAirBlock(i, j, k)) {
			return false;
		}
		if (world.getBlockId(i, j + 1, k) != Block.netherrack.blockID) {
			return false;
		}
		world.setBlockWithNotify(i, j, k, Block.glowStone.blockID);
		for (int l = 0; l < 1500; l++) {
			int i1 = (i + random.nextInt(8)) - random.nextInt(8);
			int j1 = j - random.nextInt(12);
			int k1 = (k + random.nextInt(8)) - random.nextInt(8);
			if (world.getBlockId(i1, j1, k1) != 0) {
				continue;
			}
			int l1 = 0;
			for (int i2 = 0; i2 < 6; i2++) {
				int j2 = 0;
				if (i2 == 0) {
					j2 = world.getBlockId(i1 - 1, j1, k1);
				}
				if (i2 == 1) {
					j2 = world.getBlockId(i1 + 1, j1, k1);
				}
				if (i2 == 2) {
					j2 = world.getBlockId(i1, j1 - 1, k1);
				}
				if (i2 == 3) {
					j2 = world.getBlockId(i1, j1 + 1, k1);
				}
				if (i2 == 4) {
					j2 = world.getBlockId(i1, j1, k1 - 1);
				}
				if (i2 == 5) {
					j2 = world.getBlockId(i1, j1, k1 + 1);
				}
				if (j2 == Block.glowStone.blockID) {
					l1++;
				}
			}

			if (l1 == 1) {
				world.setBlockWithNotify(i1, j1, k1, Block.glowStone.blockID);
			}
		}

		return true;
	}
}
