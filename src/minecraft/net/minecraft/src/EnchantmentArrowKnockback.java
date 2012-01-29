package net.minecraft.src;

public class EnchantmentArrowKnockback extends Enchantment {
	public EnchantmentArrowKnockback(int i, int j) {
		super(i, j, EnumEnchantmentType.bow);
		setName("arrowKnockback");
	}

	public int getMinEnchantability(int i) {
		return 12 + (i - 1) * 20;
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + 25;
	}

	public int getMaxLevel() {
		return 2;
	}
}
