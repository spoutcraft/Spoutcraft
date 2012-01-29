package net.minecraft.src;

class WorldBlockPositionType {
	int posX;
	int posY;
	int posZ;
	int acceptCountdown;
	int blockID;
	int metadata;
	final WorldClient worldClient;

	public WorldBlockPositionType(WorldClient worldclient, int i, int j, int k, int l, int i1) {
		worldClient = worldclient;

		posX = i;
		posY = j;
		posZ = k;
		acceptCountdown = 80;
		blockID = l;
		metadata = i1;
	}
}
