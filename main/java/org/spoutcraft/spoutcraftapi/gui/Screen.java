/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.spoutcraft.spoutcraftapi.gui;

import java.util.UUID;

public interface Screen extends Widget {

	/**
	 * Get's an array of all the attached widgets to this screen. Modifying this array will not affect the screen.
	 * 
	 * @return array of all widgets
	 */
	public Widget[] getAttachedWidgets();

	/**
	 * Attaches a widget to this screen
	 * 
	 * @param widget to attach
	 * @return screen
	 */
	public Screen attachWidget(Widget widget);

	/**
	 * Removes a widget from this screen
	 * 
	 * @param widget to remove
	 * @return screen
	 */
	public Screen removeWidget(Widget widget);

	/**
	 * Is true if the screen has the given widget attached to it. Uses a linear search, takes O(n) time to complete.
	 * 
	 * @param widget to search for
	 * @return true if the widget was found
	 */
	public boolean containsWidget(Widget widget);

	/**
	 * Is true if the screen has a widget with the given id attached to it. Uses a linear search, takes O(n) time to complete.
	 * 
	 * @param id to search for
	 * @return true if the widget was found
	 */
	public boolean containsWidget(UUID id);

	/**
	 * Get's the widget that is associated with the given id, or null if none was found
	 * 
	 * @param id to search for
	 * @return widget, or null if none found.
	 */
	public Widget getWidget(UUID id);

	/**
	 * Replaces any attached widget with the given widget's id with the new widget
	 * 
	 * @param widget to replace with
	 * @return true if a widget was replaced
	 */
	public boolean updateWidget(Widget widget);

	/**
	 * Is true if this grey background is visible and rendering on the screen
	 * 
	 * @return visible
	 */
	public boolean isBgVisible();

	/**
	 * Sets the visibility of the grey background. If true, it will render normally. If false, it will not appear on the screen.
	 * 
	 * @param enable the visibility
	 * @return the screen
	 */
	public GenericScreen setBgVisible(boolean enable);

	public double getHeight();

	public double getWidth();

	/**
	 * Gets the screen type of this screen
	 * @return the screen type
	 */
	public ScreenType getScreenType();

	public Screen setMouseX(int mouseX);

	public Screen setMouseY(int mouseY);

	public int getMouseX();

	public int getMouseY();
}
