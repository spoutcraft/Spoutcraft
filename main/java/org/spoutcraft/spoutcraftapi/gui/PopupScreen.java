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

public interface PopupScreen extends Screen {

	/**
	 * Is true if the popup screen has no transparency layer
	 * 
	 * @return transparency
	 */
	public boolean isTransparent();

	/**
	 * Sets the transparency layer
	 * 
	 * @param value to set
	 * @return popupscreen
	 */
	public PopupScreen setTransparent(boolean value);

	/**
	 * Closes the screen. Functionally equivelent to InGameHUD.closePopup()
	 * 
	 * @return true if the screen was closed
	 */
	public boolean close();
}
