package net.minecraft.src;

public enum EnumMobType {
	everything("everything", 0),
	mobs("mobs", 1),
	players("players", 2);

	private static final EnumMobType allMobTypes[] = (new EnumMobType[] {
		everything, mobs, players
	});

	private EnumMobType(String s, int i) {
	}
}
