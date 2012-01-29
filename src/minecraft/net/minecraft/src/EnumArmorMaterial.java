package net.minecraft.src;

public enum EnumArmorMaterial {
	CLOTH("CLOTH", 0, 5, new int[] {
		1, 3, 2, 1
	}, 15),
	CHAIN("CHAIN", 1, 15, new int[] {
		2, 5, 4, 1
	}, 12),
	IRON("IRON", 2, 15, new int[] {
		2, 6, 5, 2
	}, 9),
	GOLD("GOLD", 3, 7, new int[] {
		2, 5, 3, 1
	}, 25),
	DIAMOND("DIAMOND", 4, 33, new int[] {
		3, 8, 6, 3
	}, 10);

	private int maxDamageFactor;
	private int damageReductionAmountArray[];
	private int enchantability;
	private static final EnumArmorMaterial allArmorMaterials[] = (new EnumArmorMaterial[] {
		CLOTH, CHAIN, IRON, GOLD, DIAMOND
	});

	private EnumArmorMaterial(String s, int i, int j, int ai[], int k) {
		maxDamageFactor = j;
		damageReductionAmountArray = ai;
		enchantability = k;
	}

	public int func_40576_a(int i) {
		return ItemArmor.getMaxDamageArray()[i] * maxDamageFactor;
	}

	public int getDamageReductionAmount(int i) {
		return damageReductionAmountArray[i];
	}

	public int getEnchantability() {
		return enchantability;
	}
}
