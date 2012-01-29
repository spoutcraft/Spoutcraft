package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelWolf extends ModelBase {
	public ModelRenderer wolfHeadMain;
	public ModelRenderer wolfBody;
	public ModelRenderer wolfLeg1;
	public ModelRenderer wolfLeg2;
	public ModelRenderer wolfLeg3;
	public ModelRenderer wolfLeg4;
	ModelRenderer wolfTail;
	ModelRenderer wolfMane;

	public ModelWolf() {
		float f = 0.0F;
		float f1 = 13.5F;
		wolfHeadMain = new ModelRenderer(this, 0, 0);
		wolfHeadMain.addBox(-3F, -3F, -2F, 6, 6, 4, f);
		wolfHeadMain.setRotationPoint(-1F, f1, -7F);
		wolfBody = new ModelRenderer(this, 18, 14);
		wolfBody.addBox(-4F, -2F, -3F, 6, 9, 6, f);
		wolfBody.setRotationPoint(0.0F, 14F, 2.0F);
		wolfMane = new ModelRenderer(this, 21, 0);
		wolfMane.addBox(-4F, -3F, -3F, 8, 6, 7, f);
		wolfMane.setRotationPoint(-1F, 14F, 2.0F);
		wolfLeg1 = new ModelRenderer(this, 0, 18);
		wolfLeg1.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
		wolfLeg1.setRotationPoint(-2.5F, 16F, 7F);
		wolfLeg2 = new ModelRenderer(this, 0, 18);
		wolfLeg2.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
		wolfLeg2.setRotationPoint(0.5F, 16F, 7F);
		wolfLeg3 = new ModelRenderer(this, 0, 18);
		wolfLeg3.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
		wolfLeg3.setRotationPoint(-2.5F, 16F, -4F);
		wolfLeg4 = new ModelRenderer(this, 0, 18);
		wolfLeg4.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
		wolfLeg4.setRotationPoint(0.5F, 16F, -4F);
		wolfTail = new ModelRenderer(this, 9, 18);
		wolfTail.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
		wolfTail.setRotationPoint(-1F, 12F, 8F);
		wolfHeadMain.setTextureOffset(16, 14).addBox(-3F, -5F, 0.0F, 2, 2, 1, f);
		wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5F, 0.0F, 2, 2, 1, f);
		wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5F, 3, 3, 4, f);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		if (field_40301_k) {
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 5F * f5, 2.0F * f5);
			wolfHeadMain.renderWithRotation(f5);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24F * f5, 0.0F);
			wolfBody.render(f5);
			wolfLeg1.render(f5);
			wolfLeg2.render(f5);
			wolfLeg3.render(f5);
			wolfLeg4.render(f5);
			wolfTail.renderWithRotation(f5);
			wolfMane.render(f5);
			GL11.glPopMatrix();
		}
		else {
			wolfHeadMain.renderWithRotation(f5);
			wolfBody.render(f5);
			wolfLeg1.render(f5);
			wolfLeg2.render(f5);
			wolfLeg3.render(f5);
			wolfLeg4.render(f5);
			wolfTail.renderWithRotation(f5);
			wolfMane.render(f5);
		}
	}

	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
		EntityWolf entitywolf = (EntityWolf)entityliving;
		if (entitywolf.isAngry()) {
			wolfTail.rotateAngleY = 0.0F;
		}
		else {
			wolfTail.rotateAngleY = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		}
		if (entitywolf.isSitting()) {
			wolfMane.setRotationPoint(-1F, 16F, -3F);
			wolfMane.rotateAngleX = 1.256637F;
			wolfMane.rotateAngleY = 0.0F;
			wolfBody.setRotationPoint(0.0F, 18F, 0.0F);
			wolfBody.rotateAngleX = 0.7853982F;
			wolfTail.setRotationPoint(-1F, 21F, 6F);
			wolfLeg1.setRotationPoint(-2.5F, 22F, 2.0F);
			wolfLeg1.rotateAngleX = 4.712389F;
			wolfLeg2.setRotationPoint(0.5F, 22F, 2.0F);
			wolfLeg2.rotateAngleX = 4.712389F;
			wolfLeg3.rotateAngleX = 5.811947F;
			wolfLeg3.setRotationPoint(-2.49F, 17F, -4F);
			wolfLeg4.rotateAngleX = 5.811947F;
			wolfLeg4.setRotationPoint(0.51F, 17F, -4F);
		}
		else {
			wolfBody.setRotationPoint(0.0F, 14F, 2.0F);
			wolfBody.rotateAngleX = 1.570796F;
			wolfMane.setRotationPoint(-1F, 14F, -3F);
			wolfMane.rotateAngleX = wolfBody.rotateAngleX;
			wolfTail.setRotationPoint(-1F, 12F, 8F);
			wolfLeg1.setRotationPoint(-2.5F, 16F, 7F);
			wolfLeg2.setRotationPoint(0.5F, 16F, 7F);
			wolfLeg3.setRotationPoint(-2.5F, 16F, -4F);
			wolfLeg4.setRotationPoint(0.5F, 16F, -4F);
			wolfLeg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
			wolfLeg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
			wolfLeg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
			wolfLeg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		}
		float f3 = entitywolf.getInterestedAngle(f2) + entitywolf.getShakeAngle(f2, 0.0F);
		wolfHeadMain.rotateAngleZ = f3;
		wolfMane.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.08F);
		wolfBody.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.16F);
		wolfTail.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.2F);
		if (entitywolf.getWolfShaking()) {
			float f4 = entitywolf.getEntityBrightness(f2) * entitywolf.getShadingWhileShaking(f2);
			GL11.glColor3f(f4, f4, f4);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		wolfHeadMain.rotateAngleX = f4 / 57.29578F;
		wolfHeadMain.rotateAngleY = f3 / 57.29578F;
		wolfTail.rotateAngleX = f2;
	}
}
