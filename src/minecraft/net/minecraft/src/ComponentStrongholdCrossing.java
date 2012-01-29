package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdCrossing extends ComponentStronghold {
	protected final EnumDoor field_35044_a;
	private boolean field_35042_b;
	private boolean field_35043_c;
	private boolean field_35040_d;
	private boolean field_35041_e;

	public ComponentStrongholdCrossing(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		field_35044_a = getRandomDoor(random);
		boundingBox = structureboundingbox;
		field_35042_b = random.nextBoolean();
		field_35043_c = random.nextBoolean();
		field_35040_d = random.nextBoolean();
		field_35041_e = random.nextInt(3) > 0;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		int i = 3;
		int j = 5;
		if (coordBaseMode == 1 || coordBaseMode == 2) {
			i = 8 - i;
			j = 8 - j;
		}
		func_35028_a((ComponentStrongholdStairs2)structurecomponent, list, random, 5, 1);
		if (field_35042_b) {
			func_35032_b((ComponentStrongholdStairs2)structurecomponent, list, random, i, 1);
		}
		if (field_35043_c) {
			func_35032_b((ComponentStrongholdStairs2)structurecomponent, list, random, j, 7);
		}
		if (field_35040_d) {
			func_35029_c((ComponentStrongholdStairs2)structurecomponent, list, random, i, 1);
		}
		if (field_35041_e) {
			func_35029_c((ComponentStrongholdStairs2)structurecomponent, list, random, j, 7);
		}
	}

	public static ComponentStrongholdCrossing func_35039_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -3, 0, 10, 9, 11, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentStrongholdCrossing(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 9, 8, 10, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35044_a, 4, 3, 0);
		if (field_35042_b) {
			fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 5, 3, 0, 0, false);
		}
		if (field_35040_d) {
			fillWithBlocks(world, structureboundingbox, 9, 3, 1, 9, 5, 3, 0, 0, false);
		}
		if (field_35043_c) {
			fillWithBlocks(world, structureboundingbox, 0, 5, 7, 0, 7, 9, 0, 0, false);
		}
		if (field_35041_e) {
			fillWithBlocks(world, structureboundingbox, 9, 5, 7, 9, 7, 9, 0, 0, false);
		}
		fillWithBlocks(world, structureboundingbox, 5, 1, 10, 7, 3, 10, 0, 0, false);
		fillWithRandomizedBlocks(world, structureboundingbox, 1, 2, 1, 8, 2, 6, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 4, 4, 9, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 5, 8, 4, 9, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 1, 4, 7, 3, 4, 9, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 5, 3, 3, 6, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithBlocks(world, structureboundingbox, 1, 3, 4, 3, 3, 4, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 4, 6, 3, 4, 6, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithRandomizedBlocks(world, structureboundingbox, 5, 1, 7, 7, 1, 8, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithBlocks(world, structureboundingbox, 5, 1, 9, 7, 1, 9, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 2, 7, 7, 2, 7, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 5, 7, 4, 5, 9, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 8, 5, 7, 8, 5, 9, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 5, 7, 7, 5, 9, Block.stairDouble.blockID, Block.stairDouble.blockID, false);
		placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 6, 5, 6, structureboundingbox);
		return true;
	}
}
