package net.minecraft.src;

class ThreadedChunkLoaderPending {
	public final ChunkCoordIntPair field_40739_a;
	public final NBTTagCompound field_40738_b;

	public ThreadedChunkLoaderPending(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
		field_40739_a = chunkcoordintpair;
		field_40738_b = nbttagcompound;
	}
}
