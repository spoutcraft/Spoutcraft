package net.minecraft.src;

abstract class EntityAIBase {
	private int field_46085_a;

	EntityAIBase() {
		field_46085_a = 0;
	}

	public abstract boolean func_46082_a();

	public boolean func_46084_g() {
		return func_46082_a();
	}

	public boolean func_46078_f() {
		return true;
	}

	public void func_46080_e() {
	}

	public void func_46077_d() {
	}

	public void func_46081_b() {
	}

	public void func_46079_a(int i) {
		field_46085_a = i;
	}

	public int func_46083_c() {
		return field_46085_a;
	}
}
