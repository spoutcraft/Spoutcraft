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
package org.spoutcraft.client.gui.singleplayer;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.GuiTextDialog;
import org.spoutcraft.client.gui.GuiTextDialog.DialogEventHandler;

public class GuiWorldSelection extends GuiSpoutScreen implements ButtonUpdater {
	GenericLabel labelTitle;
	GenericButton buttonPlay, buttonRename, buttonCreate, buttonMainMenu;
	ButtonDeleteWorld buttonDelete;
	GenericListView view;
	WorldSearchField search;

	GuiScreen parent = null;
	WorldModel model;

	public GuiWorldSelection(GuiScreen parent) {
		this.parent = parent;
		model = new WorldModel(this);
	}

	@Override
	protected void createInstances() {
		labelTitle = new GenericLabel("World Selection");
		buttonPlay = new GenericButton("Play");
		buttonRename = new GenericButton("Rename");
		buttonDelete = new ButtonDeleteWorld(this);
		buttonCreate = new GenericButton("New World");
		buttonMainMenu = new GenericButton("Main Menu");
		search = new WorldSearchField(model);
		view = new GenericListView(model);

		Screen screen = getScreen();
		Addon spoutcraft = SpoutClient.getInstance().getAddonManager().getAddon("Spoutcraft");
		screen.attachWidgets(spoutcraft, labelTitle, view, buttonPlay, buttonRename, buttonDelete, buttonCreate, buttonMainMenu, search);

		updateButtons();
	}

	@Override
	protected void layoutWidgets() {
		int top = 5;
		int swidth = mc.fontRenderer.getStringWidth(labelTitle.getText());
		labelTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		search.setX(5).setWidth(100).setY(top + 3).setHeight(16);

		top += 25;

		view.setX(5).setY(top).setWidth(width - 10).setHeight(height - 55 - top);

		top = height - 50;

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonRename.setX(left).setY(top).setHeight(20).setWidth(cellWidth);
		buttonCreate.setX(center).setY(top).setHeight(20).setWidth(cellWidth);
		buttonPlay.setX(right).setY(top).setHeight(20).setWidth(cellWidth);

		top += 25;

		buttonDelete.setX(left).setY(top).setHeight(20).setWidth(cellWidth);
		buttonMainMenu.setX(right).setY(top).setHeight(20).setWidth(cellWidth);
	}

	public void deleteSelectedWorld() {
		model.deleteWorld(view.getSelectedRow());
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonMainMenu) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn == buttonPlay) {
			model.playWorld(view.getSelectedRow());
		}
		if (btn == buttonCreate) {
			SpoutClient.getHandle().displayGuiScreen(new GuiCreateWorld(this));
		}
		if (btn == buttonRename) {
			WorldItem item = (WorldItem) view.getSelectedItem();
			if (item != null) {
				final String worldname = item.getWorld().getWorldName();
				final String worldsave = item.getFileName();
				GuiTextDialog dialog = new GuiTextDialog("Enter a new name for '" + worldname + "'", worldname, new DialogEventHandler() {
					public void onDone(GuiTextDialog dialog) {
						model.rename(worldsave, dialog.getText());
					}

					public void onCancel(GuiTextDialog dialog) {}
				}, this);
				mc.displayGuiScreen(dialog);
			}
		}
	}

	public void updateButtons() {
		boolean active = true;
		try {
			WorldItem item = (WorldItem) view.getSelectedItem();
			if (item == null) {
				active = false;
			} else {
				// TODO special value-based handling perhaps?
			}
		} catch(ClassCastException e1) {
			active = false;
		}
		buttonPlay.setEnabled(active);
		buttonDelete.setEnabled(active);
		buttonRename.setEnabled(active);
	}
}
