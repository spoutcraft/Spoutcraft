package net.minecraft.src;

public enum EnumCreatureType {
	monster("monster", 0, net.minecraft.src.IMob.class, 70, Material.air, false),
	creature("creature", 1, net.minecraft.src.EntityAnimal.class, 15, Material.air, true),
	waterCreature("waterCreature", 2, net.minecraft.src.EntityWaterMob.class, 5, Material.water, true);

	private final Class creatureClass;
	private final int maxNumberOfCreature;
	private final Material creatureMaterial;
	private final boolean isPeacefulCreature;
	private static final EnumCreatureType allCreatureTypes[] = (new EnumCreatureType[] {
		monster, creature, waterCreature
	});

	private EnumCreatureType(String s, int i, Class class1, int j, Material material, boolean flag) {
		creatureClass = class1;
		maxNumberOfCreature = j;
		creatureMaterial = material;
		isPeacefulCreature = flag;
	}

	public Class getCreatureClass() {
		return creatureClass;
	}

	public int getMaxNumberOfCreature() {
		return maxNumberOfCreature;
	}

	public Material getCreatureMaterial() {
		return creatureMaterial;
	}

	public boolean getPeacefulCreature() {
		return isPeacefulCreature;
	}
}
