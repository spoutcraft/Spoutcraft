package net.minecraft.src;

public class ModelPig extends ModelQuadruped {
	public ModelPig() {
		this(0.0F);
	}

	public ModelPig(float f) {
		super(6, f);
		head.setTextureOffset(16, 16).addBox(-2F, 0.0F, -9F, 4, 3, 1, f);
		field_40331_g = 4F;
	}
}
