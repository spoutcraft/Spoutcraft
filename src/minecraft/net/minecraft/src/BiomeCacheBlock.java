package net.minecraft.src;

public class BiomeCacheBlock {
	public float temperatureValues[];
	public float rainfallValues[];
	public BiomeGenBase biomes[];
	public int xPosition;
	public int zPosition;
	public long lastAccessTime;
	final BiomeCache biomeCache;

	public BiomeCacheBlock(BiomeCache biomecache, int i, int j) {
		biomeCache = biomecache;

		temperatureValues = new float[256];
		rainfallValues = new float[256];
		biomes = new BiomeGenBase[256];
		xPosition = i;
		zPosition = j;
		BiomeCache.getWorldChunkManager(biomecache).getTemperatures(temperatureValues, i << 4, j << 4, 16, 16);
		BiomeCache.getWorldChunkManager(biomecache).getRainfall(rainfallValues, i << 4, j << 4, 16, 16);
		BiomeCache.getWorldChunkManager(biomecache).getBiomeGenAt(biomes, i << 4, j << 4, 16, 16, false);
	}

	public BiomeGenBase getBiomeGenAt(int i, int j) {
		return biomes[i & 0xf | (j & 0xf) << 4];
	}

	public float getTemperature(int i, int j) {
		return temperatureValues[i & 0xf | (j & 0xf) << 4];
	}

	public float getRainfall(int i, int j) {
		return rainfallValues[i & 0xf | (j & 0xf) << 4];
	}
}
