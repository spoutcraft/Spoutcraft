package net.minecraft.src;

public class ItemLilyPad extends ItemColored {
	public ItemLilyPad(int i) {
		super(i, false);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		MovingObjectPosition movingobjectposition = func_40402_a(world, entityplayer, true);
		if (movingobjectposition == null) {
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			if (!world.canMineBlock(entityplayer, i, j, k)) {
				return itemstack;
			}
			if (!entityplayer.canPlayerEdit(i, j, k)) {
				return itemstack;
			}
			if (world.getBlockMaterial(i, j, k) == Material.water && world.getBlockMetadata(i, j, k) == 0 && world.isAirBlock(i, j + 1, k)) {
				world.setBlockWithNotify(i, j + 1, k, Block.waterlily.blockID);
				if (!entityplayer.capabilities.depleteBuckets) {
					itemstack.stackSize--;
				}
			}
		}
		return itemstack;
	}

	public int getColorFromDamage(int i, int j) {
		return Block.waterlily.getRenderColor(i);
	}
}
