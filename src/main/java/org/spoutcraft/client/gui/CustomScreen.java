/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import org.spoutcraft.api.gui.*;
import org.spoutcraft.client.SpoutClient;

public class CustomScreen extends GuiScreen {
	public CustomScreen(PopupScreen screen) {
		update(screen);
		this.setWorldAndResolution(SpoutClient.getHandle(), (int) screen.getWidth(), (int) screen.getHeight());
		this.allowUserInput = true;
	}

	@Override
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
		this.initGui();
	}

	public void drawScreen(int x, int y, float z) {
		if (screen instanceof PopupScreen) {
			if (!((PopupScreen)screen).isTransparent()) {
				this.drawDefaultBackground();
			}
		}
		bg.setVisible(screen.isBgVisible());
		drawWidgets(x, y, z);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return ((PopupScreen) getScreen()).isPausingGame();
	}
}
