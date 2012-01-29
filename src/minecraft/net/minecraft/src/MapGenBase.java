package net.minecraft.src;

import java.util.Random;

public class MapGenBase {
	protected int range;
	protected Random rand;
	protected World worldObj;

	public MapGenBase() {
		range = 8;
		rand = new Random();
	}

	public void generate(IChunkProvider ichunkprovider, World world, int i, int j, byte abyte0[]) {
		int k = range;
		worldObj = world;
		rand.setSeed(world.getWorldSeed());
		long l = rand.nextLong();
		long l1 = rand.nextLong();
		for (int i1 = i - k; i1 <= i + k; i1++) {
			for (int j1 = j - k; j1 <= j + k; j1++) {
				long l2 = (long)i1 * l;
				long l3 = (long)j1 * l1;
				rand.setSeed(l2 ^ l3 ^ world.getWorldSeed());
				recursiveGenerate(world, i1, j1, i, j, abyte0);
			}
		}
	}

	protected void recursiveGenerate(World world, int i, int j, int k, int l, byte abyte0[]) {
	}
}
