package net.minecraft.src;

import java.util.*;

public class BlockBed extends Block {
	public static final int headBlockToFootBlockMap[][] = {
		{
			0, 1
		}, {
			-1, 0
		}, {
			0, -1
		}, {
			1, 0
		}
	};

	public BlockBed(int i) {
		super(i, 134, Material.cloth);
		setBounds();
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.multiplayerWorld) {
			return true;
		}
		int l = world.getBlockMetadata(i, j, k);
		if (!isBlockFootOfBed(l)) {
			int i1 = getDirectionFromMetadata(l);
			i += headBlockToFootBlockMap[i1][0];
			k += headBlockToFootBlockMap[i1][1];
			if (world.getBlockId(i, j, k) != blockID) {
				return true;
			}
			l = world.getBlockMetadata(i, j, k);
		}
		if (!world.worldProvider.canRespawnHere()) {
			double d = (double)i + 0.5D;
			double d1 = (double)j + 0.5D;
			double d2 = (double)k + 0.5D;
			world.setBlockWithNotify(i, j, k, 0);
			int j1 = getDirectionFromMetadata(l);
			i += headBlockToFootBlockMap[j1][0];
			k += headBlockToFootBlockMap[j1][1];
			if (world.getBlockId(i, j, k) == blockID) {
				world.setBlockWithNotify(i, j, k, 0);
				d = (d + (double)i + 0.5D) / 2D;
				d1 = (d1 + (double)j + 0.5D) / 2D;
				d2 = (d2 + (double)k + 0.5D) / 2D;
			}
			world.newExplosion(null, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 5F, true);
			return true;
		}
		if (isBedOccupied(l)) {
			EntityPlayer entityplayer1 = null;
			Iterator iterator = world.playerEntities.iterator();
			do {
				if (!iterator.hasNext()) {
					break;
				}
				EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();
				if (entityplayer2.isPlayerSleeping()) {
					ChunkCoordinates chunkcoordinates = entityplayer2.bedChunkCoordinates;
					if (chunkcoordinates.posX == i && chunkcoordinates.posY == j && chunkcoordinates.posZ == k) {
						entityplayer1 = entityplayer2;
					}
				}
			}
			while (true);
			if (entityplayer1 == null) {
				setBedOccupied(world, i, j, k, false);
			}
			else {
				entityplayer.addChatMessage("tile.bed.occupied");
				return true;
			}
		}
		EnumStatus enumstatus = entityplayer.sleepInBedAt(i, j, k);
		if (enumstatus == EnumStatus.OK) {
			setBedOccupied(world, i, j, k, true);
			return true;
		}
		if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW) {
			entityplayer.addChatMessage("tile.bed.noSleep");
		}
		else if (enumstatus == EnumStatus.NOT_SAFE) {
			entityplayer.addChatMessage("tile.bed.notSafe");
		}
		return true;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) {
			return Block.planks.blockIndexInTexture;
		}
		int k = getDirectionFromMetadata(j);
		int l = Direction.bedDirection[k][i];
		if (isBlockFootOfBed(j)) {
			if (l == 2) {
				return blockIndexInTexture + 2 + 16;
			}
			if (l == 5 || l == 4) {
				return blockIndexInTexture + 1 + 16;
			}
			else {
				return blockIndexInTexture + 1;
			}
		}
		if (l == 3) {
			return (blockIndexInTexture - 1) + 16;
		}
		if (l == 5 || l == 4) {
			return blockIndexInTexture + 16;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public int getRenderType() {
		return 14;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBounds();
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMetadata(i, j, k);
		int j1 = getDirectionFromMetadata(i1);
		if (isBlockFootOfBed(i1)) {
			if (world.getBlockId(i - headBlockToFootBlockMap[j1][0], j, k - headBlockToFootBlockMap[j1][1]) != blockID) {
				world.setBlockWithNotify(i, j, k, 0);
			}
		}
		else if (world.getBlockId(i + headBlockToFootBlockMap[j1][0], j, k + headBlockToFootBlockMap[j1][1]) != blockID) {
			world.setBlockWithNotify(i, j, k, 0);
			if (!world.multiplayerWorld) {
				dropBlockAsItem(world, i, j, k, i1, 0);
			}
		}
	}

	public int idDropped(int i, Random random, int j) {
		if (isBlockFootOfBed(i)) {
			return 0;
		}
		else {
			return Item.bed.shiftedIndex;
		}
	}

	private void setBounds() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
	}

	public static int getDirectionFromMetadata(int i) {
		return i & 3;
	}

	public static boolean isBlockFootOfBed(int i) {
		return (i & 8) != 0;
	}

	public static boolean isBedOccupied(int i) {
		return (i & 4) != 0;
	}

	public static void setBedOccupied(World world, int i, int j, int k, boolean flag) {
		int l = world.getBlockMetadata(i, j, k);
		if (flag) {
			l |= 4;
		}
		else {
			l &= -5;
		}
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	public static ChunkCoordinates getNearestEmptyChunkCoordinates(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMetadata(i, j, k);
		int j1 = getDirectionFromMetadata(i1);
		for (int k1 = 0; k1 <= 1; k1++) {
			int l1 = i - headBlockToFootBlockMap[j1][0] * k1 - 1;
			int i2 = k - headBlockToFootBlockMap[j1][1] * k1 - 1;
			int j2 = l1 + 2;
			int k2 = i2 + 2;
			for (int l2 = l1; l2 <= j2; l2++) {
				for (int i3 = i2; i3 <= k2; i3++) {
					if (!world.isBlockNormalCube(l2, j - 1, i3) || !world.isAirBlock(l2, j, i3) || !world.isAirBlock(l2, j + 1, i3)) {
						continue;
					}
					if (l > 0) {
						l--;
					}
					else {
						return new ChunkCoordinates(l2, j, i3);
					}
				}
			}
		}

		return null;
	}

	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {
		if (!isBlockFootOfBed(l)) {
			super.dropBlockAsItemWithChance(world, i, j, k, l, f, 0);
		}
	}

	public int getMobilityFlag() {
		return 1;
	}
}
