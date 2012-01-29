package net.minecraft.src;

public interface ICrafting {
	public abstract void updateCraftingInventorySlot(Container container, int i, ItemStack itemstack);

	public abstract void updateCraftingInventoryInfo(Container container, int i, int j);
}
