package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {

	protected int width;
	protected int height;
	public int xPosition;
	public int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean enabled2;


	public GuiButton(int var1, int var2, int var3, String var4) {
		this(var1, var2, var3, 200, 20, var4);
	}

	public GuiButton(int var1, int var2, int var3, int var4, int var5, String var6) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.enabled2 = true;
		this.id = var1;
		this.xPosition = var2;
		this.yPosition = var3;
		this.width = var4;
		this.height = var5;
		this.displayString = var6;
	}

	protected int getHoverState(boolean var1) {
		byte var2 = 1;
		if(!this.enabled) {
			var2 = 0;
		} else if(var1) {
			var2 = 2;
		}

		return var2;
	}

	public void drawButton(Minecraft var1, int var2, int var3) {
		if(this.enabled2) {
			FontRenderer var4 = var1.fontRenderer;
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var1.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean var5 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
			int var6 = this.getHoverState(var5);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var6 * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
			this.mouseDragged(var1, var2, var3);
			if(!this.enabled) {
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
			} else if(var5) {
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
			} else {
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
			}

		}
	}

	protected void mouseDragged(Minecraft var1, int var2, int var3) {}

	public void mouseReleased(int var1, int var2) {}

	public boolean mousePressed(Minecraft var1, int var2, int var3) {
		return this.enabled && this.enabled2 && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
	}
}
