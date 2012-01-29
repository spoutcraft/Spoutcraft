package net.minecraft.src;

import java.util.Random;

public class WorldGenSand extends WorldGenerator {
	private int sandID;
	private int field_35263_b;

	public WorldGenSand(int i, int j) {
		sandID = j;
		field_35263_b = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		if (world.getBlockMaterial(i, j, k) != Material.water) {
			return false;
		}
		int l = random.nextInt(field_35263_b - 2) + 2;
		byte byte0 = 2;
		for (int i1 = i - l; i1 <= i + l; i1++) {
			for (int j1 = k - l; j1 <= k + l; j1++) {
				int k1 = i1 - i;
				int l1 = j1 - k;
				if (k1 * k1 + l1 * l1 > l * l) {
					continue;
				}
				for (int i2 = j - byte0; i2 <= j + byte0; i2++) {
					int j2 = world.getBlockId(i1, i2, j1);
					if (j2 == Block.dirt.blockID || j2 == Block.grass.blockID) {
						world.setBlock(i1, i2, j1, sandID);
					}
				}
			}
		}

		return true;
	}
}
