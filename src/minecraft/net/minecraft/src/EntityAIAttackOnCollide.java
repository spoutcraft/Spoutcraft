package net.minecraft.src;

public class EntityAIAttackOnCollide extends EntityAIBase {
	World field_46095_a;
	EntityMob field_46093_b;
	EntityLiving field_46094_c;
	int field_46091_d;
	float field_46092_e;

	public EntityAIAttackOnCollide(EntityMob entitymob, World world, float f) {
		field_46091_d = 0;
		field_46093_b = entitymob;
		field_46095_a = world;
		field_46092_e = f;
		func_46079_a(3);
	}

	public boolean func_46082_a() {
		field_46094_c = func_46090_h();
		return field_46094_c != null;
	}

	public void func_46081_b() {
		field_46093_b.func_46012_aJ().func_46070_a(field_46094_c, field_46093_b.func_46013_aQ());
		field_46093_b.func_46008_aG().func_46141_a(field_46094_c, 30F, 30F);
		field_46091_d = Math.max(field_46091_d - 1, 0);
		double d = 4D;
		if (field_46093_b.getDistanceSqToEntity(field_46094_c) > d) {
			return;
		}
		if (field_46091_d > 0) {
			return;
		}
		else {
			field_46091_d = 20;
			field_46093_b.attackEntityAsMob(field_46094_c);
			return;
		}
	}

	private EntityLiving func_46090_h() {
		Object obj = field_46093_b.func_46007_aL();
		if (obj == null) {
			obj = field_46095_a.getClosestVulnerablePlayerToEntity(field_46093_b, field_46092_e);
		}
		if (obj == null) {
			return null;
		}
		if (((EntityLiving) (obj)).boundingBox.maxY <= field_46093_b.boundingBox.minY || ((EntityLiving) (obj)).boundingBox.minY >= field_46093_b.boundingBox.maxY) {
			return null;
		}
		if (!field_46093_b.canEntityBeSeen(((Entity) (obj)))) {
			return null;
		}
		else {
			return ((EntityLiving) (obj));
		}
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

	public void func_46080_e() {
		super.func_46080_e();
	}

	public boolean func_46078_f() {
		return super.func_46078_f();
	}

	public boolean func_46084_g() {
		return super.func_46084_g();
	}
}
