package net.minecraft.src;

import java.util.LinkedList;
import java.util.Random;

public class StructureMineshaftStart extends StructureStart {
	public StructureMineshaftStart(World world, Random random, int i, int j) {
		ComponentMineshaftRoom componentmineshaftroom = new ComponentMineshaftRoom(0, random, (i << 4) + 2, (j << 4) + 2);
		components.add(componentmineshaftroom);
		componentmineshaftroom.buildComponent(componentmineshaftroom, components, random);
		updateBoundingBox();
		markAvailableHeight(world, random, 10);
	}
}
