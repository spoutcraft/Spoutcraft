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
package org.spoutcraft.api.block.design;

import java.util.ArrayList;
import java.util.List;

public class Texture {
	public String texture;
	public String addon;
	public int width;
	public int height;
	public int spriteSize;

	public List<SubTexture> subTextures;

	public Texture(String addon, String texture, int width, int height, int spriteSize) {
		this.texture = texture;
		this.addon = addon;
		this.width = width;
		this.height = height;
		this.spriteSize = spriteSize;

		int amount = (width / spriteSize) * (height / spriteSize);

		subTextures = new ArrayList<SubTexture>(amount);

		int count = 0;
		for (int y = (height / spriteSize) - 1; y >= 0; y--) {
			for (int x = 0; x < width / spriteSize; x++) {
				subTextures.add(count, new SubTexture(this, x * spriteSize, y * spriteSize, spriteSize));
				count++;
			}
		}
	}

	public SubTexture getSubTexture(int textureId) {
		return subTextures.get(textureId);
	}

	public String getTexture() {
		return texture;
	}

	public int getSpriteSize() {
		return spriteSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getAddon() {
		return addon;
	}
}
