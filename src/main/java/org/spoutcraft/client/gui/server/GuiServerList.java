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
package org.spoutcraft.client.gui.server;

import org.lwjgl.Sys;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.database.FilterButton;
import org.spoutcraft.client.gui.database.GuiAPIDisplay;
import org.spoutcraft.client.gui.database.RandomButton;
import org.spoutcraft.client.gui.database.SearchField;
import org.spoutcraft.client.gui.database.SortButton;

public class GuiServerList extends GuiAPIDisplay {
	private ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();

	private Label labelTitle, filterTitle;
	private GenericListView view;
	private GenericScrollArea filters;
	private Button buttonJoin, buttonMainMenu, buttonFavorites, buttonRefresh, buttonReset, buttonAddServer, buttonInfo;
	SortButton featured, popular, byName, byFreeSlots, /* byPing, */ byPlayers, trending;
	RandomButton random;
	FilterButton hasPlayers, notFull;
	AccessTypeFilter accessType;
	CountryButton buttonCountry;
	SearchField search;

	boolean instancesCreated = false;

	public GuiServerList() {
		model.setCurrentGui(this);
	}

	public void createInstances() {
		labelTitle = new GenericLabel("Public Server List");
		filters = new GenericScrollArea();
		filterTitle = new GenericLabel("Sort & Filter");
		featured = new SortButton("Featured", "featured", model);
		popular = new SortButton("Popular", "popular", model);
		trending = new SortButton("Trending", "trending", model);
		byName = new SortButton("Name", "sortBy=name", model);
		byFreeSlots = new SortButton("Free Slots", "sortBy=freeslots", false, model);
		byPlayers = new SortButton("Players Online", "sortBy=players", false, model);
		//byPing = new SortButton("Ping", "sortBy=ping", model);
		random = new RandomButton(model);
		hasPlayers = new FilterButton("Has Players", "hasplayers", model);
		notFull = new FilterButton("Not Full", "notfull", model);
		accessType = new AccessTypeFilter(model);
		search = new SearchField(model);
		buttonCountry = new CountryButton();
		view = new GenericListView(model);
		buttonJoin = new GenericButton("Join Server");
		buttonMainMenu = new GenericButton("Main Menu");
		buttonFavorites = new GenericButton("Favorites");
		buttonRefresh = new GenericButton("Refresh");
		buttonReset = new GenericButton("Reset Filters");
		buttonAddServer = new GenericButton("Add Your Server");
		buttonInfo = new GenericButton("More Info...");

		if (!model.getCurrentUrl().equals(model.getDefaultUrl())) {
			model.clear();
			model.refreshAPIData(model.getDefaultUrl(), 0, true);
		}
	}

	public void initGui() {
		model.clearUrlElements();

		if (!instancesCreated) {
			createInstances();
		}

		int top = 5;
		labelTitle.setY(top + 7).setX(width / 2 - mc.fontRenderer.getStringWidth("Public Server List") / 2);
		getScreen().attachWidget("Spoutcraft", labelTitle);

		buttonRefresh.setX(width - 5 - 100).setY(top).setWidth(100).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonRefresh);

		search.setWidth(128).setHeight(18).setX(6).setY(top+1);
		getScreen().attachWidget("Spoutcraft", search);
		model.addUrlElement(search);

		top+=25;

		filters.setWidth(130).setHeight(height - top - 55);
		filters.setX(5).setY(top);
		getScreen().attachWidget("Spoutcraft", filters);

		// Filter init {
		int ftop = 5;
		filterTitle.setX(5).setY(ftop).setHeight(11).setWidth(100);
		filters.attachWidget("Spoutcraft", filterTitle);
		ftop += 16;

		featured.setAllowSorting(false);
		featured.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", featured);
		model.addUrlElement(featured);
		ftop += 25;

		trending.setAllowSorting(false);
		trending.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", trending);
		model.addUrlElement(trending);
		ftop += 25;

		popular.setAllowSorting(false);
		popular.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", popular);
		model.addUrlElement(popular);
		ftop += 25;

		random.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", random);
		model.addUrlElement(random);
		ftop += 25;

		byName.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", byName);
		model.addUrlElement(byName);
		ftop += 25;

