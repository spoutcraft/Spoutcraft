package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageWoodHut extends ComponentVillage {
	private int averageGroundLevel;
	private final boolean field_35092_b;
	private final int field_35093_c;

	public ComponentVillageWoodHut(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
		field_35092_b = random.nextBoolean();
		field_35093_c = random.nextInt(3);
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageWoodHut func_35091_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 4, 6, 5, l);
		if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageWoodHut(i1, random, structureboundingbox, l);
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
		fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 5, 4, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 3, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 3, Block.dirt.blockID, Block.dirt.blockID, false);
		if (field_35092_b) {
			fillWithBlocks(world, structureboundingbox, 1, 4, 1, 2, 4, 3, Block.wood.blockID, Block.wood.blockID, false);
		}
		else {
			fillWithBlocks(world, structureboundingbox, 1, 5, 1, 2, 5, 3, Block.wood.blockID, Block.wood.blockID, false);
		}
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 1, 4, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 2, 4, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 1, 4, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 2, 4, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 0, 4, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 0, 4, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 0, 4, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 3, 4, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 3, 4, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.wood.blockID, 0, 3, 4, 3, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 3, 0, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 3, 0, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 4, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 1, 4, 3, 3, 4, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 1, 1, 3, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 4, 2, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 3, 2, 2, structureboundingbox);
		if (field_35093_c > 0) {
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, field_35093_c, 1, 3, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.pressurePlatePlanks.blockID, 0, field_35093_c, 2, 3, structureboundingbox);
		}
		placeBlockAtCurrentPosition(world, 0, 0, 1, 1, 0, structureboundingbox);
		placeBlockAtCurrentPosition(world, 0, 0, 1, 2, 0, structureboundingbox);
		placeDoorAtCurrentPosition(world, structureboundingbox, random, 1, 1, 0, getMetadataWithOffset(Block.doorWood.blockID, 1));
		if (getBlockIdAtCurrentPosition(world, 1, 0, -1, structureboundingbox) == 0 && getBlockIdAtCurrentPosition(world, 1, -1, -1, structureboundingbox) != 0) {
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 1, 0, -1, structureboundingbox);
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				clearCurrentPositionBlocksUpwards(world, j, 6, i, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, 0, j, -1, i, structureboundingbox);
			}
		}

		spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
		return true;
	}
}
