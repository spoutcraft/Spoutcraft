package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block {
	public BlockClay(int i, int j) {
		super(i, j, Material.clay);
	}

	public int idDropped(int i, Random random, int j) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 4;
	}
}
