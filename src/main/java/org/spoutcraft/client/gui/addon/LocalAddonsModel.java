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
package org.spoutcraft.client.gui.addon;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.ServerAddon;
import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.MinecraftFont;

public class LocalAddonsModel extends AbstractListModel {
	private GuiAddonsLocal gui = null;
	private List<AddonItem> items = new ArrayList<LocalAddonsModel.AddonItem>();

	public LocalAddonsModel(GuiAddonsLocal guiAddonsLocal) {
		gui = guiAddonsLocal;
		updateAddons();
	}

	public void updateAddons() {
		for (Addon addon:Spoutcraft.getAddonManager().getAddons()) {
			if (addon.getDescription().getName().equals("Spoutcraft") || addon instanceof ServerAddon) {
				continue;
			}
			items.add(new AddonItem(addon));
		}
	}

	@Override
	public ListWidgetItem getItem(int row) {
		if (row >= 0 && row < getSize()) {
			return items.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}

	public class AddonItem implements ListWidgetItem {
		private Addon addon;
		private ListWidget widget;
		private String authors;

		public AddonItem(Addon a) {
			addon = a;
			authors = "";
			for (String author:a.getDescription().getAuthors()) {
				if (!authors.isEmpty()) {
					authors += ", ";
				}
				authors+=author;
			}
		}

		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 20;
		}

		public void render(int x, int y, int width, int height) {
			MinecraftFont font = Spoutcraft.getMinecraftFont();
			font.drawString(addon.getDescription().getName(), x+2, y+2, 0xffffffff);
			font.drawString(authors, x+2, y+11, 0xffaaaaaa);
			String version = addon.getDescription().getVersion();
			int vwidth = font.getTextWidth(version);
			font.drawString(version, x + width - vwidth - 2, y+2, 0xffffffff);
		}

		public void onClick(int x, int y, boolean doubleClick) {
		}

		public Addon getAddon() {
			return addon;
		}
	}
}
