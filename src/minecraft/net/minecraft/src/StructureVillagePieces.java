package net.minecraft.src;

import java.util.*;

public class StructureVillagePieces {
	public StructureVillagePieces() {
	}

	public static ArrayList getStructureVillageWeightedPieceList(Random random, int i) {
		ArrayList arraylist = new ArrayList();
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageHouse4_Garden.class, 4, MathHelper.getRandomIntegerInRange(random, 2 + i, 4 + i * 2)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageChurch.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageHouse1.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + i, 2 + i)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageWoodHut.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + i, 5 + i * 3)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageHall.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + i, 2 + i)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageField.class, 3, MathHelper.getRandomIntegerInRange(random, 1 + i, 4 + i)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageField2.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + i, 4 + i * 2)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageHouse2.class, 15, MathHelper.getRandomIntegerInRange(random, 0, 1 + i)));
		arraylist.add(new StructureVillagePieceWeight(net.minecraft.src.ComponentVillageHouse3.class, 8, MathHelper.getRandomIntegerInRange(random, 0 + i, 3 + i * 2)));
		Iterator iterator = arraylist.iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			if (((StructureVillagePieceWeight)iterator.next()).villagePiecesLimit == 0) {
				iterator.remove();
			}
		}
		while (true);
		return arraylist;
	}

	private static int getAvailablePieceWeight(ArrayList arraylist) {
		boolean flag = false;
		int i = 0;
		for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
			StructureVillagePieceWeight structurevillagepieceweight = (StructureVillagePieceWeight)iterator.next();
			if (structurevillagepieceweight.villagePiecesLimit > 0 && structurevillagepieceweight.villagePiecesSpawned < structurevillagepieceweight.villagePiecesLimit) {
				flag = true;
			}
			i += structurevillagepieceweight.villagePieceWeight;
		}

		return flag ? i : -1;
	}

	private static ComponentVillage getVillageComponentFromWeightedPiece(StructureVillagePieceWeight structurevillagepieceweight, List list, Random random, int i, int j, int k, int l, int i1) {
		Class class1 = structurevillagepieceweight.villagePieceClass;
		Object obj = null;
		if (class1 == (net.minecraft.src.ComponentVillageHouse4_Garden.class)) {
			obj = ComponentVillageHouse4_Garden.func_35082_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageChurch.class)) {
			obj = ComponentVillageChurch.func_35097_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageHouse1.class)) {
			obj = ComponentVillageHouse1.func_35095_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageWoodHut.class)) {
			obj = ComponentVillageWoodHut.func_35091_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageHall.class)) {
			obj = ComponentVillageHall.func_35078_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageField.class)) {
			obj = ComponentVillageField.func_35080_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageField2.class)) {
			obj = ComponentVillageField2.func_35089_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageHouse2.class)) {
			obj = ComponentVillageHouse2.func_35085_a(list, random, i, j, k, l, i1);
		}
		else if (class1 == (net.minecraft.src.ComponentVillageHouse3.class)) {
			obj = ComponentVillageHouse3.func_35101_a(list, random, i, j, k, l, i1);
		}
		return ((ComponentVillage) (obj));
	}

	private static ComponentVillage getNextVillageComponent(ComponentVillageStartPiece var0, List var1, Random var2, int var3, int var4, int var5, int var6, int var7) {
		int var8 = getAvailablePieceWeight(var0.structureVillageWeightedPieceList);
		if (var8 <= 0) {
			return null;
		}
		else {
			int var9 = 0;

			while (var9 < 5) {
				++var9;
				int var10 = var2.nextInt(var8);
				Iterator var11 = var0.structureVillageWeightedPieceList.iterator();

				while (var11.hasNext()) {
					StructureVillagePieceWeight var12 = (StructureVillagePieceWeight)var11.next();
					var10 -= var12.villagePieceWeight;
					if (var10 < 0) {
						if (!var12.canSpawnMoreVillagePiecesOfType(var7) || var12 == var0.structVillagePieceWeight && var0.structureVillageWeightedPieceList.size() > 1) {
							break;
						}

						ComponentVillage var13 = getVillageComponentFromWeightedPiece(var12, var1, var2, var3, var4, var5, var6, var7);
						if (var13 != null) {
							++var12.villagePiecesSpawned;
							var0.structVillagePieceWeight = var12;
							if (!var12.canSpawnMoreVillagePieces()) {
								var0.structureVillageWeightedPieceList.remove(var12);
							}

							return var13;
						}
					}
				}
			}

			StructureBoundingBox var14 = ComponentVillageTorch.func_35099_a(var1, var2, var3, var4, var5, var6);
			if (var14 != null) {
				return new ComponentVillageTorch(var7, var2, var14, var6);
			}
			else {
				return null;
			}
		}
	}

	private static StructureComponent getNextVillageStructureComponent(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
		if (i1 > 50) {
			return null;
		}
		if (Math.abs(i - componentvillagestartpiece.getBoundingBox().minX) > 112 || Math.abs(k - componentvillagestartpiece.getBoundingBox().minZ) > 112) {
			return null;
		}
		ComponentVillage componentvillage = getNextVillageComponent(componentvillagestartpiece, list, random, i, j, k, l, i1 + 1);
		if (componentvillage != null) {
			int j1 = (((StructureComponent) (componentvillage)).boundingBox.minX + ((StructureComponent) (componentvillage)).boundingBox.maxX) / 2;
			int k1 = (((StructureComponent) (componentvillage)).boundingBox.minZ + ((StructureComponent) (componentvillage)).boundingBox.maxZ) / 2;
			int l1 = ((StructureComponent) (componentvillage)).boundingBox.maxX - ((StructureComponent) (componentvillage)).boundingBox.minX;
			int i2 = ((StructureComponent) (componentvillage)).boundingBox.maxZ - ((StructureComponent) (componentvillage)).boundingBox.minZ;
			int j2 = l1 <= i2 ? i2 : l1;
			if (componentvillagestartpiece.getWorldChunkMngr().areBiomesViable(j1, k1, j2 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
				list.add(componentvillage);
				componentvillagestartpiece.field_35108_e.add(componentvillage);
				return componentvillage;
			}
		}
		return null;
	}

	private static StructureComponent getNextComponentVillagePath(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
		if (i1 > 3 + componentvillagestartpiece.field_35109_b) {
			return null;
		}
		if (Math.abs(i - componentvillagestartpiece.getBoundingBox().minX) > 112 || Math.abs(k - componentvillagestartpiece.getBoundingBox().minZ) > 112) {
			return null;
		}
		StructureBoundingBox structureboundingbox = ComponentVillagePathGen.func_35087_a(componentvillagestartpiece, list, random, i, j, k, l);
		if (structureboundingbox != null && structureboundingbox.minY > 10) {
			ComponentVillagePathGen componentvillagepathgen = new ComponentVillagePathGen(i1, random, structureboundingbox, l);
			int j1 = (((StructureComponent) (componentvillagepathgen)).boundingBox.minX + ((StructureComponent) (componentvillagepathgen)).boundingBox.maxX) / 2;
			int k1 = (((StructureComponent) (componentvillagepathgen)).boundingBox.minZ + ((StructureComponent) (componentvillagepathgen)).boundingBox.maxZ) / 2;
			int l1 = ((StructureComponent) (componentvillagepathgen)).boundingBox.maxX - ((StructureComponent) (componentvillagepathgen)).boundingBox.minX;
			int i2 = ((StructureComponent) (componentvillagepathgen)).boundingBox.maxZ - ((StructureComponent) (componentvillagepathgen)).boundingBox.minZ;
			int j2 = l1 <= i2 ? i2 : l1;
			if (componentvillagestartpiece.getWorldChunkMngr().areBiomesViable(j1, k1, j2 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
				list.add(componentvillagepathgen);
				componentvillagestartpiece.field_35106_f.add(componentvillagepathgen);
				return componentvillagepathgen;
			}
		}
		return null;
	}

	static StructureComponent getNextStructureComponent(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
		return getNextVillageStructureComponent(componentvillagestartpiece, list, random, i, j, k, l, i1);
	}

	static StructureComponent getNextStructureComponentVillagePath(ComponentVillageStartPiece componentvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
		return getNextComponentVillagePath(componentvillagestartpiece, list, random, i, j, k, l, i1);
	}
}
