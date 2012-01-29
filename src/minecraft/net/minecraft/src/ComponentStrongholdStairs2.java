package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentStrongholdStairs2 extends ComponentStrongholdStairs {
	public StructureStrongholdPieceWeight field_35038_a;
	public ComponentStrongholdPortalRoom field_40009_b;
	public ArrayList field_35037_b;

	public ComponentStrongholdStairs2(int i, Random random, int j, int k) {
		super(0, random, j, k);
		field_35037_b = new ArrayList();
	}

	public ChunkPosition func_40008_a_() {
		if (field_40009_b != null) {
			return field_40009_b.func_40008_a_();
		}
		else {
			return super.func_40008_a_();
		}
	}
}
