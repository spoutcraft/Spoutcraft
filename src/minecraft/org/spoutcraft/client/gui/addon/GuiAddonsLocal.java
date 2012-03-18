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

import org.lwjgl.Sys;

import net.minecraft.src.GuiMainMenu;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.addon.LocalAddonsModel.AddonItem;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.Widget;

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

		labelTitle = new GenericLabel("Installed Addons");
		addonsView = new GenericListView(model);
		addonOptions = new GenericScrollArea();
		checkPluginEnabled = new GenericCheckBox("Active");
		checkInternetAccess = new GenericCheckBox("Internet Access");
		buttonMainMenu = new GenericButton("Main Menu");
		buttonDatabase = new GenericButton("Database");
		buttonDatabase.setTooltip("Coming soon!");
		buttonDatabase.setEnabled(false);
		buttonOpenFolder = new GenericButton("Open Addons Folder");
		buttonOpenFolder.setTooltip("Place your addons here manually");
		buttonOpenConfiguration = new GenericButton("Configuration");
		buttonOpenConfiguration.setTooltip("Open Addon-specific configuration");

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

		int totalWidth = Math.min(width - 10, 200*3+10);
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
		//TODO parent screen
		if (btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
		if (btn.equals(buttonDatabase)) {
			//TODO database screen
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
			Sys.openURL("file://"+SpoutClient.getInstance().getAddonFolder().getAbsolutePath());
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
