package net.minecraft.src;

import java.util.Random;

public class WorldGenTallGrass extends WorldGenerator {
	private int tallGrassID;
	private int tallGrassMetadata;

	public WorldGenTallGrass(int i, int j) {
		tallGrassID = i;
		tallGrassMetadata = j;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		for (int l = 0; ((l = world.getBlockId(i, j, k)) == 0 || l == Block.leaves.blockID) && j > 0; j--) { }
		for (int i1 = 0; i1 < 128; i1++) {
			int j1 = (i + random.nextInt(8)) - random.nextInt(8);
			int k1 = (j + random.nextInt(4)) - random.nextInt(4);
			int l1 = (k + random.nextInt(8)) - random.nextInt(8);
			if (world.isAirBlock(j1, k1, l1) && ((BlockFlower)Block.blocksList[tallGrassID]).canBlockStay(world, j1, k1, l1)) {
				world.setBlockAndMetadata(j1, k1, l1, tallGrassID, tallGrassMetadata);
			}
		}

		return true;
	}
}
