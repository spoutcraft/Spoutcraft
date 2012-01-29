package net.minecraft.src;

import java.util.List;
import java.util.Random;

abstract class ComponentStronghold extends StructureComponent {
	protected ComponentStronghold(int i) {
		super(i);
	}

	protected void placeDoor(World world, Random random, StructureBoundingBox structureboundingbox, EnumDoor enumdoor, int i, int j, int k) {
		switch (EnumDoorHelper.doorEnum[enumdoor.ordinal()]) {
			case 1:
			default:
				fillWithBlocks(world, structureboundingbox, i, j, k, (i + 3) - 1, (j + 3) - 1, k, 0, 0, false);
				break;

			case 2:
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 1, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.doorWood.blockID, 0, i + 1, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.doorWood.blockID, 8, i + 1, j + 1, k, structureboundingbox);
				break;

			case 3:
				placeBlockAtCurrentPosition(world, 0, 0, i + 1, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, 0, 0, i + 1, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i + 1, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i + 2, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i + 2, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, i + 2, j, k, structureboundingbox);
				break;

			case 4:
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 1, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j + 2, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, i + 2, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.doorSteel.blockID, 0, i + 1, j, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.doorSteel.blockID, 8, i + 1, j + 1, k, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.button.blockID, getMetadataWithOffset(Block.button.blockID, 4), i + 2, j + 1, k + 1, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.button.blockID, getMetadataWithOffset(Block.button.blockID, 3), i + 2, j + 1, k - 1, structureboundingbox);
				break;
		}
	}

	protected EnumDoor getRandomDoor(Random random) {
		int i = random.nextInt(5);
		switch (i) {
			case 0:
			case 1:
			default:
				return EnumDoor.OPENING;

			case 2:
				return EnumDoor.WOOD_DOOR;

			case 3:
				return EnumDoor.GRATES;

			case 4:
				return EnumDoor.IRON_DOOR;
		}
	}

	protected StructureComponent func_35028_a(ComponentStrongholdStairs2 componentstrongholdstairs2, List list, Random random, int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + i, boundingBox.minY + j, boundingBox.minZ - 1, coordBaseMode, getComponentType());

			case 0:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + i, boundingBox.minY + j, boundingBox.maxZ + 1, coordBaseMode, getComponentType());

			case 1:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX - 1, boundingBox.minY + j, boundingBox.minZ + i, coordBaseMode, getComponentType());

			case 3:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.maxX + 1, boundingBox.minY + j, boundingBox.minZ + i, coordBaseMode, getComponentType());
		}
		return null;
	}

	protected StructureComponent func_35032_b(ComponentStrongholdStairs2 componentstrongholdstairs2, List list, Random random, int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType());

			case 0:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType());

			case 1:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType());

			case 3:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType());
		}
		return null;
	}

	protected StructureComponent func_35029_c(ComponentStrongholdStairs2 componentstrongholdstairs2, List list, Random random, int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType());

			case 0:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType());

			case 1:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType());

			case 3:
				return StructureStrongholdPieces.func_35850_a(componentstrongholdstairs2, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType());
		}
		return null;
	}

	protected static boolean canStrongholdGoDeeper(StructureBoundingBox structureboundingbox) {
		return structureboundingbox != null && structureboundingbox.minY > 10;
	}
}
