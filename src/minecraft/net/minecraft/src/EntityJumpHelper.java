package net.minecraft.src;

public class EntityJumpHelper {
	private EntityLiving field_46132_a;
	private boolean field_46131_b;

	public EntityJumpHelper(EntityLiving entityliving) {
		field_46131_b = false;
		field_46132_a = entityliving;
	}

	public void func_46129_a() {
		field_46131_b = true;
	}

	public void func_46130_b() {
		if (!field_46131_b) {
			return;
		}
		else {
			field_46132_a.func_46003_g(true);
			field_46131_b = false;
			return;
		}
	}
}
