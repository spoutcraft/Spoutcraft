package net.minecraft.src;

import java.util.Random;

class StructureStrongholdStones extends StructurePieceBlockSelector {
	private StructureStrongholdStones() {
	}

	public void selectBlocks(Random random, int i, int j, int k, boolean flag) {
		if (!flag) {
			selectedBlockId = 0;
			selectedBlockMetaData = 0;
		}
		else {
			selectedBlockId = Block.stoneBrick.blockID;
			float f = random.nextFloat();
			if (f < 0.2F) {
				selectedBlockMetaData = 2;
			}
			else if (f < 0.5F) {
				selectedBlockMetaData = 1;
			}
			else if (f < 0.55F) {
				selectedBlockId = Block.silverfish.blockID;
				selectedBlockMetaData = 2;
			}
			else {
				selectedBlockMetaData = 0;
			}
		}
	}

	StructureStrongholdStones(StructureStrongholdPieceWeight2 structurestrongholdpieceweight2) {
		this();
	}
}
