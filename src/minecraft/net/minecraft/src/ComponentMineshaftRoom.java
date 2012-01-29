package net.minecraft.src;

import java.util.*;

public class ComponentMineshaftRoom extends StructureComponent {
	private LinkedList field_35065_a;

	public ComponentMineshaftRoom(int i, Random random, int j, int k) {
		super(i);
		field_35065_a = new LinkedList();
		boundingBox = new StructureBoundingBox(j, 50, k, j + 7 + random.nextInt(6), 54 + random.nextInt(6), k + 7 + random.nextInt(6));
	}

	public void buildComponent(StructureComponent structurecomponent, List list, Random random) {
		int i = getComponentType();
		int j1 = boundingBox.getYSize() - 3 - 1;
		if (j1 <= 0) {
			j1 = 1;
		}
		for (int j = 0; j < boundingBox.getXSize(); j += 4) {
			j += random.nextInt(boundingBox.getXSize());
			if (j + 3 > boundingBox.getXSize()) {
				break;
			}
			StructureComponent structurecomponent1 = StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + j, boundingBox.minY + random.nextInt(j1) + 1, boundingBox.minZ - 1, 2, i);
			if (structurecomponent1 != null) {
				StructureBoundingBox structureboundingbox = structurecomponent1.getBoundingBox();
				field_35065_a.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, boundingBox.minZ + 1));
			}
		}

		for (int k = 0; k < boundingBox.getXSize(); k += 4) {
			k += random.nextInt(boundingBox.getXSize());
			if (k + 3 > boundingBox.getXSize()) {
				break;
			}
			StructureComponent structurecomponent2 = StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX + k, boundingBox.minY + random.nextInt(j1) + 1, boundingBox.maxZ + 1, 0, i);
			if (structurecomponent2 != null) {
				StructureBoundingBox structureboundingbox1 = structurecomponent2.getBoundingBox();
				field_35065_a.add(new StructureBoundingBox(structureboundingbox1.minX, structureboundingbox1.minY, boundingBox.maxZ - 1, structureboundingbox1.maxX, structureboundingbox1.maxY, boundingBox.maxZ));
			}
		}

		for (int l = 0; l < boundingBox.getZSize(); l += 4) {
			l += random.nextInt(boundingBox.getZSize());
			if (l + 3 > boundingBox.getZSize()) {
				break;
			}
			StructureComponent structurecomponent3 = StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.minX - 1, boundingBox.minY + random.nextInt(j1) + 1, boundingBox.minZ + l, 1, i);
			if (structurecomponent3 != null) {
				StructureBoundingBox structureboundingbox2 = structurecomponent3.getBoundingBox();
				field_35065_a.add(new StructureBoundingBox(boundingBox.minX, structureboundingbox2.minY, structureboundingbox2.minZ, boundingBox.minX + 1, structureboundingbox2.maxY, structureboundingbox2.maxZ));
			}
		}

		for (int i1 = 0; i1 < boundingBox.getZSize(); i1 += 4) {
			i1 += random.nextInt(boundingBox.getZSize());
			if (i1 + 3 > boundingBox.getZSize()) {
				break;
			}
			StructureComponent structurecomponent4 = StructureMineshaftPieces.getNextComponent(structurecomponent, list, random, boundingBox.maxX + 1, boundingBox.minY + random.nextInt(j1) + 1, boundingBox.minZ + i1, 3, i);
			if (structurecomponent4 != null) {
				StructureBoundingBox structureboundingbox3 = structurecomponent4.getBoundingBox();
				field_35065_a.add(new StructureBoundingBox(boundingBox.maxX - 1, structureboundingbox3.minY, structureboundingbox3.minZ, boundingBox.maxX, structureboundingbox3.maxY, structureboundingbox3.maxZ));
			}
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		if (isLiquidInStructureBoundingBox(world, structureboundingbox)) {
			return false;
		}
		fillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ, Block.dirt.blockID, 0, true);
		fillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.minY + 1, boundingBox.minZ, boundingBox.maxX, Math.min(boundingBox.minY + 3, boundingBox.maxY), boundingBox.maxZ, 0, 0, false);
		StructureBoundingBox structureboundingbox1;
		for (Iterator iterator = field_35065_a.iterator(); iterator.hasNext(); fillWithBlocks(world, structureboundingbox, structureboundingbox1.minX, structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY, structureboundingbox1.maxZ, 0, 0, false)) {
			structureboundingbox1 = (StructureBoundingBox)iterator.next();
		}

		randomlyRareFillWithBlocks(world, structureboundingbox, boundingBox.minX, boundingBox.minY + 4, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, 0, false);
		return true;
	}
}
