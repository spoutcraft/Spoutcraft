package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest(int i) {
		super(i);
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityWolf.class, 5, 4, 4));
		biomeDecorator.treesPerChunk = 10;
		biomeDecorator.grassPerChunk = 2;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random) {
		if (random.nextInt(5) == 0) {
			return worldGenForest;
		}
		if (random.nextInt(10) == 0) {
			return worldGenBigTree;
		}
		else {
			return worldGenTrees;
		}
	}
}
