package net.minecraft.src;

public class ItemBucketMilk extends Item {
	public ItemBucketMilk(int i) {
		super(i);
		setMaxStackSize(1);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.stackSize--;
		if (!world.multiplayerWorld) {
			entityplayer.func_40112_aN();
		}
		if (itemstack.stackSize <= 0) {
			return new ItemStack(Item.bucketEmpty);
		}
		else {
			return itemstack;
		}
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}
}
