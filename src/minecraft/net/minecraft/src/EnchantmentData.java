package net.minecraft.src;

public class EnchantmentData extends WeightedRandomChoice {
	public final Enchantment enchantmentobj;
	public final int enchantmentLevel;

	public EnchantmentData(Enchantment enchantment, int i) {
		super(enchantment.getWeight());
		enchantmentobj = enchantment;
		enchantmentLevel = i;
	}
}
