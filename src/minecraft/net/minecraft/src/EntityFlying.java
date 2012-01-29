package net.minecraft.src;

public abstract class EntityFlying extends EntityLiving {
	public EntityFlying(World world) {
		super(world);
	}

	protected void fall(float f) {
	}

	public void moveEntityWithHeading(float f, float f1) {
		if (isInWater()) {
			moveFlying(f, f1, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.80000001192092896D;
			motionY *= 0.80000001192092896D;
			motionZ *= 0.80000001192092896D;
		}
		else if (handleLavaMovement()) {
			moveFlying(f, f1, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}
		else {
			float f2 = 0.91F;
			if (onGround) {
				f2 = 0.5460001F;
				int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
				if (i > 0) {
					f2 = Block.blocksList[i].slipperiness * 0.91F;
				}
			}
			float f3 = 0.1627714F / (f2 * f2 * f2);
			moveFlying(f, f1, onGround ? 0.1F * f3 : 0.02F);
			f2 = 0.91F;
			if (onGround) {
				f2 = 0.5460001F;
				int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
				if (j > 0) {
					f2 = Block.blocksList[j].slipperiness * 0.91F;
				}
			}
			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= f2;
			motionZ *= f2;
		}
		field_705_Q = field_704_R;
		double d = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double(d * d + d1 * d1) * 4F;
		if (f4 > 1.0F) {
			f4 = 1.0F;
		}
		field_704_R += (f4 - field_704_R) * 0.4F;
		field_703_S += field_704_R;
	}

	public boolean isOnLadder() {
		return false;
	}
}
