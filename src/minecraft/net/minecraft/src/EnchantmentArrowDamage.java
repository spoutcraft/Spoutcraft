package net.minecraft.src;

public class EnchantmentArrowDamage extends Enchantment {
	public EnchantmentArrowDamage(int i, int j) {
		super(i, j, EnumEnchantmentType.bow);
		setName("arrowDamage");
	}

	public int getMinEnchantability(int i) {
		return 1 + (i - 1) * 10;
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + 15;
	}

	public int getMaxLevel() {
		return 5;
	}
}
