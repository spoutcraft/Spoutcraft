package net.minecraft.src;

public class EnchantmentUntouching extends Enchantment {
	protected EnchantmentUntouching(int i, int j) {
		super(i, j, EnumEnchantmentType.digger);
		setName("untouching");
	}

	public int getMinEnchantability(int i) {
		return 25;
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean canApplyTogether(Enchantment enchantment) {
		return super.canApplyTogether(enchantment) && enchantment.effectId != fortune.effectId;
	}
}
