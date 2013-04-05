/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.material.Material;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.MutableLocation;
import org.spoutcraft.api.util.MutableVector;
import org.spoutcraft.api.util.Vector;

public class SpoutInputStream extends InputStream{
	ByteBuffer buffer;
	public SpoutInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@SuppressWarnings("unused")
	public Location readLocation() {
		double x = readDouble();
		double y = readDouble();
		double z = readDouble();
		float pitch = readFloat();
		float yaw = readFloat();
		long lsb = readLong();
		long msb = readLong();
		return new MutableLocation(x, y, z, yaw, pitch);
	}

	public Vector readVector() {
		double x = readDouble();
		double y = readDouble();
		double z = readDouble();
		return new MutableVector(x,y,z);
	}

	public ItemStack readItemStack(ItemStack item) {
		int id = readInt();
		short dura = readShort();
		short amt = readShort();
		return new ItemStack(id, amt, dura);
	}

	public Material readMaterial() {
		int id = readInt();
		short dura = readShort();
		return MaterialData.getMaterial(id, dura);
	}

	public UUID readUUID() {
		long lsb = readLong();
		long msb = readLong();
		return new UUID(msb, lsb);
	}

	@Override
	public int read() {
		return buffer.get();
	}

	@Override
	public int read(byte[] b, int len, int off) {
		buffer.get(b, len, off);
		return b.length;
	}

	public int read(byte[] b) {
		buffer.get(b);
		return b.length;
	}

	public short readShort() {
		return buffer.getShort();
	}

	public int readInt() {
		return buffer.getInt();
	}

	public long readLong() {
		return buffer.getLong();
	}

	public float readFloat() {
		return buffer.getFloat();
	}

	public double readDouble() {
		return buffer.getDouble();
	}

	public char readChar() {
		return buffer.getChar();
	}

	public boolean readBoolean() {
		return buffer.get() != 0;
	}

	public String readString() {
		short size =  buffer.getShort();
		StringBuilder builder = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			builder.append(buffer.getChar());
		}
		return builder.toString();
	}

	public static final byte FLAG_COLORINVALID = 1;
	public static final byte FLAG_COLOROVERRIDE = 2;
	public Color readColor() {
		int flags = read();
		int argb = readInt();
		if ((flags & FLAG_COLORINVALID) > 0) {
			return Color.invalid();
		}
		if ((flags & FLAG_COLOROVERRIDE) > 0) {
			return Color.override();
		}
		return new Color(argb);
	}

	public ByteBuffer getRawBuffer() {
		return buffer;
	}
}
