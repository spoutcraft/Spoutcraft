package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;
import java.util.Random;

public class BiomeGenSwamp extends BiomeGenBase {
	protected BiomeGenSwamp(int par1) {
		super(par1);
		this.theBiomeDecorator.treesPerChunk = 2;
		this.theBiomeDecorator.flowersPerChunk = -999;
		this.theBiomeDecorator.deadBushPerChunk = 1;
		this.theBiomeDecorator.mushroomsPerChunk = 8;
		this.theBiomeDecorator.reedsPerChunk = 10;
		this.theBiomeDecorator.clayPerChunk = 1;
		this.theBiomeDecorator.waterlilyPerChunk = 4;
		this.waterColorMultiplier = 14745518;
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random) {
		return this.worldGeneratorSwamp;
	}

	/**
	 * Provides the basic grass color based on the biome temperature and rainfall
	 */
	public int getBiomeGrassColor() {
		double var1 = (double)this.getFloatTemperature();
		double var3 = (double)this.getFloatRainfall();
		return Colorizer.colorizeBiome(((ColorizerGrass.getGrassColor(var1, var3) & 16711422) + 5115470) / 2, Colorizer.COLOR_MAP_SWAMP_GRASS, var1, var3);
	}

	/**
	 * Provides the basic foliage color based on the biome temperature and rainfall
	 */
	public int getBiomeFoliageColor() {
		double var1 = (double)this.getFloatTemperature();
		double var3 = (double)this.getFloatRainfall();
		return Colorizer.colorizeBiome(((ColorizerFoliage.getFoliageColor(var1, var3) & 16711422) + 5115470) / 2, Colorizer.COLOR_MAP_SWAMP_FOLIAGE, var1, var3);
	}
}
