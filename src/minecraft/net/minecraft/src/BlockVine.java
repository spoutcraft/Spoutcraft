package net.minecraft.src;

import java.util.Random;

public class BlockVine extends Block {
	public BlockVine(int i) {
		super(i, 143, Material.vine);
		setTickOnLoad(true);
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public int getRenderType() {
		return 20;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		float f = 1.0F;
		float f1 = 1.0F;
		float f2 = 1.0F;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		boolean flag = l > 0;
		if ((l & 2) != 0) {
			f3 = Math.max(f3, 0.0625F);
			f = 0.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			flag = true;
		}
		if ((l & 8) != 0) {
			f = Math.min(f, 0.9375F);
			f3 = 1.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			flag = true;
		}
		if ((l & 4) != 0) {
			f5 = Math.max(f5, 0.0625F);
			f2 = 0.0F;
			f = 0.0F;
			f3 = 1.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			flag = true;
		}
		if ((l & 1) != 0) {
			f2 = Math.min(f2, 0.9375F);
			f5 = 1.0F;
			f = 0.0F;
			f3 = 1.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			flag = true;
		}
		if (!flag && canBePlacedOn(iblockaccess.getBlockId(i, j + 1, k))) {
			f1 = Math.min(f1, 0.9375F);
			f4 = 1.0F;
			f = 0.0F;
			f3 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
		}
		setBlockBounds(f, f1, f2, f3, f4, f5);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
		switch (l) {
			default:
				return false;

			case 1:
				return canBePlacedOn(world.getBlockId(i, j + 1, k));

			case 2:
				return canBePlacedOn(world.getBlockId(i, j, k + 1));

			case 3:
				return canBePlacedOn(world.getBlockId(i, j, k - 1));

			case 5:
				return canBePlacedOn(world.getBlockId(i - 1, j, k));

			case 4:
				return canBePlacedOn(world.getBlockId(i + 1, j, k));
		}
	}

	private boolean canBePlacedOn(int i) {
		if (i == 0) {
			return false;
		}
		Block block = Block.blocksList[i];
		return block.renderAsNormalBlock() && block.blockMaterial.getIsSolid();
	}

	private boolean canVineStay(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		int i1 = l;
		if (i1 > 0) {
			for (int j1 = 0; j1 <= 3; j1++) {
				int k1 = 1 << j1;
				if ((l & k1) != 0 && !canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[j1], j, k + Direction.field_35870_b[j1])) && (world.getBlockId(i, j + 1, k) != blockID || (world.getBlockMetadata(i, j + 1, k) & k1) == 0)) {
					i1 &= ~k1;
				}
			}
		}
		if (i1 == 0 && !canBePlacedOn(world.getBlockId(i, j + 1, k))) {
			return false;
		}
		if (i1 != l) {
			world.setBlockMetadataWithNotify(i, j, k, i1);
		}
		return true;
	}

	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int getRenderColor(int i) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return iblockaccess.getWorldChunkManager().getBiomeGenAt(i, k).getFoliageColorAtCoords(iblockaccess, i, j, k);
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!world.multiplayerWorld && !canVineStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (!world.multiplayerWorld && world.rand.nextInt(4) == 0) {
			byte byte0 = 4;
			int l = 5;
			boolean flag = false;
			int i1 = i - byte0;
			label0:
			do {
				if (i1 > i + byte0) {
					break;
				}
				label1:
				for (int j1 = k - byte0; j1 <= k + byte0; j1++) {
					int l1 = j - 1;
					do {
						if (l1 > j + 1) {
							continue label1;
						}
						if (world.getBlockId(i1, l1, j1) == blockID && --l <= 0) {
							flag = true;
							break label0;
						}
						l1++;
					}
					while (true);
				}

				i1++;
			}
			while (true);
			i1 = world.getBlockMetadata(i, j, k);
			int k1 = world.rand.nextInt(6);
			int i2 = Direction.field_35869_d[k1];
			if (k1 == 1 && j < world.worldHeight - 1 && world.isAirBlock(i, j + 1, k)) {
				if (flag) {
					return;
				}
				int j2 = world.rand.nextInt(16) & i1;
				if (j2 > 0) {
					for (int i3 = 0; i3 <= 3; i3++) {
						if (!canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[i3], j + 1, k + Direction.field_35870_b[i3]))) {
							j2 &= ~(1 << i3);
						}
					}

					if (j2 > 0) {
						world.setBlockAndMetadataWithNotify(i, j + 1, k, blockID, j2);
					}
				}
			}
			else if (k1 >= 2 && k1 <= 5 && (i1 & 1 << i2) == 0) {
				if (flag) {
					return;
				}
				int k2 = world.getBlockId(i + Direction.field_35871_a[i2], j, k + Direction.field_35870_b[i2]);
				if (k2 == 0 || Block.blocksList[k2] == null) {
					int j3 = i2 + 1 & 3;
					int i4 = i2 + 3 & 3;
					if ((i1 & 1 << j3) != 0 && canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[i2] + Direction.field_35871_a[j3], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[j3]))) {
						world.setBlockAndMetadataWithNotify(i + Direction.field_35871_a[i2], j, k + Direction.field_35870_b[i2], blockID, 1 << j3);
					}
					else if ((i1 & 1 << i4) != 0 && canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[i2] + Direction.field_35871_a[i4], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[i4]))) {
						world.setBlockAndMetadataWithNotify(i + Direction.field_35871_a[i2], j, k + Direction.field_35870_b[i2], blockID, 1 << i4);
					}
					else if ((i1 & 1 << j3) != 0 && world.isAirBlock(i + Direction.field_35871_a[i2] + Direction.field_35871_a[j3], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[j3]) && canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[j3], j, k + Direction.field_35870_b[j3]))) {
						world.setBlockAndMetadataWithNotify(i + Direction.field_35871_a[i2] + Direction.field_35871_a[j3], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[j3], blockID, 1 << (i2 + 2 & 3));
					}
					else if ((i1 & 1 << i4) != 0 && world.isAirBlock(i + Direction.field_35871_a[i2] + Direction.field_35871_a[i4], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[i4]) && canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[i4], j, k + Direction.field_35870_b[i4]))) {
						world.setBlockAndMetadataWithNotify(i + Direction.field_35871_a[i2] + Direction.field_35871_a[i4], j, k + Direction.field_35870_b[i2] + Direction.field_35870_b[i4], blockID, 1 << (i2 + 2 & 3));
					}
					else if (canBePlacedOn(world.getBlockId(i + Direction.field_35871_a[i2], j + 1, k + Direction.field_35870_b[i2]))) {
						world.setBlockAndMetadataWithNotify(i + Direction.field_35871_a[i2], j, k + Direction.field_35870_b[i2], blockID, 0);
					}
				}
				else if (Block.blocksList[k2].blockMaterial.getIsOpaque() && Block.blocksList[k2].renderAsNormalBlock()) {
					world.setBlockMetadataWithNotify(i, j, k, i1 | 1 << i2);
				}
			}
			else if (j > 1) {
				int l2 = world.getBlockId(i, j - 1, k);
				if (l2 == 0) {
					int k3 = world.rand.nextInt(16) & i1;
					if (k3 > 0) {
						world.setBlockAndMetadataWithNotify(i, j - 1, k, blockID, k3);
					}
				}
				else if (l2 == blockID) {
					int l3 = world.rand.nextInt(16) & i1;
					int j4 = world.getBlockMetadata(i, j - 1, k);
					if (j4 != (j4 | l3)) {
						world.setBlockMetadataWithNotify(i, j - 1, k, j4 | l3);
					}
				}
			}
		}
	}

	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		byte byte0 = 0;
		switch (l) {
			case 2:
				byte0 = 1;
				break;

			case 3:
				byte0 = 4;
				break;

			case 4:
				byte0 = 8;
				break;

			case 5:
				byte0 = 2;
				break;
		}
		if (byte0 != 0) {
			world.setBlockMetadataWithNotify(i, j, k, byte0);
		}
	}

	public int idDropped(int i, Random random, int j) {
		return 0;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if (!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.vine, 1, 0));
		}
		else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}
	}
}
