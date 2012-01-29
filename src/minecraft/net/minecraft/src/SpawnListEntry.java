package net.minecraft.src;

public class SpawnListEntry extends WeightedRandomChoice {
	public Class entityClass;
	public int field_35591_b;
	public int field_35592_c;

	public SpawnListEntry(Class class1, int i, int j, int k) {
		super(i);
		entityClass = class1;
		field_35591_b = j;
		field_35592_c = k;
	}
}
