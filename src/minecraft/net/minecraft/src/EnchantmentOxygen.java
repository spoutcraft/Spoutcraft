package net.minecraft.src;

public class EnchantmentOxygen extends Enchantment {
	public EnchantmentOxygen(int i, int j) {
		super(i, j, EnumEnchantmentType.armor_head);
		setName("oxygen");
	}

	public int getMinEnchantability(int i) {
		return 10 * i;
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + 30;
	}

	public int getMaxLevel() {
		return 3;
	}
}
