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
package org.spoutcraft.api.event.client;

import org.spoutcraft.api.event.Cancellable;
import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.gui.ScreenType;

public class KeyDownEvent extends Event implements Cancellable {
	private Keyboard key;
	private ScreenType screenType;
	protected boolean cancel = false;
	public KeyDownEvent(int keyPress, ScreenType screenType) {
		super(Type.KEY_DOWN);
		this.key = Keyboard.getKey(keyPress);
		this.screenType = screenType;
	}
	
	public Keyboard getKey() {
		return key;
	}
	
	public ScreenType getScreenType(){
		return screenType;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
