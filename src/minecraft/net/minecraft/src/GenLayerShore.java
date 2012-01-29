package net.minecraft.src;

public class GenLayerShore extends GenLayer {
	public GenLayerShore(long l, GenLayer genlayer) {
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
				if (k1 == BiomeGenBase.mushroomIsland.biomeID) {
					int l1 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
					int k2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
					int j3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
					int i4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
					if (l1 == BiomeGenBase.ocean.biomeID || k2 == BiomeGenBase.ocean.biomeID || j3 == BiomeGenBase.ocean.biomeID || i4 == BiomeGenBase.ocean.biomeID) {
						ai1[j1 + i1 * k] = BiomeGenBase.mushroomIslandShore.biomeID;
					}
					else {
						ai1[j1 + i1 * k] = k1;
					}
					continue;
				}
				if (k1 != BiomeGenBase.ocean.biomeID && k1 != BiomeGenBase.river.biomeID && k1 != BiomeGenBase.swampland.biomeID && k1 != BiomeGenBase.extremeHills.biomeID) {
					int i2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
					int l2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
					int k3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
					int j4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
					if (i2 == BiomeGenBase.ocean.biomeID || l2 == BiomeGenBase.ocean.biomeID || k3 == BiomeGenBase.ocean.biomeID || j4 == BiomeGenBase.ocean.biomeID) {
						ai1[j1 + i1 * k] = BiomeGenBase.field_46050_r.biomeID;
					}
					else {
						ai1[j1 + i1 * k] = k1;
					}
					continue;
				}
				if (k1 == BiomeGenBase.extremeHills.biomeID) {
					int j2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
					int i3 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
					int l3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
					int k4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
					if (j2 != BiomeGenBase.extremeHills.biomeID || i3 != BiomeGenBase.extremeHills.biomeID || l3 != BiomeGenBase.extremeHills.biomeID || k4 != BiomeGenBase.extremeHills.biomeID) {
						ai1[j1 + i1 * k] = BiomeGenBase.field_46046_v.biomeID;
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
