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
package org.spoutcraft.client.gui.texturepacks;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.animation.Animation;
import org.spoutcraft.api.animation.InQuadAnimationProgress;
import org.spoutcraft.api.animation.PropertyAnimation;
import org.spoutcraft.api.event.Listener;
import org.spoutcraft.api.event.animation.AnimationStopEvent;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.GenericTexture;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.Rectangle;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.database.GuiAPIDisplay;
import org.spoutcraft.client.gui.database.RandomButton;
import org.spoutcraft.client.gui.database.SearchField;
import org.spoutcraft.client.gui.database.SortButton;
import org.spoutcraft.client.gui.texturepacks.TextureItem.Download;

public class GuiTexturePacksDatabase extends GuiAPIDisplay implements Listener<AnimationStopEvent>, ButtonUpdater {
	private Label screenTitle, sortFilterTitle;
	private Button buttonMainMenu, buttonLocal, buttonDownload, buttonAdd, buttonRefresh, buttonForum, buttonCancelDownload;
	private SearchField search;
	private boolean instancesCreated = false;
	private GenericListView view;
	private TexturePacksDatabaseModel model = SpoutClient.getInstance().getTexturePacksDatabaseModel();
	private GenericScrollArea filter;
	private SortButton featured, popular, byName;
	private RandomButton random;
	private ResolutionFilter filterResolution;
	private GenericTexture animatedTexture;
	private Animation animation;

	public GuiTexturePacksDatabase() {
		TextureItem.setButtonUpdater(this);
	}

	private void createInstances() {
		buttonMainMenu = new GenericButton("Main Menu");
		buttonLocal = new GenericButton("Installed Textures");
		buttonDownload = new GenericButton("Download Texture");
		buttonAdd = new GenericButton("Add Texture");
		buttonRefresh = new GenericButton("Refresh");
		screenTitle = new GenericLabel("Texture Packs Database");
		buttonForum = new GenericButton("Show Forum Thread");
		buttonCancelDownload = new GenericButton("Cancel Download");
		buttonCancelDownload.setTooltip("Press shift to cancel all downloads");
		view = new GenericListView(model);
		model.setCurrentGui(this);
		model.refreshAPIData(model.getDefaultUrl(), 0, true);
		featured = new SortButton("Featured", "featured", model);
		featured.setAllowSorting(false);
		popular = new SortButton("Popular", "popular", model);
		popular.setAllowSorting(false);
		byName = new SortButton("Name", "sortBy=name", model);
		random = new RandomButton(model);
		filterResolution = new ResolutionFilter();
		sortFilterTitle = new GenericLabel("Sort & Filter");
		search = new SearchField(model);
		model.clearUrlElements();
		model.addUrlElement(popular);
		model.addUrlElement(search);
		model.addUrlElement(random);
		model.addUrlElement(featured);
		model.addUrlElement(byName);
		model.addUrlElement(filterResolution);
		filter = new GenericScrollArea();
	}

	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		if (!instancesCreated) createInstances();

		int top = 5;

		int swidth = mc.fontRenderer.getStringWidth(screenTitle.getText());
		screenTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		getScreen().attachWidget(spoutcraft, screenTitle);

