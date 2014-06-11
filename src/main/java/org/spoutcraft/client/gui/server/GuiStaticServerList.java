/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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

import org.lwjgl.Sys;

import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.Keyboard;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.client.SpoutClient;

public class GuiStaticServerList extends GuiScreen {
	private GuiScreen parent;

	// GUI stuff
	private Button buttonJoin, buttonAdd, buttonDelete, buttonEdit, buttonMainMenu, buttonFavoritesList, buttonClear, buttonQuickJoin, buttonMoveUp, buttonMoveDown, buttonRefresh,buttonAddServer, buttonAddtoFavorites;
	private GenericLabel labelTitle;
	private TextField textQuickJoin;
	private GenericListView view;	
	public ServerModel model = SpoutClient.getInstance().getServerManager().getStaticServerList();	
	private long pollTime = 0L;

	public GuiStaticServerList(GuiScreen parent) {
		this.parent = parent;
		model.setStaticServerGUI(this);
	}

	@Override
	public void initGui() {
		labelTitle = new GenericLabel("Public Servers");
		labelTitle.setY(12).setX(width / 2 - mc.fontRenderer.getStringWidth(labelTitle.getText()) / 2);
		labelTitle.setHeight(15).setWidth(mc.fontRenderer.getStringWidth(labelTitle.getText()) / 2);
		getScreen().attachWidget("Spoutcraft", labelTitle);

		buttonMoveUp = new GenericButton("/\\");
		buttonMoveUp.setTooltip("Move Item Up");
		buttonMoveUp.setX(5).setY(5);
		buttonMoveUp.setHeight(20).setWidth(20);
		getScreen().attachWidget("Spoutcraft", buttonMoveUp);

		buttonMoveDown = new GenericButton("\\/");
		buttonMoveDown.setTooltip("Move Item Down");
		buttonMoveDown.setX(25).setY(5);
		buttonMoveDown.setHeight(20).setWidth(20);
		getScreen().attachWidget("Spoutcraft", buttonMoveDown);

		buttonRefresh = new GenericButton("Refresh");
		buttonRefresh.setHeight(20).setWidth(100).setX(width - 105).setY(5);
		getScreen().attachWidget("Spoutcraft", buttonRefresh);

		int viewheight = height - 110;
		view = new GenericListView(model);
		view.setX(5).setY(30).setWidth(width - 10).setHeight(viewheight);
		getScreen().attachWidget("Spoutcraft", view);

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
		textQuickJoin.setX(left + 2).setY(top + 2).setHeight(16).setWidth((cellWidth * 2 + 5 - 4) - (cellWidth/2));
		textQuickJoin.setMaximumCharacters(0);
		textQuickJoin.setText(text);
		getScreen().attachWidget("Spoutcraft", textQuickJoin);

		buttonClear = new GenericButton("Clear");
		buttonClear.setX(left + 10 + (cellWidth*2) - (cellWidth/2)).setY(top).setWidth((cellWidth/2) - 5).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonClear);
		
		buttonQuickJoin = new GenericButton("Quick Join");
		buttonQuickJoin.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonQuickJoin);

		top += 25;

		buttonJoin = new GenericButton("Join Server");
		buttonJoin.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonJoin);

		buttonAddtoFavorites = new GenericButton("Add this to Favorites");
		buttonAddtoFavorites.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonAddtoFavorites);

		top += 25;
		buttonAddServer = new GenericButton("Add Your Server");
		buttonAddServer.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonAddServer);
		
		buttonFavoritesList = new GenericButton("Favorites");
		buttonFavoritesList.setX(center).setY(top).setWidth(cellWidth)
				.setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonFavoritesList);

		buttonMainMenu = new GenericButton("Main Menu");
		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonMainMenu);
		refresh();
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
		if (btn.equals(buttonAddServer)) {
			Sys.openURL("http://spoutcraft.org/resources/categories/servers.7/");
		}		
		if (btn.equals(buttonFavoritesList)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiFavorites(this));
		}

		if (btn.equals(buttonClear)) {
			  textQuickJoin.setText("");
		}

		if (btn.equals(buttonQuickJoin)) {
			doQuickJoin();
		}
		
		if (btn.equals(buttonAddtoFavorites)) {
			ServerItem item = null;
			if (view.getSelectedRow() > -1) {
				item = (ServerItem) model.getItem(view.getSelectedRow());
			}
			if (item != null) {
				SpoutClient.getInstance().getServerManager().getFavorites().addServer(item);
				SpoutClient.getInstance().getServerManager().getFavorites().save();
				SpoutClient.getHandle().displayGuiScreen(new GuiFavorites(this));
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
		StaticServerModel sslist = null;
		if (model instanceof StaticServerModel) {
			sslist = (StaticServerModel) model;
		}
		if (btn.equals(buttonMoveUp) && sslist != null) {
			if (view.getSelectedRow() != -1) {
				sslist.move(view.getSelectedRow(), view.getSelectedRow() - 1);
				view.shiftSelection(-1);
				sslist.save();
			}
		}
		if (btn.equals(buttonMoveDown) && sslist != null) {
			if (view.getSelectedRow() != -1) {
				sslist.move(view.getSelectedRow(), view.getSelectedRow() + 1);
				view.shiftSelection(1);
				sslist.save();
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
		if (buttonJoin == null) {
			return;
		}
		
		buttonJoin.setEnabled(enable);
		buttonMoveDown.setEnabled(enable);
		buttonMoveUp.setEnabled(enable);
		buttonAddtoFavorites.setEnabled(enable);

		if (model.isPolling()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Polling...");
			buttonRefresh.setDisabledColor(new Color(0f, 0f, 1f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
		}
		
		if (view.getSelectedItem() instanceof ServerItem) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			buttonJoin.setEnabled(item.isCompatible(SpoutClient.spoutcraftVersion));
			buttonAddtoFavorites.setEnabled(true);
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
			if (model.isPolling()) {
				buttonRefresh.setDisabledColor(color);
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
		buttonClear.setEnabled(textQuickJoin.getText().length() > 0);
		buttonQuickJoin.setEnabled(textQuickJoin.getText().length() > 0);
		super.updateScreen();
	}
}
