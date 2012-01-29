package net.minecraft.src;

public class ModelBook extends ModelBase {
	public ModelRenderer field_40330_a;
	public ModelRenderer field_40328_b;
	public ModelRenderer field_40329_c;
	public ModelRenderer field_40326_d;
	public ModelRenderer field_40327_e;
	public ModelRenderer field_40324_f;
	public ModelRenderer field_40325_g;

	public ModelBook() {
		field_40330_a = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6F, -5F, 0.0F, 6, 10, 0);
		field_40328_b = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5F, 0.0F, 6, 10, 0);
		field_40325_g = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1F, -5F, 0.0F, 2, 10, 0);
		field_40329_c = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4F, -0.99F, 5, 8, 1);
		field_40326_d = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4F, -0.01F, 5, 8, 1);
		field_40327_e = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4F, 0.0F, 5, 8, 0);
		field_40324_f = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4F, 0.0F, 5, 8, 0);
		field_40330_a.setRotationPoint(0.0F, 0.0F, -1F);
		field_40328_b.setRotationPoint(0.0F, 0.0F, 1.0F);
		field_40325_g.rotateAngleY = 1.570796F;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		field_40330_a.render(f5);
		field_40328_b.render(f5);
		field_40325_g.render(f5);
		field_40329_c.render(f5);
		field_40326_d.render(f5);
		field_40327_e.render(f5);
		field_40324_f.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = (MathHelper.sin(f * 0.02F) * 0.1F + 1.25F) * f3;
		field_40330_a.rotateAngleY = 3.141593F + f6;
		field_40328_b.rotateAngleY = -f6;
		field_40329_c.rotateAngleY = f6;
		field_40326_d.rotateAngleY = -f6;
		field_40327_e.rotateAngleY = f6 - f6 * 2.0F * f1;
		field_40324_f.rotateAngleY = f6 - f6 * 2.0F * f2;
		field_40329_c.rotationPointX = MathHelper.sin(f6);
		field_40326_d.rotationPointX = MathHelper.sin(f6);
		field_40327_e.rotationPointX = MathHelper.sin(f6);
		field_40324_f.rotationPointX = MathHelper.sin(f6);
	}
}
