package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiButton {

	public float sliderValue = 1.0F;
	public boolean dragging = false;
	private EnumOptions idFloat = null;


	public GuiSlider(int var1, int var2, int var3, EnumOptions var4, String var5, float var6) {
		super(var1, var2, var3, 150, 20, var5);
		this.idFloat = var4;
		this.sliderValue = var6;
	}

	protected int getHoverState(boolean var1) {
		return 0;
	}

	protected void mouseDragged(Minecraft var1, int var2, int var3) {
		if(this.drawButton) {
			if(this.dragging) {
				this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
				if(this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}

				if(this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}

				var1.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
				this.displayString = var1.gameSettings.getKeyBinding(this.idFloat);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
		}
	}

	public boolean mousePressed(Minecraft var1, int var2, int var3) {
		if(super.mousePressed(var1, var2, var3)) {
			//Spout start
			if (!this.enabled){
				return true;
			}
			//Spout End
			this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
			if(this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if(this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}

			var1.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
			this.displayString = var1.gameSettings.getKeyBinding(this.idFloat);
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	public void mouseReleased(int var1, int var2) {
		this.dragging = false;
	}
}
