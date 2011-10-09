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
package org.getspout.spout.gui.server;

import net.minecraft.src.*;

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.input.Keyboard;

public class GuiAddFav extends GuiScreen {

	private GuiScreen screen;
	private GuiTextField nameField;
	private GuiTextField Ipfield;
	private final String name;
	private final String ip;
	private boolean rename;
	public int uid = 0;


	public GuiAddFav(GuiScreen screen, String name, String ip, int uid) {
		this.screen = screen;
		this.name = name;
		this.ip = ip;
		this.uid = uid;
		this.rename = false;
	}
	
	public GuiAddFav(GuiScreen screen, String name, String ip, int uid, boolean rename) {
		this.screen = screen;
		this.name = name;
		this.ip = ip;
		this.uid = uid;
		this.rename = rename;
	}

	public void updateScreen() {
		nameField.updateCursorCounter();
		Ipfield.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey(rename ? "Edit" : "Add")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
		this.nameField = new GuiTextField(this, SpoutClient.getHandle().fontRenderer, this.width / 2 - 100, 120, 200, 20, this.name);
		//this.nameField.isEnabled = uid == 0;
		this.nameField.isFocused = false;
		this.nameField.setMaxStringLength(35);
		this.Ipfield = new GuiTextField(this, SpoutClient.getHandle().fontRenderer, this.width / 2 - 100, 60, 200, 20, this.ip);
		this.Ipfield.isEnabled = uid == 0;
		this.Ipfield.isFocused = true;
		this.Ipfield.setMaxStringLength(35);
		((GuiButton)this.controlList.get(0)).enabled = this.nameField.getText().trim().length() > 0 && this.Ipfield.getText().trim().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 1) {
				if (rename) {
					GuiFavorites.writeFav(name, ip, uid);
				}
				this.mc.displayGuiScreen(this.screen);
			}
			else if(button.id == 0) {
				GuiFavorites.writeFav(this.nameField.getText(), this.Ipfield.getText(), uid);
				this.mc.displayGuiScreen(this.screen);
			}

		}
	}

	public void keyTyped(char letter, int key) {
		this.nameField.textboxKeyTyped(letter, key);
		this.Ipfield.textboxKeyTyped(letter, key);
		((GuiButton)this.controlList.get(0)).enabled = this.nameField.getText().trim().length() > 0 && this.Ipfield.getText().trim().length() > 0;
		if (key == Keyboard.KEY_TAB) {
			if (nameField.isFocused) {
				nameField.isFocused = false;
				Ipfield.isFocused = true;
			}
			else if (Ipfield.isFocused) {
				Ipfield.isFocused = false;
				nameField.isFocused = true;
			}
		}
		else if (key == Keyboard.KEY_RETURN && ((GuiButton)this.controlList.get(0)).enabled) {
			actionPerformed(((GuiButton)this.controlList.get(0)));
		}
	}

	public void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		this.nameField.mouseClicked(var1, var2, var3);
		this.Ipfield.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(SpoutClient.getHandle().fontRenderer, (rename ? "Edit" : "Add New") + " Favorite", this.width / 2, 20, 16777215);
		this.drawString(SpoutClient.getHandle().fontRenderer, "Server IP:Port", this.width / 2 - 100, 47, 10526880);
		this.drawString(SpoutClient.getHandle().fontRenderer, "Server Name", this.width / 2 - 100, 107, 10526880);
		this.nameField.drawTextBox();
		this.Ipfield.drawTextBox();
		super.drawScreen(var1, var2, var3);
	}
}
