package net.minecraft.src;

public class EnchantmentDigging extends Enchantment {
	protected EnchantmentDigging(int i, int j) {
		super(i, j, EnumEnchantmentType.digger);
		setName("digging");
	}

	public int getMinEnchantability(int i) {
		return 1 + 15 * (i - 1);
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 5;
	}
}
