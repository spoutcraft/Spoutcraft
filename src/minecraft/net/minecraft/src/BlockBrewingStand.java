package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockBrewingStand extends BlockContainer {
	private Random field_40214_a;

	public BlockBrewingStand(int i) {
		super(i, Material.iron);
		field_40214_a = new Random();
		blockIndexInTexture = 157;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 25;
	}

	public TileEntity getBlockEntity() {
		return new TileEntityBrewingStand();
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBoundsForItemRender();
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
	}

	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.multiplayerWorld) {
			return true;
		}
		TileEntityBrewingStand tileentitybrewingstand = (TileEntityBrewingStand)world.getBlockTileEntity(i, j, k);
		if (tileentitybrewingstand != null) {
			entityplayer.displayGUIBrewingStand(tileentitybrewingstand);
		}
		return true;
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		double d = (float)i + 0.4F + random.nextFloat() * 0.2F;
		double d1 = (float)j + 0.7F + random.nextFloat() * 0.3F;
		double d2 = (float)k + 0.4F + random.nextFloat() * 0.2F;
		world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if (tileentity != null && (tileentity instanceof TileEntityBrewingStand)) {
			TileEntityBrewingStand tileentitybrewingstand = (TileEntityBrewingStand)tileentity;
			label0:
			for (int l = 0; l < tileentitybrewingstand.getSizeInventory(); l++) {
				ItemStack itemstack = tileentitybrewingstand.getStackInSlot(l);
				if (itemstack == null) {
					continue;
				}
				float f = field_40214_a.nextFloat() * 0.8F + 0.1F;
				float f1 = field_40214_a.nextFloat() * 0.8F + 0.1F;
				float f2 = field_40214_a.nextFloat() * 0.8F + 0.1F;
				do {
					if (itemstack.stackSize <= 0) {
						continue label0;
					}
					int i1 = field_40214_a.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float)field_40214_a.nextGaussian() * f3;
					entityitem.motionY = (float)field_40214_a.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float)field_40214_a.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
				while (true);
			}
		}
		super.onBlockRemoval(world, i, j, k);
	}

	public int idDropped(int i, Random random, int j) {
		return Item.brewingStand.shiftedIndex;
	}
}
