package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer
	implements IProgressUpdate {
	private String field_1004_a;
	private Minecraft mc;
	private String currentlyDisplayedText;
	private long field_1006_d;
	private boolean field_1005_e;

	public LoadingScreenRenderer(Minecraft minecraft) {
		field_1004_a = "";
		currentlyDisplayedText = "";
		field_1006_d = System.currentTimeMillis();
		field_1005_e = false;
		mc = minecraft;
	}

	public void printText(String s) {
		field_1005_e = false;
		func_597_c(s);
	}

	public void displaySavingString(String s) {
		field_1005_e = true;
		func_597_c(currentlyDisplayedText);
	}

	public void func_597_c(String s) {
		if (!mc.running) {
			if (field_1005_e) {
				return;
			}
			else {
				throw new MinecraftError();
			}
		}
		else {
			currentlyDisplayedText = s;
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			GL11.glClear(256);
			GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 100D, 300D);
			GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200F);
			return;
		}
	}

	public void displayLoadingString(String s) {
		if (!mc.running) {
			if (field_1005_e) {
				return;
			}
			else {
				throw new MinecraftError();
			}
		}
		else {
			field_1006_d = 0L;
			field_1004_a = s;
			setLoadingProgress(-1);
			field_1006_d = 0L;
			return;
		}
	}

	public void setLoadingProgress(int i) {
		if (!mc.running) {
			if (field_1005_e) {
				return;
			}
			else {
				throw new MinecraftError();
			}
		}
		long l = System.currentTimeMillis();
		if (l - field_1006_d < 20L) {
			return;
		}
		field_1006_d = l;
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int j = scaledresolution.getScaledWidth();
		int k = scaledresolution.getScaledHeight();
		GL11.glClear(256);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 100D, 300D);
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200F);
		GL11.glClear(16640);
		Tessellator tessellator = Tessellator.instance;
		int i1 = mc.renderEngine.getTexture("/gui/background.png");
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, i1);
		float f = 32F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(0x404040);
		tessellator.addVertexWithUV(0.0D, k, 0.0D, 0.0D, (float)k / f);
		tessellator.addVertexWithUV(j, k, 0.0D, (float)j / f, (float)k / f);
		tessellator.addVertexWithUV(j, 0.0D, 0.0D, (float)j / f, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		if (i >= 0) {
			byte byte0 = 100;
			byte byte1 = 2;
			int j1 = j / 2 - byte0 / 2;
			int k1 = k / 2 + 16;
			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(0x808080);
			tessellator.addVertex(j1, k1, 0.0D);
			tessellator.addVertex(j1, k1 + byte1, 0.0D);
			tessellator.addVertex(j1 + byte0, k1 + byte1, 0.0D);
			tessellator.addVertex(j1 + byte0, k1, 0.0D);
			tessellator.setColorOpaque_I(0x80ff80);
			tessellator.addVertex(j1, k1, 0.0D);
			tessellator.addVertex(j1, k1 + byte1, 0.0D);
			tessellator.addVertex(j1 + i, k1 + byte1, 0.0D);
			tessellator.addVertex(j1 + i, k1, 0.0D);
			tessellator.draw();
			GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		}
		mc.fontRenderer.drawStringWithShadow(currentlyDisplayedText, (j - mc.fontRenderer.getStringWidth(currentlyDisplayedText)) / 2, k / 2 - 4 - 16, 0xffffff);
		mc.fontRenderer.drawStringWithShadow(field_1004_a, (j - mc.fontRenderer.getStringWidth(field_1004_a)) / 2, (k / 2 - 4) + 8, 0xffffff);
		Display.update();
		try {
			Thread.yield();
		}
		catch (Exception exception) { }
	}
}
