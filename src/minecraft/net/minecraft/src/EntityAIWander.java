package net.minecraft.src;

import java.util.Random;

public class EntityAIWander extends EntityAIBase {
	private EntityCreature field_46100_a;
	private double field_46098_b;
	private double field_46099_c;
	private double field_46097_d;

	public EntityAIWander(EntityCreature entitycreature) {
		field_46100_a = entitycreature;
		func_46079_a(3);
	}

	public boolean func_46082_a() {
		if (field_46100_a.func_46011_aM() >= 100) {
			return false;
		}
		if (field_46100_a.func_46004_aK().nextInt(120) != 0) {
			return false;
		}
		Vec3D vec3d = func_46096_h();
		if (vec3d == null) {
			return false;
		}
		else {
			field_46098_b = vec3d.xCoord;
			field_46099_c = vec3d.yCoord;
			field_46097_d = vec3d.zCoord;
			return true;
		}
	}

	public boolean func_46084_g() {
		return !field_46100_a.func_46012_aJ().func_46072_b();
	}

	public void func_46080_e() {
		field_46100_a.func_46012_aJ().func_46071_a(field_46098_b, field_46099_c, field_46097_d, field_46100_a.func_46013_aQ());
	}

	private Vec3D func_46096_h() {
		Random random = field_46100_a.func_46004_aK();
		boolean flag = false;
		int i = -1;
		int j = -1;
		int k = -1;
		float f = -99999F;
		for (int l = 0; l < 10; l++) {
			int i1 = MathHelper.floor_double((field_46100_a.posX + (double)random.nextInt(13)) - 6D);
			int j1 = MathHelper.floor_double((field_46100_a.posY + (double)random.nextInt(7)) - 3D);
			int k1 = MathHelper.floor_double((field_46100_a.posZ + (double)random.nextInt(13)) - 6D);
			float f1 = field_46100_a.getBlockPathWeight(i1, j1, k1);
			if (f1 > f) {
				f = f1;
				i = i1;
				j = j1;
				k = k1;
				flag = true;
			}
		}

		if (flag) {
			return Vec3D.createVector(i, j, k);
		}
		else {
			return null;
		}
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
}
