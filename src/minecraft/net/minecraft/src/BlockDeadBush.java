package net.minecraft.src;

import java.util.Random;

public class BlockDeadBush extends BlockFlower {
	protected BlockDeadBush(int i, int j) {
		super(i, j);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i) {
		return i == Block.sand.blockID;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return blockIndexInTexture;
	}

	public int idDropped(int i, Random random, int j) {
		return -1;
	}
}
