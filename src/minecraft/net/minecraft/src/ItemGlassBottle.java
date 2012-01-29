package net.minecraft.src;

public class ItemGlassBottle extends Item {
	public ItemGlassBottle(int i) {
		super(i);
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
			if (world.getBlockMaterial(i, j, k) == Material.water) {
				itemstack.stackSize--;
				if (itemstack.stackSize <= 0) {
					return new ItemStack(Item.potion);
				}
				if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.potion))) {
					entityplayer.dropPlayerItem(new ItemStack(Item.potion.shiftedIndex, 1, 0));
				}
			}
		}
		return itemstack;
	}
}
