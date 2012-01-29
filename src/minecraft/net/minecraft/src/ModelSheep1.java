package net.minecraft.src;

public class ModelSheep1 extends ModelQuadruped {
	private float field_44016_o;

	public ModelSheep1() {
		super(12, 0.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -4F, -4F, 6, 6, 6, 0.6F);
		head.setRotationPoint(0.0F, 6F, -8F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4F, -10F, -7F, 8, 16, 6, 1.75F);
		body.setRotationPoint(0.0F, 5F, 2.0F);
		float f = 0.5F;
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg1.setRotationPoint(-3F, 12F, 7F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg2.setRotationPoint(3F, 12F, 7F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg3.setRotationPoint(-3F, 12F, -5F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg4.setRotationPoint(3F, 12F, -5F);
	}

	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
		super.setLivingAnimations(entityliving, f, f1, f2);
		head.rotationPointY = 6F + ((EntitySheep)entityliving).func_44003_c(f2) * 9F;
		field_44016_o = ((EntitySheep)entityliving).func_44002_d(f2);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		head.rotateAngleX = field_44016_o;
	}
}
