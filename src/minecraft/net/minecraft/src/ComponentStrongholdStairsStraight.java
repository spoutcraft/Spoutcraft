package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStairsStraight extends ComponentStronghold {
	private final EnumDoor field_35054_a;

	public ComponentStrongholdStairsStraight(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		field_35054_a = getRandomDoor(random);
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_35028_a((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 1);
	}

	public static ComponentStrongholdStairsStraight func_35053_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -7, 0, 5, 11, 8, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentStrongholdStairsStraight(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 10, 7, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35054_a, 1, 7, 0);
		placeDoor(world, random, structureboundingbox, EnumDoor.OPENING, 1, 1, 7);
		int i = getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 2);
		for (int j = 0; j < 6; j++) {
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, i, 1, 6 - j, 1 + j, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, i, 2, 6 - j, 1 + j, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, i, 3, 6 - j, 1 + j, structureboundingbox);
			if (j < 5) {
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 1, 5 - j, 1 + j, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 2, 5 - j, 1 + j, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3, 5 - j, 1 + j, structureboundingbox);
			}
		}

		return true;
	}
}
