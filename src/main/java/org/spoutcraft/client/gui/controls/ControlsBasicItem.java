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

import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;

public abstract class ControlsBasicItem implements ListWidgetItem {
	private ListWidget widget;
	protected ControlsModel model;
	private boolean conflicts = false;

	public ControlsBasicItem(ControlsModel model) {
		this.model = model;
	}

	public ListWidget getListWidget() {
		return widget;
	}

	public void onClick(int x, int y, boolean doubleClick) {
		model.onItemClicked(this, doubleClick);
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}

	public abstract void setKey(int id);
	public abstract int getKey();

	public boolean conflicts(ControlsBasicItem other) {
		// TODO Better handling for modifiers
		return  getKey() != -128 && getKey() == other.getKey() && getModifiers() == other.getModifiers();
	}

	/**
	 * Tell if this item accepts modifier keys such as SHIFT, CONTROL or ALT.
	 * @return true, if the item accepts modifier keys.
	 */
	public abstract boolean useModifiers();

	/**
	 * Tell if this item accepts mouse buttons to summon the action
	 * @return true, if the item accepts mouse buttons
	 */
	public abstract boolean useMouseButtons();

	public void setConflicting(boolean c) {
		this.conflicts = c;
	}

	public void setModifiers(int m) {
		// Unused
	}

	public int getModifiers() {
		return 0;
	}

	public abstract String getName();

	public boolean isConflicting() {
		return conflicts;
	}
}
