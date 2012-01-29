package net.minecraft.src;

import java.util.Random;

public class ModelGhast extends ModelBase {
	ModelRenderer body;
	ModelRenderer tentacles[];

	public ModelGhast() {
		tentacles = new ModelRenderer[9];
		byte byte0 = -16;
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-8F, -8F, -8F, 16, 16, 16);
		body.rotationPointY += 24 + byte0;
		Random random = new Random(1660L);
		for (int i = 0; i < tentacles.length; i++) {
			tentacles[i] = new ModelRenderer(this, 0, 0);
			float f = (((((float)(i % 3) - (float)((i / 3) % 2) * 0.5F) + 0.25F) / 2.0F) * 2.0F - 1.0F) * 5F;
			float f1 = (((float)(i / 3) / 2.0F) * 2.0F - 1.0F) * 5F;
			int j = random.nextInt(7) + 8;
			tentacles[i].addBox(-1F, 0.0F, -1F, 2, j, 2);
			tentacles[i].rotationPointX = f;
			tentacles[i].rotationPointZ = f1;
			tentacles[i].rotationPointY = 31 + byte0;
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		for (int i = 0; i < tentacles.length; i++) {
			tentacles[i].rotateAngleX = 0.2F * MathHelper.sin(f2 * 0.3F + (float)i) + 0.4F;
		}
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		body.render(f5);
		for (int i = 0; i < tentacles.length; i++) {
			tentacles[i].render(f5);
		}
	}
}
