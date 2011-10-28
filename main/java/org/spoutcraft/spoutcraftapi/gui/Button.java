/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

@UnsafeClass
public interface Button extends Control, Label {

	/**
	 * Get's the text that is displayed when the control is disabled
	 * 
	 * @return disabled text
	 */
	public String getDisabledText();

	/**
	 * Sets the text that is displayed when the control is disabled
	 * 
	 * @param text to display
	 * @return Button
	 */
	public Button setDisabledText(String text);

	/**
	 * Get's the color of the control while the mouse is hovering over it
	 * 
	 * @return color
	 */
	public Color getHoverColor();

	/**
	 * Sets the color of the control while the mouse is hovering over it
	 * 
	 * @param color
	 * @return Button
	 */
	public Button setHoverColor(Color color);

	public Button setText(String text);
	
	public Button setTextColor(Color color);

	public Button setAuto(boolean auto);
	
	public Button setAlign(WidgetAnchor align);
	
	/**
	 * Fires when this button is clicked on the screen.
	 * 
	 * This event is also sent to the screen listener, afterwards.
	 * 
	 * @param event
	 */
	public void onButtonClick(ButtonClickEvent event);
}
