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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.spoutcraft.api.Spoutcraft;

public class GenericBitmap extends GenericTexture implements Bitmap {
	protected final byte[] bitmap;
	protected final ByteBuffer buffer;
	protected final int bitmapWidth;
	protected final int bitmapHeight;

	public GenericBitmap(int width, int height) {
		bitmapWidth = width;
		bitmapHeight = height;
		bitmap = new byte[width * height * 4];
		buffer = ByteBuffer.wrap(bitmap);
		super.setWidth(width);
		super.setHeight(height);
	}

	public byte[] getRawBitmap() {
		return bitmap;
	}

	public int getRawWidth() {
		return bitmapWidth;
	}

	public int getRawHeight() {
		return bitmapHeight;
	}

	public ByteBuffer getByteBuffer() {
		return buffer;
	}

	public IntBuffer getBuffer() {
		return buffer.asIntBuffer();
	}

	@Override
	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}
}
