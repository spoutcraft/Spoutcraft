package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockCauldron extends Block {
	public BlockCauldron(int i) {
		super(i, Material.iron);
		blockIndexInTexture = 154;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 1) {
			return 138;
		}
		return i != 0 ? 154 : 155;
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBoundsForItemRender();
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 24;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.multiplayerWorld) {
			return true;
		}
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack == null) {
			return true;
		}
		int l = world.getBlockMetadata(i, j, k);
		if (itemstack.itemID == Item.bucketWater.shiftedIndex) {
			if (l < 3) {
				if (!entityplayer.capabilities.depleteBuckets) {
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Item.bucketEmpty));
				}
				world.setBlockMetadataWithNotify(i, j, k, 3);
			}
			return true;
		}
		if (itemstack.itemID == Item.glassBottle.shiftedIndex && l > 0) {
			ItemStack itemstack1 = new ItemStack(Item.potion, 1, 0);
			if (!entityplayer.inventory.addItemStackToInventory(itemstack1)) {
				world.spawnEntityInWorld(new EntityItem(world, (double)i + 0.5D, (double)j + 1.5D, (double)k + 0.5D, itemstack1));
			}
			itemstack.stackSize--;
			if (itemstack.stackSize <= 0) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			}
			world.setBlockMetadataWithNotify(i, j, k, l - 1);
		}
		return true;
	}

	public int idDropped(int i, Random random, int j) {
		return Item.cauldron.shiftedIndex;
	}
}
