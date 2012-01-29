package net.minecraft.src;

import java.util.Random;

public class BlockGlowStone extends Block {
	public BlockGlowStone(int i, int j, Material material) {
		super(i, j, material);
	}

	public int quantityDroppedWithBonus(int i, Random random) {
		return MathHelper.clamp_int(quantityDropped(random) + random.nextInt(i + 1), 1, 4);
	}

	public int quantityDropped(Random random) {
		return 2 + random.nextInt(3);
	}

	public int idDropped(int i, Random random, int j) {
		return Item.lightStoneDust.shiftedIndex;
	}
}
