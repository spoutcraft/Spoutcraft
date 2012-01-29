package net.minecraft.src;

public class EnchantmentDamage extends Enchantment {
	private static final String protectionName[] = {
		"all", "undead", "arthropods"
	};
	private static final int baseEnchantability[] = {
		1, 5, 5
	};
	private static final int levelEnchantability[] = {
		16, 8, 8
	};
	private static final int thresholdEnchantability[] = {
		20, 20, 20
	};
	public final int damageType;

	public EnchantmentDamage(int i, int j, int k) {
		super(i, j, EnumEnchantmentType.weapon);
		damageType = k;
	}

	public int getMinEnchantability(int i) {
		return baseEnchantability[damageType] + (i - 1) * levelEnchantability[damageType];
	}

	public int getMaxEnchantability(int i) {
		return getMinEnchantability(i) + thresholdEnchantability[damageType];
	}

	public int getMaxLevel() {
		return 5;
	}

	public int calcModifierLiving(int i, EntityLiving entityliving) {
		if (damageType == 0) {
			return i * 3;
		}
		if (damageType == 1 && entityliving.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			return i * 4;
		}
		if (damageType == 2 && entityliving.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
			return i * 4;
		}
		else {
			return 0;
		}
	}

	public String getName() {
		return (new StringBuilder()).append("enchantment.damage.").append(protectionName[damageType]).toString();
	}

	public boolean canApplyTogether(Enchantment enchantment) {
		return !(enchantment instanceof EnchantmentDamage);
	}
}
