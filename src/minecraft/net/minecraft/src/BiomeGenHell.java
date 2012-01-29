package net.minecraft.src;

import java.util.List;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell(int i) {
		super(i);
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityGhast.class, 50, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityPigZombie.class, 100, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityMagmaCube.class, 1, 4, 4));
	}
}
