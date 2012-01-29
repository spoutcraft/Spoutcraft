package net.minecraft.src;

import java.util.Random;

public class MapGenMineshaft extends MapGenStructure {
	public MapGenMineshaft() {
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		return rand.nextInt(100) == 0 && rand.nextInt(80) < Math.max(Math.abs(i), Math.abs(j));
	}

	protected StructureStart getStructureStart(int i, int j) {
		return new StructureMineshaftStart(worldObj, rand, i, j);
	}
}
