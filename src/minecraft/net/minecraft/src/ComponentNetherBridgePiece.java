package net.minecraft.src;

import java.util.*;

abstract class ComponentNetherBridgePiece extends StructureComponent {
	protected ComponentNetherBridgePiece(int i) {
		super(i);
	}

	private int func_40017_a(List list) {
		boolean flag = false;
		int i = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			StructureNetherBridgePieceWeight structurenetherbridgepieceweight = (StructureNetherBridgePieceWeight)iterator.next();
			if (structurenetherbridgepieceweight.field_40695_d > 0 && structurenetherbridgepieceweight.field_40698_c < structurenetherbridgepieceweight.field_40695_d) {
				flag = true;
			}
			i += structurenetherbridgepieceweight.field_40697_b;
		}

		return flag ? i : -1;
	}

	private ComponentNetherBridgePiece func_40020_a(ComponentNetherBridgeStartPiece var1, List var2, List var3, Random var4, int var5, int var6, int var7, int var8, int var9) {
		int var10 = this.func_40017_a(var2);
		boolean var11 = var10 > 0 && var9 <= 30;
		int var12 = 0;

		while (var12 < 5 && var11) {
			++var12;
			int var13 = var4.nextInt(var10);
			Iterator var14 = var2.iterator();

			while (var14.hasNext()) {
				StructureNetherBridgePieceWeight var15 = (StructureNetherBridgePieceWeight)var14.next();
				var13 -= var15.field_40697_b;
				if (var13 < 0) {
					if (!var15.func_40693_a(var9) || var15 == var1.field_40037_a && !var15.field_40696_e) {
						break;
					}

					ComponentNetherBridgePiece var16 = StructureNetherBridgePieces.func_40688_a(var15, var3, var4, var5, var6, var7, var8, var9);
					if (var16 != null) {
						++var15.field_40698_c;
						var1.field_40037_a = var15;
						if (!var15.func_40694_a()) {
							var2.remove(var15);
						}

						return var16;
					}
				}
			}
		}

		StructureNetherBridgeEnd var17 = StructureNetherBridgeEnd.func_40023_a(var3, var4, var5, var6, var7, var8, var9);
		return var17;
	}

	private StructureComponent func_40018_a(ComponentNetherBridgeStartPiece componentnetherbridgestartpiece, List list, Random random, int i, int j, int k, int l,
	        int i1, boolean flag) {
		if (Math.abs(i - componentnetherbridgestartpiece.getBoundingBox().minX) > 112 || Math.abs(k - componentnetherbridgestartpiece.getBoundingBox().minZ) > 112) {
			StructureNetherBridgeEnd structurenetherbridgeend = StructureNetherBridgeEnd.func_40023_a(list, random, i, j, k, l, i1);
			return structurenetherbridgeend;
		}
		List list1 = componentnetherbridgestartpiece.field_40035_b;
		if (flag) {
			list1 = componentnetherbridgestartpiece.field_40036_c;
		}
		ComponentNetherBridgePiece componentnetherbridgepiece = func_40020_a(componentnetherbridgestartpiece, list1, list, random, i, j, k, l, i1 + 1);
		if (componentnetherbridgepiece != null) {
			list.add(componentnetherbridgepiece);
			componentnetherbridgestartpiece.field_40034_d.add(componentnetherbridgepiece);
		}
		return componentnetherbridgepiece;
	}

	protected StructureComponent func_40022_a(ComponentNetherBridgeStartPiece componentnetherbridgestartpiece, List list, Random random, int i, int j, boolean flag) {
		switch (coordBaseMode) {
			case 2:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + i, boundingBox.minY + j, boundingBox.minZ - 1, coordBaseMode, getComponentType(), flag);

			case 0:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + i, boundingBox.minY + j, boundingBox.maxZ + 1, coordBaseMode, getComponentType(), flag);

			case 1:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX - 1, boundingBox.minY + j, boundingBox.minZ + i, coordBaseMode, getComponentType(), flag);

			case 3:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.maxX + 1, boundingBox.minY + j, boundingBox.minZ + i, coordBaseMode, getComponentType(), flag);
		}
		return null;
	}

	protected StructureComponent func_40019_b(ComponentNetherBridgeStartPiece componentnetherbridgestartpiece, List list, Random random, int i, int j, boolean flag) {
		switch (coordBaseMode) {
			case 2:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType(), flag);

			case 0:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX - 1, boundingBox.minY + i, boundingBox.minZ + j, 1, getComponentType(), flag);

			case 1:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType(), flag);

			case 3:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.minZ - 1, 2, getComponentType(), flag);
		}
		return null;
	}

	protected StructureComponent func_40016_c(ComponentNetherBridgeStartPiece componentnetherbridgestartpiece, List list, Random random, int i, int j, boolean flag) {
		switch (coordBaseMode) {
			case 2:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType(), flag);

			case 0:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.maxX + 1, boundingBox.minY + i, boundingBox.minZ + j, 3, getComponentType(), flag);

			case 1:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType(), flag);

			case 3:
				return func_40018_a(componentnetherbridgestartpiece, list, random, boundingBox.minX + j, boundingBox.minY + i, boundingBox.maxZ + 1, 0, getComponentType(), flag);
		}
		return null;
	}

	protected static boolean func_40021_a(StructureBoundingBox structureboundingbox) {
		return structureboundingbox != null && structureboundingbox.minY > 10;
	}
}
