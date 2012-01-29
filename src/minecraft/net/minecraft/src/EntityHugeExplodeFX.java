package net.minecraft.src;

import java.util.Random;

public class EntityHugeExplodeFX extends EntityFX {
	private int field_35139_a;
	private int field_35138_ay;

	public EntityHugeExplodeFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		field_35139_a = 0;
		field_35138_ay = 0;
		field_35138_ay = 8;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
	}

	public void onUpdate() {
		for (int i = 0; i < 6; i++) {
			double d = posX + (rand.nextDouble() - rand.nextDouble()) * 4D;
			double d1 = posY + (rand.nextDouble() - rand.nextDouble()) * 4D;
			double d2 = posZ + (rand.nextDouble() - rand.nextDouble()) * 4D;
			worldObj.spawnParticle("largeexplode", d, d1, d2, (float)field_35139_a / (float)field_35138_ay, 0.0D, 0.0D);
		}

		field_35139_a++;
		if (field_35139_a == field_35138_ay) {
			setEntityDead();
		}
	}

	public int getFXLayer() {
		return 1;
	}
}
