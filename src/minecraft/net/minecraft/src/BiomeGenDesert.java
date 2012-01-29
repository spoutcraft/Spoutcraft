package net.minecraft.src;

import java.util.List;

public class BiomeGenDesert extends BiomeGenBase {
	public BiomeGenDesert(int i) {
		super(i);
		spawnableCreatureList.clear();
		topBlock = (byte)Block.sand.blockID;
		fillerBlock = (byte)Block.sand.blockID;
		biomeDecorator.treesPerChunk = -999;
		biomeDecorator.deadBushPerChunk = 2;
		biomeDecorator.reedsPerChunk = 50;
		biomeDecorator.cactiPerChunk = 10;
	}
}
