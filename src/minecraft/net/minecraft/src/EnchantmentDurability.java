package net.minecraft.src;

public class EnchantmentDurability extends Enchantment {
	protected EnchantmentDurability(int i, int j) {
		super(i, j, EnumEnchantmentType.digger);
		setName("durability");
	}

	public int getMinEnchantability(int i) {
		return 5 + (i - 1) * 10;
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 3;
	}
}
