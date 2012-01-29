package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCrossing extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCrossing(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_40022_a((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 2, 0, false);
		func_40019_b((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 0, 2, false);
		func_40016_c((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 0, 2, false);
	}

	public static ComponentNetherBridgeCrossing func_40028_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -2, 0, 0, 7, 9, 7, l);
		if (!func_40021_a(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentNetherBridgeCrossing(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 7, 6, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 6, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 6, 1, 6, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 6, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 2, 6, 6, 6, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 6, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 6, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 2, 0, 6, 6, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 2, 5, 6, 6, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 6, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 6, 6, 4, 6, 6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 2, 5, 6, 4, 5, 6, Block.netherFence.blockID, Block.netherFence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 6, 2, 0, 6, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 2, 0, 5, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 6, 2, 6, 6, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 5, 2, 6, 5, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		for (int i = 0; i <= 6; i++) {
			for (int j = 0; j <= 6; j++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, i, -1, j, structureboundingbox);
			}
		}

		return true;
	}
}
