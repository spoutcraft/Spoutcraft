package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator {
	private final boolean field_41061_a;

	public WorldGenerator() {
		field_41061_a = false;
	}

	public WorldGenerator(boolean flag) {
		field_41061_a = flag;
	}

	public abstract boolean generate(World world, Random random, int i, int j, int k);

	public void func_517_a(double d, double d1, double d2) {
	}

	protected void func_41060_a(World world, int i, int j, int k, int l, int i1) {
		if (field_41061_a) {
			world.setBlockAndMetadataWithNotify(i, j, k, l, i1);
		}
		else {
			world.setBlockAndMetadata(i, j, k, l, i1);
		}
	}
}
