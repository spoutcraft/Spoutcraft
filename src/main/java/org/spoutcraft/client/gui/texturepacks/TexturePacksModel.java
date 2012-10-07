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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackImplementation;
import net.minecraft.src.TexturePackList;

import org.spoutcraft.api.gui.AbstractListModel;
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
		if (wasSandboxed) SpoutClient.disableSandbox();
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
					items.add(new TexturePackItem(this, (TexturePackImplementation) pack));
				}
			}
		} finally {
			if (wasSandboxed) SpoutClient.enableSandbox();
		}
	}

	@SuppressWarnings("unchecked")
	public List<TexturePackBase> getTextures() {
		return (List<TexturePackBase>)textures.availableTexturePacks();
	}

	public void changeTexturePack(TexturePackBase pack) {
		// TODO
	}
}
