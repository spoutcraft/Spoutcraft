package net.minecraft.src;

import java.util.Random;

public class BlockObsidian extends BlockStone {
	public BlockObsidian(int i, int j) {
		super(i, j);
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int i, Random random, int j) {
		return Block.obsidian.blockID;
	}
}
