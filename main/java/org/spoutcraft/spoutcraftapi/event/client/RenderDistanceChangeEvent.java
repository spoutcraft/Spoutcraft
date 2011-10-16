/*
 * This file is part of Spout API (http://wiki.getspout.org/).
 * 
 * Spout API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spout API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.event.client;

import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.Event;
import org.spoutcraft.spoutcraftapi.event.HandlerList;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;

public class RenderDistanceChangeEvent extends Event<RenderDistanceChangeEvent> implements Cancellable {

	protected RenderDistance newView;

	public RenderDistanceChangeEvent(RenderDistance newView) {
		this.newView = newView;
	}

	public RenderDistance getCurrentRenderDistance() {
		return null; // TODO: get method in player
	}

	public RenderDistance getNewRenderDistance() {
		return newView;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public static final HandlerList<RenderDistanceChangeEvent> handlers = new HandlerList<RenderDistanceChangeEvent>();

	@Override
	public HandlerList<RenderDistanceChangeEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Render Distance Change Event";
	}

}
