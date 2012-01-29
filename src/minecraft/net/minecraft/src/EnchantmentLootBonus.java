package net.minecraft.src;

public class EnchantmentLootBonus extends Enchantment {
	protected EnchantmentLootBonus(int i, int j, EnumEnchantmentType enumenchantmenttype) {
		super(i, j, enumenchantmenttype);
		setName("lootBonus");
		if (enumenchantmenttype == EnumEnchantmentType.digger) {
			setName("lootBonusDigger");
		}
	}

	public int getMinEnchantability(int i) {
		return 20 + (i - 1) * 12;
	}

	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean canApplyTogether(Enchantment enchantment) {
		return super.canApplyTogether(enchantment) && enchantment.effectId != silkTouch.effectId;
	}
}
