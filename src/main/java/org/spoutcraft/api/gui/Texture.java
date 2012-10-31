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

import org.spoutcraft.api.UnsafeClass;

@UnsafeClass
public interface Texture extends Widget {
	/**
	 * Gets the url of this texture to render
	 *
	 * @return url
	 */
	public String getUrl();

	/**
	 * Sets the url of this texture to render
	 * All textures must be of png type and a size that is a factor of 2 (e.g 64x128). Use the alpha channel for hiding empty space.
	 * @param url to set this texture to
	 * @return texture
	 */
	public Texture setUrl(String Url);

	/**
	 * Gets if the texture draws the full alpha channel instead of just using a bit-mask.
	 * @return if it's drawing the alpha channel
	 */
	public boolean isDrawingAlphaChannel();

	/**
	 * Sets if the texture should draw the full alpha channel instead of just using a bit-mask.
	 * @param draw to set the drawing state
	 * @return texture
	 */
	public Texture setDrawAlphaChannel(boolean draw);

	/**
	 * Set the offset to the top of the image.
	 * Setting this to a anything other than -1 will draw a 1:1 slice of the
	 * texture rather than scaling it to fit the width and height.
	 * @param top the top offset
	 * @return texture
	 */
	public Texture setTop(int top);

	/**
	 * Get the offset to the top of the image.
	 * @return top offset
	 */
	public int getTop();

	/**
	 * Set the offset to the left of the image.
	 * Setting this to a anything other than -1 will draw a 1:1 slice of the
	 * texture rather than scaling it to fit the width and height.
	 * @param left the left offset
	 * @return texture
	 */
	public Texture setLeft(int left);

	/**
	 * Get the offset to the left of the image.
	 * @return left offset
	 */
	public int getLeft();

	/**
	 * @see setFinishDelegate
	 * @return the finishdelegate
	 */
	public Runnable getFinishDelegate();

	/**
	 * Given deleages run()-method will be called when the download of the image has been completed.
	 * The internal delegate-variable will be set to null after that.
	 * @param finishDelegate
	 * @return instance of the texture
	 */
	public Texture setFinishDelegate(Runnable finishDelegate);

	/**
	 * @return the actual width of the image
	 */
	public int getOriginalWidth();

	/**
	 * @return the actual height of the image
	 */
	public int getOriginalHeight();

	/**
	 * True if this texture is a local texture (inside the minecraft or spoutcraft jar)
	 * @return local texture
	 */
	public boolean isLocal();

	/**
	 * Marks this as a texture that is local (inside the minecraft or spoutcraft jar)
	 * @param value to set
	 * @return this
	 */
	public Texture setLocal(boolean value);
}
