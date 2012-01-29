package net.minecraft.src;

final class StructureStrongholdPieceWeight2 extends StructureStrongholdPieceWeight {
	StructureStrongholdPieceWeight2(Class class1, int i, int j) {
		super(class1, i, j);
	}

	public boolean canSpawnMoreStructuresOfType(int i) {
		return super.canSpawnMoreStructuresOfType(i) && i > 4;
	}
}
