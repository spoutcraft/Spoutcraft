package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButtonLanguage extends GuiButton {
	public GuiButtonLanguage(int i, int j, int k) {
		super(i, j, k, 20, 20, "");
	}

	public void drawButton(Minecraft minecraft, int i, int j) {
		if (!drawButton) {
			return;
		}
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
		int k = 106;
		if (flag) {
			k += height;
		}
		drawTexturedModalRect(xPosition, yPosition, 0, k, width, height);
	}
}
