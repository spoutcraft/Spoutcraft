package net.minecraft.src;

public class ItemArmor extends Item {
	private static final int maxDamageArray[] = {
		11, 16, 15, 13
	};
	public final int armorType;
	public final int damageReduceAmount;
	public final int renderIndex;
	private final EnumArmorMaterial material;

	public ItemArmor(int i, EnumArmorMaterial enumarmormaterial, int j, int k) {
		super(i);
		material = enumarmormaterial;
		armorType = k;
		renderIndex = j;
		damageReduceAmount = enumarmormaterial.getDamageReductionAmount(k);
		setMaxDamage(enumarmormaterial.func_40576_a(k));
		maxStackSize = 1;
	}

	public int getItemEnchantability() {
		return material.getEnchantability();
	}

	static int[] getMaxDamageArray() {
		return maxDamageArray;
	}
}
