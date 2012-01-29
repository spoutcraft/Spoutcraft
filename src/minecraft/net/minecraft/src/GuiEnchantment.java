package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiEnchantment extends GuiContainer {
	private static ModelBook field_40220_w = new ModelBook();
	private Random field_40230_x;
	private ContainerEnchantment containerEnchantment;
	public int field_40227_h;
	public float field_40229_i;
	public float field_40225_j;
	public float field_40226_k;
	public float field_40223_l;
	public float field_40224_m;
	public float field_40221_n;
	ItemStack field_40222_o;

	public GuiEnchantment(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
		super(new ContainerEnchantment(inventoryplayer, world, i, j, k));
		field_40230_x = new Random();
		containerEnchantment = (ContainerEnchantment)inventorySlots;
	}

	public void onGuiClosed() {
		super.onGuiClosed();
	}

	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString("Enchant", 12, 6, 0x404040);
		fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
	}

	public void updateScreen() {
		super.updateScreen();
		func_40219_x_();
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		for (int j1 = 0; j1 < 3; j1++) {
			int k1 = i - (l + 60);
			int l1 = j - (i1 + 14 + 19 * j1);
			if (k1 >= 0 && l1 >= 0 && k1 < 108 && l1 < 19 && containerEnchantment.enchantItem(mc.thePlayer, j1)) {
				mc.playerController.func_40593_a(containerEnchantment.windowId, j1);
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/gui/enchant.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		GL11.glPushMatrix();
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		GL11.glViewport(((scaledresolution.getScaledWidth() - 320) / 2) * scaledresolution.scaleFactor, ((scaledresolution.getScaledHeight() - 240) / 2) * scaledresolution.scaleFactor, 320 * scaledresolution.scaleFactor, 240 * scaledresolution.scaleFactor);
		GL11.glTranslatef(-0.34F, 0.23F, 0.0F);
		GLU.gluPerspective(90F, 1.333333F, 9F, 80F);
		float f1 = 1.0F;
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		RenderHelper.enableStandardItemLighting();
		GL11.glTranslatef(0.0F, 3.3F, -16F);
		GL11.glScalef(f1, f1, f1);
		float f2 = 5F;
		GL11.glScalef(f2, f2, f2);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/item/book.png"));
		GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
		float f3 = field_40221_n + (field_40224_m - field_40221_n) * f;
		GL11.glTranslatef((1.0F - f3) * 0.2F, (1.0F - f3) * 0.1F, (1.0F - f3) * 0.25F);
		GL11.glRotatef(-(1.0F - f3) * 90F - 90F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		float f4 = field_40225_j + (field_40229_i - field_40225_j) * f + 0.25F;
		float f5 = field_40225_j + (field_40229_i - field_40225_j) * f + 0.75F;
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
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		field_40220_w.render(null, 0.0F, f4, f5, f3, 0.0F, 0.0625F);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		RenderHelper.disableStandardItemLighting();
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		GL11.glPopMatrix();
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		EnchantmentNameParts.field_40253_a.func_40250_a(containerEnchantment.nameSeed);
		for (int j1 = 0; j1 < 3; j1++) {
			String s = EnchantmentNameParts.field_40253_a.func_40249_a();
			zLevel = 0.0F;
			mc.renderEngine.bindTexture(k);
			int k1 = containerEnchantment.enchantLevels[j1];
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if (k1 == 0) {
				drawTexturedModalRect(l + 60, i1 + 14 + 19 * j1, 0, 185, 108, 19);
				continue;
			}
			String s1 = (new StringBuilder()).append("").append(k1).toString();
			FontRenderer fontrenderer = mc.standardGalacticFontRenderer;
			int l1 = 0x685e4a;
			if (mc.thePlayer.playerLevel < k1) {
				drawTexturedModalRect(l + 60, i1 + 14 + 19 * j1, 0, 185, 108, 19);
				fontrenderer.drawSplitString(s, l + 62, i1 + 16 + 19 * j1, 104, (l1 & 0xfefefe) >> 1);
				fontrenderer = mc.fontRenderer;
				l1 = 0x407f10;
				fontrenderer.drawStringWithShadow(s1, (l + 62 + 104) - fontrenderer.getStringWidth(s1), i1 + 16 + 19 * j1 + 7, l1);
				continue;
			}
			int i2 = i - (l + 60);
			int j2 = j - (i1 + 14 + 19 * j1);
			if (i2 >= 0 && j2 >= 0 && i2 < 108 && j2 < 19) {
				drawTexturedModalRect(l + 60, i1 + 14 + 19 * j1, 0, 204, 108, 19);
				l1 = 0xffff80;
			}
			else {
				drawTexturedModalRect(l + 60, i1 + 14 + 19 * j1, 0, 166, 108, 19);
			}
			fontrenderer.drawSplitString(s, l + 62, i1 + 16 + 19 * j1, 104, l1);
			fontrenderer = mc.fontRenderer;
			l1 = 0x80ff20;
			fontrenderer.drawStringWithShadow(s1, (l + 62 + 104) - fontrenderer.getStringWidth(s1), i1 + 16 + 19 * j1 + 7, l1);
		}
	}

	public void func_40219_x_() {
		ItemStack itemstack = inventorySlots.getSlot(0).getStack();
		if (!ItemStack.areItemStacksEqual(itemstack, field_40222_o)) {
			field_40222_o = itemstack;
			do {
				field_40226_k += field_40230_x.nextInt(4) - field_40230_x.nextInt(4);
			}
			while (field_40229_i <= field_40226_k + 1.0F && field_40229_i >= field_40226_k - 1.0F);
		}
		field_40227_h++;
		field_40225_j = field_40229_i;
		field_40221_n = field_40224_m;
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (containerEnchantment.enchantLevels[i] != 0) {
				flag = true;
			}
		}

		if (flag) {
			field_40224_m += 0.2F;
		}
		else {
			field_40224_m -= 0.2F;
		}
		if (field_40224_m < 0.0F) {
			field_40224_m = 0.0F;
		}
		if (field_40224_m > 1.0F) {
			field_40224_m = 1.0F;
		}
		float f = (field_40226_k - field_40229_i) * 0.4F;
		float f1 = 0.2F;
		if (f < -f1) {
			f = -f1;
		}
		if (f > f1) {
			f = f1;
		}
		field_40223_l += (f - field_40223_l) * 0.9F;
		field_40229_i = field_40229_i + field_40223_l;
	}
}
