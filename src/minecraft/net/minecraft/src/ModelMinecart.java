package net.minecraft.src;

public class ModelMinecart extends ModelBase {
	public ModelRenderer sideModels[];

	public ModelMinecart() {
		sideModels = new ModelRenderer[7];
		sideModels[0] = new ModelRenderer(this, 0, 10);
		sideModels[1] = new ModelRenderer(this, 0, 0);
		sideModels[2] = new ModelRenderer(this, 0, 0);
		sideModels[3] = new ModelRenderer(this, 0, 0);
		sideModels[4] = new ModelRenderer(this, 0, 0);
		sideModels[5] = new ModelRenderer(this, 44, 10);
		byte byte0 = 20;
		byte byte1 = 8;
		byte byte2 = 16;
		byte byte3 = 4;
		sideModels[0].addBox(-byte0 / 2, -byte2 / 2, -1F, byte0, byte2, 2, 0.0F);
		sideModels[0].setRotationPoint(0.0F, 0 + byte3, 0.0F);
		sideModels[5].addBox(-byte0 / 2 + 1, -byte2 / 2 + 1, -1F, byte0 - 2, byte2 - 2, 1, 0.0F);
		sideModels[5].setRotationPoint(0.0F, 0 + byte3, 0.0F);
		sideModels[1].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		sideModels[1].setRotationPoint(-byte0 / 2 + 1, 0 + byte3, 0.0F);
		sideModels[2].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		sideModels[2].setRotationPoint(byte0 / 2 - 1, 0 + byte3, 0.0F);
		sideModels[3].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		sideModels[3].setRotationPoint(0.0F, 0 + byte3, -byte2 / 2 + 1);
		sideModels[4].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		sideModels[4].setRotationPoint(0.0F, 0 + byte3, byte2 / 2 - 1);
		sideModels[0].rotateAngleX = 1.570796F;
		sideModels[1].rotateAngleY = 4.712389F;
		sideModels[2].rotateAngleY = 1.570796F;
		sideModels[3].rotateAngleY = 3.141593F;
		sideModels[5].rotateAngleX = -1.570796F;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		sideModels[5].rotationPointY = 4F - f2;
		for (int i = 0; i < 6; i++) {
			sideModels[i].render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}
}
