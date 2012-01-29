package net.minecraft.src;

public class EnchantmentArrowFire extends Enchantment {
	public EnchantmentArrowFire(int i, int j) {
		super(i, j, EnumEnchantmentType.bow);
		setName("arrowFire");
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
