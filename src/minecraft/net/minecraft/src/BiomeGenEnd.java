package net.minecraft.src;

import java.util.List;

public class BiomeGenEnd extends BiomeGenBase {
	public BiomeGenEnd(int i) {
		super(i);
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityEnderman.class, 10, 4, 4));
		topBlock = (byte)Block.dirt.blockID;
		fillerBlock = (byte)Block.dirt.blockID;
		biomeDecorator = new BiomeEndDecorator(this);
	}

	public int getSkyColorByTemp(float f) {
		return 0;
	}
}
