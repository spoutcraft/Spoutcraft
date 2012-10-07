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
package org.spoutcraft.api.gui;

import java.nio.IntBuffer;

/**
 * You must specify the width and height of the background bitmap to be used in
 * the constructor!
 */
public interface Bitmap {
	/**
	 * Get the raw RGBA array used to create the texture.
	 * @return an array of width * height * 4 bytes
	 */
	public byte[] getRawBitmap();

	/**
	 * Get the raw width of the bitmap (the widget itself can be scaled to a
	 * different size like normal).
	 * @return bitmap size
	 */
	public int getRawWidth();

	/**
	 * Get the raw height of the bitmap (the widget itself can be scaled to a
	 * different size like normal).
	 * @return bitmap size
	 */
	public int getRawHeight();

	/**
	 * Get an IntBuffer to use for setting individual pixels in single calls.
	 * @return the buffer
	 */
	public IntBuffer getBuffer();
}
