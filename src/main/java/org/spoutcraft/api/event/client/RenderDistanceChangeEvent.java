/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.api.event.client;

import org.spoutcraft.api.event.Cancellable;
import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.event.HandlerList;
import org.spoutcraft.api.player.RenderDistance;

public class RenderDistanceChangeEvent extends Event<RenderDistanceChangeEvent> implements Cancellable {
	protected RenderDistance newView;

	public RenderDistanceChangeEvent(RenderDistance newView) {
		this.newView = newView;
	}

	public RenderDistance getCurrentRenderDistance() {
		return null; // TODO Get method in player
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
