package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdRightTurn extends ComponentStrongholdLeftTurn {
	public ComponentStrongholdRightTurn(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i, random, structureboundingbox, j);
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		if (coordBaseMode == 2 || coordBaseMode == 3) {
			func_35029_c((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 1);
		}
		else {
			func_35032_b((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 1);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35046_a, 1, 1, 0);
		if (coordBaseMode == 2 || coordBaseMode == 3) {
			fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, 0, 0, false);
		}
		else {
			fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, 0, 0, false);
		}
		return true;
	}
}
