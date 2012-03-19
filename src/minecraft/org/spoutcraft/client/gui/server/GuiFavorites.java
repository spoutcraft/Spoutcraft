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
package org.spoutcraft.client.gui.server;

import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.TextField;

import org.spoutcraft.client.SpoutClient;

public class GuiFavorites extends GuiScreen {
	@SuppressWarnings("unused")
	private GuiScreen parent;
	//GUI stuff
	private Button buttonJoin, buttonAdd, buttonDelete, buttonEdit,
			buttonMainMenu, buttonServerList, buttonQuickJoin,
			buttonMoveUp, buttonMoveDown, buttonRefresh;
	private TextField textQuickJoin;
	private GenericListView view;
	private Label title;
	public FavoritesModel model = SpoutClient.getInstance().getServerManager().getFavorites();

	public GuiFavorites(GuiScreen parent) {
		this.parent = parent;
		model.setCurrentGUI(this);
	}

	@Override
	public void initGui() {
		Addon spoutcraft = SpoutClient.getInstance().getAddonManager().getAddon("spoutcraft");

		title = new GenericLabel("Favorite Servers");
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(12);
		title.setHeight(15).setWidth(SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		getScreen().attachWidget(spoutcraft, title);

		buttonMoveUp = new GenericButton("/\\");
		buttonMoveUp.setTooltip("Move Item Up");
		buttonMoveUp.setX(5).setY(5);
		buttonMoveUp.setHeight(20).setWidth(20);
		getScreen().attachWidget(spoutcraft, buttonMoveUp);

		buttonMoveDown = new GenericButton("\\/");
		buttonMoveDown.setTooltip("Move Item Down");
		buttonMoveDown.setX(25).setY(5);
		buttonMoveDown.setHeight(20).setWidth(20);
		getScreen().attachWidget(spoutcraft, buttonMoveDown);

		buttonRefresh = new GenericButton("Refresh");
		buttonRefresh.setHeight(20).setWidth(100).setX(width - 105).setY(5);
		getScreen().attachWidget(spoutcraft, buttonRefresh);

		view = new GenericListView(model);
		view.setX(5).setY(30).setWidth(width - 10).setHeight(height - 110);
		getScreen().attachWidget(spoutcraft, view);

		int top = (int) (view.getY() + view.getHeight() + 5);

		int totalWidth = Math.min(width - 9, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;

		String text = SpoutClient.getHandle().gameSettings.lastServer.replace("_", ":");
		if (textQuickJoin != null) {
			text = textQuickJoin.getText();
		}
		textQuickJoin = new QuickJoin();
		textQuickJoin.setX(left + 2).setY(top + 2).setHeight(16).setWidth(cellWidth * 2 + 5 - 4);
		textQuickJoin.setMaximumCharacters(0);
		textQuickJoin.setText(text);
		getScreen().attachWidget(spoutcraft, textQuickJoin);

		buttonQuickJoin = new GenericButton("Quick Join");
		buttonQuickJoin.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonQuickJoin);

		top += 25;

		buttonJoin = new GenericButton("Join Server");
		buttonJoin.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonJoin);

		buttonAdd = new GenericButton("Add Favorite");
		buttonAdd.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonAdd);

		buttonEdit = new GenericButton("Edit");
		buttonEdit.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonEdit);

		top += 25;

		buttonDelete = new DeleteFavoriteButton(this);
		buttonDelete.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDelete);

		buttonServerList = new GenericButton("Server List");
		buttonServerList.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonServerList);

		buttonMainMenu = new GenericButton("Main Menu");
		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);

		updateButtons();
	}

	private class QuickJoin extends GenericTextField {
		@Override
		public boolean onKeyPressed(Keyboard key) {
			if (key == Keyboard.KEY_RETURN && buttonQuickJoin.isEnabled()) {
				doQuickJoin();
				return true;
			} else {
				buttonQuickJoin.setEnabled(textQuickJoin.getText().length() > 0);
			}
			return false;
		}
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
		//TODO: Draw the refresh button thingie
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn.equals(buttonMainMenu)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiMainMenu());
		}
		if (btn.equals(buttonServerList)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiServerList());
		}
		if (btn.equals(buttonQuickJoin)) {
			doQuickJoin();
		}
		if (btn.equals(buttonAdd)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiAddFavorite(textQuickJoin.getText(), this));
		}
		if (btn.equals(buttonEdit)) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			//Produces a "hang" why ever :(
			if (item != null) {
				SpoutClient.getHandle().displayGuiScreen(new GuiAddFavorite(item, this));
			} else {
				updateButtons();
			}
		}
		if (btn.equals(buttonJoin)) {
			ServerItem item = null;
			if (view.getSelectedRow() > -1) {
				item = (ServerItem) model.getItem(view.getSelectedRow());
			}
			if (item != null) {
				SpoutClient.getInstance().getServerManager().join(item, this, "Favorites");
			} else {
				//Just in case something weird happens
				updateButtons();
			}
		}
		if (btn.equals(buttonMoveUp)) {
			if (view.getSelectedRow() != -1) {
				model.move(view.getSelectedRow(), view.getSelectedRow() - 1);
				view.shiftSelection(-1);
				model.save();
			}
		}
		if (btn.equals(buttonMoveDown)) {
			if (view.getSelectedRow() != -1) {
				model.move(view.getSelectedRow(), view.getSelectedRow() + 1);
				view.shiftSelection(1);
				model.save();
			}
		}
		if (btn.equals(buttonRefresh)) {
			for (int i = 0; i < model.getSize(); i++) {
				ServerItem item = (ServerItem) model.getItem(i);
				item.poll();
			}
		}
	}

	public void deleteCurrentFavorite() {
		model.removeServer((ServerItem) view.getSelectedItem());
		model.save();
	}

	public void doQuickJoin() {
		try {
			String adress = textQuickJoin.getText();
			if (!adress.isEmpty()) {
				String split[] = adress.split(":");
				String ip = split[0];
				int port = split.length > 1 ? Integer.parseInt(split[1]) : ServerItem.DEFAULT_PORT;
				SpoutClient.getHandle().gameSettings.lastServer = adress.replace(":", "_");
				SpoutClient.getHandle().gameSettings.saveOptions();
				SpoutClient.getInstance().getServerManager().join(ip, port, this, "Favorites");
			}
		} catch (Exception e) {
		}
	}

	public void updateButtons() {
		boolean enable = true;
		if (view != null && view.getSelectedRow() == -1) {
			enable = false;
		}
		buttonEdit.setEnabled(enable);
		buttonDelete.setEnabled(enable);
		buttonJoin.setEnabled(enable);
		buttonDelete.setText("Delete");

		if (model.isPolling()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Polling...");
			buttonRefresh.setDisabledColor(new Color(0f, 0f, 1f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
		}
	}

	@Override
	public void updateScreen() {
		if (model.isPolling()) {
			Color color = new Color(0, 0f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setBlue(1f - (float) darkness);
			buttonRefresh.setDisabledColor(color);
		}
		buttonQuickJoin.setEnabled(textQuickJoin.getText().length() > 0);
		super.updateScreen();
	}
}
