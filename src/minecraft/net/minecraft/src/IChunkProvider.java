package net.minecraft.src;

import java.util.List;

public interface IChunkProvider {
	public abstract boolean chunkExists(int i, int j);

	public abstract Chunk provideChunk(int i, int j);

	public abstract Chunk loadChunk(int i, int j);

	public abstract void populate(IChunkProvider ichunkprovider, int i, int j);

	public abstract boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate);

	public abstract boolean unload100OldestChunks();

	public abstract boolean canSave();

	public abstract String makeString();

	public abstract List func_40377_a(EnumCreatureType enumcreaturetype, int i, int j, int k);

	public abstract ChunkPosition func_40376_a(World world, String s, int i, int j, int k);
}
