package net.minecraft.src;

public class EnchantmentArrowInfinite extends Enchantment {
	public EnchantmentArrowInfinite(int i, int j) {
		super(i, j, EnumEnchantmentType.bow);
		setName("arrowInfinite");
	}

	public int getMinEnchantability(int i) {
		return 20;
	}

	public int getMaxEnchantability(int i) {
		return 50;
	}

	public int getMaxLevel() {
		return 1;
	}
}
