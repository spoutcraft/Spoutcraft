package net.minecraft.src;

import java.util.*;

public abstract class StructureComponent {
	protected StructureBoundingBox boundingBox;
	protected int coordBaseMode;
	protected int componentType;

	protected StructureComponent(int i) {
		componentType = i;
		coordBaseMode = -1;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
	}

	public abstract boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox);

	public StructureBoundingBox getBoundingBox() {
		return boundingBox;
	}

	public int getComponentType() {
		return componentType;
	}

	public static StructureComponent getIntersectingStructureComponent(List list, StructureBoundingBox structureboundingbox) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			StructureComponent structurecomponent = (StructureComponent)iterator.next();
			if (structurecomponent.getBoundingBox() != null && structurecomponent.getBoundingBox().intersectsWith(structureboundingbox)) {
				return structurecomponent;
			}
		}

		return null;
	}

	public ChunkPosition func_40008_a_() {
		return new ChunkPosition(boundingBox.func_40597_e(), boundingBox.func_40596_f(), boundingBox.func_40598_g());
	}

	protected boolean isLiquidInStructureBoundingBox(World world, StructureBoundingBox structureboundingbox) {
		int i = Math.max(boundingBox.minX - 1, structureboundingbox.minX);
		int j = Math.max(boundingBox.minY - 1, structureboundingbox.minY);
		int k = Math.max(boundingBox.minZ - 1, structureboundingbox.minZ);
		int l = Math.min(boundingBox.maxX + 1, structureboundingbox.maxX);
		int i1 = Math.min(boundingBox.maxY + 1, structureboundingbox.maxY);
		int j1 = Math.min(boundingBox.maxZ + 1, structureboundingbox.maxZ);
		for (int k1 = i; k1 <= l; k1++) {
			for (int j2 = k; j2 <= j1; j2++) {
				int i3 = world.getBlockId(k1, j, j2);
				if (i3 > 0 && Block.blocksList[i3].blockMaterial.getIsLiquid()) {
					return true;
				}
				i3 = world.getBlockId(k1, i1, j2);
				if (i3 > 0 && Block.blocksList[i3].blockMaterial.getIsLiquid()) {
					return true;
				}
			}
		}

		for (int l1 = i; l1 <= l; l1++) {
			for (int k2 = j; k2 <= i1; k2++) {
				int j3 = world.getBlockId(l1, k2, k);
				if (j3 > 0 && Block.blocksList[j3].blockMaterial.getIsLiquid()) {
					return true;
				}
				j3 = world.getBlockId(l1, k2, j1);
				if (j3 > 0 && Block.blocksList[j3].blockMaterial.getIsLiquid()) {
					return true;
				}
			}
		}

		for (int i2 = k; i2 <= j1; i2++) {
			for (int l2 = j; l2 <= i1; l2++) {
				int k3 = world.getBlockId(i, l2, i2);
				if (k3 > 0 && Block.blocksList[k3].blockMaterial.getIsLiquid()) {
					return true;
				}
				k3 = world.getBlockId(l, l2, i2);
				if (k3 > 0 && Block.blocksList[k3].blockMaterial.getIsLiquid()) {
					return true;
				}
			}
		}

		return false;
	}

	protected int getXWithOffset(int i, int j) {
		switch (coordBaseMode) {
			case 0:
			case 2:
				return boundingBox.minX + i;

			case 1:
				return boundingBox.maxX - j;

			case 3:
				return boundingBox.minX + j;
		}
		return i;
	}

	protected int getYWithOffset(int i) {
		if (coordBaseMode == -1) {
			return i;
		}
		else {
			return i + boundingBox.minY;
		}
	}

	protected int getZWithOffset(int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return boundingBox.maxZ - j;

			case 0:
				return boundingBox.minZ + j;

			case 1:
			case 3:
				return boundingBox.minZ + i;
		}
		return j;
	}

	protected int getMetadataWithOffset(int i, int j) {
		if (i == Block.rail.blockID) {
			if (coordBaseMode == 1 || coordBaseMode == 3) {
				return j != 1 ? 1 : 0;
			}
		}
		else if (i == Block.doorWood.blockID || i == Block.doorSteel.blockID) {
			if (coordBaseMode == 0) {
				if (j == 0) {
					return 2;
				}
				if (j == 2) {
					return 0;
				}
			}
			else {
				if (coordBaseMode == 1) {
					return j + 1 & 3;
				}
				if (coordBaseMode == 3) {
					return j + 3 & 3;
				}
			}
		}
		else if (i == Block.stairCompactCobblestone.blockID || i == Block.stairCompactPlanks.blockID || i == Block.stairsNetherBrick.blockID || i == Block.stairsStoneBrickSmooth.blockID) {
			if (coordBaseMode == 0) {
				if (j == 2) {
					return 3;
				}
				if (j == 3) {
					return 2;
				}
			}
			else if (coordBaseMode == 1) {
				if (j == 0) {
					return 2;
				}
				if (j == 1) {
					return 3;
				}
				if (j == 2) {
					return 0;
				}
				if (j == 3) {
					return 1;
				}
			}
			else if (coordBaseMode == 3) {
				if (j == 0) {
					return 2;
				}
				if (j == 1) {
					return 3;
				}
				if (j == 2) {
					return 1;
				}
				if (j == 3) {
					return 0;
				}
			}
		}
		else if (i == Block.ladder.blockID) {
			if (coordBaseMode == 0) {
				if (j == 2) {
					return 3;
				}
				if (j == 3) {
					return 2;
				}
			}
			else if (coordBaseMode == 1) {
				if (j == 2) {
					return 4;
				}
				if (j == 3) {
					return 5;
				}
				if (j == 4) {
					return 2;
				}
				if (j == 5) {
					return 3;
				}
			}
			else if (coordBaseMode == 3) {
				if (j == 2) {
					return 5;
				}
				if (j == 3) {
					return 4;
				}
				if (j == 4) {
					return 2;
				}
				if (j == 5) {
					return 3;
				}
			}
		}
		else if (i == Block.button.blockID) {
			if (coordBaseMode == 0) {
				if (j == 3) {
					return 4;
				}
				if (j == 4) {
					return 3;
				}
			}
			else if (coordBaseMode == 1) {
				if (j == 3) {
					return 1;
				}
				if (j == 4) {
					return 2;
				}
				if (j == 2) {
					return 3;
				}
				if (j == 1) {
					return 4;
				}
			}
			else if (coordBaseMode == 3) {
				if (j == 3) {
					return 2;
				}
				if (j == 4) {
					return 1;
				}
				if (j == 2) {
					return 3;
				}
				if (j == 1) {
					return 4;
				}
			}
		}
		return j;
	}

	protected void placeBlockAtCurrentPosition(World world, int i, int j, int k, int l, int i1, StructureBoundingBox structureboundingbox) {
		int j1 = getXWithOffset(k, i1);
		int k1 = getYWithOffset(l);
		int l1 = getZWithOffset(k, i1);
		if (!structureboundingbox.isVecInside(j1, k1, l1)) {
			return;
		}
		else {
			world.setBlockAndMetadata(j1, k1, l1, i, j);
			return;
		}
	}

	protected int getBlockIdAtCurrentPosition(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
		int l = getXWithOffset(i, k);
		int i1 = getYWithOffset(j);
		int j1 = getZWithOffset(i, k);
		if (!structureboundingbox.isVecInside(l, i1, j1)) {
			return 0;
		}
		else {
			return world.getBlockId(l, i1, j1);
		}
	}

	protected void fillWithBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1,
	        int j1, int k1, int l1, boolean flag) {
		for (int i2 = j; i2 <= i1; i2++) {
			for (int j2 = i; j2 <= l; j2++) {
				for (int k2 = k; k2 <= j1; k2++) {
					if (flag && getBlockIdAtCurrentPosition(world, j2, i2, k2, structureboundingbox) == 0) {
						continue;
					}
					if (i2 == j || i2 == i1 || j2 == i || j2 == l || k2 == k || k2 == j1) {
						placeBlockAtCurrentPosition(world, k1, 0, j2, i2, k2, structureboundingbox);
					}
					else {
						placeBlockAtCurrentPosition(world, l1, 0, j2, i2, k2, structureboundingbox);
					}
				}
			}
		}
	}

	protected void fillWithRandomizedBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1,
	        int j1, boolean flag, Random random, StructurePieceBlockSelector structurepieceblockselector) {
		for (int k1 = j; k1 <= i1; k1++) {
			for (int l1 = i; l1 <= l; l1++) {
				for (int i2 = k; i2 <= j1; i2++) {
					if (!flag || getBlockIdAtCurrentPosition(world, l1, k1, i2, structureboundingbox) != 0) {
						structurepieceblockselector.selectBlocks(random, l1, k1, i2, k1 == j || k1 == i1 || l1 == i || l1 == l || i2 == k || i2 == j1);
						placeBlockAtCurrentPosition(world, structurepieceblockselector.getSelectedBlockId(), structurepieceblockselector.getSelectedBlockMetaData(), l1, k1, i2, structureboundingbox);
					}
				}
			}
		}
	}

	protected void randomlyFillWithBlocks(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k,
	        int l, int i1, int j1, int k1, int l1, boolean flag) {
		for (int i2 = j; i2 <= i1; i2++) {
			for (int j2 = i; j2 <= l; j2++) {
				for (int k2 = k; k2 <= j1; k2++) {
					if (random.nextFloat() > f || flag && getBlockIdAtCurrentPosition(world, j2, i2, k2, structureboundingbox) == 0) {
						continue;
					}
					if (i2 == j || i2 == i1 || j2 == i || j2 == l || k2 == k || k2 == j1) {
						placeBlockAtCurrentPosition(world, k1, 0, j2, i2, k2, structureboundingbox);
					}
					else {
						placeBlockAtCurrentPosition(world, l1, 0, j2, i2, k2, structureboundingbox);
					}
				}
			}
		}
	}

	protected void randomlyPlaceBlock(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k,
	        int l, int i1) {
		if (random.nextFloat() < f) {
			placeBlockAtCurrentPosition(world, l, i1, i, j, k, structureboundingbox);
		}
	}

	protected void randomlyRareFillWithBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1,
	        int j1, int k1, boolean flag) {
		float f = (l - i) + 1;
		float f1 = (i1 - j) + 1;
		float f2 = (j1 - k) + 1;
		float f3 = (float)i + f / 2.0F;
		float f4 = (float)k + f2 / 2.0F;
		for (int l1 = j; l1 <= i1; l1++) {
			float f5 = (float)(l1 - j) / f1;
			for (int i2 = i; i2 <= l; i2++) {
				float f6 = ((float)i2 - f3) / (f * 0.5F);
				for (int j2 = k; j2 <= j1; j2++) {
					float f7 = ((float)j2 - f4) / (f2 * 0.5F);
					if (flag && getBlockIdAtCurrentPosition(world, i2, l1, j2, structureboundingbox) == 0) {
						continue;
					}
					float f8 = f6 * f6 + f5 * f5 + f7 * f7;
					if (f8 <= 1.05F) {
						placeBlockAtCurrentPosition(world, k1, 0, i2, l1, j2, structureboundingbox);
					}
				}
			}
		}
	}

	protected void clearCurrentPositionBlocksUpwards(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
		int l = getXWithOffset(i, k);
		int i1 = getYWithOffset(j);
		int j1 = getZWithOffset(i, k);
		if (!structureboundingbox.isVecInside(l, i1, j1)) {
			return;
		}
		for (; !world.isAirBlock(l, i1, j1) && i1 < world.worldMaxY; i1++) {
			world.setBlockAndMetadata(l, i1, j1, 0, 0);
		}
	}

	protected void fillCurrentPositionBlocksDownwards(World world, int i, int j, int k, int l, int i1, StructureBoundingBox structureboundingbox) {
		int j1 = getXWithOffset(k, i1);
		int k1 = getYWithOffset(l);
		int l1 = getZWithOffset(k, i1);
		if (!structureboundingbox.isVecInside(j1, k1, l1)) {
			return;
		}
		for (; (world.isAirBlock(j1, k1, l1) || world.getBlockMaterial(j1, k1, l1).getIsLiquid()) && k1 > 1; k1--) {
			world.setBlockAndMetadata(j1, k1, l1, i, j);
		}
	}

	protected void createTreasureChestAtCurrentPosition(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, StructurePieceTreasure astructurepiecetreasure[],
	        int l) {
		int i1 = getXWithOffset(i, k);
		int j1 = getYWithOffset(j);
		int k1 = getZWithOffset(i, k);
		if (structureboundingbox.isVecInside(i1, j1, k1) && world.getBlockId(i1, j1, k1) != Block.chest.blockID) {
			world.setBlockWithNotify(i1, j1, k1, Block.chest.blockID);
			TileEntityChest tileentitychest = (TileEntityChest)world.getBlockTileEntity(i1, j1, k1);
			if (tileentitychest != null) {
				fillTreasureChestWithLoot(random, astructurepiecetreasure, tileentitychest, l);
			}
		}
	}

	private static void fillTreasureChestWithLoot(Random random, StructurePieceTreasure astructurepiecetreasure[], TileEntityChest tileentitychest, int i) {
		for (int j = 0; j < i; j++) {
			StructurePieceTreasure structurepiecetreasure = (StructurePieceTreasure)WeightedRandom.chooseOne(random, astructurepiecetreasure);
			int k = structurepiecetreasure.minItemStack + random.nextInt((structurepiecetreasure.maxItemStack - structurepiecetreasure.minItemStack) + 1);
			if (Item.itemsList[structurepiecetreasure.itemID].getItemStackLimit() >= k) {
				tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), new ItemStack(structurepiecetreasure.itemID, k, structurepiecetreasure.itemMetadata));
				continue;
			}
			for (int l = 0; l < k; l++) {
				tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), new ItemStack(structurepiecetreasure.itemID, 1, structurepiecetreasure.itemMetadata));
			}
		}
	}

	protected void placeDoorAtCurrentPosition(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, int l) {
		int i1 = getXWithOffset(i, k);
		int j1 = getYWithOffset(j);
		int k1 = getZWithOffset(i, k);
		if (structureboundingbox.isVecInside(i1, j1, k1)) {
			ItemDoor.placeDoorBlock(world, i1, j1, k1, l, Block.doorWood);
		}
	}
}
