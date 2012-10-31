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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.SaveFormatComparator;

import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.FilterItem;
import org.spoutcraft.client.gui.FilterModel;
import org.spoutcraft.client.gui.mainmenu.MainMenu;

public class WorldModel extends FilterModel {
	ISaveFormat access;
	List<SaveFormatComparator> worlds;
	private ButtonUpdater buttonUpdater;

	public WorldModel(ButtonUpdater updater) {
		refresh();
		this.buttonUpdater = updater;
	}

	protected void refreshContents() {
		access = SpoutClient.getHandle().getSaveLoader();
		worlds = access.getSaveList();
		Collections.sort(worlds);
		items.clear();
		for (SaveFormatComparator world:worlds) {
			WorldItem item = new WorldItem(world, access);
			items.add(item);
		}

		sizeChanged();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		buttonUpdater.updateButtons();
		if (doubleClick) {
			playWorld(item);
		}
	}

	public void playWorld(int i) {
		Minecraft mc = SpoutClient.getHandle();
		EnumGameType gameType = worlds.get(i).getEnumGameType();
		String s = worlds.get(i).getFileName();
		if (s == null) {
			s = (new StringBuilder()).append("World").append(i).toString();
		}
		mc.launchIntegratedServer(s, worlds.get(i).getDisplayName(), null);
		SpoutClient.getInstance().getServerManager().setJoinedFrom(new MainMenu(), "Main Menu");
		mc.displayGuiScreen(null);
	}

	public void deleteWorld(int selectedRow) {
		access.deleteWorldDirectory(worlds.get(selectedRow).getFileName());
		System.out.println("Deleted world.");
		refresh();
		buttonUpdater.updateButtons();
	}

	/**
	 * Renames a world
	 * @param oldworld the name of the world to be renamed
	 * @param newworld the new name of the world
	 */
	public void rename(String oldworld, String newworld) {
		access.renameWorld(oldworld, newworld);
		refresh();
	}
}
