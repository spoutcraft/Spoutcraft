package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHouse2 extends ComponentVillage {
	private static final StructurePieceTreasure field_46002_a[];
	private int averageGroundLevel;
	private boolean field_46001_c;

	public ComponentVillageHouse2(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		averageGroundLevel = -1;
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public static ComponentVillageHouse2 func_35085_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 10, 6, 7, l);
		if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentVillageHouse2(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, structureboundingbox);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 6) - 1, 0);
		}
		fillWithBlocks(world, structureboundingbox, 0, 1, 0, 9, 4, 6, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 9, 0, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 4, 0, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 5, 0, 9, 5, 6, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 5, 1, 8, 5, 5, 0, 0, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 6, 0, 4, 6, Block.wood.blockID, Block.wood.blockID, false);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 3, 3, 1, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 3, 2, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 4, 1, 3, 5, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 1, 1, 6, 5, 3, 6, Block.planks.blockID, Block.planks.blockID, false);
		fillWithBlocks(world, structureboundingbox, 5, 1, 0, 5, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 9, 1, 0, 9, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
		fillWithBlocks(world, structureboundingbox, 6, 1, 4, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		placeBlockAtCurrentPosition(world, Block.lavaMoving.blockID, 0, 7, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.lavaMoving.blockID, 0, 8, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, 9, 2, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.fenceIron.blockID, 0, 9, 2, 4, structureboundingbox);
		fillWithBlocks(world, structureboundingbox, 7, 2, 4, 8, 2, 5, 0, 0, false);
		placeBlockAtCurrentPosition(world, Block.cobblestone.blockID, 0, 6, 1, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stoneOvenIdle.blockID, 0, 6, 2, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stoneOvenIdle.blockID, 0, 6, 3, 3, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairDouble.blockID, 0, 8, 1, 1, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 2, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 0, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 2, 2, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.thinGlass.blockID, 0, 4, 2, 6, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.fence.blockID, 0, 2, 1, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.pressurePlatePlanks.blockID, 0, 2, 2, 4, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.planks.blockID, 0, 1, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3), 2, 1, 5, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.stairCompactPlanks.blockID, getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1), 1, 1, 4, structureboundingbox);
		if (!field_46001_c) {
			int i = getYWithOffset(1);
			int l = getXWithOffset(5, 5);
			int j1 = getZWithOffset(5, 5);
			if (structureboundingbox.isVecInside(l, i, j1)) {
				field_46001_c = true;
				createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 5, 1, 5, field_46002_a, 3 + random.nextInt(6));
			}
		}
		for (int j = 6; j <= 8; j++) {
			if (getBlockIdAtCurrentPosition(world, j, 0, -1, structureboundingbox) == 0 && getBlockIdAtCurrentPosition(world, j, -1, -1, structureboundingbox) != 0) {
				placeBlockAtCurrentPosition(world, Block.stairCompactCobblestone.blockID, getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), j, 0, -1, structureboundingbox);
			}
		}

		for (int k = 0; k < 7; k++) {
			for (int i1 = 0; i1 < 10; i1++) {
				clearCurrentPositionBlocksUpwards(world, i1, 6, k, structureboundingbox);
				fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, 0, i1, -1, k, structureboundingbox);
			}
		}

		spawnVillagers(world, structureboundingbox, 7, 1, 1, 1);
		return true;
	}

	protected int getVillagerType(int i) {
		return 3;
	}

	static {
		field_46002_a = (new StructurePieceTreasure[] {
		            new StructurePieceTreasure(Item.diamond.shiftedIndex, 0, 1, 3, 3), new StructurePieceTreasure(Item.ingotIron.shiftedIndex, 0, 1, 5, 10), new StructurePieceTreasure(Item.ingotGold.shiftedIndex, 0, 1, 3, 5), new StructurePieceTreasure(Item.bread.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.appleRed.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.pickaxeSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.swordSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.plateSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.helmetSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.legsSteel.shiftedIndex, 0, 1, 1, 5),
		            new StructurePieceTreasure(Item.bootsSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Block.obsidian.blockID, 0, 3, 7, 5), new StructurePieceTreasure(Block.sapling.blockID, 0, 3, 7, 5)
		        });
	}
}
