/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagEnd;
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

	public net.minecraft.src.ItemStack readItemStack() throws IOException {
		final int id = readInt();
		net.minecraft.src.ItemStack nmsStack = null;

		if (id >= 0) {
			final byte amount = (byte) read();
			final short damage = readShort();
			nmsStack = new net.minecraft.src.ItemStack(id, amount, damage);
			nmsStack.setTagCompound(readNBTTagCompound());
		}

		return nmsStack;
	}

	public Material readMaterial() {
		return MaterialData.getMaterial(readInt(), readShort());
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

	public NBTTagCompound readNBTTagCompound() throws IOException {
		final short len = readShort();
		final byte[] compressed = new byte[len];
		read(compressed);
		final NBTBase tag = decompress(compressed);
		if (tag instanceof NBTTagCompound) {
			return (NBTTagCompound) tag;
		} else {
			throw new IOException("Attempt to get NBTTagCompound but the tag's class is " + tag.getClass().getSimpleName());
		}
	}


	private NBTBase decompress(byte[] compressed) throws IOException {
		try (DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(compressed))))) {
			final byte typeId = datainputstream.readByte();
			if (typeId == 0) {
				return new NBTTagEnd();
			}
			readString();
			// TODO: Check this
			NBTBase found = NBTBase.newTag(typeId, "");
			if (found == null) {
				throw new IOException("NBTTag sent from client does not exist on this server (hack?)!");
			}
			final Method nbtLoad = found.getClass().getDeclaredMethod("load", new Class[] {DataInput.class, Integer.class});
			nbtLoad.setAccessible(true);
			nbtLoad.invoke(found, datainputstream, 0);
			return found;
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
