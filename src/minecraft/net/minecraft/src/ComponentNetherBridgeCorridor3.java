package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCorridor3 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCorridor3(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_40022_a((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 1, 0, true);
	}

	public static ComponentNetherBridgeCorridor3 func_40042_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -7, 0, 5, 14, 10, l);
		if (!func_40021_a(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentNetherBridgeCorridor3(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		int i = getMetadataWithOffset(Block.stairsNetherBrick.blockID, 2);
		for (int j = 0; j <= 9; j++) {
			int k = Math.max(1, 7 - j);
			int l = Math.min(Math.max(k + 5, 14 - j), 13);
			int i1 = j;
			fillWithBlocks(world, structureboundingbox, 0, 0, i1, 4, k, i1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			fillWithBlocks(world, structureboundingbox, 1, k + 1, i1, 3, l - 1, i1, 0, 0, false);
			if (j <= 6) {
				placeBlockAtCurrentPosition(world, Block.stairsNetherBrick.blockID, i, 1, k + 1, i1, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairsNetherBrick.blockID, i, 2, k + 1, i1, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairsNetherBrick.blockID, i, 3, k + 1, i1, structureboundingbox);
			}
			fillWithBlocks(world, structureboundingbox, 0, l, i1, 4, l, i1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			fillWithBlocks(world, structureboundingbox, 0, k + 1, i1, 0, l - 1, i1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			fillWithBlocks(world, structureboundingbox, 4, k + 1, i1, 4, l - 1, i1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			if ((j & 1) == 0) {
				fillWithBlocks(world, structureboundingbox, 0, k + 2, i1, 0, k + 3, i1, Block.netherFence.blockID, Block.netherFence.blockID, false);
				fillWithBlocks(world, structureboundingbox, 4, k + 2, i1, 4, k + 3, i1, Block.netherFence.blockID, Block.netherFence.blockID, false);
			}
			for (int j1 = 0; j1 <= 4; j1++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, j1, -1, i1, structureboundingbox);
			}
		}

		return true;
	}
}
