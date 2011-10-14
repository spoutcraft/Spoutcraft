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
package org.spoutcraft.spoutcraftapi.event.screen;

import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.Event;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;

public abstract class ScreenEvent<TEvent extends ScreenEvent<TEvent>> extends Event<TEvent> implements Cancellable{
	protected Screen screen;
	protected Player player;
	protected ScreenType type;
	protected boolean cancel = false;
	protected ScreenEvent(Player player, Screen screen, ScreenType type) {
		super();
		this.screen = screen;
		this.player = player;
		this.type = type;
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public ScreenType getScreenType(){
		return type;
	}
		
	public Player getPlayer() {
		return player;
	}
	
	public boolean isCancelled(){
		return cancel;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
