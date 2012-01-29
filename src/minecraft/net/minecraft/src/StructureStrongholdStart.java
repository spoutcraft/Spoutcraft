package net.minecraft.src;

import java.util.*;

class StructureStrongholdStart extends StructureStart {
	public StructureStrongholdStart(World world, Random random, int i, int j) {
		StructureStrongholdPieces.prepareStructurePieces();
		ComponentStrongholdStairs2 componentstrongholdstairs2 = new ComponentStrongholdStairs2(0, random, (i << 4) + 2, (j << 4) + 2);
		components.add(componentstrongholdstairs2);
		componentstrongholdstairs2.buildComponent(componentstrongholdstairs2, components, random);
		StructureComponent structurecomponent;
		for (ArrayList arraylist = componentstrongholdstairs2.field_35037_b; !arraylist.isEmpty(); structurecomponent.buildComponent(componentstrongholdstairs2, components, random)) {
			int k = random.nextInt(arraylist.size());
			structurecomponent = (StructureComponent)arraylist.remove(k);
		}

		updateBoundingBox();
		markAvailableHeight(world, random, 10);
	}
}
