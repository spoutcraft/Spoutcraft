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
public interface ListWidgetItem {
	/**
	 * Set the parent listwidget
	 * @param widget the parent widget
	 */
	public void setListWidget(ListWidget widget);

	/**
	 * Gets the parent listwidget
	 * @returns parent widget
	 */
	public ListWidget getListWidget();

	/**
	 * @return the height of the content of the item.
	 */
	public int getHeight();

	/**
	 * Renders the item.
	 * @param x position of the item
	 * @param y position of the item
	 * @param width of the item
	 * @param height of the item
	 */
	public void render(int x, int y, int width, int height);

	/**
	 * Will be called when someone clicks on this item
	 * @param x the relative x position where the item was clicked
	 * @param y the relative y position where the item was clicked
	 * @param doubleClick wether the item has been doubleclicked
	 */
	public void onClick(int x, int y, boolean doubleClick);
}
