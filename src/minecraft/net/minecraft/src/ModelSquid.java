package net.minecraft.src;

public class ModelSquid extends ModelBase {
	ModelRenderer squidBody;
	ModelRenderer squidTentacles[];

	public ModelSquid() {
		squidTentacles = new ModelRenderer[8];
		byte byte0 = -16;
		squidBody = new ModelRenderer(this, 0, 0);
		squidBody.addBox(-6F, -8F, -6F, 12, 16, 12);
		squidBody.rotationPointY += 24 + byte0;
		for (int i = 0; i < squidTentacles.length; i++) {
			squidTentacles[i] = new ModelRenderer(this, 48, 0);
			double d = ((double)i * 3.1415926535897931D * 2D) / (double)squidTentacles.length;
			float f = (float)Math.cos(d) * 5F;
			float f1 = (float)Math.sin(d) * 5F;
			squidTentacles[i].addBox(-1F, 0.0F, -1F, 2, 18, 2);
			squidTentacles[i].rotationPointX = f;
			squidTentacles[i].rotationPointZ = f1;
			squidTentacles[i].rotationPointY = 31 + byte0;
			d = ((double)i * 3.1415926535897931D * -2D) / (double)squidTentacles.length + 1.5707963267948966D;
			squidTentacles[i].rotateAngleY = (float)d;
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		for (int i = 0; i < squidTentacles.length; i++) {
			squidTentacles[i].rotateAngleX = f2;
		}
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		squidBody.render(f5);
		for (int i = 0; i < squidTentacles.length; i++) {
			squidTentacles[i].render(f5);
		}
	}
}
