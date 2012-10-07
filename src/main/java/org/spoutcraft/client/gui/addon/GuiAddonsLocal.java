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
package org.spoutcraft.client.gui.addon;

import net.minecraft.src.StringTranslate;

import org.lwjgl.Sys;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.addon.LocalAddonsModel.AddonItem;

public class GuiAddonsLocal extends GuiSpoutScreen {
	private GenericLabel labelTitle;
	private GenericListView addonsView;
	private GenericScrollArea addonOptions;
	private GenericCheckBox checkPluginEnabled, checkInternetAccess;
	private GenericButton buttonMainMenu, buttonDatabase, buttonOpenFolder, buttonOpenConfiguration;

	private LocalAddonsModel model = new LocalAddonsModel(this);

	@Override
	protected void createInstances() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		StringTranslate t = StringTranslate.getInstance();

		labelTitle = new GenericLabel(t.translateKey("spout.addon.title", "Installed Addons"));
		addonsView = new GenericListView(model);
		addonOptions = new GenericScrollArea();
		checkPluginEnabled = new GenericCheckBox(t.translateKey("spout.addon.active", "Active"));
		checkInternetAccess = new GenericCheckBox(t.translateKey("spout.addon.internet", "Internet Access"));
		buttonMainMenu = new GenericButton(t.translateKey("gui.done", "Main Menu"));
		buttonDatabase = new GenericButton(t.translateKey("spout.addon.database", "Database"));
		buttonDatabase.setTooltip(t.translateKey("spout.addon.tip.database", "Feature discontinued"));
		buttonDatabase.setEnabled(false);
		buttonOpenFolder = new GenericButton(t.translateKey("spout.addon.open", "Open Addons Folder"));
		buttonOpenFolder.setTooltip(t.translateKey("spout.addon.tip.open", "Place your addons here manually"));
		buttonOpenConfiguration = new GenericButton(t.translateKey("spout.addon.config", "Configuration"));
		buttonOpenConfiguration.setTooltip(t.translateKey("spout.addon.tip.config", "Open addon configuration"));

		getScreen().attachWidget(spoutcraft, addonsView);
		getScreen().attachWidget(spoutcraft, addonOptions);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);
		getScreen().attachWidget(spoutcraft, buttonDatabase);
		getScreen().attachWidget(spoutcraft, labelTitle);
		getScreen().attachWidget(spoutcraft, buttonOpenFolder);
		addonOptions.attachWidget(spoutcraft, checkPluginEnabled);
		addonOptions.attachWidget(spoutcraft, buttonOpenConfiguration);
		//addonOptions.attachWidget(spoutcraft, checkInternetAccess);

		updateButtons();
	}

	@Override
	protected void layoutWidgets() {
		int top = 5;

		int swidth = mc.fontRenderer.getStringWidth(labelTitle.getText());
		labelTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);

		top+=25;

		int sheight = height - top - 30;

		addonOptions.setX(width - 135).setY(top).setWidth(130).setHeight(sheight);

		addonsView.setX(5).setY(top).setWidth((int) (width - 15 - addonOptions.getWidth())).setHeight(sheight);

		int ftop = 5;
		checkPluginEnabled.setX(5).setY(ftop).setHeight(20).setWidth(100);
		ftop+=25;
		buttonOpenConfiguration.setX(5).setY(ftop).setHeight(20).setWidth(100);
		ftop+=25;
		checkInternetAccess.setX(5).setY(ftop).setHeight(20).setWidth(100);

		for (Widget w:addonOptions.getAttachedWidgets()) {
			w.setWidth(addonOptions.getViewportSize(Orientation.HORIZONTAL) - 10);
		}

		top += 5 + addonsView.getHeight();

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonOpenFolder.setX(left).setY(top).setWidth(cellWidth).setHeight(20);

		buttonDatabase.setX(center).setY(top).setWidth(cellWidth).setHeight(20);

		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);

	}

	public void updateButtons() {
		boolean enable = addonsView.getSelectedItem() != null;
		checkPluginEnabled.setEnabled(enable);
		checkInternetAccess.setEnabled(enable);
		buttonOpenConfiguration.setEnabled(enable);
		AddonItem item = (AddonItem) addonsView.getSelectedItem();
		if (item != null) {
			checkPluginEnabled.setChecked(Spoutcraft.getAddonStore().isEnabled(item.getAddon()));
			checkInternetAccess.setChecked(Spoutcraft.getAddonStore().hasInternetAccess(item.getAddon()));
			boolean oldLock = SpoutClient.enableSandbox();
			try {
				buttonOpenConfiguration.setEnabled(item.getAddon().hasConfigurationGUI());
			} finally {
				SpoutClient.enableSandbox(oldLock);
			}
		}

	}

	@Override
	protected void buttonClicked(Button btn) {
		// TODO Parent screen
		if (btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonDatabase)) {
			// TODO Database screen
		}
		if (btn.equals(checkPluginEnabled)) {
			AddonItem item = (AddonItem) addonsView.getSelectedItem();
			if (item != null) {
				SpoutClient.getInstance().getAddonStore().getAddonInfo(item.getAddon()).setEnabled(checkPluginEnabled.isChecked());
			}
		}
		if (btn.equals(checkInternetAccess)) {
			AddonItem item = (AddonItem) addonsView.getSelectedItem();
			if (item != null) {
				SpoutClient.getInstance().getAddonStore().getAddonInfo(item.getAddon()).setHasInternetAccess(checkInternetAccess.isChecked());
			}
		}
		if (btn.equals(buttonOpenFolder)) {
			Sys.openURL("file://" + SpoutClient.getInstance().getAddonFolder().getAbsolutePath());
		}
		if (btn.equals(buttonOpenConfiguration)) {
			AddonItem item = (AddonItem) addonsView.getSelectedItem();
			GuiAddonConfigurationWrapper wrapper = new GuiAddonConfigurationWrapper(item.getAddon(), this);
			if (item != null) {
				item.getAddon().setupConfigurationGUI(wrapper.getContentsView());
				mc.displayGuiScreen(wrapper);
			}
		}
	}
}
