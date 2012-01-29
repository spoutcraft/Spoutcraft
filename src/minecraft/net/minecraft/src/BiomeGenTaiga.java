package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
	public BiomeGenTaiga(int i) {
		super(i);
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityWolf.class, 8, 4, 4));
		biomeDecorator.treesPerChunk = 10;
		biomeDecorator.grassPerChunk = 1;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenTaiga1();
		}
		else {
			return new WorldGenTaiga2(false);
		}
	}
}
