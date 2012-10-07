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
public interface Control extends Widget {
	/**
	 * True if the control is enabled and can receive input
	 *
	 * @return enabled
	 */
	public boolean isEnabled();

	/**
	 * Disables input to the control, but still allows it to be visible
	 *
	 * @param enable
	 * @return Control
	 */
	public Control setEnabled(boolean enable);

	/**
	 * Gets the color of this control
	 *
	 * @return color
	 */
	public Color getColor();

	/**
	 * Sets the color of this control
	 *
	 * @param color to set
	 * @return Control
	 */
	public Control setColor(Color color);

	/**
	 * Gets the color of this control when it is disabled
	 *
	 * @return disabled color
	 */
	public Color getDisabledColor();

	/**
	 * Sets the color of this control when it is disabled
	 *
	 * @param color to set
	 * @return Control
	 */
	public Control setDisabledColor(Color color);

	public boolean isFocus();

	public Control setFocus(boolean focus);

	/**
	 * Will be called if this control has focus and a key was pressed.
	 * @param key the pressed key.
	 * @return true if you handled the keypress and it shouldn't be handled elsewhere.
	 */
	public boolean onKeyPressed(Keyboard key);

	/**
	 * Will be called if this control has focus and a key was released.
	 * @param key the released key.
	 * @return true if you handled the keyrelease and it shouldn't be handled elsewhere.
	 */
	public boolean onKeyReleased(Keyboard key);
}
