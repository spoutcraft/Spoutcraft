package net.minecraft.src;

public class PathNavigate
	implements INavigate {
	private EntityLiving field_46076_a;
	private World field_46074_b;
	private PathEntity field_46075_c;
	private float field_46073_d;

	public PathNavigate(EntityLiving entityliving, World world) {
		field_46076_a = entityliving;
		field_46074_b = world;
	}

	public void func_46071_a(double d, double d1, double d2, float f) {
		field_46075_c = field_46074_b.getEntityPathToXYZ(field_46076_a, (int)d, (int)d1, (int)d2, 10F);
		field_46073_d = f;
	}

	public void func_46070_a(EntityLiving entityliving, float f) {
		field_46075_c = field_46074_b.getPathToEntity(field_46076_a, entityliving, 16F);
		field_46073_d = f;
	}

	public void func_46069_a() {
		if (field_46075_c == null) {
			return;
		}
		float f = field_46076_a.width;
		Vec3D vec3d;
		for (vec3d = field_46075_c.getPosition(field_46076_a); vec3d != null && vec3d.squareDistanceTo(field_46076_a.posX, vec3d.yCoord, field_46076_a.posZ) < (double)(f * f);) {
			field_46075_c.incrementPathIndex();
			if (field_46075_c.isFinished()) {
				vec3d = null;
				field_46075_c = null;
			}
			else {
				vec3d = field_46075_c.getPosition(field_46076_a);
			}
		}

		if (vec3d == null) {
			return;
		}
		else {
			field_46076_a.func_46009_aH().func_46033_a(field_46073_d);
			field_46076_a.func_46009_aH().func_46035_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
			return;
		}
	}

	public boolean func_46072_b() {
		return field_46075_c == null || field_46075_c.isFinished();
	}
}
