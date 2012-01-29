package net.minecraft.src;

public enum EnumRarity {
	common("common", 0, 15, "Common"),
	uncommon("uncommon", 1, 14, "Uncommon"),
	rare("rare", 2, 11, "Rare"),
	epic("epic", 3, 13, "Epic");

	public final int field_40535_e;
	public final String field_40532_f;
	private static final EnumRarity allRarities[] = (new EnumRarity[] {
		common, uncommon, rare, epic
	});

	private EnumRarity(String s, int i, int j, String s1) {
		field_40535_e = j;
		field_40532_f = s1;
	}
}
