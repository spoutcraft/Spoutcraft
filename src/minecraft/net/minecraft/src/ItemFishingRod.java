package net.minecraft.src;

import java.util.Random;

public class ItemFishingRod extends Item {
	public ItemFishingRod(int i) {
		super(i);
		setMaxDamage(64);
		setMaxStackSize(1);
	}

	public boolean isFull3D() {
		return true;
	}

	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (entityplayer.fishEntity != null) {
			int i = entityplayer.fishEntity.catchFish();
			itemstack.damageItem(i, entityplayer);
			entityplayer.swingItem();
		}
		else {
			world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.multiplayerWorld) {
				world.spawnEntityInWorld(new EntityFishHook(world, entityplayer));
			}
			entityplayer.swingItem();
		}
		return itemstack;
	}
}
