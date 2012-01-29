package net.minecraft.src;

import java.util.Random;

public class BlockTallGrass extends BlockFlower {
	protected BlockTallGrass(int i, int j) {
		super(i, j);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (j == 1) {
			return blockIndexInTexture;
		}
		if (j == 2) {
			return blockIndexInTexture + 16 + 1;
		}
		if (j == 0) {
			return blockIndexInTexture + 16;
		}
		else {
			return blockIndexInTexture;
		}
	}

	public int getBlockColor() {
		double d = 0.5D;
		double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d, d1);
	}

	public int getRenderColor(int i) {
		if (i == 0) {
			return 0xffffff;
		}
		else {
			return ColorizerFoliage.getFoliageColorBasic();
		}
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		if (l == 0) {
			return 0xffffff;
		}
		else {
			return iblockaccess.getWorldChunkManager().getBiomeGenAt(i, k).getGrassColorAtCoords(iblockaccess, i, j, k);
		}
	}

	public int idDropped(int i, Random random, int j) {
		if (random.nextInt(8) == 0) {
			return Item.seeds.shiftedIndex;
		}
		else {
			return -1;
		}
	}

	public int quantityDroppedWithBonus(int i, Random random) {
		return 1 + random.nextInt(i * 2 + 1);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if (!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.tallGrass, 1, l));
		}
		else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}
	}
}
