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

import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

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
		//TODO better handling for modifiers
		return getKey() == other.getKey() && getModifiers() == other.getModifiers();
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
		//Unused
	}

	public int getModifiers() {
		return 0;
	}

	public abstract String getName();

	public boolean isConflicting() {
		return conflicts;
	}
}
