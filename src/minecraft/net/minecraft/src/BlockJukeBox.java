package net.minecraft.src;

import java.util.Random;

public class BlockJukeBox extends BlockContainer {
	protected BlockJukeBox(int i, int j) {
		super(i, j, Material.wood);
	}

	public int getBlockTextureFromSide(int i) {
		return blockIndexInTexture + (i != 1 ? 0 : 1);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.getBlockMetadata(i, j, k) == 0) {
			return false;
		}
		else {
			insertDisc(world, i, j, k);
			return true;
		}
	}

	public void ejectRecord(World world, int i, int j, int k, int l) {
		if (world.multiplayerWorld) {
			return;
		}
		TileEntityRecordPlayer tileentityrecordplayer = (TileEntityRecordPlayer)world.getBlockTileEntity(i, j, k);
		if (tileentityrecordplayer == null) {
			return;
		}
		else {
			tileentityrecordplayer.record = l;
			tileentityrecordplayer.onInventoryChanged();
			world.setBlockMetadataWithNotify(i, j, k, 1);
			return;
		}
	}

	public void insertDisc(World world, int i, int j, int k) {
		if (world.multiplayerWorld) {
			return;
		}
		TileEntityRecordPlayer tileentityrecordplayer = (TileEntityRecordPlayer)world.getBlockTileEntity(i, j, k);
		if (tileentityrecordplayer == null) {
			return;
		}
		int l = tileentityrecordplayer.record;
		if (l == 0) {
			return;
		}
		else {
			world.playAuxSFX(1005, i, j, k, 0);
			world.playRecord(null, i, j, k);
			tileentityrecordplayer.record = 0;
			tileentityrecordplayer.onInventoryChanged();
			world.setBlockMetadataWithNotify(i, j, k, 0);
			int i1 = l;
			float f = 0.7F;
			double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.20000000000000001D + 0.59999999999999998D;
			double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(i1, 1, 0));
			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
			return;
		}
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		insertDisc(world, i, j, k);
		super.onBlockRemoval(world, i, j, k);
	}

	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {
		if (world.multiplayerWorld) {
			return;
		}
		else {
			super.dropBlockAsItemWithChance(world, i, j, k, l, f, 0);
			return;
		}
	}

	public TileEntity getBlockEntity() {
		return new TileEntityRecordPlayer();
	}
}
