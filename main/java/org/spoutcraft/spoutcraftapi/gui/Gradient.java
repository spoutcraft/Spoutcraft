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


public interface Gradient extends Widget {
	
	/**
	 * Gets the top colour of the gradient to render
	 * @return color
	 */
	public Color getTopColor();
	
	/**
	 * Sets the top colour of the gradient to render
	 * @param color
	 * @return gradient
	 */
	public Gradient setTopColor(Color color);
	
	/**
	 * Gets the bottom colour of the gradient to render
	 * @return color
	 */
	public Color getBottomColor();
	
	/**
	 * Sets the bottom colour of the gradient to render
	 * @param color
	 * @return gradient
	 */
	public Gradient setBottomColor(Color color);

}
