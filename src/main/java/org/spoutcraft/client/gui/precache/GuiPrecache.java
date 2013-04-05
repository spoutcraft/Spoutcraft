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
package org.spoutcraft.client.gui.precache;

import net.minecraft.src.GuiScreen;
import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.WidgetAnchor;

public class GuiPrecache extends GuiScreen {
	public GenericLabel statusText;

	@Override
	public void initGui() {
		statusText = new GenericLabel();
		statusText.setAnchor(WidgetAnchor.CENTER_CENTER);
		statusText.setAlign(WidgetAnchor.CENTER_CENTER);
		statusText.setText(ChatColor.BLUE + "Spoutcraft" + "\n" + " " + "\n" + ChatColor.WHITE + "Loading terrain and custom resources" + "\n" + "\n" + ChatColor.MAGIC + "ShowMagic");
		getScreen().attachWidgets("Spoutcraft", statusText);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawBackground(0);
		super.drawScreen(par1, par2, par3);
	}
}
