package net.minecraft.src;

class StructureStrongholdPieceWeight {
	public Class pieceClass;
	public final int pieceWeight;
	public int instancesSpawned;
	public int instancesLimit;

	public StructureStrongholdPieceWeight(Class class1, int i, int j) {
		pieceClass = class1;
		pieceWeight = i;
		instancesLimit = j;
	}

	public boolean canSpawnMoreStructuresOfType(int i) {
		return instancesLimit == 0 || instancesSpawned < instancesLimit;
	}

	public boolean canSpawnMoreStructures() {
		return instancesLimit == 0 || instancesSpawned < instancesLimit;
	}
}
