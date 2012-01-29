package net.minecraft.src;

import java.util.Random;

public class BlockMycelium extends Block {
	protected BlockMycelium(int i) {
		super(i, Material.grass);
		blockIndexInTexture = 77;
		setTickOnLoad(true);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 1) {
			return 78;
		}
		return i != 0 ? 77 : 2;
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return 78;
		}
		if (l == 0) {
			return 2;
		}
		Material material = iblockaccess.getBlockMaterial(i, j + 1, k);
		return material != Material.snow && material != Material.craftedSnow ? 77 : 68;
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if (world.multiplayerWorld) {
			return;
		}
		if (world.getBlockLightValue(i, j + 1, k) < 4 && Block.lightOpacity[world.getBlockId(i, j + 1, k)] > 2) {
			world.setBlockWithNotify(i, j, k, Block.dirt.blockID);
		}
		else if (world.getBlockLightValue(i, j + 1, k) >= 9) {
			for (int l = 0; l < 4; l++) {
				int i1 = (i + random.nextInt(3)) - 1;
				int j1 = (j + random.nextInt(5)) - 3;
				int k1 = (k + random.nextInt(3)) - 1;
				int l1 = world.getBlockId(i1, j1 + 1, k1);
				if (world.getBlockId(i1, j1, k1) == Block.dirt.blockID && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && Block.lightOpacity[l1] <= 2) {
					world.setBlockWithNotify(i1, j1, k1, blockID);
				}
			}
		}
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		super.randomDisplayTick(world, i, j, k, random);
		if (random.nextInt(10) == 0) {
			world.spawnParticle("townaura", (float)i + random.nextFloat(), (float)j + 1.1F, (float)k + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	public int idDropped(int i, Random random, int j) {
		return Block.dirt.idDropped(0, random, j);
	}
}
