package net.minecraft.src;

public class EnchantmentProtection extends Enchantment {
	private static final String protectionName[] = {
		"all", "fire", "fall", "explosion", "projectile"
	};
	private static final int baseEnchantability[] = {
		1, 10, 5, 5, 3
	};
	private static final int levelEnchantability[] = {
		16, 8, 6, 8, 6
	};
	private static final int thresholdEnchantability[] = {
		20, 12, 10, 12, 15
	};
	public final int protectionType;

	public EnchantmentProtection(int i, int j, int k) {
		super(i, j, EnumEnchantmentType.armor);
		protectionType = k;
		if (k == 2) {
			type = EnumEnchantmentType.armor_feet;
		}
	}

	public int getMinEnchantability(int i) {
		return baseEnchantability[protectionType] + (i - 1) * levelEnchantability[protectionType];
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + thresholdEnchantability[protectionType];
	}

	public int getMaxLevel() {
		return 4;
	}

	public int calcModifierDamage(int i, DamageSource damagesource) {
		if (damagesource.canHarmInCreative()) {
			return 0;
		}
		int j = (6 + i * i) / 2;
		if (protectionType == 0) {
			return j;
		}
		if (protectionType == 1 && damagesource.fireDamage()) {
			return j;
		}
		if (protectionType == 2 && damagesource == DamageSource.fall) {
			return j * 2;
		}
		if (protectionType == 3 && damagesource == DamageSource.explosion) {
			return j;
		}
		if (protectionType == 4 && damagesource.isProjectile()) {
			return j;
		}
		else {
			return 0;
		}
	}

	public String getName() {
		return (new StringBuilder()).append("enchantment.protect.").append(protectionName[protectionType]).toString();
	}

	public boolean canApplyTogether(Enchantment enchantment) {
		if (enchantment instanceof EnchantmentProtection) {
			EnchantmentProtection enchantmentprotection = (EnchantmentProtection)enchantment;
			if (enchantmentprotection.protectionType == protectionType) {
				return false;
			}
			return protectionType == 2 || enchantmentprotection.protectionType == 2;
		}
		else {
			return super.canApplyTogether(enchantment);
		}
	}
}
