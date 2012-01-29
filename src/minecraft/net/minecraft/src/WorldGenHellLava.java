package net.minecraft.src;

import java.util.Random;

public class WorldGenHellLava extends WorldGenerator {
	private int hellLavaID;

	public WorldGenHellLava(int i) {
		hellLavaID = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		if (world.getBlockId(i, j + 1, k) != Block.netherrack.blockID) {
			return false;
		}
		if (world.getBlockId(i, j, k) != 0 && world.getBlockId(i, j, k) != Block.netherrack.blockID) {
			return false;
		}
		int l = 0;
		if (world.getBlockId(i - 1, j, k) == Block.netherrack.blockID) {
			l++;
		}
		if (world.getBlockId(i + 1, j, k) == Block.netherrack.blockID) {
			l++;
		}
		if (world.getBlockId(i, j, k - 1) == Block.netherrack.blockID) {
			l++;
		}
		if (world.getBlockId(i, j, k + 1) == Block.netherrack.blockID) {
			l++;
		}
		if (world.getBlockId(i, j - 1, k) == Block.netherrack.blockID) {
			l++;
		}
		int i1 = 0;
		if (world.isAirBlock(i - 1, j, k)) {
			i1++;
		}
		if (world.isAirBlock(i + 1, j, k)) {
			i1++;
		}
		if (world.isAirBlock(i, j, k - 1)) {
			i1++;
		}
		if (world.isAirBlock(i, j, k + 1)) {
			i1++;
		}
		if (world.isAirBlock(i, j - 1, k)) {
			i1++;
		}
		if (l == 4 && i1 == 1) {
			world.setBlockWithNotify(i, j, k, hellLavaID);
			world.scheduledUpdatesAreImmediate = true;
			Block.blocksList[hellLavaID].updateTick(world, i, j, k, random);
			world.scheduledUpdatesAreImmediate = false;
		}
		return true;
	}
}
