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
			this.drawTexturedModalRect((int) (slider.getScreenX()+ (slider.getSliderPosition() * (float)(slider.getWidth() - 8))), (int) slider.getScreenY(), 0, 66, 4, (int) slider.getHeight());
			this.drawTexturedModalRect((int) (slider.getScreenX()+ (slider.getSliderPosition() * (float)(slider.getWidth() - 8)) + 4), (int) slider.getScreenY(), 196, 66, 4, (int) slider.getHeight());
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
			boolean hovering = mouseX >= slider.getScreenX() && mouseY >= slider.getScreenY() && mouseX < slider.getScreenX() + slider.getWidth() && mouseY < slider.getScreenY() + slider.getHeight();
			int hoverState = this.getHoverState(hovering);
			this.drawTexturedModalRect((int) slider.getScreenX(), (int) slider.getScreenY(), 0, 46 + hoverState * 20, (int) (slider.getWidth() / 2), (int) slider.getHeight());
			this.drawTexturedModalRect((int) (slider.getScreenX() + slider.getWidth() / 2), (int) slider.getScreenY(), (int) (200 - slider.getWidth() / 2), 46 + hoverState * 20, (int) (slider.getWidth() / 2), (int) slider.getHeight());
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