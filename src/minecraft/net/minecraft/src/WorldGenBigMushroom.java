package net.minecraft.src;

import java.util.Random;

public class WorldGenBigMushroom extends WorldGenerator {
	private int mushroomType;

	public WorldGenBigMushroom(int i) {
		mushroomType = -1;
		mushroomType = i;
	}

	public WorldGenBigMushroom() {
		mushroomType = -1;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		int l = random.nextInt(2);
		if (mushroomType >= 0) {
			l = mushroomType;
		}
		int i1 = random.nextInt(3) + 4;
		boolean flag = true;
		if (j < 1 || j + i1 + 1 > world.worldHeight) {
			return false;
		}
		for (int j1 = j; j1 <= j + 1 + i1; j1++) {
			byte byte0 = 3;
			if (j1 == j) {
				byte0 = 0;
			}
			for (int i2 = i - byte0; i2 <= i + byte0 && flag; i2++) {
				for (int l2 = k - byte0; l2 <= k + byte0 && flag; l2++) {
					if (j1 >= 0 && j1 < world.worldHeight) {
						int k3 = world.getBlockId(i2, j1, l2);
						if (k3 != 0 && k3 != Block.leaves.blockID) {
							flag = false;
						}
					}
					else {
						flag = false;
					}
				}
			}
		}

		if (!flag) {
			return false;
		}
		int k1 = world.getBlockId(i, j - 1, k);
		if (k1 != Block.dirt.blockID && k1 != Block.grass.blockID && k1 != Block.mycelium.blockID) {
			return false;
		}
		if (!Block.mushroomBrown.canPlaceBlockAt(world, i, j, k)) {
			return false;
		}
		world.setBlock(i, j - 1, k, Block.dirt.blockID);
		int l1 = j + i1;
		if (l == 1) {
			l1 = (j + i1) - 3;
		}
		for (int j2 = l1; j2 <= j + i1; j2++) {
			int i3 = 1;
			if (j2 < j + i1) {
				i3++;
			}
			if (l == 0) {
				i3 = 3;
			}
			for (int l3 = i - i3; l3 <= i + i3; l3++) {
				for (int i4 = k - i3; i4 <= k + i3; i4++) {
					int j4 = 5;
					if (l3 == i - i3) {
						j4--;
					}
					if (l3 == i + i3) {
						j4++;
					}
					if (i4 == k - i3) {
						j4 -= 3;
					}
					if (i4 == k + i3) {
						j4 += 3;
					}
					if (l == 0 || j2 < j + i1) {
						if ((l3 == i - i3 || l3 == i + i3) && (i4 == k - i3 || i4 == k + i3)) {
							continue;
						}
						if (l3 == i - (i3 - 1) && i4 == k - i3) {
							j4 = 1;
						}
						if (l3 == i - i3 && i4 == k - (i3 - 1)) {
							j4 = 1;
						}
						if (l3 == i + (i3 - 1) && i4 == k - i3) {
							j4 = 3;
						}
						if (l3 == i + i3 && i4 == k - (i3 - 1)) {
							j4 = 3;
						}
						if (l3 == i - (i3 - 1) && i4 == k + i3) {
							j4 = 7;
						}
						if (l3 == i - i3 && i4 == k + (i3 - 1)) {
							j4 = 7;
						}
						if (l3 == i + (i3 - 1) && i4 == k + i3) {
							j4 = 9;
						}
						if (l3 == i + i3 && i4 == k + (i3 - 1)) {
							j4 = 9;
						}
					}
					if (j4 == 5 && j2 < j + i1) {
						j4 = 0;
					}
					if ((j4 != 0 || j >= (j + i1) - 1) && !Block.opaqueCubeLookup[world.getBlockId(l3, j2, i4)]) {
						world.setBlockAndMetadata(l3, j2, i4, Block.mushroomCapBrown.blockID + l, j4);
					}
				}
			}
		}

		for (int k2 = 0; k2 < i1; k2++) {
			int j3 = world.getBlockId(i, j + k2, k);
			if (!Block.opaqueCubeLookup[j3]) {
				world.setBlockAndMetadata(i, j + k2, k, Block.mushroomCapBrown.blockID + l, 10);
			}
		}

		return true;
	}
}
