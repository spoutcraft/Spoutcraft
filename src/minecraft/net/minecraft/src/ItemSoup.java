package net.minecraft.src;

public class ItemSoup extends ItemFood {
	public ItemSoup(int i, int j) {
		super(i, j, false);
		setMaxStackSize(1);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		super.onFoodEaten(itemstack, world, entityplayer);
		return new ItemStack(Item.bowlEmpty);
	}
}
