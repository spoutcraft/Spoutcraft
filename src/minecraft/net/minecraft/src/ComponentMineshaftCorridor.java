package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentMineshaftCorridor extends StructureComponent {
	private final boolean field_35070_a;
	private final boolean field_35068_b;
	private boolean field_35069_c;
	private int field_35067_d;

	public ComponentMineshaftCorridor(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
		field_35070_a = random.nextInt(3) == 0;
		field_35068_b = !field_35070_a && random.nextInt(23) == 0;
		if (coordBaseMode == 2 || coordBaseMode == 0) {
			field_35067_d = structureboundingbox.getZSize() / 5;
		}
		else {
			field_35067_d = structureboundingbox.getXSize() / 5;
		}
	}

	public static StructureBoundingBox func_35066_a(List list, Random random, int i, int j, int k, int l) {
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);
		int i1 = random.nextInt(3) + 2;
		do {
			if (i1 <= 0) {
				break;
			}
			int j1 = i1 * 5;
			switch (l) {
				case 2:
					structureboundingbox.maxX = i + 2;
					structureboundingbox.minZ = k - (j1 - 1);
					break;

				case 0:
					structureboundingbox.maxX = i + 2;
					structureboundingbox.maxZ = k + (j1 - 1);
					break;

				case 1:
					structureboundingbox.minX = i - (j1 - 1);
					structureboundingbox.maxZ = k + 2;
					break;

				case 3:
					structureboundingbox.maxX = i + (j1 - 1);
					structureboundingbox.maxZ = k + 2;
					break;
			}
			if (StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) == null) {
				break;
			}
			i1--;
		}
		while (true);
		if (i1 > 0) {
			return structureboundingbox;
		}
		else {
			return null;
		}
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		int i = getComponentType();
		int j = random.nextInt(4);
		switch (coordBaseMode) {
			case 2:
				if (j <= 1) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ - 1, coordBaseMode, i);
				}
				else if (j == 2) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ, 1, i);
				}
				else {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ, 3, i);
				}
				break;

			case 0:
				if (j <= 1) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.maxZ + 1, coordBaseMode, i);
				}
				else if (j == 2) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.maxZ - 3, 1, i);
				}
				else {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.maxZ - 3, 3, i);
				}
				break;

			case 1:
				if (j <= 1) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
				}
				else if (j == 2) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ - 1, 2, i);
				}
				else {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.maxZ + 1, 0, i);
				}
				break;

			case 3:
				if (j <= 1) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
				}
				else if (j == 2) {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX - 3, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.minZ - 1, 2, i);
				}
				else {
					StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX - 3, (boundingBox.minY - 1) + random.nextInt(3), boundingBox.maxZ + 1, 0, i);
				}
				break;
		}
		if (i < 8) {
			if (coordBaseMode == 2 || coordBaseMode == 0) {
				for (int k = boundingBox.minZ + 3; k + 3 <= boundingBox.maxZ; k += 5) {
					int i1 = random.nextInt(5);
					if (i1 == 0) {
						StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY, k, 1, i + 1);
					}
					else if (i1 == 1) {
						StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY, k, 3, i + 1);
					}
				}
			}
			else {
				for (int l = boundingBox.minX + 3; l + 3 <= boundingBox.maxX; l += 5) {
					int j1 = random.nextInt(5);
					if (j1 == 0) {
						StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, l, boundingBox.minY, boundingBox.minZ - 1, 2, i + 1);
						continue;
					}
					if (j1 == 1) {
						StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, l, boundingBox.minY, boundingBox.maxZ + 1, 0, i + 1);
					}
				}
			}
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		int i = field_35067_d * 5 - 1;
		fillWithBlocks(world, structureboundingbox, 0, 0, 0, 2, 1, i, 0, 0, false);
		randomlyFillWithBlocks(world, structureboundingbox, random, 0.8F, 0, 2, 0, 2, 2, i, 0, 0, false);
		if (field_35068_b) {
			randomlyFillWithBlocks(world, structureboundingbox, random, 0.6F, 0, 0, 0, 2, 1, i, Block.web.blockID, 0, false);
		}
		for (int j = 0; j < field_35067_d; j++) {
			int l = 2 + j * 5;
			fillWithBlocks(world, structureboundingbox, 0, 0, l, 0, 1, l, Block.fence.blockID, 0, false);
			fillWithBlocks(world, structureboundingbox, 2, 0, l, 2, 1, l, Block.fence.blockID, 0, false);
			if (random.nextInt(4) != 0) {
				fillWithBlocks(world, structureboundingbox, 0, 2, l, 2, 2, l, Block.planks.blockID, 0, false);
			}
			else {
				fillWithBlocks(world, structureboundingbox, 0, 2, l, 0, 2, l, Block.planks.blockID, 0, false);
				fillWithBlocks(world, structureboundingbox, 2, 2, l, 2, 2, l, Block.planks.blockID, 0, false);
			}
			randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 0, 2, l - 1, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 2, 2, l - 1, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 0, 2, l + 1, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 2, 2, l + 1, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 0, 2, l - 2, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 2, 2, l - 2, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 0, 2, l + 2, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 2, 2, l + 2, Block.web.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 1, 2, l - 1, Block.torchWood.blockID, 0);
			randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, 1, 2, l + 1, Block.torchWood.blockID, 0);
			if (random.nextInt(100) == 0) {
				createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 2, 0, l - 1, StructureMineshaftPieces.getTreasurePieces(), 3 + random.nextInt(4));
			}
			if (random.nextInt(100) == 0) {
				createTreasureChestAtCurrentPosition(world, structureboundingbox, random, 0, 0, l + 1, StructureMineshaftPieces.getTreasurePieces(), 3 + random.nextInt(4));
			}
			if (!field_35068_b || field_35069_c) {
				continue;
			}
			int j1 = getYWithOffset(0);
			int k1 = (l - 1) + random.nextInt(3);
			int l1 = getXWithOffset(1, k1);
			k1 = getZWithOffset(1, k1);
			if (!structureboundingbox.isVecInside(l1, j1, k1)) {
				continue;
			}
			field_35069_c = true;
			world.setBlockWithNotify(l1, j1, k1, Block.mobSpawner.blockID);
			TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getBlockTileEntity(l1, j1, k1);
			if (tileentitymobspawner != null) {
				tileentitymobspawner.setMobID("CaveSpider");
			}
		}

		if (field_35070_a) {
			for (int k = 0; k <= i; k++) {
				int i1 = getBlockIdAtCurrentPosition(world, 1, -1, k, structureboundingbox);
				if (i1 > 0 && Block.opaqueCubeLookup[i1]) {
					randomlyPlaceBlock(world, structureboundingbox, random, 0.7F, 1, 0, k, Block.rail.blockID, getMetadataWithOffset(Block.rail.blockID, 0));
				}
			}
		}
		return true;
	}
}
