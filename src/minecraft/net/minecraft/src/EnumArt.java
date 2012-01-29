package net.minecraft.src;

public enum EnumArt {
	Kebab("Kebab", 0, "Kebab", 16, 16, 0, 0),
	Aztec("Aztec", 1, "Aztec", 16, 16, 16, 0),
	Alban("Alban", 2, "Alban", 16, 16, 32, 0),
	Aztec2("Aztec2", 3, "Aztec2", 16, 16, 48, 0),
	Bomb("Bomb", 4, "Bomb", 16, 16, 64, 0),
	Plant("Plant", 5, "Plant", 16, 16, 80, 0),
	Wasteland("Wasteland", 6, "Wasteland", 16, 16, 96, 0),
	Pool("Pool", 7, "Pool", 32, 16, 0, 32),
	Courbet("Courbet", 8, "Courbet", 32, 16, 32, 32),
	Sea("Sea", 9, "Sea", 32, 16, 64, 32),
	Sunset("Sunset", 10, "Sunset", 32, 16, 96, 32),
	Creebet("Creebet", 11, "Creebet", 32, 16, 128, 32),
	Wanderer("Wanderer", 12, "Wanderer", 16, 32, 0, 64),
	Graham("Graham", 13, "Graham", 16, 32, 16, 64),
	Match("Match", 14, "Match", 32, 32, 0, 128),
	Bust("Bust", 15, "Bust", 32, 32, 32, 128),
	Stage("Stage", 16, "Stage", 32, 32, 64, 128),
	Void("Void", 17, "Void", 32, 32, 96, 128),
	SkullAndRoses("SkullAndRoses", 18, "SkullAndRoses", 32, 32, 128, 128),
	Fighters("Fighters", 19, "Fighters", 64, 32, 0, 96),
	Pointer("Pointer", 20, "Pointer", 64, 64, 0, 192),
	Pigscene("Pigscene", 21, "Pigscene", 64, 64, 64, 192),
	BurningSkull("BurningSkull", 22, "BurningSkull", 64, 64, 128, 192),
	Skeleton("Skeleton", 23, "Skeleton", 64, 48, 192, 64),
	DonkeyKong("DonkeyKong", 24, "DonkeyKong", 64, 48, 192, 112);

	public static final int maxArtTitleLength = "SkullAndRoses".length();
	public final String title;
	public final int sizeX;
	public final int sizeY;
	public final int offsetX;
	public final int offsetY;
	private static final EnumArt allArt[] = (new EnumArt[] {
		Kebab, Aztec, Alban, Aztec2, Bomb, Plant, Wasteland, Pool, Courbet, Sea,
		Sunset, Creebet, Wanderer, Graham, Match, Bust, Stage, Void, SkullAndRoses, Fighters,
		Pointer, Pigscene, BurningSkull, Skeleton, DonkeyKong
	});

	private EnumArt(String s, int i, String s1, int j, int k, int l, int i1) {
		title = s1;
		sizeX = j;
		sizeY = k;
		offsetX = l;
		offsetY = i1;
	}
}
