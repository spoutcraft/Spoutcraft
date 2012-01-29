package net.minecraft.src;

import java.util.List;

public class BiomeGenBeach extends BiomeGenBase {
	public BiomeGenBeach(int i) {
		super(i);
		spawnableCreatureList.clear();
		topBlock = (byte)Block.sand.blockID;
		fillerBlock = (byte)Block.sand.blockID;
		biomeDecorator.treesPerChunk = -999;
		biomeDecorator.deadBushPerChunk = 0;
		biomeDecorator.reedsPerChunk = 0;
		biomeDecorator.cactiPerChunk = 0;
	}
}
