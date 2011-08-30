package org.spoutcraft.api.entity;

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.inventory.PlayerInventory;

public interface HumanEntity extends LivingEntity, AnimalTamer {

	public String getName();

	public PlayerInventory getInventory();

	public ItemStack getItemInHand();

	public void setItemInHand(ItemStack item);

	public boolean isSleeping();

	public int getSleepTicks();

}
