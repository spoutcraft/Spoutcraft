/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.event.player;

import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.gui.ScreenType;

public class MouseDownEvent extends Event {
	private int x;
	private int y;
	private Mouse button;
	private ScreenType screenType;
	public MouseDownEvent(int mouseButton, int x, int y, ScreenType screenType) {
		super(Type.MOUSE_DOWN);
		this.x = x;
		this.y = y;
		this.button = Mouse.getButton(mouseButton);
		this.screenType = screenType;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Mouse getButton() {
		return button;
	}
	
	public ScreenType getScreenType(){
		return screenType;
	}
}
