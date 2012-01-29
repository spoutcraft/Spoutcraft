package net.minecraft.src;

import java.util.Random;

public abstract class StructurePieceBlockSelector {
	protected int selectedBlockId;
	protected int selectedBlockMetaData;

	protected StructurePieceBlockSelector() {
	}

	public abstract void selectBlocks(Random random, int i, int j, int k, boolean flag);

	public int getSelectedBlockId() {
		return selectedBlockId;
	}

	public int getSelectedBlockMetaData() {
		return selectedBlockMetaData;
	}
}
