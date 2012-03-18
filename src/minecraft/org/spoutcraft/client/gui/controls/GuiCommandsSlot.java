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
package org.spoutcraft.client.gui.controls;

import net.minecraft.src.FontRenderer;

import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;

public class GuiCommandsSlot extends GenericListWidget {
	GuiEditShortcut parent;
	Shortcut shortcut;

	public GuiCommandsSlot(GuiEditShortcut parent) {
		setWidth(parent.width);
		setHeight(parent.height - 70 - 25);
		setX(0).setY(60);
		this.parent = parent;
		this.shortcut = parent.getShortcut();
		updateItems();
	}

	public void updateItems() {
		clear();
		for (String cmd : shortcut.getCommands()) {
			addItem(new CommandLWI(cmd));
		}
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		if (doubleClick) {
			parent.editCommand(item);
		}
		parent.updateButtons();
	}

	private class CommandLWI implements ListWidgetItem {
		ListWidget widget;
		String cmd;

		public CommandLWI(String cmd) {
			this.cmd = cmd;
		}

		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 23;
		}

		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			parent.drawString(font, cmd, x + 2, y + 2, 0xffffff);
			if (cmd.startsWith("/")) {
				parent.drawString(font, "Command", x + 2, y + 13, 0xaaaaaa);
			} else {
				parent.drawString(font, "Chat Message", x + 2, y + 13, 0xaaaaaa);
			}
		}

		public void onClick(int x, int y, boolean d) {
		}
	}
}
