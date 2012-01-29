package net.minecraft.src;

import java.util.Random;

public class EntityAILookIdle extends EntityAIBase {
	private EntityLiving field_46089_a;
	private double field_46087_b;
	private double field_46088_c;
	private int field_46086_d;

	public EntityAILookIdle(EntityLiving entityliving) {
		field_46086_d = 0;
		field_46089_a = entityliving;
		func_46079_a(3);
	}

	public boolean func_46082_a() {
		return field_46089_a.func_46004_aK().nextFloat() < 0.02F;
	}

	public boolean func_46084_g() {
		return field_46086_d >= 0;
	}

	public void func_46080_e() {
		double d = 6.2831853071795862D * field_46089_a.func_46004_aK().nextDouble();
		field_46087_b = Math.cos(d);
		field_46088_c = Math.sin(d);
		field_46086_d = 20 + field_46089_a.func_46004_aK().nextInt(20);
	}

	public void func_46081_b() {
		field_46086_d--;
		field_46089_a.func_46008_aG().func_46143_a(field_46089_a.posX + field_46087_b, field_46089_a.posY + (double)field_46089_a.getEyeHeight(), field_46089_a.posZ + field_46088_c, 10F, field_46089_a.getVerticalFaceSpeed());
	}

	public int func_46083_c() {
		return super.func_46083_c();
	}

	public void func_46079_a(int i) {
		super.func_46079_a(i);
	}

	public void func_46077_d() {
		super.func_46077_d();
	}

	public boolean func_46078_f() {
		return super.func_46078_f();
	}
}
