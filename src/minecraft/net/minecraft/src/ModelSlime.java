package net.minecraft.src;

public class ModelSlime extends ModelBase {
	ModelRenderer slimeBodies;
	ModelRenderer slimeRightEye;
	ModelRenderer slimeLeftEye;
	ModelRenderer slimeMouth;

	public ModelSlime(int i) {
		slimeBodies = new ModelRenderer(this, 0, i);
		slimeBodies.addBox(-4F, 16F, -4F, 8, 8, 8);
		if (i > 0) {
			slimeBodies = new ModelRenderer(this, 0, i);
			slimeBodies.addBox(-3F, 17F, -3F, 6, 6, 6);
			slimeRightEye = new ModelRenderer(this, 32, 0);
			slimeRightEye.addBox(-3.25F, 18F, -3.5F, 2, 2, 2);
			slimeLeftEye = new ModelRenderer(this, 32, 4);
			slimeLeftEye.addBox(1.25F, 18F, -3.5F, 2, 2, 2);
			slimeMouth = new ModelRenderer(this, 32, 8);
			slimeMouth.addBox(0.0F, 21F, -3.5F, 1, 1, 1);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		slimeBodies.render(f5);
		if (slimeRightEye != null) {
			slimeRightEye.render(f5);
			slimeLeftEye.render(f5);
			slimeMouth.render(f5);
		}
	}
}
