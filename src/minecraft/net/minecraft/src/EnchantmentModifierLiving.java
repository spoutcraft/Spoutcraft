package net.minecraft.src;

final class EnchantmentModifierLiving
	implements IEnchantmentModifier {
	public int livingModifier;
	public EntityLiving entityLiving;

	private EnchantmentModifierLiving() {
	}

	public void calculateModifier(Enchantment enchantment, int i) {
		livingModifier += enchantment.calcModifierLiving(i, entityLiving);
	}

	EnchantmentModifierLiving(Empty3 empty3) {
		this();
	}
}
