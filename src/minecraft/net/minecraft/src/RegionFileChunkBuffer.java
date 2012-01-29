package net.minecraft.src;

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer extends ByteArrayOutputStream {
	private int chunkX;
	private int chunkZ;
	final RegionFile regionFile;

	public RegionFileChunkBuffer(RegionFile regionfile, int i, int j) {
		super(8096);
		regionFile = regionfile;
		chunkX = i;
		chunkZ = j;
	}

	public void close() {
		regionFile.write(chunkX, chunkZ, buf, count);
	}
}
