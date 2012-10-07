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
package org.spoutcraft.client.gui.minimap;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.tuple.Pair;

import org.spoutcraft.api.gui.Point;

public class MapWidgetRenderer extends Thread {
	public Queue<Point> renderQueue = new LinkedBlockingQueue<Point>();

	public MapWidgetRenderer() {
		super();
	}

	@Override
	public void run() {
		while (true) {
			while (!renderQueue.isEmpty()) {
				try {
					Point coords = renderQueue.remove();
					MapWidget.drawChunk(coords.getX(), coords.getY(), true);
				} catch(NoSuchElementException e) {
					break;
				} catch(Exception e) {
					continue;
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
