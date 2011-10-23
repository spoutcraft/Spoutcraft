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

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.*;

public class GuiFilters extends GuiScreen {

	private GuiScreen parentScreen;
	private GuiTextField field_22114_h;


	public GuiFilters(GuiMultiplayer var1) {
		this.doFramerateLimit = true;
		this.parentScreen = var1;
	}

	public void updateScreen() {
		this.field_22114_h.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectWorld.renameButton")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				SpoutClient.getHandle().displayGuiScreen(this.parentScreen);
			} else if(var1.id == 0) {
				SpoutClient.getHandle().getSaveLoader();
				SpoutClient.getHandle().displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void keyTyped(char var1, int var2) {
		this.field_22114_h.textboxKeyTyped(var1, var2);
		((GuiButton)this.controlList.get(0)).enabled = this.field_22114_h.getText().trim().length() > 0;
		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

	}

	public void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		this.field_22114_h.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(SpoutClient.getHandle().fontRenderer, var4.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(SpoutClient.getHandle().fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
		this.field_22114_h.drawTextBox();
		super.drawScreen(var1, var2, var3);
	}
}
