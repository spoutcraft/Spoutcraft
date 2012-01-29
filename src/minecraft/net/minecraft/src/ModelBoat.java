package net.minecraft.src;

public class ModelBoat extends ModelBase {
	public ModelRenderer boatSides[];

	public ModelBoat() {
		boatSides = new ModelRenderer[5];
		boatSides[0] = new ModelRenderer(this, 0, 8);
		boatSides[1] = new ModelRenderer(this, 0, 0);
		boatSides[2] = new ModelRenderer(this, 0, 0);
		boatSides[3] = new ModelRenderer(this, 0, 0);
		boatSides[4] = new ModelRenderer(this, 0, 0);
		byte byte0 = 24;
		byte byte1 = 6;
		byte byte2 = 20;
		byte byte3 = 4;
		boatSides[0].addBox(-byte0 / 2, -byte2 / 2 + 2, -3F, byte0, byte2 - 4, 4, 0.0F);
		boatSides[0].setRotationPoint(0.0F, 0 + byte3, 0.0F);
		boatSides[1].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		boatSides[1].setRotationPoint(-byte0 / 2 + 1, 0 + byte3, 0.0F);
		boatSides[2].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		boatSides[2].setRotationPoint(byte0 / 2 - 1, 0 + byte3, 0.0F);
		boatSides[3].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		boatSides[3].setRotationPoint(0.0F, 0 + byte3, -byte2 / 2 + 1);
		boatSides[4].addBox(-byte0 / 2 + 2, -byte1 - 1, -1F, byte0 - 4, byte1, 2, 0.0F);
		boatSides[4].setRotationPoint(0.0F, 0 + byte3, byte2 / 2 - 1);
		boatSides[0].rotateAngleX = 1.570796F;
		boatSides[1].rotateAngleY = 4.712389F;
		boatSides[2].rotateAngleY = 1.570796F;
		boatSides[3].rotateAngleY = 3.141593F;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		for (int i = 0; i < 5; i++) {
			boatSides[i].render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}
}
