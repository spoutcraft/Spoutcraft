package net.minecraft.src;

import java.util.*;

class StructureVillageStart extends StructureStart {
	private boolean hasMoreThanTwoComponents;

	public StructureVillageStart(World world, Random random, int i, int j, int k) {
		hasMoreThanTwoComponents = false;
		int l = k;
		ArrayList arraylist = StructureVillagePieces.getStructureVillageWeightedPieceList(random, l);
		ComponentVillageStartPiece componentvillagestartpiece = new ComponentVillageStartPiece(world.getWorldChunkManager(), 0, random, (i << 4) + 2, (j << 4) + 2, arraylist, l);
		components.add(componentvillagestartpiece);
		componentvillagestartpiece.buildComponent(componentvillagestartpiece, components, random);
		ArrayList arraylist1 = componentvillagestartpiece.field_35106_f;
		for (ArrayList arraylist2 = componentvillagestartpiece.field_35108_e; !arraylist1.isEmpty() || !arraylist2.isEmpty();) {
			if (!arraylist1.isEmpty()) {
				int i1 = random.nextInt(arraylist1.size());
				StructureComponent structurecomponent = (StructureComponent)arraylist1.remove(i1);
				structurecomponent.buildComponent(componentvillagestartpiece, components, random);
			}
			else {
				int j1 = random.nextInt(arraylist2.size());
				StructureComponent structurecomponent1 = (StructureComponent)arraylist2.remove(j1);
				structurecomponent1.buildComponent(componentvillagestartpiece, components, random);
			}
		}

		updateBoundingBox();
		int k1 = 0;
		Iterator iterator = components.iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			StructureComponent structurecomponent2 = (StructureComponent)iterator.next();
			if (!(structurecomponent2 instanceof ComponentVillageRoadPiece)) {
				k1++;
			}
		}
		while (true);
		hasMoreThanTwoComponents = k1 > 2;
	}

	public boolean isSizeableStructure() {
		return hasMoreThanTwoComponents;
	}
}
