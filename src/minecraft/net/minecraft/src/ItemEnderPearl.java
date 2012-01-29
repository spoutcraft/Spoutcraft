package net.minecraft.src;

import java.util.Random;

public class ItemEnderPearl extends Item {
	public ItemEnderPearl(int i) {
		super(i);
		maxStackSize = 16;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (entityplayer.capabilities.depleteBuckets) {
			return itemstack;
		}
		itemstack.stackSize--;
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.multiplayerWorld) {
			world.spawnEntityInWorld(new EntityEnderPearl(world, entityplayer));
		}
		return itemstack;
	}
}
