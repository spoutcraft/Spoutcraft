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
public interface Gradient extends Widget {
	/**
	 * Gets the top colour of the gradient to render
	 *
	 * @return color
	 */
	public Color getTopColor();

	/**
	 * Sets the top colour of the gradient to render
	 *
	 * @param color
	 * @return gradient
	 */
	public Gradient setTopColor(Color color);

	/**
	 * Gets the bottom colour of the gradient to render
	 *
	 * @return color
	 */
	public Color getBottomColor();

	/**
	 * Sets the bottom colour of the gradient to render
	 *
	 * @param color
	 * @return gradient
	 */
	public Gradient setBottomColor(Color color);

	/**
	 * Set the direction the gradient is drawn.
	 * Default is VERTICAL, if using HORIZONTAL then read top as left and bottom as right.
	 * @param axis the orientation to draw in
	 * @return
	 */
	public Gradient setOrientation(Orientation axis);

	/**
	 * Get the direction the gradient is drawn.
	 * Default is VERTICAL, if using HORIZONTAL then read top as left and bottom as right.
	 * @return the orientation being used
	 */
	public Orientation getOrientation();
}
