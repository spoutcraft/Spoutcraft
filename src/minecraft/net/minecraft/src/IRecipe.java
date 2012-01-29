package net.minecraft.src;

public interface IRecipe {
	public abstract boolean matches(InventoryCrafting inventorycrafting);

	public abstract ItemStack getCraftingResult(InventoryCrafting inventorycrafting);

	public abstract int getRecipeSize();

	public abstract ItemStack getRecipeOutput();
}
