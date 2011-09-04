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

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.getspout.spout.packet.*;

public class CustomTextField extends GuiButton {
	protected Screen screen;
	private int count = 0;
	private boolean focus = false;
	public CustomTextField(Screen screen, TextField field) {
		super(0, 0, 0, 0, 0, null);
		this.screen = screen;
		this.field = field;
	}
	
	public void textboxKeyTyped(char key, int keyId) {
		boolean dirty = false;
		try {
			if(getField().isEnabled() && this.focus) {
				if(key == 22) {
					String clipboard = GuiScreen.getClipboardString();
					if(clipboard == null) {
						clipboard = "";
					}

					int max = 32 - getField().getText().length();
					if(max > clipboard.length()) {
						max = clipboard.length();
					}

					if(max > 0) {
						getField().setText(getField().getText() + clipboard.substring(0, max));
						dirty = true;
					}
				}
				if (keyId == Keyboard.KEY_RIGHT && getField().getCursorPosition() < getField().getText().length()) {
					getField().setCursorPosition(getField().getCursorPosition() + 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_LEFT && getField().getCursorPosition() > 0) {
					getField().setCursorPosition(getField().getCursorPosition() - 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_DELETE && getField().getCursorPosition() > 0 && getField().getCursorPosition() < getField().getText().length()) {
					getField().setText(getField().getText().substring(0, getField().getCursorPosition()) + getField().getText().substring(getField().getCursorPosition() + 1));
					dirty = true;
				}
				else if(keyId == Keyboard.KEY_BACK && getField().getText().length() > 0 && getField().getCursorPosition() > 0) {
					getField().setText(getField().getText().substring(0, getField().getCursorPosition() - 1) + getField().getText().substring(getField().getCursorPosition()));
					getField().setCursorPosition(getField().getCursorPosition() - 1);
					dirty = true;
				}
				if(ChatAllowedCharacters.allowedCharacters.indexOf(key) > -1 && (getField().getText().length() < getField().getMaximumCharacters() || getField().getMaximumCharacters() == 0)) {
					String newText = "";
					if (getField().getCursorPosition() > 0) {
						newText += getField().getText().substring(0, getField().getCursorPosition());
					}
					newText += key;
					if (getField().getCursorPosition() < getField().getText().length()) {
						newText += getField().getText().substring(getField().getCursorPosition());
					}
					getField().setText(newText);
					getField().setCursorPosition(getField().getCursorPosition() + 1);
					dirty = true;
				}
				if (dirty) {
					((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, field, getField().getText(), getField().getCursorPosition())));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft game, int mouseX, int mouseY) {
		this.setFocused(getField().isEnabled() && getField().isVisible() && mouseX >= getField().getScreenX() && mouseX < getField().getScreenX() + getField().getWidth() && mouseY >= getField().getScreenY() && mouseY < getField().getScreenY() + getField().getHeight());
		return isFocused();
	}
	
	public boolean isFocused() {
		return focus;
	}
	
	public void setFocused(boolean focus) {
		if(focus && !this.focus) {
			getField().setCursorPosition(getField().getText().length());
		}

		this.focus = focus;
	}
	
	@Override
	public void drawButton(Minecraft game, int mouseX, int mouseY) {
		this.drawRect((int) (getField().getScreenX() - 1), (int) (getField().getScreenY() - 1), (int) (getField().getScreenX() + getField().getWidth() + 1), (int) (getField().getScreenY() + getField().getHeight() + 1), getField().getBorderColor().toInt());
		this.drawRect((int)getField().getScreenX(), (int) getField().getScreenY(), (int) (getField().getScreenX() + getField().getWidth()), (int) (getField().getScreenY() + getField().getHeight()), getField().getFieldColor().toInt());
		if(getField().isEnabled()) {
			count++;
			boolean showCursor = this.focus && count % 40 < 15;
			String text = getField().getText();
			if (getField().getCursorPosition() < 0) getField().setCursorPosition(0);
			if (getField().getCursorPosition() > text.length()) getField().setCursorPosition(text.length());
			if (showCursor) {
				text = "";
				if (getField().getCursorPosition() > 0) {
					text += getField().getText().substring(0, getField().getCursorPosition());
				}
				text += "_";
				if (getField().getCursorPosition() < getField().getText().length()) {
					text += getField().getText().substring(getField().getCursorPosition() + 1);
				}
			}
			this.drawString(game.fontRenderer, text, (int) (getField().getScreenX() + 4), (int) (getField().getScreenY() + (getField().getHeight() - 8) / 2), getField().getColor().toInt());
		} else {
			this.drawString(game.fontRenderer, getField().getText(), (int) (getField().getScreenX() + 4), (int) (getField().getScreenY() + (getField().getHeight() - 8) / 2), getField().getDisabledColor().toInt());
		}

	}
	
	public TextField getField() {
		return (TextField) field;
	}
	
	@Override
	public void updateWidget(Control widget) {
		super.updateWidget(widget);
		if (getField().getCursorPosition() < 0) getField().setCursorPosition(0);
		if (getField().getCursorPosition() > getField().getText().length()) getField().setCursorPosition(getField().getText().length());
	}
}