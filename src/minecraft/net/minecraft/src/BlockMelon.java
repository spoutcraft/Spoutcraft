package net.minecraft.src;

import java.util.Random;

public class BlockMelon extends Block {
	protected BlockMelon(int i) {
		super(i, Material.pumpkin);
		blockIndexInTexture = 136;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return i != 1 && i != 0 ? 136 : 137;
	}

	public int getBlockTextureFromSide(int i) {
		return i != 1 && i != 0 ? 136 : 137;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.melon.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 3 + random.nextInt(5);
	}

	public int quantityDroppedWithBonus(int i, Random random) {
		int j = quantityDropped(random) + random.nextInt(1 + i);
		if (j > 9) {
			j = 9;
		}
		return j;
	}
}
