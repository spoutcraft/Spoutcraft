package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdRoomCrossing extends ComponentStronghold {
	private static final StructurePieceTreasure field_35061_c[];
	protected final EnumDoor field_35062_a;
	protected final int field_35060_b;

	public ComponentStrongholdRoomCrossing(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		field_35062_a = getRandomDoor(random);
		boundingBox = structureboundingbox;
		field_35060_b = random.nextInt(5);
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		func_35028_a((ComponentStrongholdStairs2)structurecomponent, list, random, 4, 1);
		func_35032_b((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 4);
		func_35029_c((ComponentStrongholdStairs2)structurecomponent, list, random, 1, 4);
	}

	public static ComponentStrongholdRoomCrossing func_35059_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 11, 7, 11, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentStrongholdRoomCrossing(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 6, 10, true, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, field_35062_a, 4, 1, 0);
		fillWithBlocks(world, structureboundingbox, 4, 1, 10, 6, 3, 10, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 6, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 10, 1, 4, 10, 3, 6, 0, 0, false);
		switch (field_35060_b) {
			default:
				break;

			case 0:
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 2, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 3, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 4, 3, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 6, 3, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 5, 3, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 5, 3, 6, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 4, 1, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 4, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 4, 1, 6, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 6, 1, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 6, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 6, 1, 6, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 5, 1, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stairSingle.blockID, 0, 5, 1, 6, structureboundingbox);
				break;

			case 1:
				for (int i = 0; i < 5; i++) {
					placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3, 1, 3 + i, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 7, 1, 3 + i, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3 + i, 1, 3, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 3 + i, 1, 7, structureboundingbox);
				}

				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 2, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.stoneBrick.blockID, 0, 5, 3, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.waterMoving.blockID, 0, 5, 4, 5, structureboundingbox);
				break;

			case 2:
				for (int j = 1; j <= 9; j++) {
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 1, 3, j, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 9, 3, j, structureboundingbox);
				}

				for (int k = 1; k <= 9; k++) {
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, k, 3, 1, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, k, 3, 9, structureboundingbox);
				}

				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 5, 1, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 5, 1, 6, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 5, 3, 4, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 5, 3, 6, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, 1, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, 3, 5, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, 3, 5, structureboundingbox);
				for (int l = 1; l <= 3; l++) {
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, l, 4, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, l, 4, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 4, l, 6, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, l, 6, structureboundingbox);
				}

				placeBlockAtCurrentPosition(world, Block.torchWood.blockID, 0, 5, 3, 5, structureboundingbox);
				for (int i1 = 2; i1 <= 8; i1++) {
					placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 2, 3, i1, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 3, 3, i1, structureboundingbox);
					if (i1 <= 3 || i1 >= 7) {
						placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 4, 3, i1, structureboundingbox);
						placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 5, 3, i1, structureboundingbox);
						placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 6, 3, i1, structureboundingbox);
					}
					placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 7, 3, i1, structureboundingbox);
					placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 8, 3, i1, structureboundingbox);
				}

				placeBlockAtCurrentPosition(world, Block.ladder.blockID, getMetadataWithOffset(Block.ladder.blockID, 4), 9, 1, 3, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.ladder.blockID, getMetadataWithOffset(Block.ladder.blockID, 4), 9, 2, 3, structureboundingbox);
				placeBlockAtCurrentPosition(world, Block.ladder.blockID, getMetadataWithOffset(Block.ladder.blockID, 4), 9, 3, 3, structureboundingbox);
				createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 3, 4, 8, field_35061_c, 1 + random.nextInt(4));
				break;
		}
		return true;
	}

	static {
		field_35061_c = (new StructurePieceTreasure[] {
		            new StructurePieceTreasure(Item.ingotIron.shiftedIndex, 0, 1, 5, 10), new StructurePieceTreasure(Item.ingotGold.shiftedIndex, 0, 1, 3, 5), new StructurePieceTreasure(Item.redstone.shiftedIndex, 0, 4, 9, 5), new StructurePieceTreasure(Item.coal.shiftedIndex, 0, 3, 8, 10), new StructurePieceTreasure(Item.bread.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.appleRed.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.pickaxeSteel.shiftedIndex, 0, 1, 1, 1)
		        });
	}
}
