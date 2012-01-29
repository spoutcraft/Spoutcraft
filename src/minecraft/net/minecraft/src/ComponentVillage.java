package net.minecraft.src;

import java.util.List;
import java.util.Random;

abstract class ComponentVillage extends StructureComponent {
	private int villagersSpawned;

	protected ComponentVillage(int i) {
		super(i);
	}

	protected StructureComponent func_35077_a(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType());

			case 0:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType());

			case 1:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType());

			case 3:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType());
		}
		return null;
	}

	protected StructureComponent func_35076_b(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j) {
		switch (coordBaseMode) {
			case 2:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType());

			case 0:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType());

			case 1:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType());

			case 3:
				return StructureVillagePieces.getNextStructureComponent(componentvillagestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType());
		}
		return null;
	}

	protected int getAverageGroundLevel(World world, StructureBoundingBox structureboundingbox) {
		int i = 0;
		int j = 0;
		for (int k = boundingBox.minZ; k <= boundingBox.maxZ; k++) {
			for (int l = boundingBox.minX; l <= boundingBox.maxX; l++) {
				if (structureboundingbox.isVecInside(l, 64, k)) {
					i += Math.max(world.getTopSolidOrLiquidBlock(l, k), world.worldProvider.func_46066_g());
					j++;
				}
			}
		}

		if (j == 0) {
			return -1;
		}
		else {
			return i / j;
		}
	}

	protected static boolean canVillageGoDeeper(StructureBoundingBox structureboundingbox) {
		return structureboundingbox != null && structureboundingbox.minY > 10;
	}

	protected void spawnVillagers(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
		if (villagersSpawned >= l) {
			return;
		}
		int i1 = villagersSpawned;
		do {
			if (i1 >= l) {
				break;
			}
			int j1 = getXWithOffset(i + i1, k);
			int k1 = getYWithOffset(j);
			int l1 = getZWithOffset(i + i1, k);
			if (!structureboundingbox.isVecInside(j1, k1, l1)) {
				break;
			}
			villagersSpawned++;
			EntityVillager entityvillager = new EntityVillager(world, getVillagerType(i1));
			entityvillager.setLocationAndAngles((double)j1 + 0.5D, k1, (double)l1 + 0.5D, 0.0F, 0.0F);
			world.spawnEntityInWorld(entityvillager);
			i1++;
		}
		while (true);
	}

	protected int getVillagerType(int i) {
		return 0;
	}
}
