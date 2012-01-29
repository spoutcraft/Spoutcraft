package net.minecraft.src;

public class ModelVillager extends ModelBase {
	public ModelRenderer field_40340_a;
	public ModelRenderer field_40338_b;
	public ModelRenderer field_40339_c;
	public ModelRenderer field_40336_d;
	public ModelRenderer field_40337_e;
	public int field_40334_f;
	public int field_40335_g;
	public boolean field_40341_n;
	public boolean field_40342_o;

	public ModelVillager() {
		this(0.0F);
	}

	public ModelVillager(float f) {
		this(f, 0.0F);
	}

	public ModelVillager(float f, float f1) {
		field_40334_f = 0;
		field_40335_g = 0;
		field_40341_n = false;
		field_40342_o = false;
		byte byte0 = 64;
		byte byte1 = 64;
		field_40340_a = (new ModelRenderer(this)).setTextureSize(byte0, byte1);
		field_40340_a.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		field_40340_a.setTextureOffset(0, 0).addBox(-4F, -10F, -4F, 8, 10, 8, f);
		field_40340_a.setTextureOffset(24, 0).addBox(-1F, -3F, -6F, 2, 4, 2, f);
		field_40338_b = (new ModelRenderer(this)).setTextureSize(byte0, byte1);
		field_40338_b.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		field_40338_b.setTextureOffset(16, 20).addBox(-4F, 0.0F, -3F, 8, 12, 6, f);
		field_40338_b.setTextureOffset(0, 38).addBox(-4F, 0.0F, -3F, 8, 18, 6, f + 0.5F);
		field_40339_c = (new ModelRenderer(this)).setTextureSize(byte0, byte1);
		field_40339_c.setRotationPoint(0.0F, 0.0F + f1 + 2.0F, 0.0F);
		field_40339_c.setTextureOffset(44, 22).addBox(-8F, -2F, -2F, 4, 8, 4, f);
		field_40339_c.setTextureOffset(44, 22).addBox(4F, -2F, -2F, 4, 8, 4, f);
		field_40339_c.setTextureOffset(40, 38).addBox(-4F, 2.0F, -2F, 8, 4, 4, f);
		field_40336_d = (new ModelRenderer(this, 0, 22)).setTextureSize(byte0, byte1);
		field_40336_d.setRotationPoint(-2F, 12F + f1, 0.0F);
		field_40336_d.addBox(-2F, 0.0F, -2F, 4, 12, 4, f);
		field_40337_e = (new ModelRenderer(this, 0, 22)).setTextureSize(byte0, byte1);
		field_40337_e.mirror = true;
		field_40337_e.setRotationPoint(2.0F, 12F + f1, 0.0F);
		field_40337_e.addBox(-2F, 0.0F, -2F, 4, 12, 4, f);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		field_40340_a.render(f5);
		field_40338_b.render(f5);
		field_40336_d.render(f5);
		field_40337_e.render(f5);
		field_40339_c.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		field_40340_a.rotateAngleY = f3 / 57.29578F;
		field_40340_a.rotateAngleX = f4 / 57.29578F;
		field_40339_c.rotationPointY = 3F;
		field_40339_c.rotationPointZ = -1F;
		field_40339_c.rotateAngleX = -0.75F;
		field_40336_d.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 * 0.5F;
		field_40337_e.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1 * 0.5F;
		field_40336_d.rotateAngleY = 0.0F;
		field_40337_e.rotateAngleY = 0.0F;
	}
}
