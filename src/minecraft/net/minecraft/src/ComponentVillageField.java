package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageField extends ComponentVillage {
	private int averageGroundLevel;

	public ComponentVillageField(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageField func_35080_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 13, 4, 9, l);
		if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageField(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, structureboundingbox);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 4) - 1, 0);
		}
		fillWithBlocks(world, structureboundingbox, 0, 1, 0, 12, 4, 8, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		fillWithBlocks(world, structureboundingbox, 7, 0, 1, 8, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		fillWithBlocks(world, structureboundingbox, 10, 0, 1, 11, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 12, 0, 0, 12, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 0, 11, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 0, 8, 11, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
		fillWithBlocks(world, structureboundingbox, 9, 0, 1, 9, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
		for (int i = 1; i <= 7; i++) {
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 1, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 2, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 4, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 5, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 7, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 8, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 10, 1, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random, 2, 7), 11, 1, i, structureboundingbox);
		}

		for (int j = 0; j < 9; j++) {
			for (int k = 0; k < 13; k++) {
				clearCurrentPositionBlocksUpwards(world, k, 4, j, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.dirt.blockID, 0, k, -1, j, structureboundingbox);
			}
		}

		return true;
	}
}
