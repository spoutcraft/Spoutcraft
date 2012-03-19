/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.spoutcraft.client.packet.CompressablePacket;
import org.spoutcraft.client.packet.SpoutPacket;

public class PacketDecompressionThread extends Thread {
	private static PacketDecompressionThread instance = null;
	private static final int QUEUE_CAPACITY = 1024 * 10;
	private final LinkedBlockingDeque<CompressablePacket> queue = new LinkedBlockingDeque<CompressablePacket>(QUEUE_CAPACITY);
	private final List<CompressablePacket> decompressed = Collections.synchronizedList(new LinkedList<CompressablePacket>());

	private PacketDecompressionThread() {

	}

	public static void startThread() {
		instance = new PacketDecompressionThread();
		instance.start();
	}

	public static void endThread() {
		if (instance == null) {
			return;
		}
		instance.interrupt();
		try {
			instance.join();
		} catch (InterruptedException ie) {
		}
		instance = null;
	}

	public static PacketDecompressionThread getInstance() {
		return instance;
	}

	public static void add(CompressablePacket packet) {
		if (instance != null) {
			instance.queue.add(packet);
		}
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				CompressablePacket packet = queue.take();
				packet.decompress();
				synchronized (decompressed) {
					decompressed.add(packet);
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public static void onTick() {
		synchronized (instance.decompressed) {
			Iterator<CompressablePacket> i = instance.decompressed.iterator();
			while (i.hasNext()) {
				SpoutPacket packet = i.next();
				try {
					packet.run(SpoutClient.getHandle().thePlayer.entityId);
					i.remove();
				} catch (Exception e) {
					System.out.println("------------------------");
					System.out.println("Unexpected Exception: " + packet.getPacketType());
					e.printStackTrace();
					System.out.println("------------------------");
				}
			}
		}
	}
}
