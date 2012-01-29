package net.minecraft.src;

public class GenLayerHills extends GenLayer {
	public GenLayerHills(long l, GenLayer genlayer) {
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
				if (nextInt(3) == 0) {
					int l1 = k1;
					if (k1 == BiomeGenBase.desert.biomeID) {
						l1 = BiomeGenBase.field_46049_s.biomeID;
					}
					else if (k1 == BiomeGenBase.forest.biomeID) {
						l1 = BiomeGenBase.field_46048_t.biomeID;
					}
					else if (k1 == BiomeGenBase.taiga.biomeID) {
						l1 = BiomeGenBase.field_46047_u.biomeID;
					}
					else if (k1 == BiomeGenBase.plains.biomeID) {
						l1 = BiomeGenBase.forest.biomeID;
					}
					else if (k1 == BiomeGenBase.icePlains.biomeID) {
						l1 = BiomeGenBase.iceMountains.biomeID;
					}
					if (l1 != k1) {
						int i2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
						int j2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
						int k2 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
						int l2 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
						if (i2 == k1 && j2 == k1 && k2 == k1 && l2 == k1) {
							ai1[j1 + i1 * k] = l1;
						}
						else {
							ai1[j1 + i1 * k] = k1;
						}
					}
					else {
						ai1[j1 + i1 * k] = k1;
					}
				}
				else {
					ai1[j1 + i1 * k] = k1;
				}
			}
		}

		return ai1;
	}
}
