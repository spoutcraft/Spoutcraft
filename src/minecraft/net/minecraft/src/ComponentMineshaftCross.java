package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentMineshaftCross extends StructureComponent {
	private final int field_35073_a;
	private final boolean field_35072_b;

	public ComponentMineshaftCross(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		field_35073_a = j;
		boundingBox = structureboundingbox;
		field_35072_b = structureboundingbox.getYSize() > 3;
	}

	public static StructureBoundingBox func_35071_a(List list, Random random, int i, int j, int k, int l) {
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);
		if (random.nextInt(4) == 0) {
			structureboundingbox.maxY += 4;
		}
		switch (l) {
			case 2:
				structureboundingbox.minX = i - 1;
				structureboundingbox.maxX = i + 3;
				structureboundingbox.minZ = k - 4;
				break;

			case 0:
				structureboundingbox.minX = i - 1;
				structureboundingbox.maxX = i + 3;
				structureboundingbox.maxZ = k + 4;
				break;

			case 1:
				structureboundingbox.minX = i - 4;
				structureboundingbox.minZ = k - 1;
				structureboundingbox.maxZ = k + 3;
				break;

			case 3:
				structureboundingbox.maxX = i + 4;
				structureboundingbox.minZ = k - 1;
				structureboundingbox.maxZ = k + 3;
				break;
		}
		if (StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return structureboundingbox;
		}
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		int i = getComponentType();
		switch (field_35073_a) {
			case 2:
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
				break;

			case 0:
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
				break;

			case 1:
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				break;

			case 3:
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
				break;
		}
		if (field_35072_b) {
			if (random.nextBoolean()) {
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ - 1, 2, i);
			}
			if (random.nextBoolean()) {
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 1, i);
			}
			if (random.nextBoolean()) {
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 3, i);
			}
			if (random.nextBoolean()) {
				StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.maxZ + 1, 0, i);
			}
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		if (field_35072_b) {
			fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ, boundingBox.maxX - 1, (boundingBox.minY + 3) - 1, boundingBox.maxZ, 0, 0, false);
			fillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX, (boundingBox.minY + 3) - 1, boundingBox.maxZ - 1, 0, 0, false);
			fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.maxY - 2, boundingBox.minZ, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, 0, 0, false);
			fillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.maxY - 2, boundingBox.minZ + 1, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, 0, 0, false);
			fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.minY + 3, boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.minY + 3, boundingBox.maxZ - 1, 0, 0, false);
		}
		else {
			fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, 0, 0, false);
			fillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, 0, 0, false);
		}
		fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.minZ + 1, Block.planks.blockID, 0, false);
		fillWithBlocks(world, structureboundingbox, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.maxZ - 1, Block.planks.blockID, 0, false);
		fillWithBlocks(world, structureboundingbox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.minZ + 1, Block.planks.blockID, 0, false);
		fillWithBlocks(world, structureboundingbox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ - 1, Block.planks.blockID, 0, false);
		return true;
	}
}
