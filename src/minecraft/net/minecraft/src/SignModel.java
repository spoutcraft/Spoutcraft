package net.minecraft.src;

public class SignModel extends ModelBase {
	public ModelRenderer signBoard;
	public ModelRenderer signStick;

	public SignModel() {
		signBoard = new ModelRenderer(this, 0, 0);
		signBoard.addBox(-12F, -14F, -1F, 24, 12, 2, 0.0F);
		signStick = new ModelRenderer(this, 0, 14);
		signStick.addBox(-1F, -2F, -1F, 2, 14, 2, 0.0F);
	}

	public void renderSign() {
		signBoard.render(0.0625F);
		signStick.render(0.0625F);
	}
}
