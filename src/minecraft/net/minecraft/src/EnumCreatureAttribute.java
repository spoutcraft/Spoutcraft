package net.minecraft.src;

public enum EnumCreatureAttribute {
	UNDEFINED("UNDEFINED", 0),
	UNDEAD("UNDEAD", 1),
	ARTHROPOD("ARTHROPOD", 2);

	private static final EnumCreatureAttribute allCreatureAttributes[] = (new EnumCreatureAttribute[] {
		UNDEFINED, UNDEAD, ARTHROPOD
	});

	private EnumCreatureAttribute(String s, int i) {
	}
}
