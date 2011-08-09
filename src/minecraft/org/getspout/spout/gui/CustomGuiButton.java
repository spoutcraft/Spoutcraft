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
			boolean hovering = mouseX >= button.getScreenX() && mouseY >= button.getScreenY() && mouseX < button.getScreenX() + button.getWidth() && mouseY < button.getScreenY() + button.getHeight();
			int hoverState = this.getHoverState(hovering);
			this.drawTexturedModalRect((int) button.getScreenX(), (int) button.getScreenY(), 0, 46 + hoverState * 20, (int) (button.getWidth() / 2), (int) button.getHeight());
			this.drawTexturedModalRect((int) (button.getScreenX() + button.getWidth() / 2), (int) button.getScreenY(), (int) (200 - button.getWidth() / 2), 46 + hoverState * 20, (int) (button.getWidth() / 2), (int) button.getHeight());
			this.mouseDragged(game, mouseX, mouseY);
			if(!button.isEnabled()) {
				this.drawCenteredString(font, button.getText(), (int) (button.getScreenX() + button.getWidth() / 2), (int) (button.getScreenY() + (button.getHeight() - 8) / 2), button.getDisabledColor());
			} else if(hovering) {
				this.drawCenteredString(font, button.getText(), (int) (button.getScreenX() + button.getWidth() / 2), (int) (button.getScreenY() + (button.getHeight() - 8) / 2), button.getHoverColor());
			} else {
				this.drawCenteredString(font, button.getText(), (int) (button.getScreenX() + button.getWidth() / 2), (int) (button.getScreenY() + (button.getHeight() - 8) / 2), button.getColor());
			}

		}
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