package net.minecraft.src;

public abstract class BlockContainer extends Block {
	protected BlockContainer(int i, Material material) {
		super(i, material);
		isBlockContainer[blockID] = true;
	}

	protected BlockContainer(int i, int j, Material material) {
		super(i, j, material);
		isBlockContainer[blockID] = true;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.setBlockTileEntity(i, j, k, getBlockEntity());
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		super.onBlockRemoval(world, i, j, k);
		world.removeBlockTileEntity(i, j, k);
	}

	public abstract TileEntity getBlockEntity();

	public void powerBlock(World world, int i, int j, int k, int l, int i1) {
		super.powerBlock(world, i, j, k, l, i1);
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			tileentity.onTileEntityPowered(l, i1);
		}
	}
}
