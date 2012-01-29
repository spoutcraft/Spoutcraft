package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHouse4_Garden extends ComponentVillage {
	private int averageGroundLevel;
	private final boolean field_35083_b;

	public ComponentVillageHouse4_Garden(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
		field_35083_b = random.nextBoolean();
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageHouse4_Garden func_35082_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 5, 6, 5, l);
		if (StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageHouse4_Garden(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, structureboundingbox);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 6) - 1, 0);
		}
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 4, 1, 3, 4, 3, Block.planks.blockID, Block.planks.blockID, false);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 3, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 3, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 1, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 3, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 1, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 3, 4, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 4, 3, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 1, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 1, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 1, 3, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 2, 3, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 3, 3, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 3, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 3, 1, 0, structureboundingbox);
		if (getBlockIdAtCurrentPosition(world, 2, 0, -1, structureboundingbox) == 0 && getBlockIdAtCurrentPosition(world, 2, -1, -1, structureboundingbox) != 0) {
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureboundingbox);
		}
		fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 3, 0, 0, false);
		if (field_35083_b) {
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 0, 5, 0, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 1, 5, 0, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 2, 5, 0, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 3, 5, 0, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 4, 5, 0, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 0, 5, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 1, 5, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 2, 5, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 3, 5, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 4, 5, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 4, 5, 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 4, 5, 2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 4, 5, 3, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 0, 5, 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 0, 5, 2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 0, 5, 3, structureboundingbox);
		}
		if (field_35083_b) {
			int i = getMetadataWithOffset(Block.ladder.blockID, 3);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, i, 3, 1, 3, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, i, 3, 2, 3, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, i, 3, 3, 3, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, i, 3, 4, 3, structureboundingbox);
		}
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 2, 3, 1, structureboundingbox);
		for (int j = 0; j < 5; j++) {
			for (int k = 0; k < 5; k++) {
				clearCurrentPositionBlocksUpwards(world, k, 6, j, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, 0, k, -1, j, structureboundingbox);
			}
		}

		spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
		return true;
	}
}
