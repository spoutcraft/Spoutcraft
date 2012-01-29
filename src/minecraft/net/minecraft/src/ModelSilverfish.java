package net.minecraft.src;

public class ModelSilverfish extends ModelBase {
	private ModelRenderer silverfishBodyParts[];
	private ModelRenderer silverfishWings[];
	private float field_35399_c[];
	private static final int silverfishBoxLength[][] = {
		{
			3, 2, 2
		}, {
			4, 3, 2
		}, {
			6, 4, 3
		}, {
			3, 3, 3
		}, {
			2, 2, 3
		}, {
			2, 1, 2
		}, {
			1, 1, 2
		}
	};
	private static final int silverfishTexturePositions[][] = {
		{
			0, 0
		}, {
			0, 4
		}, {
			0, 9
		}, {
			0, 16
		}, {
			0, 22
		}, {
			11, 0
		}, {
			13, 4
		}
	};

	public ModelSilverfish() {
		field_35399_c = new float[7];
		silverfishBodyParts = new ModelRenderer[7];
		float f = -3.5F;
		for (int i = 0; i < silverfishBodyParts.length; i++) {
			silverfishBodyParts[i] = new ModelRenderer(this, silverfishTexturePositions[i][0], silverfishTexturePositions[i][1]);
			silverfishBodyParts[i].addBox((float)silverfishBoxLength[i][0] * -0.5F, 0.0F, (float)silverfishBoxLength[i][2] * -0.5F, silverfishBoxLength[i][0], silverfishBoxLength[i][1], silverfishBoxLength[i][2]);
			silverfishBodyParts[i].setRotationPoint(0.0F, 24 - silverfishBoxLength[i][1], f);
			field_35399_c[i] = f;
			if (i < silverfishBodyParts.length - 1) {
				f += (float)(silverfishBoxLength[i][2] + silverfishBoxLength[i + 1][2]) * 0.5F;
			}
		}

		silverfishWings = new ModelRenderer[3];
		silverfishWings[0] = new ModelRenderer(this, 20, 0);
		silverfishWings[0].addBox(-5F, 0.0F, (float)silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
		silverfishWings[0].setRotationPoint(0.0F, 16F, field_35399_c[2]);
		silverfishWings[1] = new ModelRenderer(this, 20, 11);
		silverfishWings[1].addBox(-3F, 0.0F, (float)silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
		silverfishWings[1].setRotationPoint(0.0F, 20F, field_35399_c[4]);
		silverfishWings[2] = new ModelRenderer(this, 20, 18);
		silverfishWings[2].addBox(-3F, 0.0F, (float)silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
		silverfishWings[2].setRotationPoint(0.0F, 19F, field_35399_c[1]);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		for (int i = 0; i < silverfishBodyParts.length; i++) {
			silverfishBodyParts[i].render(f5);
		}

		for (int j = 0; j < silverfishWings.length; j++) {
			silverfishWings[j].render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		for (int i = 0; i < silverfishBodyParts.length; i++) {
			silverfishBodyParts[i].rotateAngleY = MathHelper.cos(f2 * 0.9F + (float)i * 0.15F * 3.141593F) * 3.141593F * 0.05F * (float)(1 + Math.abs(i - 2));
			silverfishBodyParts[i].rotationPointX = MathHelper.sin(f2 * 0.9F + (float)i * 0.15F * 3.141593F) * 3.141593F * 0.2F * (float)Math.abs(i - 2);
		}

		silverfishWings[0].rotateAngleY = silverfishBodyParts[2].rotateAngleY;
		silverfishWings[1].rotateAngleY = silverfishBodyParts[4].rotateAngleY;
		silverfishWings[1].rotationPointX = silverfishBodyParts[4].rotationPointX;
		silverfishWings[2].rotateAngleY = silverfishBodyParts[1].rotateAngleY;
		silverfishWings[2].rotationPointX = silverfishBodyParts[1].rotationPointX;
	}
}
