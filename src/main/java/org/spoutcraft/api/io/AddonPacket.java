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
package org.spoutcraft.api.io;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.spoutcraft.api.Spoutcraft;

public abstract class AddonPacket {
	private static HashMap<String, Class<? extends AddonPacket>> packets = new HashMap<String, Class<? extends AddonPacket>>();
	private static HashMap<Class<? extends AddonPacket>, String> packetsIds = new HashMap<Class<? extends AddonPacket>, String>();

	public AddonPacket() {
	}

	public abstract void run();

	public abstract void read(SpoutInputStream input);

	public abstract void write(SpoutOutputStream output);

	public final void send() {
		Spoutcraft.send(this);
	}

	@SuppressWarnings("unchecked")
	public static void register(Class<? extends AddonPacket> packet, String uniqueId) {
		boolean emptyConstructor = false;
		Constructor<? extends AddonPacket>[] constructors = (Constructor<? extends AddonPacket>[]) packet.getConstructors();
		for (Constructor<? extends AddonPacket> c : constructors) {
			if (c.getGenericParameterTypes().length == 0) {
				emptyConstructor = true;
				break;
			}
		}
		if (!emptyConstructor) {
			throw new IllegalArgumentException("Any AddonPacket Must Provide An Empty Constructor");
		}


		packets.put(uniqueId, packet);
		packetsIds.put(packet, uniqueId);
	}

	public static String getPacketId(Class<? extends AddonPacket> packet) {
		return packetsIds.get(packet);
	}

	public static Class<? extends AddonPacket> getPacketFromId(String id) {
		return packets.get(id);
	}
}
