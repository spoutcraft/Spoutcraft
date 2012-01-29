package net.minecraft.src;

public class GenLayerIsland extends GenLayer {
	public GenLayerIsland(long l, GenLayer genlayer) {
		super(l);
		parent = genlayer;
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
				int k2 = ai[j2 + 0 + (i2 + 0) * k1];
				int l2 = ai[j2 + 2 + (i2 + 0) * k1];
				int i3 = ai[j2 + 0 + (i2 + 2) * k1];
				int j3 = ai[j2 + 2 + (i2 + 2) * k1];
				int k3 = ai[j2 + 1 + (i2 + 1) * k1];
				initChunkSeed(j2 + i, i2 + j);
				if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0)) {
					int l3 = 1;
					int i4 = 1;
					if (k2 != 0 && nextInt(l3++) == 0) {
						i4 = k2;
					}
					if (l2 != 0 && nextInt(l3++) == 0) {
						i4 = l2;
					}
					if (i3 != 0 && nextInt(l3++) == 0) {
						i4 = i3;
					}
					if (j3 != 0 && nextInt(l3++) == 0) {
						i4 = j3;
					}
					if (nextInt(3) == 0) {
						ai1[j2 + i2 * k] = i4;
						continue;
					}
					if (i4 == BiomeGenBase.icePlains.biomeID) {
						ai1[j2 + i2 * k] = BiomeGenBase.frozenOcean.biomeID;
					}
					else {
						ai1[j2 + i2 * k] = 0;
					}
					continue;
				}
				if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0)) {
					if (nextInt(5) == 0) {
						if (k3 == BiomeGenBase.icePlains.biomeID) {
							ai1[j2 + i2 * k] = BiomeGenBase.frozenOcean.biomeID;
						}
						else {
							ai1[j2 + i2 * k] = 0;
						}
					}
					else {
						ai1[j2 + i2 * k] = k3;
					}
				}
				else {
					ai1[j2 + i2 * k] = k3;
				}
			}
		}

		return ai1;
	}
}
