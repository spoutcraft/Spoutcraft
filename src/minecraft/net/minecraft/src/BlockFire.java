package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block {
	private int chanceToEncourageFire[];
	private int abilityToCatchFire[];

	protected BlockFire(int i, int j) {
		super(i, j, Material.fire);
		chanceToEncourageFire = new int[256];
		abilityToCatchFire = new int[256];
		setTickOnLoad(true);
	}

	public void initializeBlock() {
		setBurnRate(Block.planks.blockID, 5, 20);
		setBurnRate(Block.fence.blockID, 5, 20);
		setBurnRate(Block.stairCompactPlanks.blockID, 5, 20);
		setBurnRate(Block.wood.blockID, 5, 5);
		setBurnRate(Block.leaves.blockID, 30, 60);
		setBurnRate(Block.bookShelf.blockID, 30, 20);
		setBurnRate(Block.tnt.blockID, 15, 100);
		setBurnRate(Block.tallGrass.blockID, 60, 100);
		setBurnRate(Block.cloth.blockID, 30, 60);
		setBurnRate(Block.vine.blockID, 15, 100);
	}

	private void setBurnRate(int i, int j, int k) {
		chanceToEncourageFire[i] = j;
		abilityToCatchFire[i] = k;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int tickRate() {
		return 40;
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		boolean flag = world.getBlockId(i, j - 1, k) == Block.netherrack.blockID;
		if ((world.worldProvider instanceof WorldProviderEnd) && world.getBlockId(i, j - 1, k) == Block.bedrock.blockID) {
			flag = true;
		}
		if (!canPlaceBlockAt(world, i, j, k)) {
			world.setBlockWithNotify(i, j, k, 0);
		}
		if (!flag && world.isRaining() && (world.canLightningStrikeAt(i, j, k) || world.canLightningStrikeAt(i - 1, j, k) || world.canLightningStrikeAt(i + 1, j, k) || world.canLightningStrikeAt(i, j, k - 1) || world.canLightningStrikeAt(i, j, k + 1))) {
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		if (l < 15) {
			world.setBlockMetadata(i, j, k, l + random.nextInt(3) / 2);
		}
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		if (!flag && !func_263_h(world, i, j, k)) {
			if (!world.isBlockNormalCube(i, j - 1, k) || l > 3) {
				world.setBlockWithNotify(i, j, k, 0);
			}
			return;
		}
		if (!flag && !canBlockCatchFire(world, i, j - 1, k) && l == 15 && random.nextInt(4) == 0) {
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		tryToCatchBlockOnFire(world, i + 1, j, k, 300, random, l);
		tryToCatchBlockOnFire(world, i - 1, j, k, 300, random, l);
		tryToCatchBlockOnFire(world, i, j - 1, k, 250, random, l);
		tryToCatchBlockOnFire(world, i, j + 1, k, 250, random, l);
		tryToCatchBlockOnFire(world, i, j, k - 1, 300, random, l);
		tryToCatchBlockOnFire(world, i, j, k + 1, 300, random, l);
		for (int i1 = i - 1; i1 <= i + 1; i1++) {
			for (int j1 = k - 1; j1 <= k + 1; j1++) {
				for (int k1 = j - 1; k1 <= j + 4; k1++) {
					if (i1 == i && k1 == j && j1 == k) {
						continue;
					}
					int l1 = 100;
					if (k1 > j + 1) {
						l1 += (k1 - (j + 1)) * 100;
					}
					int i2 = getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);
					if (i2 <= 0) {
						continue;
					}
					int j2 = (i2 + 40) / (l + 30);
					if (j2 <= 0 || random.nextInt(l1) > j2 || world.isRaining() && world.canLightningStrikeAt(i1, k1, j1) || world.canLightningStrikeAt(i1 - 1, k1, k) || world.canLightningStrikeAt(i1 + 1, k1, j1) || world.canLightningStrikeAt(i1, k1, j1 - 1) || world.canLightningStrikeAt(i1, k1, j1 + 1)) {
						continue;
					}
					int k2 = l + random.nextInt(5) / 4;
					if (k2 > 15) {
						k2 = 15;
					}
					world.setBlockAndMetadataWithNotify(i1, k1, j1, blockID, k2);
				}
			}
		}
	}

	private void tryToCatchBlockOnFire(World world, int i, int j, int k, int l, Random random, int i1) {
		int j1 = abilityToCatchFire[world.getBlockId(i, j, k)];
		if (random.nextInt(l) < j1) {
			boolean flag = world.getBlockId(i, j, k) == Block.tnt.blockID;
			if (random.nextInt(i1 + 10) < 5 && !world.canLightningStrikeAt(i, j, k)) {
				int k1 = i1 + random.nextInt(5) / 4;
				if (k1 > 15) {
					k1 = 15;
				}
				world.setBlockAndMetadataWithNotify(i, j, k, blockID, k1);
			}
			else {
				world.setBlockWithNotify(i, j, k, 0);
			}
			if (flag) {
				Block.tnt.onBlockDestroyedByPlayer(world, i, j, k, 1);
			}
		}
	}

	private boolean func_263_h(World world, int i, int j, int k) {
		if (canBlockCatchFire(world, i + 1, j, k)) {
			return true;
		}
		if (canBlockCatchFire(world, i - 1, j, k)) {
			return true;
		}
		if (canBlockCatchFire(world, i, j - 1, k)) {
			return true;
		}
		if (canBlockCatchFire(world, i, j + 1, k)) {
			return true;
		}
		if (canBlockCatchFire(world, i, j, k - 1)) {
			return true;
		}
		return canBlockCatchFire(world, i, j, k + 1);
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int i, int j, int k) {
		int l = 0;
		if (!world.isAirBlock(i, j, k)) {
			return 0;
		}
		else {
			l = getChanceToEncourageFire(world, i + 1, j, k, l);
			l = getChanceToEncourageFire(world, i - 1, j, k, l);
			l = getChanceToEncourageFire(world, i, j - 1, k, l);
			l = getChanceToEncourageFire(world, i, j + 1, k, l);
			l = getChanceToEncourageFire(world, i, j, k - 1, l);
			l = getChanceToEncourageFire(world, i, j, k + 1, l);
			return l;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess iblockaccess, int i, int j, int k) {
		return chanceToEncourageFire[iblockaccess.getBlockId(i, j, k)] > 0;
	}

	public int getChanceToEncourageFire(World world, int i, int j, int k, int l) {
		int i1 = chanceToEncourageFire[world.getBlockId(i, j, k)];
		if (i1 > l) {
			return i1;
		}
		else {
			return l;
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return world.isBlockNormalCube(i, j - 1, k) || func_263_h(world, i, j, k);
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!world.isBlockNormalCube(i, j - 1, k) && !func_263_h(world, i, j, k)) {
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		else {
			return;
		}
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		if (world.worldProvider.worldType <= 0 && world.getBlockId(i, j - 1, k) == Block.obsidian.blockID && Block.portal.tryToCreatePortal(world, i, j, k)) {
			return;
		}
		if (!world.isBlockNormalCube(i, j - 1, k) && !func_263_h(world, i, j, k)) {
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		else {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			return;
		}
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (random.nextInt(24) == 0) {
			world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
		}
		if (world.isBlockNormalCube(i, j - 1, k) || Block.fire.canBlockCatchFire(world, i, j - 1, k)) {
			for (int l = 0; l < 3; l++) {
				float f = (float)i + random.nextFloat();
				float f6 = (float)j + random.nextFloat() * 0.5F + 0.5F;
				float f12 = (float)k + random.nextFloat();
				world.spawnParticle("largesmoke", f, f6, f12, 0.0D, 0.0D, 0.0D);
			}
		}
		else {
			if (Block.fire.canBlockCatchFire(world, i - 1, j, k)) {
				for (int i1 = 0; i1 < 2; i1++) {
					float f1 = (float)i + random.nextFloat() * 0.1F;
					float f7 = (float)j + random.nextFloat();
					float f13 = (float)k + random.nextFloat();
					world.spawnParticle("largesmoke", f1, f7, f13, 0.0D, 0.0D, 0.0D);
				}
			}
			if (Block.fire.canBlockCatchFire(world, i + 1, j, k)) {
				for (int j1 = 0; j1 < 2; j1++) {
					float f2 = (float)(i + 1) - random.nextFloat() * 0.1F;
					float f8 = (float)j + random.nextFloat();
					float f14 = (float)k + random.nextFloat();
					world.spawnParticle("largesmoke", f2, f8, f14, 0.0D, 0.0D, 0.0D);
				}
			}
			if (Block.fire.canBlockCatchFire(world, i, j, k - 1)) {
				for (int k1 = 0; k1 < 2; k1++) {
					float f3 = (float)i + random.nextFloat();
					float f9 = (float)j + random.nextFloat();
					float f15 = (float)k + random.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f3, f9, f15, 0.0D, 0.0D, 0.0D);
				}
			}
			if (Block.fire.canBlockCatchFire(world, i, j, k + 1)) {
				for (int l1 = 0; l1 < 2; l1++) {
					float f4 = (float)i + random.nextFloat();
					float f10 = (float)j + random.nextFloat();
					float f16 = (float)(k + 1) - random.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f4, f10, f16, 0.0D, 0.0D, 0.0D);
				}
			}
			if (Block.fire.canBlockCatchFire(world, i, j + 1, k)) {
				for (int i2 = 0; i2 < 2; i2++) {
					float f5 = (float)i + random.nextFloat();
					float f11 = (float)(j + 1) - random.nextFloat() * 0.1F;
					float f17 = (float)k + random.nextFloat();
					world.spawnParticle("largesmoke", f5, f11, f17, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
}
