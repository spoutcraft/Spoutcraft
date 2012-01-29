package net.minecraft.src;

public class EnchantmentWaterWorker extends Enchantment {
	public EnchantmentWaterWorker(int i, int j) {
		super(i, j, EnumEnchantmentType.armor_head);
		setName("waterWorker");
	}

	public int getMinEnchantability(int i) {
		return 1;
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + 40;
	}

	public int getMaxLevel() {
		return 1;
	}
}
