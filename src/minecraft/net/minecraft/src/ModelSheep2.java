package net.minecraft.src;

public class ModelSheep2 extends ModelQuadruped {
	private float field_44017_o;

	public ModelSheep2() {
		super(12, 0.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -4F, -6F, 6, 6, 8, 0.0F);
		head.setRotationPoint(0.0F, 6F, -8F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4F, -10F, -7F, 8, 16, 6, 0.0F);
		body.setRotationPoint(0.0F, 5F, 2.0F);
	}

	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
		super.setLivingAnimations(entityliving, f, f1, f2);
		head.rotationPointY = 6F + ((EntitySheep)entityliving).func_44003_c(f2) * 9F;
		field_44017_o = ((EntitySheep)entityliving).func_44002_d(f2);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		head.rotateAngleX = field_44017_o;
	}
}
