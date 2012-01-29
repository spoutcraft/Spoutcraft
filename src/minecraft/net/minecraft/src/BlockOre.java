package net.minecraft.src;

import java.util.Random;

public class BlockOre extends Block {
	public BlockOre(int i, int j) {
		super(i, j, Material.rock);
	}

	public int idDropped(int i, Random random, int j) {
		if (blockID == Block.oreCoal.blockID) {
			return Item.coal.shiftedIndex;
		}
		if (blockID == Block.oreDiamond.blockID) {
			return Item.diamond.shiftedIndex;
		}
		if (blockID == Block.oreLapis.blockID) {
			return Item.dyePowder.shiftedIndex;
		}
		else {
			return blockID;
		}
	}

	public int quantityDropped(Random random) {
		if (blockID == Block.oreLapis.blockID) {
			return 4 + random.nextInt(5);
		}
		else {
			return 1;
		}
	}

	public int quantityDroppedWithBonus(int i, Random random) {
		if (i > 0 && blockID != idDropped(0, random, i)) {
			int j = random.nextInt(i + 2) - 1;
			if (j < 0) {
				j = 0;
			}
			return quantityDropped(random) * (j + 1);
		}
		else {
			return quantityDropped(random);
		}
	}

	protected int damageDropped(int i) {
		return blockID != Block.oreLapis.blockID ? 0 : 4;
	}
}
