package net.minecraft.src;

public abstract class TileEntitySpecialRenderer {
	protected TileEntityRenderer tileEntityRenderer;

	public TileEntitySpecialRenderer() {
	}

	public abstract void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
	        float f);

	protected void bindTextureByName(String s) {
		RenderEngine renderengine = tileEntityRenderer.renderEngine;
		if (renderengine != null) {
			renderengine.bindTexture(renderengine.getTexture(s));
		}
	}

	public void setTileEntityRenderer(TileEntityRenderer tileentityrenderer) {
		tileEntityRenderer = tileentityrenderer;
	}

	public void func_31069_a(World world) {
	}

	public FontRenderer getFontRenderer() {
		return tileEntityRenderer.getFontRenderer();
	}
}
