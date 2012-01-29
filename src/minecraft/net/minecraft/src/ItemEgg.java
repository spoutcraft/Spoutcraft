package net.minecraft.src;

import java.util.Random;

public class ItemEgg extends Item {
	public ItemEgg(int i) {
		super(i);
		maxStackSize = 16;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (!entityplayer.capabilities.depleteBuckets) {
			itemstack.stackSize--;
		}
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.multiplayerWorld) {
			world.spawnEntityInWorld(new EntityEgg(world, entityplayer));
		}
		return itemstack;
	}
}