		buttonRefresh.setX(width - 5 - 100).setY(top).setWidth(100).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonRefresh);

		search.setGeometry(5, 5, 130, 16);
		getScreen().attachWidget(spoutcraft, search);

		top+=25;

		int sheight = height - top - 55;

		filter.setX(5).setY(top).setWidth(130).setHeight(sheight);
		getScreen().attachWidget(spoutcraft, filter);

		view.setX((int) (5 + filter.getX() + filter.getWidth())).setY(top).setWidth((int) (width - 15 - filter.getWidth())).setHeight(sheight);
		getScreen().attachWidget(spoutcraft, view);

		top += 5 + view.getHeight();

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonForum.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonForum);

		buttonCancelDownload.setGeometry(center, top, cellWidth, 20);
		getScreen().attachWidget(spoutcraft, buttonCancelDownload);

		buttonDownload.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDownload);

		top += 25;

		buttonAdd.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonAdd);

		buttonLocal.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonLocal);

		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);

		// Filter init
		int ftop = 5;
		sortFilterTitle.setX(5).setY(ftop).setHeight(11).setWidth(100);
		filter.attachWidget(spoutcraft, sortFilterTitle);
		ftop += 16;

		featured.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, featured);
		ftop += 25;

		popular.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, popular);
		ftop += 25;

		random.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, random);
		ftop += 25;

		byName.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, byName);
		ftop += 25;

		filterResolution.setWidth(100).setHeight(20).setY(ftop).setX(5);
		filter.attachWidget(spoutcraft, filterResolution);
		ftop += 25;

		// Stretch to real width
		int fw = filter.getViewportSize(Orientation.HORIZONTAL);
		fw-=10;
		for (Widget w:filter.getAttachedWidgets()) {
			w.setWidth(fw);
		}

		if (!instancesCreated) {
			featured.setSelected(true);
		}

		updateButtons();
		instancesCreated = true;
	}

	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
	}

	public void updateButtons() {
		int sel = view.getSelectedRow();
		//boolean enable = false;
		boolean allowForum = false;
		boolean allowDownload = false;
		TextureItem texture = null;
		if (sel >= 0) {
			ListWidgetItem item = model.getItem(sel);
			if (item instanceof TextureItem) {
				texture = (TextureItem)item;
				//enable = true;
				allowDownload = !(texture.isDownloading() || texture.isInstalled());
				allowForum = !texture.getForumlink().isEmpty();
			}
		}
		buttonDownload.setEnabled(allowDownload);
		buttonCancelDownload.setEnabled(TextureItem.hasDownloads());
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			buttonCancelDownload.setText("Cancel All Downloads");
			buttonCancelDownload.setEnabled(TextureItem.hasDownloads());
		} else {
			buttonCancelDownload.setText("Cancel Download");
			buttonCancelDownload.setEnabled(texture!=null&&texture.isDownloading());
		}
		buttonForum.setEnabled(allowForum);

		if (model.isLoading()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Loading...");
			buttonRefresh.setDisabledColor(new Color(0f, 1f, 0f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
		}
	}

	public void buttonClicked(Button btn) {
		if (btn.equals(buttonLocal)) {
			mc.displayGuiScreen(new GuiTexturePacks());
		}
		if (btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonAdd)) {
			Sys.openURL("http://textures.spout.org/submit");
		}
		if (btn.equals(buttonDownload)) {
			int sel = view.getSelectedRow();
			if (sel >= 0) {
				ListWidgetItem item = model.getItem(sel);
				if (item instanceof TextureItem) {
					((TextureItem)item).download();
					Rectangle itemPos = view.getItemRect(view.getSelectedRow());
					itemPos.moveBy(2 + view.getX(), 2 - view.getScrollPosition(Orientation.VERTICAL) + view.getY());
					itemPos.resize(25, 25);
					Rectangle finalPos = buttonLocal.getGeometry().clone();
					finalPos.moveBy(2, 2);
					finalPos.resize(16, 16);
					animatedTexture = new GenericTexture(((TextureItem)item).getIconUrl());
					getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), animatedTexture);
					animatedTexture.setGeometry(itemPos);
					PropertyAnimation ani = new PropertyAnimation(animatedTexture, "geometry");
					ani.setEndValue(finalPos);
					ani.setDuration(1000);
					ani.setAnimationProgress(new InQuadAnimationProgress());
					ani.start();
					animation = ani;
					updateButtons();
				}
			}
		}
		if (btn.equals(buttonRefresh)) {
			model.refreshAPIData(model.getCurrentUrl(), 0, true);
		}
		if (btn.equals(buttonForum)) {
			int sel = view.getSelectedRow();
			if (sel >= 0) {
				ListWidgetItem item = model.getItem(sel);
				if (item instanceof TextureItem) {
					((TextureItem)item).download();
					Sys.openURL(((TextureItem)item).getForumlink());
				}
			}
		}
		if (btn.equals(buttonCancelDownload)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				TextureItem.cancelAllDownloads();
			} else {
				int sel = view.getSelectedRow();
				if (sel >= 0) {
					ListWidgetItem item = model.getItem(sel);
					if (item != null && item instanceof TextureItem) {
						TextureItem t = (TextureItem) item;
						if (t.isDownloading()) {
							TextureItem.getDownload(t.getId()).cancel();
						}
					}
				}
			}
			updateButtons();
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

	public void onEvent(AnimationStopEvent event) {
		if (animatedTexture != null && event.getAnimation() == animation) {
			animatedTexture.setVisible(false);
		}
	}

	@Override
	public void handleKeyboardInput() {
		updateButtons();
		super.handleKeyboardInput();
	}
}
