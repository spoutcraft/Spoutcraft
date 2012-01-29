package net.minecraft.src;

public class GenLayerSmooth extends GenLayer {
	public GenLayerSmooth(long l, GenLayer genlayer) {
		super(l);
		super.parent = genlayer;
	}

	public int[] getInts(int i, int j, int k, int l) {
		int i1 = i - 1;
		int j1 = j - 1;
		int k1 = k + 2;
		int l1 = l + 2;
		int ai[] = parent.getInts(i1, j1, k1, l1);
		int ai1[] = IntCache.getIntCache(k * l);
		for (int i2 = 0; i2 < l; i2++) {
			for (int j2 = 0; j2 < k; j2++) {
				int k2 = ai[j2 + 0 + (i2 + 1) * k1];
				int l2 = ai[j2 + 2 + (i2 + 1) * k1];
				int i3 = ai[j2 + 1 + (i2 + 0) * k1];
				int j3 = ai[j2 + 1 + (i2 + 2) * k1];
				int k3 = ai[j2 + 1 + (i2 + 1) * k1];
				if (k2 == l2 && i3 == j3) {
					initChunkSeed(j2 + i, i2 + j);
					if (nextInt(2) == 0) {
						k3 = k2;
					}
					else {
						k3 = i3;
					}
				}
				else {
					if (k2 == l2) {
						k3 = k2;
					}
					if (i3 == j3) {
						k3 = i3;
					}
				}
				ai1[j2 + i2 * k] = k3;
			}
		}

		return ai1;
	}
}
