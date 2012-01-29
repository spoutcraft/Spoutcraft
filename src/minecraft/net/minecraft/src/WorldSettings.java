package net.minecraft.src;

public final class WorldSettings {
	private final long seed;
	private final int gameType;
	private final boolean mapFeaturesEnabled;
	private final boolean hardcoreEnabled;
	private final EnumWorldType field_46108_e;

	public WorldSettings(long l, int i, boolean flag, boolean flag1, EnumWorldType enumworldtype) {
		seed = l;
		gameType = i;
		mapFeaturesEnabled = flag;
		hardcoreEnabled = flag1;
		field_46108_e = enumworldtype;
	}

	public long getSeed() {
		return seed;
	}

	public int getGameType() {
		return gameType;
	}

	public boolean getHardcoreEnabled() {
		return hardcoreEnabled;
	}

	public boolean isMapFeaturesEnabled() {
		return mapFeaturesEnabled;
	}

	public EnumWorldType func_46107_e() {
		return field_46108_e;
	}
}
