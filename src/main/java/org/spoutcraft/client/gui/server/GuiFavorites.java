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
package org.spoutcraft.client.gui.server;

import net.minecraft.src.GuiScreen;

import org.bukkit.ChatColor;

import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericComboBox;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.Keyboard;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.LinkButton;

public class GuiFavorites extends GuiScreen {
	private GuiScreen parent;

	// GUI stuff
	private Button buttonJoin, buttonAdd, buttonDelete, buttonEdit, buttonMainMenu, buttonServerList, buttonQuickJoin, buttonMoveUp, buttonMoveDown, buttonRefresh, buttonGetISeeYou;
	private GenericLabel labelDescriptionISeeYou, labelLookingForServers;
	private TextField textQuickJoin;
	private GenericListView view;
	private GenericComboBox comboSource;
	public ServerModel model = SpoutClient.getInstance().getServerManager()
			.getFavorites();
	private long pollTime = 0L;

	public GuiFavorites(GuiScreen parent) {
		this.parent = parent;
		model.setCurrentGUI(this);
	}

	@Override
	public void initGui() {
		Addon spoutcraft = SpoutClient.getInstance().getAddonManager().getAddon("spoutcraft");

		int selection = 0;
		if (comboSource != null) {
			selection = comboSource.getSelectedRow();
		}

		comboSource = new SwitchViewSourceCombo(this);
		getScreen().attachWidget(spoutcraft, comboSource);
		comboSource.setGeometry(width / 2 - 75, 5, 150, 20);
		comboSource.setSelection(selection);

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

		boolean helpVisible = (buttonGetISeeYou != null) ? buttonGetISeeYou.isVisible() : false;

		int viewheight = height - 110;
		view = new GenericListView(model);
		view.setX(5).setY(30).setWidth(width - 10).setHeight(viewheight);
		getScreen().attachWidget(spoutcraft, view);

		buttonGetISeeYou = new LinkButton("Get " + ChatColor.BOLD + "I See You" + ChatColor.RESET + " now", "http://spout.in/iseeyou");
		labelLookingForServers = new GenericLabel("Looking for servers in your local network ...");
		labelLookingForServers.setX(15).setVisible(helpVisible);
		labelLookingForServers.setPriority(RenderPriority.Low);
		getScreen().attachWidget(spoutcraft, labelLookingForServers);

		labelDescriptionISeeYou = new GenericLabel(
						"If your server should be displayed here, "
						+ "assure that it is turned on, the network address is "
						+ "available from your computer, and that you have the "
						+ "I See You plugin installed on your Bukkit server.\n"
						+ "The new Spout Server will support this feature soon.");
		labelDescriptionISeeYou.setWidth(width - 30).setPriority(RenderPriority.Low);;
		labelDescriptionISeeYou.setWrapLines(true);
		// labelDescriptionISeeYou.setAlign(WidgetAnchor.TOP_CENTER); // This align code sucks
		labelDescriptionISeeYou.recalculateLines();

		int lh = (int) labelDescriptionISeeYou.getHeight();
		labelDescriptionISeeYou.setY((int) (30 + 16 + (viewheight / 2 - (lh + 25 + 16) / 2)));
		labelDescriptionISeeYou.setX(15);
		labelDescriptionISeeYou.setVisible(helpVisible);
		getScreen().attachWidget(spoutcraft, labelDescriptionISeeYou);

		labelLookingForServers.setY(labelDescriptionISeeYou.getY() - 16);

		buttonGetISeeYou.setX(width / 2 - 150 / 2).setPriority(RenderPriority.Low);
		buttonGetISeeYou.setY(labelDescriptionISeeYou.getY() + lh + 5);
		buttonGetISeeYou.setHeight(20).setWidth(150);
		buttonGetISeeYou.setVisible(helpVisible);
		getScreen().attachWidget(spoutcraft, buttonGetISeeYou);

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
		buttonServerList.setX(center).setY(top).setWidth(cellWidth)
				.setHeight(20);
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
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn.equals(buttonMainMenu)) {
			SpoutClient.getHandle().displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonServerList)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiServerList());
		}
		if (btn.equals(buttonQuickJoin)) {
			doQuickJoin();
		}
		if (btn.equals(buttonAdd)) {
			String address = "";
			if (model instanceof FavoritesModel) {
				address = textQuickJoin.getText();
			} else if (model instanceof LANModel) {
				ServerItem item = (ServerItem) view.getSelectedItem();
				address = item.ip + ":" + item.port;
			}
			SpoutClient.getHandle().displayGuiScreen(new GuiAddFavorite(address, this));
		}
		if (btn.equals(buttonEdit)) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			// Produces a "hang" why ever :(
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
				// Just in case something weird happens
				updateButtons();
			}
		}
		FavoritesModel fav = null;
		if (model instanceof FavoritesModel) {
			fav = (FavoritesModel) model;
		}
		if (btn.equals(buttonMoveUp) && fav != null) {
			if (view.getSelectedRow() != -1) {
				fav.move(view.getSelectedRow(), view.getSelectedRow() - 1);
				view.shiftSelection(-1);
				fav.save();
			}
		}
		if (btn.equals(buttonMoveDown) && fav != null) {
			if (view.getSelectedRow() != -1) {
				fav.move(view.getSelectedRow(), view.getSelectedRow() + 1);
				view.shiftSelection(1);
				fav.save();
			}
		}
		if (btn.equals(buttonRefresh)) {
			refresh();
		}
	}

	public void refresh() {
		pollTime = System.currentTimeMillis();
		for (int i = 0; i < model.getSize(); i++) {
			ServerItem item = (ServerItem) model.getItem(i);
			item.poll();
		}
	}

	public void deleteCurrentFavorite() {
		if (model instanceof FavoritesModel) {
			FavoritesModel fav = (FavoritesModel) model;
			fav.removeServer((ServerItem) view.getSelectedItem());
			fav.save();
		}
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
		// GUI has not been initialized
		if (buttonEdit == null) {
			return;
		}
		buttonEdit.setEnabled(enable);
		buttonDelete.setEnabled(enable);
		buttonJoin.setEnabled(enable);
		buttonDelete.setText("Delete");
		buttonMoveDown.setEnabled(enable);
		buttonMoveUp.setEnabled(enable);
		buttonAdd.setEnabled(true);
		if (model instanceof LANModel) {
			buttonEdit.setEnabled(false);
			buttonDelete.setEnabled(false);
			buttonMoveDown.setEnabled(false);
			buttonMoveUp.setEnabled(false);
			if (view.getSelectedItem() == null) {
				buttonAdd.setEnabled(false);
			} else {
				ServerItem item = (ServerItem) view.getSelectedItem();
				if (SpoutClient.getInstance().getServerManager().getFavorites().containsSever(item)) {
					buttonAdd.setEnabled(false);
				}
			}
		}
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
		if (model.isPolling() || buttonGetISeeYou.isVisible()) {
			Color color = new Color(0, 0f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setBlue(1f - (float) darkness);
			if (model.isPolling()) {
				buttonRefresh.setDisabledColor(color);
			}
			if (buttonGetISeeYou.isVisible()) {
				labelLookingForServers.setTextColor(color);
			}

			// If polling locks up and takes > 15s, unlock the button
			if (pollTime + 15000L < System.currentTimeMillis()) {
				for (int i = 0; i < model.getSize(); i++) {
					ServerItem item = (ServerItem) model.getItem(i);
					if (item.isPolling()) {
						item.endPolling();
					}
				}
				model.setPolling(false);
			}
		}

		buttonQuickJoin.setEnabled(textQuickJoin.getText().length() > 0);

		if (buttonGetISeeYou.isVisible()) {
			LANModel lan = (LANModel) model;
			if (lan.getSize() > 0) {
				setISeeYouHelpVisible(false);
			}
		}
		super.updateScreen();
	}

	public void setModel(ServerModel model) {
		this.model = model;
		view.setSelection(-1);
		view.setModel(model);
		model.setCurrentGUI(this);

		if (model instanceof LANModel) {
			LANModel lan = (LANModel) model;
			setISeeYouHelpVisible(lan.getSize() == 0);
		} else {
			setISeeYouHelpVisible(false);
		}
	}

	public void setISeeYouHelpVisible(boolean visible) {
		//view.setVisible(!visible);
		buttonGetISeeYou.setVisible(visible);
		labelDescriptionISeeYou.setVisible(visible);
		labelLookingForServers.setVisible(visible);
	}
}
