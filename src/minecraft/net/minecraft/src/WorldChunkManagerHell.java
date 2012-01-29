package net.minecraft.src;

import java.util.*;

public class WorldChunkManagerHell extends WorldChunkManager {
	private BiomeGenBase biomeGenerator;
	private float hellTemperature;
	private float field_4199_g;

	public WorldChunkManagerHell(BiomeGenBase biomegenbase, float f, float f1) {
		biomeGenerator = biomegenbase;
		hellTemperature = f;
		field_4199_g = f1;
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkcoordintpair) {
		return biomeGenerator;
	}

	public BiomeGenBase getBiomeGenAt(int i, int j) {
		return biomeGenerator;
	}

	public BiomeGenBase[] loadRendererData(int i, int j, int k, int l) {
		rendererBiomeGenCache = loadBlockGeneratorData(rendererBiomeGenCache, i, j, k, l);
		return rendererBiomeGenCache;
	}

	public BiomeGenBase[] func_35557_b(BiomeGenBase abiomegenbase[], int i, int j, int k, int l) {
		if (abiomegenbase == null || abiomegenbase.length < k * l) {
			abiomegenbase = new BiomeGenBase[k * l];
		}
		Arrays.fill(abiomegenbase, 0, k * l, biomeGenerator);
		return abiomegenbase;
	}

	public float[] getTemperatures(float af[], int i, int j, int k, int l) {
		if (af == null || af.length < k * l) {
			af = new float[k * l];
		}
		Arrays.fill(af, 0, k * l, hellTemperature);
		return af;
	}

	public float[] initTemperatureCache(int i, int j, int k, int l) {
		return getTemperatures(new float[k * l], i, j, k, l);
	}

	public float[] getRainfall(float af[], int i, int j, int k, int l) {
		if (af == null || af.length < k * l) {
			af = new float[k * l];
		}
		Arrays.fill(af, 0, k * l, field_4199_g);
		return af;
	}

	public float getRainfall(int i, int j) {
		return field_4199_g;
	}

	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase abiomegenbase[], int i, int j, int k, int l) {
		if (abiomegenbase == null || abiomegenbase.length < k * l) {
			abiomegenbase = new BiomeGenBase[k * l];
		}
		Arrays.fill(abiomegenbase, 0, k * l, biomeGenerator);
		return abiomegenbase;
	}

	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase abiomegenbase[], int i, int j, int k, int l, boolean flag) {
		return loadBlockGeneratorData(abiomegenbase, i, j, k, l);
	}

	public ChunkPosition func_35556_a(int i, int j, int k, List list, Random random) {
		if (list.contains(biomeGenerator)) {
			return new ChunkPosition((i - k) + random.nextInt(k * 2 + 1), 0, (j - k) + random.nextInt(k * 2 + 1));
		}
		else {
			return null;
		}
	}

	public boolean areBiomesViable(int i, int j, int k, List list) {
		return list.contains(biomeGenerator);
	}
}
