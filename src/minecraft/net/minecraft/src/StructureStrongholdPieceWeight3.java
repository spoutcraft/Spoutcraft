package net.minecraft.src;

final class StructureStrongholdPieceWeight3 extends StructureStrongholdPieceWeight {
	StructureStrongholdPieceWeight3(Class class1, int i, int j) {
		super(class1, i, j);
	}

	public boolean canSpawnMoreStructuresOfType(int i) {
		return super.canSpawnMoreStructuresOfType(i) && i > 5;
	}
}
