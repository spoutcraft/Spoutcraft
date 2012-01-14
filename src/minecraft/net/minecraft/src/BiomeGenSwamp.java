package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;//Spout HD
import java.util.Random;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.WorldGenerator;

public class BiomeGenSwamp extends BiomeGenBase {

	protected BiomeGenSwamp(int var1) {
		super(var1);
		this.biomeDecorator.treesPerChunk = 2;
		this.biomeDecorator.flowersPerChunk = -999;
		this.biomeDecorator.deadBushPerChunk = 1;
		this.biomeDecorator.mushroomsPerChunk = 8;
		this.biomeDecorator.reedsPerChunk = 10;
		this.biomeDecorator.clayPerChunk = 1;
		this.biomeDecorator.waterlilyPerChunk = 4;
		this.waterColorMultiplier = 14745518;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random var1) {
		return this.worldGenSwamp;
	}

	public int getGrassColorAtCoords(IBlockAccess var1, int var2, int var3, int var4) {
		double var5 = (double)var1.getWorldChunkManager().getTemperature(var2, var3, var4);
		double var7 = (double)var1.getWorldChunkManager().getRainfall(var2, var4);
		return Colorizer.colorizeBiome(((ColorizerGrass.getGrassColor(var5, var7) & 16711422) + 5115470) / 2, Colorizer.COLOR_MAP_SWAMP_GRASS, var5, var7); //Spout HD
	}

	public int getFoliageColorAtCoords(IBlockAccess var1, int var2, int var3, int var4) {
		double var5 = (double)var1.getWorldChunkManager().getTemperature(var2, var3, var4);
		double var7 = (double)var1.getWorldChunkManager().getRainfall(var2, var4);
		return Colorizer.colorizeBiome(((ColorizerFoliage.getFoliageColor(var5, var7) & 16711422) + 5115470) / 2, Colorizer.COLOR_MAP_SWAMP_FOLIAGE, var5, var7);//Spout HD
	}
}
