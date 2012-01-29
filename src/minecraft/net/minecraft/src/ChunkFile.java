package net.minecraft.src;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChunkFile
	implements Comparable {
	private final File chunkFile;
	private final int xChunk;
	private final int yChunk;

	public ChunkFile(File file) {
		chunkFile = file;
		Matcher matcher = ChunkFilePattern.dataFilenamePattern.matcher(file.getName());
		if (matcher.matches()) {
			xChunk = Integer.parseInt(matcher.group(1), 36);
			yChunk = Integer.parseInt(matcher.group(2), 36);
		}
		else {
			xChunk = 0;
			yChunk = 0;
		}
	}

	public int compareChunks(ChunkFile chunkfile) {
		int i = xChunk >> 5;
		int j = chunkfile.xChunk >> 5;
		if (i == j) {
			int k = yChunk >> 5;
			int l = chunkfile.yChunk >> 5;
			return k - l;
		}
		else {
			return i - j;
		}
	}

	public File getChunkFile() {
		return chunkFile;
	}

	public int getXChunk() {
		return xChunk;
	}

	public int getYChunk() {
		return yChunk;
	}

	public int compareTo(Object obj) {
		return compareChunks((ChunkFile)obj);
	}
}
