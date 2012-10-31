/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui.chat;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;

import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.SafeButton;

public class GuiURLConfirm extends GuiScreen {
	private Button doneButton = null, doneAndNeverAskButton = null, cancelButton = null, copyLinkButton = null;
	GuiChat parent;
	String url;
	URI uri;
	public GuiURLConfirm(GuiChat parent, String url, URI uri) {
		this.parent = parent;
		this.url = url;
		this.uri = uri;
	}

	@Override
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		GenericLabel label = new GenericLabel("Confirm Unknown URL");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText()) * 2;
		label.setScale(2.0F);
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);

		label = new GenericLabel("Are you sure you want to visit this url?");
		label.setTextColor(new Color(0xFF0000));
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(50);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);

		label = new GenericLabel(url);
		label.setTextColor(new Color(0x0099CC));
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(70);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);

		int left = (int)(width / 2  - 175);
		int right = (int)(width / 2 + 10);

		doneButton = new GenericButton("Yes");
		doneButton.setAlign(WidgetAnchor.TOP_CENTER);
		doneButton.setX(right).setY(height - 50);
		doneButton.setHeight(20).setWidth(170);
		getScreen().attachWidget(spoutcraft, doneButton);

		doneAndNeverAskButton = new NeverAskAgainButton("Yes and never ask again!");
		doneAndNeverAskButton.setAlign(WidgetAnchor.TOP_CENTER);
		doneAndNeverAskButton.setX(right).setY(height - 72);
		doneAndNeverAskButton.setHeight(20).setWidth(170);
		getScreen().attachWidget(spoutcraft, doneAndNeverAskButton);

		cancelButton = new GenericButton("No");
		cancelButton.setAlign(WidgetAnchor.TOP_CENTER);
		cancelButton.setX(left).setY(height - 50);
		cancelButton.setHeight(20).setWidth(170);
		getScreen().attachWidget(spoutcraft, cancelButton);

		copyLinkButton = new GenericButton("Copy to Clipboard");
		copyLinkButton.setAlign(WidgetAnchor.TOP_CENTER);
		copyLinkButton.setX(left).setY(height - 72);
		copyLinkButton.setHeight(20).setWidth(170);
		getScreen().attachWidget(spoutcraft, copyLinkButton);
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		super.drawScreen(x, y, z);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(doneAndNeverAskButton)) {
			Configuration.setAskBeforeOpeningUrl(false);
		}
		if (btn.equals(doneButton) || btn.equals(doneAndNeverAskButton)) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mc.displayGuiScreen(parent);
		}
		if (btn.equals(copyLinkButton)) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
			mc.displayGuiScreen(parent);
		}
		if (btn.equals(cancelButton)) {
			mc.displayGuiScreen(parent);
		}
	}
}

class NeverAskAgainButton extends SafeButton {
	NeverAskAgainButton(String label) {
		this.setText(label);
	}
	@Override
	protected void executeAction() {
	}
}
