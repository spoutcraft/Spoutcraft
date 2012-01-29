package net.minecraft.src;

import java.util.Random;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int i, int j) {
		super(i, j, Material.wood);
	}

	public int getBlockTextureFromSide(int i) {
		if (i <= 1) {
			return 4;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public int quantityDropped(Random random) {
		return 3;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.book.shiftedIndex;
	}
}
