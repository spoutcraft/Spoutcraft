package net.minecraft.src;

public class EnchantmentFireAspect extends Enchantment {
	protected EnchantmentFireAspect(int i, int j) {
		super(i, j, EnumEnchantmentType.weapon);
		setName("fire");
	}

	public int getMinEnchantability(int i) {
		return 10 + 20 * (i - 1);
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 2;
	}
}
