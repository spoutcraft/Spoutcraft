package net.minecraft.src;

public class EnchantmentKnockback extends Enchantment {
	protected EnchantmentKnockback(int i, int j) {
		super(i, j, EnumEnchantmentType.weapon);
		setName("knockback");
	}

	public int getMinEnchantability(int i) {
		return 5 + 20 * (i - 1);
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 2;
	}
}
