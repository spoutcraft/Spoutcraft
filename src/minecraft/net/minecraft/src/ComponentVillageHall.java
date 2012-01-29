package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHall extends ComponentVillage {
	private int averageGroundLevel;

	public ComponentVillageHall(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageHall func_35078_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 9, 7, 11, l);
		if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageHall(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, structureboundingbox);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 7) - 1, 0);
		}
		fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 4, 4, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 2, 1, 6, 8, 4, 10, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 2, 0, 6, 8, 0, 10, Block.dirt.blockID, Block.dirt.blockID, false);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, 0, 6, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 2, 1, 6, 2, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 8, 1, 6, 8, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 1, 10, 7, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 1, 7, 0, 4, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 8, 0, 0, 8, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 0, 7, 1, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 5, 7, 1, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 2, 5, 7, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 4, 1, 8, 4, 1, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 4, 4, 8, 4, 4, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 2, 8, 5, 3, Block.planks.blockID, Block.planks.blockID, false);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 0, 4, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 0, 4, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 8, 4, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 8, 4, 3, structureboundingbox);
		int i = getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3);
		int j = getMetadataWithOffset(Block.stairCompactPlanks.blockID, 2);
		for (int k = -1; k <= 2; k++) {
			for (int i1 = 0; i1 <= 8; i1++) {
				placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, i, i1, 4 + k, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, j, i1, 4 + k, 5 - k, structureboundingbox);
			}
		}

		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 0, 2, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 0, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 8, 2, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 8, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 8, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 8, 2, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 2, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 3, 2, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 5, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 6, 2, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 2, 1, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.pressurePlatePlanks.blockID, 0, 2, 2, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 1, 1, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3), 2, 1, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1), 1, 1, 3, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 5, 0, 1, 7, 0, 3, Block.stairDouble.blockID, Block.stairDouble.blockID, false);
		placeBlockAtCurrentPosition(world, Block.stairDouble.blockID, 0, 6, 1, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairDouble.blockID, 0, 6, 1, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, 0, 0, 2, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, 0, 0, 2, 2, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 2, 3, 1, structureboundingbox);
		placeDoorAtCurrentPosition(world, structureboundingbox, random, 2, 1, 0, getMetadataWithOffset(Block.doorWood.blockID, 1));
		if (getBlockIdAtCurrentPosition(world, 2, 0, -1, structureboundingbox) == 0 && getBlockIdAtCurrentPosition(world, 2, -1, -1, structureboundingbox) != 0) {
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureboundingbox);
		}
		placeBlockAtCurrentPosition(world, 0, 0, 6, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, 0, 0, 6, 2, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 6, 3, 4, structureboundingbox);
		placeDoorAtCurrentPosition(world, structureboundingbox, random, 6, 1, 5, getMetadataWithOffset(Block.doorWood.blockID, 1));
		for (int l = 0; l < 5; l++) {
			for (int j1 = 0; j1 < 9; j1++) {
				clearCurrentPositionBlocksUpwards(world, j1, 7, l, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, 0, j1, -1, l, structureboundingbox);
			}
		}

		spawnVillagers(world, structureboundingbox, 4, 1, 2, 2);
		return true;
	}

	protected int getVillagerType(int i) {
		return i != 0 ? 0 : 4;
	}
}
