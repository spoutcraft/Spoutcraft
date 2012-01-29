package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCrossing3 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCrossing3(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	protected ComponentNetherBridgeCrossing3(Random random, int i, int j) {
		super(0);
		coordBaseMode = random.nextInt(4);
		switch (coordBaseMode) {
			case 0:
			case 2:
				boundingBox = new StructureBoundingBox(i, 64, j, (i + 19) - 1, 73, (j + 19) - 1);
				break;

			default:
				boundingBox = new StructureBoundingBox(i, 64, j, (i + 19) - 1, 73, (j + 19) - 1);
				break;
		}
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_40022_a((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 8, 3, false);
		func_40019_b((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 3, 8, false);
		func_40016_c((ComponentNetherBridgeStartPiece)structurecomponent, list, random, 3, 8, false);
	}

	public static ComponentNetherBridgeCrossing3 func_40033_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -8, -3, 0, 19, 10, 19, l);
		if (!func_40021_a(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentNetherBridgeCrossing3(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		fillWithBlocks(world, structureboundingbox, 7, 3, 0, 11, 4, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 3, 7, 18, 4, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 7, 18, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 8, 18, 7, 10, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 7, 5, 0, 7, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 5, 11, 7, 5, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 11, 5, 0, 11, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 11, 5, 11, 11, 5, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 7, 7, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 11, 5, 7, 18, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 11, 7, 5, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 11, 5, 11, 18, 5, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 2, 0, 11, 2, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 2, 13, 11, 2, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 0, 0, 11, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 0, 15, 11, 1, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		for (int i = 7; i <= 11; i++) {
			for (int k = 0; k <= 2; k++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, i, -1, k, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, i, -1, 18 - k, structureboundingbox);
			}
		}

		fillWithBlocks(world, structureboundingbox, 0, 2, 7, 5, 2, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 13, 2, 7, 18, 2, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 7, 3, 1, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		fillWithBlocks(world, structureboundingbox, 15, 0, 7, 18, 1, 11, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		for (int j = 0; j <= 2; j++) {
			for (int l = 7; l <= 11; l++) {
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, j, -1, l, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.netherBrick.blockID, 0, 18 - j, -1, l, structureboundingbox);
			}
		}

		return true;
	}
}
