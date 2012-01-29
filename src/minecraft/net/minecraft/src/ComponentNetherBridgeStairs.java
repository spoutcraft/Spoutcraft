package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeStairs extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeStairs(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_40016_c((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 6, 2, false);
	}

	public static ComponentNetherBridgeStairs func_40031_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -2, 0, 0, 7, 11, 7, l);
		if (!func_40021_a(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentNetherBridgeStairs(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 10, 6, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 8, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 8, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 8, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 2, 1, 6, 8, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 2, 6, 5, 8, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 3, 2, 0, 5, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 3, 2, 6, 5, 2, Block.netherFence.blockID, Block.netherFence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 3, 4, 6, 5, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		placeBlockAtCurrentPosition(world, Block.netherBrick.blockID, 0, 5, 2, 5, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 4, 2, 5, 4, 3, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 2, 5, 3, 4, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 2, 5, 2, 5, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 2, 5, 1, 6, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 7, 1, 5, 7, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 8, 2, 6, 8, 4, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 8, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
		for (int i = 0; i <= 6; i++) {
			for (int j = 0; j <= 6; j++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, i, -1, j, structureboundingbox);
			}
		}

		return true;
	}
}
