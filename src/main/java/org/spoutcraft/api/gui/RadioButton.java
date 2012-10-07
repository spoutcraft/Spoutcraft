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
package org.spoutcraft.api.gui;

import org.spoutcraft.api.UnsafeClass;

@UnsafeClass
public interface RadioButton extends Button {
	/**
	 * Is true if the radio button is selected
	 * @return selected
	 */
	public boolean isSelected();

	/**
	 * Sets this radio button as selected
	 * @param selected
	 * @return this
	 */
	public RadioButton setSelected(boolean selected);

	/**
	 * Gets the group id for this radio button. Radio buttons on the same screen, with the same group id will be grouped together
	 * @return group id
	 */
	public int getGroup();

	/**
	 * Sets the group id for this radio button
	 * @param group id to set
	 * @return this
	 */
	public RadioButton setGroup(int group);
}
