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
package org.spoutcraft.client.gui.texturepacks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;

import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;

import org.spoutcraft.client.SpoutClient;

public class TexturePacksModel extends AbstractListModel {
	TexturePackList textures = SpoutClient.getHandle().texturePackList;
	List<TexturePackItem> items = new ArrayList<TexturePackItem>();
	GuiTexturePacks currentGui = null;

	public TexturePacksModel() {
	}

	public void setCurrentGui(GuiTexturePacks gui) {
		currentGui = gui;
	}

	public int getSize() {
		return items.size();
	}

	@Override
	public TexturePackItem getItem(int row) {
		return items.get(row);
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		if (currentGui != null) {
			currentGui.updateButtons();
		}
	}

	public void update() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		textures = SpoutClient.getHandle().texturePackList;
		try {
			textures.updateAvaliableTexturePacks();

			List<TexturePackItem> oldPacks = new ArrayList<TexturePackItem>();
			oldPacks.addAll(items);
			items.clear();

			for (TexturePackItem item : oldPacks) {
				boolean found = false;
				for (TexturePackBase pack : getTextures()) {
					if (item.getPack() == pack) {
						found = true;
						break;
					}
				}
				if (found) {
					items.add(item);
				}
			}

			for (TexturePackBase pack : getTextures()) {
				boolean found = false;
				for (TexturePackItem item : oldPacks) {
					if (item.getPack() == pack) {
						found = true;
						break;
					}
				}
				if (!found) {
					items.add(new TexturePackItem(pack));
				}
			}
		} finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<TexturePackBase> getTextures() {
		return (List<TexturePackBase>) textures.availableTexturePacks();
	}

	public void changeTexturePack(TexturePackBase pack) {
		// TODO
	}
}
