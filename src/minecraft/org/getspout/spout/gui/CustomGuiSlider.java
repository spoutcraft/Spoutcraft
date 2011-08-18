package org.getspout.spout.gui;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import org.getspout.spout.packet.*;

public class CustomGuiSlider extends GuiSlider {
	protected Screen screen;
	protected Slider slider;
	public CustomGuiSlider(Screen screen, Slider slider) {
		super(0, 0, 0, null, null, 0);
		this.screen = screen;
		this.slider = slider;
	}
	
	@Override
	protected void mouseDragged(Minecraft game, int mouseX, int mouseY) {
		if(slider.isVisible()) {
			if(this.dragging) {
				slider.setSliderPosition((float)(mouseX - (slider.getScreenX()+ 4)) / (float)(slider.getWidth() - 8));
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float width = (float) (slider.getWidth() < 200 ? slider.getWidth() : 200) - 8;
			this.drawTexturedModalRect((int) (slider.getSliderPosition() * width), 0, 0, 66, 4, 20);
			this.drawTexturedModalRect((int) (slider.getSliderPosition() * width) + 4, 0, 196, 66, 4, 20);
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft game, int mouseX, int mouseY) {
		if(mousePressedWidget(game, mouseX, mouseY)) {
			slider.setSliderPosition((float)(mouseX - (slider.getScreenX() + 4)) / (float)(slider.getWidth() - 8));
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void drawButton(Minecraft game, int mouseX, int mouseY) {
		if(slider.isVisible()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, game.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float width = (float) (slider.getWidth() < 200 ? slider.getWidth() : 200);
			GL11.glTranslatef((float) slider.getScreenX(), (float) slider.getScreenY(), 0);
			GL11.glScalef((float) slider.getWidth() / width, (float) slider.getHeight() / 20f, 1);
			
			boolean hovering = mouseX >= slider.getScreenX() && mouseY >= slider.getScreenY() && mouseX < slider.getScreenX() + slider.getWidth() && mouseY < slider.getScreenY() + slider.getHeight();
			
			int hoverState = this.getHoverState(hovering);
			this.drawTexturedModalRect(0, 0, 0, 46 + hoverState * 20, (int) Math.ceil(width / 2), 20);
			this.drawTexturedModalRect((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46 + hoverState * 20, (int) Math.ceil(width / 2), 20);
			this.mouseDragged(game, mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, slider, slider.getSliderPosition())));
	}
	
	public boolean mousePressedWidget(Minecraft game, int mouseX, int mouseY) {
		return slider.isEnabled() && mouseX >= slider.getScreenX() && mouseY >= slider.getScreenY() && mouseX < slider.getScreenX() + slider.getWidth() && mouseY < slider.getScreenY() + slider.getHeight();
	}
	
	public Slider getWidget() {
		return slider;
	}
	
	public boolean equals(Widget widget) {
		return widget.getId().equals(slider.getId());
	}
	
	public void updateWidget(Slider widget) {
		this.slider = widget;
	}
}