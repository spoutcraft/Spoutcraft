package net.minecraft.src;

import java.util.Random;

public class EntityAISwimming extends EntityAIBase {
	private EntityLiving field_46106_a;

	public EntityAISwimming(EntityLiving entityliving) {
		field_46106_a = entityliving;
		func_46079_a(4);
	}

	public boolean func_46082_a() {
		return field_46106_a.func_46004_aK().nextFloat() < 0.8F && (field_46106_a.isInWater() || field_46106_a.handleLavaMovement());
	}

	public void func_46080_e() {
		field_46106_a.func_46005_aI().func_46129_a();
	}

	public int func_46083_c() {
		return super.func_46083_c();
	}

	public void func_46079_a(int i) {
		super.func_46079_a(i);
	}

	public void func_46081_b() {
		super.func_46081_b();
	}

	public void func_46077_d() {
		super.func_46077_d();
	}

	public boolean func_46078_f() {
		return super.func_46078_f();
	}

	public boolean func_46084_g() {
		return super.func_46084_g();
	}
}
