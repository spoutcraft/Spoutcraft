package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCrossing2 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCrossing2(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_40022_a((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 1, 0, true);
		func_40019_b((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 0, 1, true);
		func_40016_c((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 0, 1, true);
	}

	public static ComponentNetherBridgeCrossing2 func_40025_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, 0, 0, 5, 7, 5, l);
		if (!func_40021_a(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentNetherBridgeCrossing2(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 2, 4, 0, 5, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 2, 4, 4, 5, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		for (int i = 0; i <= 4; i++) {
			for (int j = 0; j <= 4; j++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, i, -1, j, structureboundingbox);
			}
		}

		return true;
	}
}
