package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderEnchantmentTable extends TileEntitySpecialRenderer {
	private ModelBook field_40450_a;

	public RenderEnchantmentTable() {
		field_40450_a = new ModelBook();
	}

	public void func_40449_a(TileEntityEnchantmentTable tileentityenchantmenttable, double d, double d1, double d2,
	        float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.75F, (float)d2 + 0.5F);
		float f1 = (float)tileentityenchantmenttable.field_40068_a + f;
		GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(f1 * 0.1F) * 0.01F, 0.0F);
		float f2;
		for (f2 = tileentityenchantmenttable.field_40069_h - tileentityenchantmenttable.field_40067_p; f2 >= 3.141593F; f2 -= 6.283185F) { }
		for (; f2 < -3.141593F; f2 += 6.283185F) { }
		float f3 = tileentityenchantmenttable.field_40067_p + f2 * f;
		GL11.glRotatef((-f3 * 180F) / 3.141593F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(80F, 0.0F, 0.0F, 1.0F);
		bindTextureByName("/item/book.png");
		float f4 = tileentityenchantmenttable.field_40065_c + (tileentityenchantmenttable.field_40063_b - tileentityenchantmenttable.field_40065_c) * f + 0.25F;
		float f5 = tileentityenchantmenttable.field_40065_c + (tileentityenchantmenttable.field_40063_b - tileentityenchantmenttable.field_40065_c) * f + 0.75F;
		f4 = (f4 - (float)MathHelper.func_40346_b(f4)) * 1.6F - 0.3F;
		f5 = (f5 - (float)MathHelper.func_40346_b(f5)) * 1.6F - 0.3F;
		if (f4 < 0.0F) {
			f4 = 0.0F;
		}
		if (f5 < 0.0F) {
			f5 = 0.0F;
		}
		if (f4 > 1.0F) {
			f4 = 1.0F;
		}
		if (f5 > 1.0F) {
			f5 = 1.0F;
		}
		float f6 = tileentityenchantmenttable.field_40060_g + (tileentityenchantmenttable.field_40059_f - tileentityenchantmenttable.field_40060_g) * f;
		field_40450_a.render(null, f1, f4, f5, f6, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
	        float f) {
		func_40449_a((TileEntityEnchantmentTable)tileentity, d, d1, d2, f);
	}
}
