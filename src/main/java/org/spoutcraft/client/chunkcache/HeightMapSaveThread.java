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
package org.spoutcraft.client.chunkcache;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class HeightMapSaveThread extends Thread {
	private Queue<HeightMap> saveQueue = new LinkedBlockingDeque<HeightMap>();
	private boolean running;

	public void addMap(HeightMap heightMap) {
		synchronized (saveQueue) {
			saveQueue.add(heightMap);
		}
		if (!running) {
			start();
		}
	}

	@Override
	public void run() {
		running = true;
		synchronized (saveQueue) {
			while (!saveQueue.isEmpty()) {
				saveQueue.poll().save();
			}
		}
		running = false;
	}
}
