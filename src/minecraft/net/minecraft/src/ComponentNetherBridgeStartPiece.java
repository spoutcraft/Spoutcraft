package net.minecraft.src;

import java.util.*;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3 {
	public StructureNetherBridgePieceWeight field_40037_a;
	public List field_40035_b;
	public List field_40036_c;
	public ArrayList field_40034_d;

	public ComponentNetherBridgeStartPiece(Random random, int i, int j) {
		super(random, i, j);
		field_40034_d = new ArrayList();
		field_40035_b = new ArrayList();
		StructureNetherBridgePieceWeight astructurenetherbridgepieceweight[] = StructureNetherBridgePieces.func_40689_a();
		int k = astructurenetherbridgepieceweight.length;
		for (int l = 0; l < k; l++) {
			StructureNetherBridgePieceWeight structurenetherbridgepieceweight = astructurenetherbridgepieceweight[l];
			structurenetherbridgepieceweight.field_40698_c = 0;
			field_40035_b.add(structurenetherbridgepieceweight);
		}

		field_40036_c = new ArrayList();
		astructurenetherbridgepieceweight = StructureNetherBridgePieces.func_40687_b();
		k = astructurenetherbridgepieceweight.length;
		for (int i1 = 0; i1 < k; i1++) {
			StructureNetherBridgePieceWeight structurenetherbridgepieceweight1 = astructurenetherbridgepieceweight[i1];
			structurenetherbridgepieceweight1.field_40698_c = 0;
			field_40036_c.add(structurenetherbridgepieceweight1);
		}
	}
}
