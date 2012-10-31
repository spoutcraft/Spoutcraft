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
package org.spoutcraft.api.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.api.io.SpoutInputStream;

public abstract class PacketUtil {
	public static int[] readIntArray(SpoutInputStream input) throws IOException {
		int length = input.readInt();
		if (length > 256) {
			throw new IllegalArgumentException("Int array exceeded max length (" + length + ")");
		}
		int[] newArray = new int[length];
		for (int i = 0; i < length; i++) {
			newArray[i] = input.readInt();
		}
		return newArray;
	}

	public static float[] readQuadFloat(SpoutInputStream input) throws IOException {
		float[] newArray = new float[4];
		for (int i = 0; i < 4; i++) {
			newArray[i] = input.readFloat();
		}
		return newArray;
	}

	public static int getDoubleArrayLength(float[][] doubleArray) {
		return doubleArray.length * 16;
	}

	public static float[][] readDoubleArray(SpoutInputStream input) throws IOException {
		int length = input.readShort();
		if (length > 256) {
			throw new IllegalArgumentException("Double array exceeded max length (" + length + ")");
		}
		float[][] newDoubleArray = new float[length][];
		for (int i = 0; i < length; i++) {
			newDoubleArray[i] = readQuadFloat(input);
		}
		return newDoubleArray;
	}

	public static void writeIntArray(DataOutputStream output, int[] ints) throws IOException {
		if (ints.length > 256) {
			throw new IllegalArgumentException("Array containing " + ints.length + " ints passed to writeQuadFloat");
		}
		output.writeInt(ints.length);
		for (int i = 0; i < ints.length; i++) {
			output.writeInt(ints[i]);
		}
	}
}
