package net.minecraft.src;

public class GenLayerSwampRivers extends GenLayer {
	public GenLayerSwampRivers(long l, GenLayer genlayer) {
		super(l);
		parent = genlayer;
	}

	public int[] getInts(int i, int j, int k, int l) {
		int ai[] = parent.getInts(i - 1, j - 1, k + 2, l + 2);
		int ai1[] = IntCache.getIntCache(k * l);
		for (int i1 = 0; i1 < l; i1++) {
			for (int j1 = 0; j1 < k; j1++) {
				initChunkSeed(j1 + i, i1 + j);
				int k1 = ai[j1 + 1 + (i1 + 1) * (k + 2)];
				if (k1 == BiomeGenBase.swampland.biomeID && nextInt(6) == 0) {
					ai1[j1 + i1 * k] = BiomeGenBase.river.biomeID;
				}
				else {
					ai1[j1 + i1 * k] = k1;
				}
			}
		}

		return ai1;
	}
}
