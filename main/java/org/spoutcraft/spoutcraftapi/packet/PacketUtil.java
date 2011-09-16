/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.spoutcraft.spoutcraftapi.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.gui.Color;

public abstract class PacketUtil {
	public static final int maxString = 32767;
	public static final byte FLAG_COLORINVALID = 1;
	public static final byte FLAG_COLOROVERRIDE = 2;
	
	public static void writeString(DataOutputStream output, String s) {
		try {
			output.writeShort(s.length());
			output.writeChars(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readString(DataInputStream input) {
		return readString(input, maxString);
	}
	
	public static int getNumBytes(String str) {
		if (str != null) {
			return 2 + str.length() * 2;
		}
		return 2;
	}
	
	public static String readString(DataInputStream input, int maxSize) {
		try {
			short size = input.readShort();

			if (size > maxSize) {
				throw new IOException("Received string length longer than maximum allowed (" + size + " > " + maxSize + ")");
			} else if (size < 0) {
				throw new IOException("Received string length is less than zero! Weird string!");
			} else {
				StringBuilder stringbuilder = new StringBuilder();

				for (int j = 0; j < size; ++j) {
					stringbuilder.append(input.readChar());
				}

				return stringbuilder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeColor(DataOutputStream output, Color color) {
		try {
			byte flags = 0x0;
			
			if (color.getRedF() == -1F)
				flags |= FLAG_COLORINVALID;
			else if (color.getRedF() == -2F)
				flags |= FLAG_COLOROVERRIDE;
			
			output.writeByte(flags);
			output.writeInt(color.toInt());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Color readColor(DataInputStream input) {
		try {
			byte flags = input.readByte();
			int argb = input.readInt();
			
			if ((flags & FLAG_COLORINVALID) > 0)
				return Color.invalid();
			if ((flags & FLAG_COLOROVERRIDE) > 0)
				return Color.override();
			
			return new Color(argb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
