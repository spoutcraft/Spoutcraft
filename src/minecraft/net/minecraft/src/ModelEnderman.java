package net.minecraft.src;

public class ModelEnderman extends ModelBiped {
	public boolean isCarrying;
	public boolean isAttacking;

	public ModelEnderman() {
		super(0.0F, -14F);
		isCarrying = false;
		isAttacking = false;
		float f = -14F;
		float f1 = 0.0F;
		bipedHeadwear = new ModelRenderer(this, 0, 16);
		bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, f1 - 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		bipedBody = new ModelRenderer(this, 32, 16);
		bipedBody.addBox(-4F, 0.0F, -2F, 8, 12, 4, f1);
		bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		bipedRightArm = new ModelRenderer(this, 56, 0);
		bipedRightArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
		bipedRightArm.setRotationPoint(-3F, 2.0F + f, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 56, 0);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
		bipedLeftArm.setRotationPoint(5F, 2.0F + f, 0.0F);
		bipedRightLeg = new ModelRenderer(this, 56, 0);
		bipedRightLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
		bipedRightLeg.setRotationPoint(-2F, 12F + f, 0.0F);
		bipedLeftLeg = new ModelRenderer(this, 56, 0);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
		bipedLeftLeg.setRotationPoint(2.0F, 12F + f, 0.0F);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		bipedHead.showModel = true;
		float f6 = -14F;
		bipedBody.rotateAngleX = 0.0F;
		bipedBody.rotationPointY = f6;
		bipedBody.rotationPointZ = -0F;
		bipedRightLeg.rotateAngleX -= 0.0F;
		bipedLeftLeg.rotateAngleX -= 0.0F;
		bipedRightArm.rotateAngleX *= 0.5D;
		bipedLeftArm.rotateAngleX *= 0.5D;
		bipedRightLeg.rotateAngleX *= 0.5D;
		bipedLeftLeg.rotateAngleX *= 0.5D;
		float f7 = 0.4F;
		if (bipedRightArm.rotateAngleX > f7) {
			bipedRightArm.rotateAngleX = f7;
		}
		if (bipedLeftArm.rotateAngleX > f7) {
			bipedLeftArm.rotateAngleX = f7;
		}
		if (bipedRightArm.rotateAngleX < -f7) {
			bipedRightArm.rotateAngleX = -f7;
		}
		if (bipedLeftArm.rotateAngleX < -f7) {
			bipedLeftArm.rotateAngleX = -f7;
		}
		if (bipedRightLeg.rotateAngleX > f7) {
			bipedRightLeg.rotateAngleX = f7;
		}
		if (bipedLeftLeg.rotateAngleX > f7) {
			bipedLeftLeg.rotateAngleX = f7;
		}
		if (bipedRightLeg.rotateAngleX < -f7) {
			bipedRightLeg.rotateAngleX = -f7;
		}
		if (bipedLeftLeg.rotateAngleX < -f7) {
			bipedLeftLeg.rotateAngleX = -f7;
		}
		if (isCarrying) {
			bipedRightArm.rotateAngleX = -0.5F;
			bipedLeftArm.rotateAngleX = -0.5F;
			bipedRightArm.rotateAngleZ = 0.05F;
			bipedLeftArm.rotateAngleZ = -0.05F;
		}
		bipedRightArm.rotationPointZ = 0.0F;
		bipedLeftArm.rotationPointZ = 0.0F;
		bipedRightLeg.rotationPointZ = 0.0F;
		bipedLeftLeg.rotationPointZ = 0.0F;
		bipedRightLeg.rotationPointY = 9F + f6;
		bipedLeftLeg.rotationPointY = 9F + f6;
		bipedHead.rotationPointZ = -0F;
		bipedHead.rotationPointY = f6 + 1.0F;
		bipedHeadwear.rotationPointX = bipedHead.rotationPointX;
		bipedHeadwear.rotationPointY = bipedHead.rotationPointY;
		bipedHeadwear.rotationPointZ = bipedHead.rotationPointZ;
		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ;
		if (isAttacking) {
			float f8 = 1.0F;
			bipedHead.rotationPointY -= f8 * 5F;
		}
	}
}
