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
package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class PlayerBedEnterEvent extends PlayerEvent<PlayerBedEnterEvent> implements Cancellable {
	
	protected Block bed;

	protected PlayerBedEnterEvent(Player player, Block bed) {
		super(player);
		this.bed = bed;
	}
	
	public Block getBed() {
		return bed;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public static final HandlerList<PlayerBedEnterEvent> handlers = new HandlerList<PlayerBedEnterEvent>();
	
	@Override
	public HandlerList<PlayerBedEnterEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Player Bed Enter Event";
	}

}
