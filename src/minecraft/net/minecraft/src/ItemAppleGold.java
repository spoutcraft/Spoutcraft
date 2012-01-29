package net.minecraft.src;

public class ItemAppleGold extends ItemFood {
	public ItemAppleGold(int i, int j, float f, boolean flag) {
		super(i, j, f, flag);
	}

	public boolean hasEffect(ItemStack itemstack) {
		return true;
	}

	public EnumRarity getRarity(ItemStack itemstack) {
		return EnumRarity.epic;
	}
}
