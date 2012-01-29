package net.minecraft.src;

public class ItemSaddle extends Item {
	public ItemSaddle(int i) {
		super(i);
		maxStackSize = 1;
	}

	public void useItemOnEntity(ItemStack itemstack, EntityLiving entityliving) {
		if (entityliving instanceof EntityPig) {
			EntityPig entitypig = (EntityPig)entityliving;
			if (!entitypig.getSaddled()) {
				entitypig.setSaddled(true);
				itemstack.stackSize--;
			}
		}
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		useItemOnEntity(itemstack, entityliving);
		return true;
	}
}
