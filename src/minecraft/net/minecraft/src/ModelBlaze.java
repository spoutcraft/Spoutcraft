package net.minecraft.src;

public class ModelBlaze extends ModelBase {
	private ModelRenderer field_40323_a[];
	private ModelRenderer field_40322_b;

	public ModelBlaze() {
		field_40323_a = new ModelRenderer[12];
		for (int i = 0; i < field_40323_a.length; i++) {
			field_40323_a[i] = new ModelRenderer(this, 0, 16);
			field_40323_a[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
		}

		field_40322_b = new ModelRenderer(this, 0, 0);
		field_40322_b.addBox(-4F, -4F, -4F, 8, 8, 8);
	}

	public int func_40321_a() {
		return 8;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		field_40322_b.render(f5);
		for (int i = 0; i < field_40323_a.length; i++) {
			field_40323_a[i].render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = f2 * 3.141593F * -0.1F;
		for (int i = 0; i < 4; i++) {
			field_40323_a[i].rotationPointY = -2F + MathHelper.cos(((float)(i * 2) + f2) * 0.25F);
			field_40323_a[i].rotationPointX = MathHelper.cos(f6) * 9F;
			field_40323_a[i].rotationPointZ = MathHelper.sin(f6) * 9F;
			f6 += 1.570796F;
		}

		f6 = 0.7853982F + f2 * 3.141593F * 0.03F;
		for (int j = 4; j < 8; j++) {
			field_40323_a[j].rotationPointY = 2.0F + MathHelper.cos(((float)(j * 2) + f2) * 0.25F);
			field_40323_a[j].rotationPointX = MathHelper.cos(f6) * 7F;
			field_40323_a[j].rotationPointZ = MathHelper.sin(f6) * 7F;
			f6 += 1.570796F;
		}

		f6 = 0.4712389F + f2 * 3.141593F * -0.05F;
		for (int k = 8; k < 12; k++) {
			field_40323_a[k].rotationPointY = 11F + MathHelper.cos(((float)k * 1.5F + f2) * 0.5F);
			field_40323_a[k].rotationPointX = MathHelper.cos(f6) * 5F;
			field_40323_a[k].rotationPointZ = MathHelper.sin(f6) * 5F;
			f6 += 1.570796F;
		}

		field_40322_b.rotateAngleY = f3 / 57.29578F;
		field_40322_b.rotateAngleX = f4 / 57.29578F;
	}
}
