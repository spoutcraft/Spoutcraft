package net.minecraft.src;

public class ModelZombie extends ModelBiped {
	public ModelZombie() {
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		float f6 = MathHelper.sin(onGround * 3.141593F);
		float f7 = MathHelper.sin((1.0F - (1.0F - onGround) * (1.0F - onGround)) * 3.141593F);
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
		bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
		bipedRightArm.rotateAngleX = -1.570796F;
		bipedLeftArm.rotateAngleX = -1.570796F;
		bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
	}
}
