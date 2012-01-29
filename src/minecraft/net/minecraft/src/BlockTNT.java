package net.minecraft.src;

import java.util.Random;

public class BlockTNT extends Block {
	public BlockTNT(int i, int j) {
		super(i, j, Material.tnt);
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 0) {
			return blockIndexInTexture + 2;
		}
		if (i == 1) {
			return blockIndexInTexture + 1;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
			onBlockDestroyedByPlayer(world, i, j, k, 1);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j, k)) {
			onBlockDestroyedByPlayer(world, i, j, k, 1);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void onBlockDestroyedByExplosion(World world, int i, int j, int k) {
		EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
		entitytntprimed.fuse = world.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
		world.spawnEntityInWorld(entitytntprimed);
	}

	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l) {
		if (world.multiplayerWorld) {
			return;
		}
		if ((l & 1) == 0) {
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.tnt.blockID, 1, 0));
		}
		else {
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
			world.spawnEntityInWorld(entitytntprimed);
			world.playSoundAtEntity(entitytntprimed, "random.fuse", 1.0F, 1.0F);
		}
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.flintAndSteel.shiftedIndex) {
			world.setBlockMetadata(i, j, k, 1);
		}
		super.onBlockClicked(world, i, j, k, entityplayer);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return super.blockActivated(world, i, j, k, entityplayer);
	}

	protected ItemStack createStackedBlock(int i) {
		return null;
	}
}
