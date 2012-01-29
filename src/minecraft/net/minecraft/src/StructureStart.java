package net.minecraft.src;

import java.util.*;

public abstract class StructureStart {
	protected LinkedList components;
	protected StructureBoundingBox boundingBox;

	protected StructureStart() {
		components = new LinkedList();
	}

	public StructureBoundingBox getBoundingBox() {
		return boundingBox;
	}

	public LinkedList func_40560_b() {
		return components;
	}

	public void generateStructure(World world, Random random, StructureBoundingBox structureboundingbox) {
		Iterator iterator = components.iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			StructureComponent structurecomponent = (StructureComponent)iterator.next();
			if (structurecomponent.getBoundingBox().intersectsWith(structureboundingbox) && !structurecomponent.addComponentParts(world, random, structureboundingbox)) {
				iterator.remove();
			}
		}
		while (true);
	}

	protected void updateBoundingBox() {
		boundingBox = StructureBoundingBox.getNewBoundingBox();
		StructureComponent structurecomponent;
		for (Iterator iterator = components.iterator(); iterator.hasNext(); boundingBox.resizeBoundingBoxTo(structurecomponent.getBoundingBox())) {
			structurecomponent = (StructureComponent)iterator.next();
		}
	}

	protected void markAvailableHeight(World world, Random random, int i) {
		int j = world.seaLevel - i;
		int k = boundingBox.getYSize() + 1;
		if (k < j) {
			k += random.nextInt(j - k);
		}
		int l = k - boundingBox.maxY;
		boundingBox.offset(0, l, 0);
		StructureComponent structurecomponent;
		for (Iterator iterator = components.iterator(); iterator.hasNext(); structurecomponent.getBoundingBox().offset(0, l, 0)) {
			structurecomponent = (StructureComponent)iterator.next();
		}
	}

	protected void func_40559_a(World world, Random random, int i, int j) {
		int k = ((j - i) + 1) - boundingBox.getYSize();
		int l = 1;
		if (k > 1) {
			l = i + random.nextInt(k);
		}
		else {
			l = i;
		}
		int i1 = l - boundingBox.minY;
		boundingBox.offset(0, i1, 0);
		StructureComponent structurecomponent;
		for (Iterator iterator = components.iterator(); iterator.hasNext(); structurecomponent.getBoundingBox().offset(0, i1, 0)) {
			structurecomponent = (StructureComponent)iterator.next();
		}
	}

	public boolean isSizeableStructure() {
		return true;
	}
}
