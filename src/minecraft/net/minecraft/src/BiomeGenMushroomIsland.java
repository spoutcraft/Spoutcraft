package net.minecraft.src;

import java.util.List;

public class BiomeGenMushroomIsland extends BiomeGenBase {
	public BiomeGenMushroomIsland(int i) {
		super(i);
		biomeDecorator.treesPerChunk = -100;
		biomeDecorator.flowersPerChunk = -100;
		biomeDecorator.grassPerChunk = -100;
		biomeDecorator.mushroomsPerChunk = 1;
		biomeDecorator.bigMushroomsPerChunk = 1;
		topBlock = (byte)Block.mycelium.blockID;
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityMooshroom.class, 8, 4, 8));
	}
}
