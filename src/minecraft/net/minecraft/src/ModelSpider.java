package net.minecraft.src;

public class ModelSpider extends ModelBase {
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLeg1;
	public ModelRenderer spiderLeg2;
	public ModelRenderer spiderLeg3;
	public ModelRenderer spiderLeg4;
	public ModelRenderer spiderLeg5;
	public ModelRenderer spiderLeg6;
	public ModelRenderer spiderLeg7;
	public ModelRenderer spiderLeg8;

	public ModelSpider() {
		float f = 0.0F;
		int i = 15;
		spiderHead = new ModelRenderer(this, 32, 4);
		spiderHead.addBox(-4F, -4F, -8F, 8, 8, 8, f);
		spiderHead.setRotationPoint(0.0F, 0 + i, -3F);
		spiderNeck = new ModelRenderer(this, 0, 0);
		spiderNeck.addBox(-3F, -3F, -3F, 6, 6, 6, f);
		spiderNeck.setRotationPoint(0.0F, i, 0.0F);
		spiderBody = new ModelRenderer(this, 0, 12);
		spiderBody.addBox(-5F, -4F, -6F, 10, 8, 12, f);
		spiderBody.setRotationPoint(0.0F, 0 + i, 9F);
		spiderLeg1 = new ModelRenderer(this, 18, 0);
		spiderLeg1.addBox(-15F, -1F, -1F, 16, 2, 2, f);
		spiderLeg1.setRotationPoint(-4F, 0 + i, 2.0F);
		spiderLeg2 = new ModelRenderer(this, 18, 0);
		spiderLeg2.addBox(-1F, -1F, -1F, 16, 2, 2, f);
		spiderLeg2.setRotationPoint(4F, 0 + i, 2.0F);
		spiderLeg3 = new ModelRenderer(this, 18, 0);
		spiderLeg3.addBox(-15F, -1F, -1F, 16, 2, 2, f);
		spiderLeg3.setRotationPoint(-4F, 0 + i, 1.0F);
		spiderLeg4 = new ModelRenderer(this, 18, 0);
		spiderLeg4.addBox(-1F, -1F, -1F, 16, 2, 2, f);
		spiderLeg4.setRotationPoint(4F, 0 + i, 1.0F);
		spiderLeg5 = new ModelRenderer(this, 18, 0);
		spiderLeg5.addBox(-15F, -1F, -1F, 16, 2, 2, f);
		spiderLeg5.setRotationPoint(-4F, 0 + i, 0.0F);
		spiderLeg6 = new ModelRenderer(this, 18, 0);
		spiderLeg6.addBox(-1F, -1F, -1F, 16, 2, 2, f);
		spiderLeg6.setRotationPoint(4F, 0 + i, 0.0F);
		spiderLeg7 = new ModelRenderer(this, 18, 0);
		spiderLeg7.addBox(-15F, -1F, -1F, 16, 2, 2, f);
		spiderLeg7.setRotationPoint(-4F, 0 + i, -1F);
		spiderLeg8 = new ModelRenderer(this, 18, 0);
		spiderLeg8.addBox(-1F, -1F, -1F, 16, 2, 2, f);
		spiderLeg8.setRotationPoint(4F, 0 + i, -1F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		spiderHead.render(f5);
		spiderNeck.render(f5);
		spiderBody.render(f5);
		spiderLeg1.render(f5);
		spiderLeg2.render(f5);
		spiderLeg3.render(f5);
		spiderLeg4.render(f5);
		spiderLeg5.render(f5);
		spiderLeg6.render(f5);
		spiderLeg7.render(f5);
		spiderLeg8.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		spiderHead.rotateAngleY = f3 / 57.29578F;
		spiderHead.rotateAngleX = f4 / 57.29578F;
		float f6 = 0.7853982F;
		spiderLeg1.rotateAngleZ = -f6;
		spiderLeg2.rotateAngleZ = f6;
		spiderLeg3.rotateAngleZ = -f6 * 0.74F;
		spiderLeg4.rotateAngleZ = f6 * 0.74F;
		spiderLeg5.rotateAngleZ = -f6 * 0.74F;
		spiderLeg6.rotateAngleZ = f6 * 0.74F;
		spiderLeg7.rotateAngleZ = -f6;
		spiderLeg8.rotateAngleZ = f6;
		float f7 = -0F;
		float f8 = 0.3926991F;
		spiderLeg1.rotateAngleY = f8 * 2.0F + f7;
		spiderLeg2.rotateAngleY = -f8 * 2.0F - f7;
		spiderLeg3.rotateAngleY = f8 * 1.0F + f7;
		spiderLeg4.rotateAngleY = -f8 * 1.0F - f7;
		spiderLeg5.rotateAngleY = -f8 * 1.0F + f7;
		spiderLeg6.rotateAngleY = f8 * 1.0F - f7;
		spiderLeg7.rotateAngleY = -f8 * 2.0F + f7;
		spiderLeg8.rotateAngleY = f8 * 2.0F - f7;
		float f9 = -(MathHelper.cos(f * 0.6662F * 2.0F + 0.0F) * 0.4F) * f1;
		float f10 = -(MathHelper.cos(f * 0.6662F * 2.0F + 3.141593F) * 0.4F) * f1;
		float f11 = -(MathHelper.cos(f * 0.6662F * 2.0F + 1.570796F) * 0.4F) * f1;
		float f12 = -(MathHelper.cos(f * 0.6662F * 2.0F + 4.712389F) * 0.4F) * f1;
		float f13 = Math.abs(MathHelper.sin(f * 0.6662F + 0.0F) * 0.4F) * f1;
		float f14 = Math.abs(MathHelper.sin(f * 0.6662F + 3.141593F) * 0.4F) * f1;
		float f15 = Math.abs(MathHelper.sin(f * 0.6662F + 1.570796F) * 0.4F) * f1;
		float f16 = Math.abs(MathHelper.sin(f * 0.6662F + 4.712389F) * 0.4F) * f1;
		spiderLeg1.rotateAngleY += f9;
		spiderLeg2.rotateAngleY += -f9;
		spiderLeg3.rotateAngleY += f10;
		spiderLeg4.rotateAngleY += -f10;
		spiderLeg5.rotateAngleY += f11;
		spiderLeg6.rotateAngleY += -f11;
		spiderLeg7.rotateAngleY += f12;
		spiderLeg8.rotateAngleY += -f12;
		spiderLeg1.rotateAngleZ += f13;
		spiderLeg2.rotateAngleZ += -f13;
		spiderLeg3.rotateAngleZ += f14;
		spiderLeg4.rotateAngleZ += -f14;
		spiderLeg5.rotateAngleZ += f15;
		spiderLeg6.rotateAngleZ += -f15;
		spiderLeg7.rotateAngleZ += f16;
		spiderLeg8.rotateAngleZ += -f16;
	}
}
