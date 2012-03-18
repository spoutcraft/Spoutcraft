/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.addon;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;

public class GuiAddonConfigurationWrapper extends GuiSpoutScreen {
	private GuiAddonsLocal parent = null;
	private GenericScrollArea contents = new GenericScrollArea();
	private Addon addon;
	private GenericLabel title;
	private GenericButton buttonDone;
	
	public GuiAddonConfigurationWrapper(Addon addon, GuiAddonsLocal parent) {
		this.parent = parent;
		this.addon = addon;
	}
	
	public GenericScrollArea getContentsView() {
		return contents;
	}

	@Override
	protected void createInstances() {
		buttonDone = new GenericButton("Done");
		title = new GenericLabel(addon.getDescription().getName() + " Configuration");
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, buttonDone, title, contents);
	}

	@Override
	protected void layoutWidgets() {
		int top = 5;

		int swidth = mc.fontRenderer.getStringWidth(title.getText());
		title.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);

		top+=25;
		
		contents.setX(5).setY(top).setWidth(width - 10).setHeight(height - top - 30);
		contents.removeWidgets(addon);
		
		boolean oldLock = SpoutClient.enableSandbox();
		try {
			addon.setupConfigurationGUI(contents);
		} finally {
			SpoutClient.enableSandbox(oldLock);
		}
		
		contents.updateInnerSize();
		
		top+= contents.getHeight() + 5;
		
		buttonDone.setX(width - 10 - 200).setY(top).setHeight(20).setWidth(200);
	}
	
	@Override 
	public void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}

}
