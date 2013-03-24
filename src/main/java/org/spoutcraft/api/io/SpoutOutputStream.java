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

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.material.Material;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.Vector;

public class SpoutOutputStream extends OutputStream{
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	public SpoutOutputStream() {
	}

	public void writeLocation(Location location) {
		this.writeDouble(location.getX());
		this.writeDouble(location.getY());
		this.writeDouble(location.getZ());
		this.writeFloat((float) location.getPitch());
		this.writeFloat((float) location.getYaw());
		this.writeLong(0);
		this.writeLong(0);
	}

	public void writeVector(Vector vector) {
		this.writeDouble(vector.getX());
		this.writeDouble(vector.getY());
		this.writeDouble(vector.getZ());
	}

	public void writeItemStack(ItemStack item) {
		this.writeInt(item.getTypeId());
		this.writeShort(item.getDurability());
		this.writeShort((short)item.getAmount());
	}

	public void writeMaterial(Material material) {
		this.writeInt(material.getRawId());
		this.writeShort((short) material.getRawData());
	}

	public void writeUUID(UUID uuid) {
		this.writeLong(uuid.getLeastSignificantBits());
		this.writeLong(uuid.getMostSignificantBits());
	}

	@Override
	public void write(byte[] b) {
		while (buffer.remaining() < b.length) {
			expand();
		}
		buffer.put(b);
	}

	@Override
	public void write(byte[] b, int len, int off) {
		while (buffer.remaining() < b.length) {
			expand();
		}
		buffer.put(b, len, off);
	}

	@Override
	public void write(int b) {
		if (buffer.remaining() < 1) {
			expand();
		}
		buffer.put((byte)b);
	}

	public void writeShort(short s) {
		if (buffer.remaining() < 2) {
			expand();
		}
		buffer.putShort(s);
	}

	public void writeInt(int i) {
		if (buffer.remaining() < 4) {
			expand();
		}
		buffer.putInt(i);
	}

	public void writeLong(long l) {
		if (buffer.remaining() < 8) {
			expand();
		}
		buffer.putLong(l);
	}

	public void writeFloat(float f) {
		if (buffer.remaining() < 4) {
			expand();
		}
		buffer.putFloat(f);
	}

	public void writeDouble(double d) {
		if (buffer.remaining() < 8) {
			expand();
		}
		buffer.putDouble(d);
	}

	public void writeChar(char ch) {
		if (buffer.remaining() < 2) {
			expand();
		}
		buffer.putChar(ch);
	}

	public void writeString(String s) {
		while (buffer.remaining() < (2 + s.length() * 2)) {
			expand();
		}
		buffer.putShort((short) s.length());
		for (int i = 0; i < s.length(); i++) {
			buffer.putChar(s.charAt(i));
		}
	}

	public void writeBoolean(boolean b) {
		write(b ? 1 : 0);
	}

	public static final byte FLAG_COLORINVALID = 1;
	public static final byte FLAG_COLOROVERRIDE = 2;
	public void writeColor(Color c) {
		byte flags = 0x0;

		if (c.getRedF() == -1F) {
			flags |= FLAG_COLORINVALID;
		} else if (c.getRedF() == -2F) {
			flags |= FLAG_COLOROVERRIDE;
		}

		write(flags);
		writeInt(c.toInt());
	}

	public ByteBuffer getRawBuffer() {
		return buffer;
	}

	private void expand() {
		ByteBuffer replacement = ByteBuffer.allocate(buffer.capacity() * 2);
		replacement.put(buffer.array());
		replacement.position(buffer.position());
		buffer = replacement;
	}
}
