package net.minecraft.src;

public class ChunkCoordinates
	implements Comparable {
	public int posX;
	public int posY;
	public int posZ;

	public ChunkCoordinates() {
	}

	public ChunkCoordinates(int i, int j, int k) {
		posX = i;
		posY = j;
		posZ = k;
	}

	public ChunkCoordinates(ChunkCoordinates chunkcoordinates) {
		posX = chunkcoordinates.posX;
		posY = chunkcoordinates.posY;
		posZ = chunkcoordinates.posZ;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ChunkCoordinates)) {
			return false;
		}
		else {
			ChunkCoordinates chunkcoordinates = (ChunkCoordinates)obj;
			return posX == chunkcoordinates.posX && posY == chunkcoordinates.posY && posZ == chunkcoordinates.posZ;
		}
	}

	public int hashCode() {
		return posX + posZ << 8 + posY << 16;
	}

	public int compareChunkCoordinate(ChunkCoordinates chunkcoordinates) {
		if (posY == chunkcoordinates.posY) {
			if (posZ == chunkcoordinates.posZ) {
				return posX - chunkcoordinates.posX;
			}
			else {
				return posZ - chunkcoordinates.posZ;
			}
		}
		else {
			return posY - chunkcoordinates.posY;
		}
	}

	public double getSqDistanceTo(int i, int j, int k) {
		int l = posX - i;
		int i1 = posY - j;
		int j1 = posZ - k;
		return Math.sqrt(l * l + i1 * i1 + j1 * j1);
	}

	public int compareTo(Object obj) {
		return compareChunkCoordinate((ChunkCoordinates)obj);
	}
}
