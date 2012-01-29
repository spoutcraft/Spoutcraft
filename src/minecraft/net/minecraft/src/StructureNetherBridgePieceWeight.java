package net.minecraft.src;

class StructureNetherBridgePieceWeight {
	public Class field_40699_a;
	public final int field_40697_b;
	public int field_40698_c;
	public int field_40695_d;
	public boolean field_40696_e;

	public StructureNetherBridgePieceWeight(Class class1, int i, int j, boolean flag) {
		field_40699_a = class1;
		field_40697_b = i;
		field_40695_d = j;
		field_40696_e = flag;
	}

	public StructureNetherBridgePieceWeight(Class class1, int i, int j) {
		this(class1, i, j, false);
	}

	public boolean func_40693_a(int i) {
		return field_40695_d == 0 || field_40698_c < field_40695_d;
	}

	public boolean func_40694_a() {
		return field_40695_d == 0 || field_40698_c < field_40695_d;
	}
}
