package net.minecraft.src;

class EntityAITaskEntry {
	public EntityAIBase field_46114_a;
	public int field_46112_b;
	final EntityAITasks field_46113_c;

	public EntityAITaskEntry(EntityAITasks entityaitasks, int i, EntityAIBase entityaibase) {
		field_46113_c = entityaitasks;

		field_46112_b = i;
		field_46114_a = entityaibase;
	}
}
