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
package org.spoutcraft.client.gui.controls;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;

public class ControlsModel extends AbstractListModel {
	private GuiControls gui;
	protected GameSettings options = SpoutClient.getHandle().gameSettings;
	private List<ControlsBasicItem> items = new LinkedList<ControlsBasicItem>();
	private SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	private boolean editing = false;
	private ControlsBasicItem lastEdit = null;

	public ControlsModel(GuiControls gui) {
		this.gui = gui;
	}

	public void refresh() {
		items.clear();

		if (gui.checkVanilla.isChecked()) {
			// Minecraft items
			int n = 0;
			for (KeyBinding binding:options.keyBindings) {
				items.add(new VanillaBindingItem(n, binding, this));
				n++;
			}
		}

		if (gui.checkSpoutcraft.isChecked()) {
			// Spoutcraft items
			for (KeyBinding binding:options.spoutcraftBindings) {
				items.add(new SpoutcraftBindingItem(binding, this));
			}
		}

		if (gui.checkShortcuts.isChecked()) {
			// Shortcuts
			for (Shortcut sh:manager.getAllShortcuts()) {
				items.add(new ShortcutBindingItem(sh, this));
			}
		}

		if (gui.checkBindings.isChecked()) {
			// Plugin controls
			for (org.spoutcraft.api.keyboard.KeyBinding binding:manager.getAllBindings()) {
				items.add(new KeyBindingItem(binding, this));
			}
		}

		// Check for conflicting keys
		outer: for (ControlsBasicItem item1:items) {
			for (ControlsBasicItem item2:items) {
				if (!item1.equals(item2)) {
					item1.setConflicting(item1.conflicts(item2));
					if (item1.isConflicting()) {
						continue outer;
					}
				}
			}
		}

		if (!gui.search.getText().isEmpty()) {
			for (int i = 0; i < items.size(); i++) {
				ControlsBasicItem item = items.get(i);
				if (!item.getName().toLowerCase().contains(gui.search.getText().toLowerCase())) {
					items.remove(i);
					i--;
				}
			}
		}

		sizeChanged();
	}

	public void setCurrentGui(GuiControls gui) {
		this.gui = gui;
	}

	@Override
	public ControlsBasicItem getItem(int row) {
		if (row < 0 || row >= items.size()) {
			return null;
		}
		return items.get(row);
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
	}

	protected void onItemClicked(ControlsBasicItem item, boolean doubleClicked) {
		if (doubleClicked) {
			editing = !editing;
			if (item != lastEdit) {
				editing = true;
			}
			lastEdit = item;
		}
		gui.updateButtons();
	}

	public void setEditing(ControlsBasicItem item) {
		editing = true;
		lastEdit = item;
	}

	public ControlsBasicItem getEditingItem() {
		return editing?lastEdit:null;
	}

	public void finishEdit() {
		editing = false;
		refresh();
	}
}
