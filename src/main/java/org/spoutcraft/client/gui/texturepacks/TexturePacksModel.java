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
package org.spoutcraft.client.gui.texturepacks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ITexturePack;
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
		textures = SpoutClient.getHandle().texturePackList;
		textures.updateAvaliableTexturePacks();

		List<TexturePackItem> oldPacks = new ArrayList<TexturePackItem>();
		oldPacks.addAll(items);
		items.clear();

		for (TexturePackItem item : oldPacks) {
			boolean found = false;
			for (ITexturePack pack : getTextures()) {
				if (item.getPack() == pack) {
					found = true;
					break;
				}
			}
			if (found) {
				items.add(item);
			}
		}

		for (ITexturePack pack : getTextures()) {
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
	}

	@SuppressWarnings("unchecked")
	public List<ITexturePack> getTextures() {
		return (List<ITexturePack>)textures.availableTexturePacks();
	}

	public void changeTexturePack(ITexturePack pack) {
		// TODO
	}
}
