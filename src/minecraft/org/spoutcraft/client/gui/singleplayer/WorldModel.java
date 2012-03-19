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
package org.spoutcraft.client.gui.singleplayer;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.SaveFormatComparator;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.FilterModel;

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
		for (SaveFormatComparator world : worlds) {
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
		int gameType = worlds.get(i).getGameType();
		if (gameType == 0) {
			mc.playerController = new PlayerControllerSP(mc);
		} else {
			mc.playerController = new PlayerControllerCreative(mc);
		}
		String s = worlds.get(i).getFileName();
		if (s == null) {
			s = (new StringBuilder()).append("World").append(i).toString();
		}
		mc.startWorld(s, worlds.get(i).getDisplayName(), null);
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
