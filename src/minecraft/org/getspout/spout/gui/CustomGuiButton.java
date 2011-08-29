/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
			float width = (float) (button.getWidth() < 200 ? button.getWidth() : 200);
			GL11.glScalef((float) button.getWidth() / width, (float) button.getHeight() / 20f, 1);
			
			boolean hovering = mouseX >= button.getScreenX() && mouseY >= button.getScreenY() && mouseX < button.getScreenX() + button.getWidth() && mouseY < button.getScreenY() + button.getHeight();
			int hoverState = this.getHoverState(hovering);
			this.drawTexturedModalRect(0, 0, 0, 46 + hoverState * 20, (int) Math.ceil(width / 2), 20);
			this.drawTexturedModalRect((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46 + hoverState * 20, (int) Math.ceil(width / 2), 20);
			this.mouseDragged(game, mouseX, mouseY);
			Color color = button.getTextColor();
			if(!button.isEnabled()) {
				color = button.getDisabledColor();
			} else if(hovering) {
				color = button.getHoverColor();
			}
			int left = (int) 5;
			switch (button.getAlign()) {
				case TOP_CENTER:
				case CENTER_CENTER:
				case BOTTOM_CENTER:
					left = (int) ((width / 2) - (font.getStringWidth(button.getText()) / 2)); break;
				case TOP_RIGHT:
				case CENTER_RIGHT:
				case BOTTOM_RIGHT:
					left = (int) (width - font.getStringWidth(button.getText())) - 5; break;
			}
			this.drawString(font, button.getText(), left, 6, color.toInt());
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		return button.isEnabled() && button.isVisible() && i >= button.getScreenX() && j >= button.getScreenY() && i < button.getScreenX() + button.getWidth() && j < button.getScreenY() + button.getHeight();
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