package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStraight extends ComponentStronghold {
	private final EnumDoor field_35050_a;
	private final boolean field_35048_b;
	private final boolean field_35049_c;

	public ComponentStrongholdStraight(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		field_35050_a = getRandomDoor(random);
		boundingBox = structureboundingbox;
		field_35048_b = random.nextInt(2) == 0;
		field_35049_c = random.nextInt(2) == 0;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_35028_a((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 1);
		if (field_35048_b) {
			func_35032_b((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 2);
		}
		if (field_35049_c) {
			func_35029_c((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 2);
		}
	}

	public static ComponentStrongholdStraight func_35047_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 7, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentStrongholdStraight(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35050_a, 1, 1, 0);
		placeDoor(world, random, structureboundingbox, EnumDoor.OPENING, 1, 1, 6);
		randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 1, Block.torchWood.blockID, 0);
		randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 1, Block.torchWood.blockID, 0);
		randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 5, Block.torchWood.blockID, 0);
		randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 5, Block.torchWood.blockID, 0);
		if (field_35048_b) {
			fillWithBlocks(world, structureboundingbox, 0, 1, 2, 0, 3, 4, 0, 0, false);
		}
		if (field_35049_c) {
			fillWithBlocks(world, structureboundingbox, 4, 1, 2, 4, 3, 4, 0, 0, false);
		}
		return true;
	}
}
