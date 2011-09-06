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


public interface TextField extends Control{
	
	/**
	 * Gets the position of the cursor in the text field. Position zero is the start of the text.
	 * @return position
	 */
	public int getCursorPosition();
	
	/**
	 * Sets the position of the cursor in the text field.
	 * @param position to set to
	 * @return textfield
	 */
	public TextField setCursorPosition(int position);
	
	/**
	 * Gets the text typed in this text field
	 * @return text
	 */
	public String getText();
	
	/**
	 * Sets the text visible in this text field
	 * @param text inside of the text field
	 * @return textfield
	 */
	public TextField setText(String text);
	
	/**
	 * Gets the maximum characters that can be typed into this text field
	 * @return maximum characters
	 */
	public int getMaximumCharacters();
	
	/**
	 * Sets the maximum characters that can be typed into this text field
	 * @param max characters that can be typed
	 * @return max chars
	 */
	public TextField setMaximumCharacters(int max);
	
	/**
	 * Gets the color of the inner field area of the text box.
	 * @return field color
	 */
	public Color getFieldColor();
	
	/**
	 * Sets the field color of the inner field area of the text box.
	 * @param color to render as
	 * @return textfield
	 */
	public TextField setFieldColor(Color color);
	
	/**
	 * Gets the outside color of the field area of the text box.
	 * @return border color
	 */
	public Color getBorderColor();
	
	/**
	 * Sets the outside color of the field area of the text box.
	 * @param color to render as
	 * @return textfield
	 */
	public TextField setBorderColor(Color color);
	
	public PopupScreen getScreen();
}
