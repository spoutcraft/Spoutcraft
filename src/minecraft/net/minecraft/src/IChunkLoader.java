package net.minecraft.src;

import java.io.IOException;

public interface IChunkLoader {
	public abstract Chunk loadChunk(World world, int i, int j)
	throws IOException;

	public abstract void saveChunk(World world, Chunk chunk)
	throws IOException;

	public abstract void saveExtraChunkData(World world, Chunk chunk)
	throws IOException;

	public abstract void func_814_a();

	public abstract void saveExtraData();
}
