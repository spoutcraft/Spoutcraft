package org.getspout.spout.gui;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;
import net.minecraft.client.Minecraft;

public class CustomGuiButton extends GuiButton {
	protected Screen screen;
	protected Button button;
	public CustomGuiButton(Screen screen, Button button) {
		super(0, (int) button.getScreenX(), (int) button.getScreenY(), (int) button.getWidth(), (int) button.getHeight(), button.getText());
		this.screen = screen;
		this.button = button;
	}
	
	@Override
	public void drawButton(Minecraft game, int mouseX, int mouseY) {
		if(button.isVisible()) {
			FontRenderer font = game.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, game.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) button.getScreenX(), (float) button.getScreenY(), 0);
			GL11.glScalef((float) button.getWidth() / 200f, (float) button.getHeight() / 20f, 1);
			
			boolean hovering = mouseX >= button.getScreenX() && mouseY >= button.getScreenY() && mouseX < button.getScreenX() + button.getWidth() && mouseY < button.getScreenY() + button.getHeight();
			int hoverState = this.getHoverState(hovering);
			this.drawTexturedModalRect(0, 0, 0, 46 + hoverState * 20, 100, 20);
			this.drawTexturedModalRect(100, 0, 100, 46 + hoverState * 20, 100, 20);
			this.mouseDragged(game, mouseX, mouseY);
			int color = button.getColor();
			if(!button.isEnabled()) {
				color = button.getDisabledColor();
			} else if(hovering) {
				color = button.getHoverColor();
			}
			int left = (int) 5;
			switch (button.getAlignX()) {
				case SECOND: left = (int) (100 - (font.getStringWidth(button.getText()) / 2)); break;
				case THIRD: left = (int) (200 - font.getStringWidth(button.getText())) - 5; break;
			}
			this.drawString(font, button.getText(), left, 6, color);
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		return enabled && i >= button.getScreenX() && j >= button.getScreenY() && i < button.getScreenX() + button.getWidth() && j < button.getScreenY() + button.getHeight();
	}
	
	public Button getWidget() {
		return button;
	}
	
	public boolean equals(Widget widget) {
		return widget.getId().equals(button.getId());
	}
	
	public void updateWidget(Button widget) {
		this.button = widget;
	}
}