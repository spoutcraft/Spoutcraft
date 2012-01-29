package net.minecraft.src;

public class BlockSponge extends Block {
	protected BlockSponge(int i) {
		super(i, Material.sponge);
		blockIndexInTexture = 48;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		byte byte0 = 2;
		for (int l = i - byte0; l <= i + byte0; l++) {
			for (int i1 = j - byte0; i1 <= j + byte0; i1++) {
				for (int j1 = k - byte0; j1 <= k + byte0; j1++) {
					if (world.getBlockMaterial(l, i1, j1) != Material.water);
				}
			}
		}
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		byte byte0 = 2;
		for (int l = i - byte0; l <= i + byte0; l++) {
			for (int i1 = j - byte0; i1 <= j + byte0; i1++) {
				for (int j1 = k - byte0; j1 <= k + byte0; j1++) {
					world.notifyBlocksOfNeighborChange(l, i1, j1, world.getBlockId(l, i1, j1));
				}
			}
		}
	}
}
