package net.minecraft.src;

import java.util.Random;

public class BlockPumpkin extends Block {
	private boolean blockType;

	protected BlockPumpkin(int i, int j, boolean flag) {
		super(i, Material.pumpkin);
		blockIndexInTexture = j;
		setTickOnLoad(true);
		blockType = flag;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 1) {
			return blockIndexInTexture;
		}
		if (i == 0) {
			return blockIndexInTexture;
		}
		int k = blockIndexInTexture + 1 + 16;
		if (blockType) {
			k++;
		}
		if (j == 2 && i == 2) {
			return k;
		}
		if (j == 3 && i == 5) {
			return k;
		}
		if (j == 0 && i == 3) {
			return k;
		}
		if (j == 1 && i == 4) {
			return k;
		}
		else {
			return blockIndexInTexture + 16;
		}
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return blockIndexInTexture;
		}
		if (i == 0) {
			return blockIndexInTexture;
		}
		if (i == 3) {
			return blockIndexInTexture + 1 + 16;
		}
		else {
			return blockIndexInTexture + 16;
		}
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		if (world.getBlockId(i, j - 1, k) == Block.blockSnow.blockID && world.getBlockId(i, j - 2, k) == Block.blockSnow.blockID) {
			if (!world.multiplayerWorld) {
				world.setBlockWithNotify(i, j, k, 0);
				world.setBlockWithNotify(i, j - 1, k, 0);
				world.setBlockWithNotify(i, j - 2, k, 0);
				EntitySnowman entitysnowman = new EntitySnowman(world);
				entitysnowman.setLocationAndAngles((double)i + 0.5D, (double)j - 1.95D, (double)k + 0.5D, 0.0F, 0.0F);
				world.spawnEntityInWorld(entitysnowman);
			}
			for (int l = 0; l < 120; l++) {
				world.spawnParticle("snowshovel", (double)i + world.rand.nextDouble(), (double)(j - 2) + world.rand.nextDouble() * 2.5D, (double)k + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		int l = world.getBlockId(i, j, k);
		return (l == 0 || Block.blocksList[l].blockMaterial.getIsGroundCover()) && world.isBlockNormalCube(i, j - 1, k);
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;
		world.setBlockMetadataWithNotify(i, j, k, l);
	}
}
