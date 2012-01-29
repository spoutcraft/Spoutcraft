package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdLibrary extends ComponentStronghold {
	private static final StructurePieceTreasure field_35056_b[];
	protected final EnumDoor field_35058_a;
	private final boolean field_35057_c;

	public ComponentStrongholdLibrary(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		field_35058_a = getRandomDoor(random);
		boundingBox = structureboundingbox;
		field_35057_c = structureboundingbox.getYSize() > 6;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentStrongholdLibrary func_35055_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 14, 11, 15, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 14, 6, 15, l);
			if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
				return null;
			}
		}
		return new ComponentStrongholdLibrary(i1, random, structureboundingbox, l);
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		byte byte0 = 11;
		if (!field_35057_c) {
			byte0 = 6;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 13, byte0 - 1, 14, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35058_a, 4, 1, 0);
		randomlyFillWithBlocks(world, structureboundingbox, random, 0.07F, 2, 1, 1, 11, 4, 13, Block.web.blockID, Block.web.blockID, false);
		for (int i = 1; i <= 13; i++) {
			if ((i - 1) % 4 == 0) {
				fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i, Block.planks.blockID, Block.planks.blockID, false);
				fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i, Block.planks.blockID, Block.planks.blockID, false);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 2, 3, i, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 11, 3, i, structureboundingbox);
				if (field_35057_c) {
					fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i, Block.planks.blockID, Block.planks.blockID, false);
					fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i, Block.planks.blockID, Block.planks.blockID, false);
				}
				continue;
			}
			fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
			fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
			if (field_35057_c) {
				fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
				fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
			}
		}

		for (int j = 3; j < 12; j += 2) {
			fillWithBlocks(world, structureboundingbox, 3, 1, j, 4, 3, j, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
			fillWithBlocks(world, structureboundingbox, 6, 1, j, 7, 3, j, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
			fillWithBlocks(world, structureboundingbox, 9, 1, j, 10, 3, j, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
		}

		if (field_35057_c) {
			fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
			fillWithBlocks(world, structureboundingbox, 10, 5, 1, 12, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
			fillWithBlocks(world, structureboundingbox, 4, 5, 1, 9, 5, 2, Block.planks.blockID, Block.planks.blockID, false);
			fillWithBlocks(world, structureboundingbox, 4, 5, 12, 9, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
			placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 9, 5, 11, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 8, 5, 11, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 9, 5, 10, structureboundingbox);
			fillWithBlocks(world, structureboundingbox, 3, 6, 2, 3, 6, 12, Block.fence.blockID, Block.fence.blockID, false);
			fillWithBlocks(world, structureboundingbox, 10, 6, 2, 10, 6, 10, Block.fence.blockID, Block.fence.blockID, false);
			fillWithBlocks(world, structureboundingbox, 4, 6, 2, 9, 6, 2, Block.fence.blockID, Block.fence.blockID, false);
			fillWithBlocks(world, structureboundingbox, 4, 6, 12, 8, 6, 12, Block.fence.blockID, Block.fence.blockID, false);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 9, 6, 11, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 8, 6, 11, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 9, 6, 10, structureboundingbox);
			int k = getMetadataWithOffset(Block.ladder.blockID, 3);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 1, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 2, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 3, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 4, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 5, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 6, 13, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.ladder.blockID, k, 10, 7, 13, structureboundingbox);
			byte byte1 = 7;
			byte byte2 = 7;
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 1, 9, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1, 9, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 1, 8, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1, 8, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 1, 7, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1, 7, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 2, 7, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 + 1, 7, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 1, 7, byte2 - 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1 - 1, 7, byte2 + 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1, 7, byte2 - 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, byte1, 7, byte2 + 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1 - 2, 8, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1 + 1, 8, byte2, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1 - 1, 8, byte2 - 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1 - 1, 8, byte2 + 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1, 8, byte2 - 1, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, byte1, 8, byte2 + 1, structureboundingbox);
		}
		createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 3, 3, 5, field_35056_b, 1 + random.nextInt(4));
		if (field_35057_c) {
			placeBlockAtCurrentPosition(world, 0, 0, 12, 9, 1, structureboundingbox);
			createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 12, 8, 1, field_35056_b, 1 + random.nextInt(4));
		}
		return true;
	}

	static {
		field_35056_b = (new StructurePieceTreasure[] {
		            new StructurePieceTreasure(Item.book.shiftedIndex, 0, 1, 3, 20), new StructurePieceTreasure(Item.paper.shiftedIndex, 0, 2, 7, 20), new StructurePieceTreasure(Item.map.shiftedIndex, 0, 1, 1, 1), new StructurePieceTreasure(Item.compass.shiftedIndex, 0, 1, 1, 1)
		        });
	}
}
