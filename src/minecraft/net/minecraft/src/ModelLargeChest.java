package net.minecraft.src;

public class ModelLargeChest extends ModelChest {
	public ModelLargeChest() {
		chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
		chestLid.addBox(0.0F, -5F, -14F, 30, 5, 14, 0.0F);
		chestLid.rotationPointX = 1.0F;
		chestLid.rotationPointY = 7F;
		chestLid.rotationPointZ = 15F;
		chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
		chestKnob.addBox(-1F, -2F, -15F, 2, 4, 1, 0.0F);
		chestKnob.rotationPointX = 16F;
		chestKnob.rotationPointY = 7F;
		chestKnob.rotationPointZ = 15F;
		chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(128, 64);
		chestBelow.addBox(0.0F, 0.0F, 0.0F, 30, 10, 14, 0.0F);
		chestBelow.rotationPointX = 1.0F;
		chestBelow.rotationPointY = 6F;
		chestBelow.rotationPointZ = 1.0F;
	}
}
