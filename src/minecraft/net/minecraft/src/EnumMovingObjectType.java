package net.minecraft.src;

public enum EnumMovingObjectType {
	TILE("TILE", 0),
	ENTITY("ENTITY", 1);

	private static final EnumMovingObjectType allMovingObjectTypes[] = (new EnumMovingObjectType[] {
		TILE, ENTITY
	});

	private EnumMovingObjectType(String s, int i) {
	}
}
