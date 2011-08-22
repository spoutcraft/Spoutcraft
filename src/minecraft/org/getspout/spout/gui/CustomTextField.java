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
import java.util.UUID;

public class CustomTextField extends GuiButton {
	protected Screen screen;
	protected TextField field;
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
			if(field.isEnabled() && this.focus) {
				if(key == 22) {
					String clipboard = GuiScreen.getClipboardString();
					if(clipboard == null) {
						clipboard = "";
					}

					int max = 32 - field.getText().length();
					if(max > clipboard.length()) {
						max = clipboard.length();
					}

					if(max > 0) {
						field.setText(field.getText() + clipboard.substring(0, max));
						dirty = true;
					}
				}
				
				if (keyId == Keyboard.KEY_RIGHT && field.getCursorPosition() < field.getText().length()) {
					field.setCursorPosition(field.getCursorPosition() + 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_LEFT && field.getCursorPosition() > 0) {
					field.setCursorPosition(field.getCursorPosition() - 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_DELETE && field.getCursorPosition() > 0 && field.getCursorPosition() < field.getText().length()) {
					field.setText(field.getText().substring(0, field.getCursorPosition()) + field.getText().substring(field.getCursorPosition() + 1));
					dirty = true;
				}
				else if(keyId == Keyboard.KEY_BACK && field.getText().length() > 0 && field.getCursorPosition() > 0) {
					field.setText(field.getText().substring(0, field.getCursorPosition() - 1) + field.getText().substring(field.getCursorPosition()));
					field.setCursorPosition(field.getCursorPosition() - 1);
					dirty = true;
				}

				if(ChatAllowedCharacters.allowedCharacters.indexOf(key) > -1 && (field.getText().length() < field.getMaximumCharacters() || field.getMaximumCharacters() == 0)) {
					String newText = "";
					if (field.getCursorPosition() > 0) {
						newText += field.getText().substring(0, field.getCursorPosition());
					}
					newText += key;
					if (field.getCursorPosition() < field.getText().length()) {
						newText += field.getText().substring(field.getCursorPosition());
					}
					field.setText(newText);
					field.setCursorPosition(field.getCursorPosition() + 1);
					dirty = true;
				}
				if (dirty) {
					((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, field, field.getText(), field.getCursorPosition())));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft game, int mouseX, int mouseY) {
		this.setFocused(field.isEnabled() && field.isVisible() && mouseX >= field.getScreenX() && mouseX < field.getScreenX() + field.getWidth() && mouseY >= field.getScreenY() && mouseY < field.getScreenY() + field.getHeight());
		return isFocused();
	}
	
	public boolean isFocused() {
		return focus;
	}
	
	public void setFocused(boolean focus) {
		if(focus && !this.focus) {
			field.setCursorPosition(field.getText().length());
		}

		this.focus = focus;
	}
	
	@Override
	public void drawButton(Minecraft game, int mouseX, int mouseY) {
		this.drawRect((int) (field.getScreenX() - 1), (int) (field.getScreenY() - 1), (int) (field.getScreenX() + field.getWidth() + 1), (int) (field.getScreenY() + field.getHeight() + 1), field.getBorderColor().toInt());
		this.drawRect((int)field.getScreenX(), (int) field.getScreenY(), (int) (field.getScreenX() + field.getWidth()), (int) (field.getScreenY() + field.getHeight()), field.getFieldColor().toInt());
		if(field.isEnabled()) {
			count++;
			boolean showCursor = this.focus && count % 40 < 15;
			String text = field.getText();
			if (field.getCursorPosition() < 0) field.setCursorPosition(0);
			if (field.getCursorPosition() > text.length()) field.setCursorPosition(text.length());
			if (showCursor) {
				text = "";
				if (field.getCursorPosition() > 0) {
					text += field.getText().substring(0, field.getCursorPosition());
				}
				text += "_";
				if (field.getCursorPosition() < field.getText().length()) {
					text += field.getText().substring(field.getCursorPosition() + 1);
				}
			}
			this.drawString(game.fontRenderer, text, (int) (field.getScreenX() + 4), (int) (field.getScreenY() + (field.getHeight() - 8) / 2), field.getColor().toInt());
		} else {
			this.drawString(game.fontRenderer, field.getText(), (int) (field.getScreenX() + 4), (int) (field.getScreenY() + (field.getHeight() - 8) / 2), field.getDisabledColor().toInt());
		}

	}

	public TextField getWidget() {
		return field;
	}
	
	public UUID getId() {
		return field.getId();
	}
	
	public boolean isEqual(Widget widget) {
		return widget.getId().equals(field.getId());
	}
	
	public void updateWidget(TextField widget) {
		this.field = widget;
		if (field.getCursorPosition() < 0) field.setCursorPosition(0);
		if (field.getCursorPosition() > field.getText().length()) field.setCursorPosition(field.getText().length());
	}
}