		byFreeSlots.setWidth(100).setHeight(20).setX(5).setY(ftop);
		byFreeSlots.setTooltip("Sorts by the number of free slots\non the server (maxplayers - players)");
		filters.attachWidget("Spoutcraft", byFreeSlots);
		model.addUrlElement(byFreeSlots);
		ftop += 25;

		byPlayers.setWidth(100).setHeight(20).setX(5).setY(ftop);
		byPlayers.setTooltip("Sorts by the actual number of\nonline players on the server");
		filters.attachWidget("Spoutcraft", byPlayers);
		model.addUrlElement(byPlayers);
		ftop += 25;

		/*byPing.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", byPing);
		model.addUrlElement(byPing);
		ftop += 25;*/

		hasPlayers.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", hasPlayers);
		model.addUrlElement(hasPlayers);
		ftop += 25;

		notFull.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", notFull);
		model.addUrlElement(notFull);
		ftop += 25;

		accessType.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", accessType);
		model.addUrlElement(accessType);
		ftop += 25;

		buttonCountry.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget("Spoutcraft", buttonCountry);
		model.addUrlElement(buttonCountry);
		ftop += 25;

		// Stretch to real width
		int fw = filters.getViewportSize(Orientation.HORIZONTAL);
		fw-=10;
		for (Widget w:filters.getAttachedWidgets()) {
			w.setWidth(fw);
		}

		if (!instancesCreated) {
			featured.setSelected(true);
		}
		// Filter init }

		view.setX((int) filters.getWidth() + filters.getX() + 5).setY(top).setWidth((int) (width - filters.getWidth() - 10 - filters.getX())).setHeight(height - top - 55);
		getScreen().attachWidget("Spoutcraft", view);

		top += view.getHeight() + 5;

		int totalWidth = Math.min(width - 9, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;

		buttonReset.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonReset);

		buttonInfo.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonInfo);

		buttonJoin.setHeight(20).setWidth(cellWidth).setX(right).setY(top);
		getScreen().attachWidget("Spoutcraft", buttonJoin);

		top+=25;

		buttonAddServer.setHeight(20).setWidth(cellWidth).setX(left).setY(top);
		getScreen().attachWidget("Spoutcraft", buttonAddServer);

		buttonFavorites.setHeight(20).setWidth(cellWidth).setX(center).setY(top);
		getScreen().attachWidget("Spoutcraft", buttonFavorites);

		buttonMainMenu.setHeight(20).setWidth(cellWidth).setX(right).setY(top);
		getScreen().attachWidget("Spoutcraft", buttonMainMenu);

		updateButtons();
		instancesCreated = true;
	}

	public void drawScreen(int a, int b, float c) {
		drawDefaultBackground();
	}

	public void buttonClicked(Button btn) {
		if (btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonFavorites)) {
			GuiFavorites fav = new GuiFavorites(new org.spoutcraft.client.gui.mainmenu.MainMenu());
			mc.displayGuiScreen(fav);
			fav.refresh();
		}
		if (btn.equals(buttonJoin)) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			if (item != null) {
				item.onClick(-1, -1, true);
			} else {
				updateButtons();
			}
		}
		if (btn.equals(buttonRefresh)) {
			model.updateUrl();
		}
		if (btn.equals(buttonReset)) {
			model.clearElementFilters();
			featured.setSelected(true);
			model.updateUrl();
		}
		if (btn.equals(buttonAddServer)) {
			Sys.openURL("http://servers.spout.org/submit");
		}
		if (btn.equals(buttonInfo)) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			if (item != null) {
				mc.displayGuiScreen(new GuiServerInfo(item, this));
			} else {
				updateButtons();
			}
		}
	}

	public void updateButtons() {
		boolean b = true;
		if (view.getSelectedRow() == -1 || !(view.getSelectedItem() instanceof ServerItem)) {
			b = false;
		}
		buttonJoin.setEnabled(b);
		buttonInfo.setEnabled(b);

		if (model.isLoading()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Loading...");
			buttonRefresh.setDisabledColor(new Color(0f,1f,0f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
		}
	}

	@Override
	public void updateScreen() {
		if (model.isLoading()) {
			Color color = new Color(0, 1f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setGreen(1f - (float)darkness);
			buttonRefresh.setDisabledColor(color);
		}
		super.updateScreen();
	}
}
