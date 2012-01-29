package net.minecraft.src;

public class GenLayerRiverInit extends GenLayer {
	public GenLayerRiverInit(long l, GenLayer genlayer) {
		super(l);
		parent = genlayer;
	}

	public int[] getInts(int i, int j, int k, int l) {
		int ai[] = parent.getInts(i, j, k, l);
		int ai1[] = IntCache.getIntCache(k * l);
		for (int i1 = 0; i1 < l; i1++) {
			for (int j1 = 0; j1 < k; j1++) {
				initChunkSeed(j1 + i, i1 + j);
				ai1[j1 + i1 * k] = ai[j1 + i1 * k] <= 0 ? 0 : nextInt(2) + 2;
			}
		}

		return ai1;
	}
}
