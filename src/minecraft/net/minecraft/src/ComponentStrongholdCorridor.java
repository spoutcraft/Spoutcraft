package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdCorridor extends ComponentStronghold {
	private final int field_35052_a;

	public ComponentStrongholdCorridor(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
		field_35052_a = j != 2 && j != 0 ? structureboundingbox.getXSize() : structureboundingbox.getZSize();
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static StructureBoundingBox func_35051_a(List list, Random random, int i, int j, int k, int l) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 4, l);
		StructureComponent structurecomponent = StructureComponent.getIntersectingStructureComponent(list, structureboundingbox);
		if (structurecomponent == null) {
			return null;
		}
		if (structurecomponent.getBoundingBox().minY == structureboundingbox.minY) {
			for (int i1 = 3; i1 >= 1; i1--) {
				StructureBoundingBox structureboundingbox1 = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, i1 - 1, l);
				if (!structurecomponent.getBoundingBox().intersectsWith(structureboundingbox1)) {
					return StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, i1, l);
				}
			}
		}
		return null;
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		for (int i = 0; i < field_35052_a; i++) {
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 0, 0, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 1, 0, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 2, 0, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3, 0, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 4, 0, i, structureboundingbox);
			for (int j = 1; j <= 3; j++) {
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 0, j, i, structureboundingbox);
				placeBlockAtCurrentPosition(world, 0, 0, 1, j, i, structureboundingbox);
				placeBlockAtCurrentPosition(world, 0, 0, 2, j, i, structureboundingbox);
				placeBlockAtCurrentPosition(world, 0, 0, 3, j, i, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 4, j, i, structureboundingbox);
			}

			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 0, 4, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 1, 4, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 2, 4, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3, 4, i, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 4, 4, i, structureboundingbox);
		}

		return true;
	}
}
