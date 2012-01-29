package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdPortalRoom extends ComponentStronghold {
	private boolean field_40015_a;

	public ComponentStrongholdPortalRoom(int i, Random random, StructureBoundingBox structureboundingbox, int j) {
		super(i);
		coordBaseMode = j;
		boundingBox = structureboundingbox;
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		if (structurecomponent != null) {
			((ComponentStrongholdStairs2)structurecomponent).field_40009_b = this;
		}
	}

	public static ComponentStrongholdPortalRoom func_40014_a(List list, Random random, int i, int j, int k, int l, int i1) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 11, 8, 16, l);
		if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.getIntersectingStructureComponent(list, structureboundingbox) != null) {
			return null;
		}
		else {
			return new ComponentStrongholdPortalRoom(i1, random, structureboundingbox, l);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 7, 15, false, random, StructureStrongholdPieces.getStrongholdStones());
		placeDoor(world, random, structureboundingbox, EnumDoor.GRATES, 4, 1, 0);
		byte byte0 = 6;
		fillWithRandomizedBlocks(world, structureboundingbox, 1, byte0, 1, 1, byte0, 14, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 9, byte0, 1, 9, byte0, 14, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 2, byte0, 1, 8, byte0, 2, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 2, byte0, 14, 8, byte0, 14, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 1, 1, 1, 2, 1, 4, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 1, 9, 1, 4, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithBlocks(world, structureboundingbox, 1, 1, 1, 1, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
		fillWithBlocks(world, structureboundingbox, 9, 1, 1, 9, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
		fillWithRandomizedBlocks(world, structureboundingbox, 3, 1, 8, 7, 1, 12, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithBlocks(world, structureboundingbox, 4, 1, 9, 6, 1, 11, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
		for (int j = 3; j < 14; j += 2) {
			fillWithBlocks(world, structureboundingbox, 0, 3, j, 0, 4, j, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
			fillWithBlocks(world, structureboundingbox, 10, 3, j, 10, 4, j, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
		}

		for (int k = 2; k < 9; k += 2) {
			fillWithBlocks(world, structureboundingbox, k, 3, 15, k, 4, 15, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
		}

		int l = getMetadataWithOffset(Block.stairsStoneBrickSmooth.blockID, 3);
		fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 6, 1, 7, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 4, 2, 6, 6, 2, 7, false, random, StructureStrongholdPieces.getStrongholdStones());
		fillWithRandomizedBlocks(world, structureboundingbox, 4, 3, 7, 6, 3, 7, false, random, StructureStrongholdPieces.getStrongholdStones());
		for (int i1 = 4; i1 <= 6; i1++) {
			placeBlockAtCurrentPosition(world, Block.stairsStoneBrickSmooth.blockID, l, i1, 1, 4, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stairsStoneBrickSmooth.blockID, l, i1, 2, 5, structureboundingbox);
			placeBlockAtCurrentPosition(world, Block.stairsStoneBrickSmooth.blockID, l, i1, 3, 6, structureboundingbox);
		}

		byte byte1 = 2;
		byte byte2 = 0;
		byte byte3 = 3;
		byte byte4 = 1;
		switch (coordBaseMode) {
			case 0:
				byte1 = 0;
				byte2 = 2;
				break;

			case 3:
				byte1 = 3;
				byte2 = 1;
				byte3 = 0;
				byte4 = 2;
				break;

			case 1:
				byte1 = 1;
				byte2 = 3;
				byte3 = 0;
				byte4 = 2;
				break;
		}
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte1 + (random.nextFloat() <= 0.9F ? 0 : 4), 4, 3, 8, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte1 + (random.nextFloat() <= 0.9F ? 0 : 4), 5, 3, 8, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte1 + (random.nextFloat() <= 0.9F ? 0 : 4), 6, 3, 8, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte2 + (random.nextFloat() <= 0.9F ? 0 : 4), 4, 3, 12, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte2 + (random.nextFloat() <= 0.9F ? 0 : 4), 5, 3, 12, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte2 + (random.nextFloat() <= 0.9F ? 0 : 4), 6, 3, 12, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte3 + (random.nextFloat() <= 0.9F ? 0 : 4), 3, 3, 9, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte3 + (random.nextFloat() <= 0.9F ? 0 : 4), 3, 3, 10, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte3 + (random.nextFloat() <= 0.9F ? 0 : 4), 3, 3, 11, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte4 + (random.nextFloat() <= 0.9F ? 0 : 4), 7, 3, 9, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte4 + (random.nextFloat() <= 0.9F ? 0 : 4), 7, 3, 10, structureboundingbox);
		placeBlockAtCurrentPosition(world, Block.endPortalFrame.blockID, byte4 + (random.nextFloat() <= 0.9F ? 0 : 4), 7, 3, 11, structureboundingbox);
		if (!field_40015_a) {
			int i = getYWithOffset(3);
			int j1 = getXWithOffset(5, 6);
			int k1 = getZWithOffset(5, 6);
			if (structureboundingbox.isVecInside(j1, i, k1)) {
				field_40015_a = true;
				world.setBlockWithNotify(j1, i, k1, Block.mobSpawner.blockID);
				TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getBlockTileEntity(j1, i, k1);
				if (tileentitymobspawner != null) {
					tileentitymobspawner.setMobID("Silverfish");
				}
			}
		}
		return true;
	}
}
