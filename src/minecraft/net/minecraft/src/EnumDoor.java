package net.minecraft.src;

public enum EnumDoor {
	OPENING("OPENING", 0),
	WOOD_DOOR("WOOD_DOOR", 1),
	GRATES("GRATES", 2),
	IRON_DOOR("IRON_DOOR", 3);

	private static final EnumDoor allDoors[] = (new EnumDoor[] {
		OPENING, WOOD_DOOR, GRATES, IRON_DOOR
	});

	private EnumDoor(String s, int i) {
	}
}
