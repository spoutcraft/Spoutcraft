package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageChurch extends ComponentVillage {
	private int averageGroundLevel;

	public ComponentVillageChurch(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageChurch func_35097_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 5, 12, 9, l);
		if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageChurch(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, structureboundingbox);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 12) - 1, 0);
		}
		fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 7, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 9, 3, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 0, 3, 0, 8, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 0, 3, 10, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 10, 3, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 10, 3, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 4, 0, 4, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 0, 4, 4, 4, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 8, 3, 4, 8, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 5, 4, 3, 10, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 5, 5, 3, 5, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 9, 0, 4, 9, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 0, 11, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 11, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 2, 11, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 2, 11, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 1, 1, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 1, 1, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 2, 1, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 3, 1, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 3, 1, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 1, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 1, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 3, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 1), 1, 2, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 0), 3, 2, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 3, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 3, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 6, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 7, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 6, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 7, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 6, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 7, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 6, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 7, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 3, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 3, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 3, 8, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 2, 4, 7, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 1, 4, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 3, 4, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 2, 4, 5, structureboundingbox);
		int i = getMetadataWithOffset(Block.ladder.blockID, 4);
		for (int j = 1; j <= 9; j++) {
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, i, 3, j, 3, structureboundingbox);
		}

		placeBlockAtCurrentPosition(world, 0, 0, 2, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, 0, 0, 2, 2, 0, structureboundingbox);
		placeDoorAtCurrentPosition(world, structureboundingbox, random, 2, 1, 0, getMetadataWithOffset(Block.doorWood.blockID, 1));
		if (getBlockIdAtCurrentPosition(world, 2, 0, -1, structureboundingbox) == 0 && getBlockIdAtCurrentPosition(world, 2, -1, -1, structureboundingbox) != 0) {
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureboundingbox);
		}
		for (int k = 0; k < 9; k++) {
			for (int l = 0; l < 5; l++) {
				clearCurrentPositionBlocksUpwards(world, l, 12, k, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, 0, l, -1, k, structureboundingbox);
			}
		}

		spawnVillagers(world, structureboundingbox, 2, 1, 2, 1);
		return true;
	}

	protected int getVillagerType(int i) {
		return 2;
	}
}
