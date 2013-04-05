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
package org.spoutcraft.api.material.block;

import org.spoutcraft.api.block.design.GenericCubeBlockDesign;

public abstract class GenericCubeCustomBlock extends GenericCustomBlock {
	/**
	 * Creates a new cube block material
	 *
	 * @param plugin making the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param design to use for the block
	 */
	public GenericCubeCustomBlock(String addon, String name, boolean isOpaque, GenericCubeBlockDesign design) {
		super(addon, name, isOpaque, design);
	}

	/**
	 * Creates a new opaque/solid cube block material
	 *
	 * @param plugin making the block
	 * @param name of the block
	 * @param design to use for the block
	 */
	public GenericCubeCustomBlock(String addon, String name, GenericCubeBlockDesign design) {
		super(addon, name);
		this.setBlockDesign(design);
	}

	/**
	 * Creates a new basic opaque/solid cube block material
	 *
	 * @param plugin making the block
	 * @param name of the block
	 * @param texture url to use for the block - must be a square PNG
	 * @param textureSize width and height of the texture in pixels
	 */
	public GenericCubeCustomBlock(String addon, String name, String texture, int textureSize) {
		super(addon, name);
		this.setBlockDesign(new GenericCubeBlockDesign(addon, texture, textureSize));
	}
}
