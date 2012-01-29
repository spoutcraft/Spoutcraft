package net.minecraft.src;

public class ChestItemRenderHelper {
	public static ChestItemRenderHelper instance = new ChestItemRenderHelper();
	private TileEntityChest field_35610_b;

	public ChestItemRenderHelper() {
		field_35610_b = new TileEntityChest();
	}

	public void func_35609_a(Block block, int i, float f) {
		TileEntityRenderer.instance.renderTileEntityAt(field_35610_b, 0.0D, 0.0D, 0.0D, 0.0F);
	}
}
