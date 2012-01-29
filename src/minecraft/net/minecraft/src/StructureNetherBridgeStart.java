package net.minecraft.src;

import java.util.*;

class StructureNetherBridgeStart extends StructureStart {
	public StructureNetherBridgeStart(World world, Random random, int i, int j) {
		ComponentNetherBridgeStartPiece componentnetherbridgestartpiece = new ComponentNetherBridgeStartPiece(random, (i << 4) + 2, (j << 4) + 2);
		components.add(componentnetherbridgestartpiece);
		componentnetherbridgestartpiece.buildComponent(componentnetherbridgestartpiece, components, random);
		StructureComponent structurecomponent;
		for (ArrayList arraylist = componentnetherbridgestartpiece.field_40034_d; !arraylist.isEmpty(); structurecomponent.buildComponent(componentnetherbridgestartpiece, components, random)) {
			int k = random.nextInt(arraylist.size());
			structurecomponent = (StructureComponent)arraylist.remove(k);
		}

		updateBoundingBox();
		func_40559_a(world, random, 48, 70);
	}
}
