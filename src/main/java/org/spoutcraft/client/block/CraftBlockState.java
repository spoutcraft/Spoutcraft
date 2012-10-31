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
package org.spoutcraft.client.block;

import org.spoutcraft.api.World;
import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.BlockState;
import org.spoutcraft.api.block.Chunk;
import org.spoutcraft.api.util.FastLocation;
import org.spoutcraft.api.util.FixedLocation;
import org.spoutcraft.client.SpoutcraftWorld;

public class CraftBlockState implements BlockState {
	private final SpoutcraftWorld world;
	private final SpoutcraftChunk chunk;
	private final int x;
	private final int y;
	private final int z;
	protected int type;
	protected byte light;
	protected FixedLocation location;

	public CraftBlockState(final Block block) {
		this.world = (SpoutcraftWorld) block.getWorld();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.type = block.getTypeId();
		this.light = block.getLightLevel();
		this.chunk = (SpoutcraftChunk) block.getChunk();
		this.location = new FastLocation(x, y, z, 0, 0, world);
	}

	/**
	 * Gets the world which contains this Block
	 *
	 * @return World containing this block
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the x-coordinate of this block
	 *
	 * @return x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y-coordinate of this block
	 *
	 * @return y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z-coordinate of this block
	 *
	 * @return z-coordinate
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Gets the chunk which contains this block
	 *
	 * @return Containing Chunk
	 */
	public Chunk getChunk() {
		return chunk;
	}

	/**
	 * Gets the type-id of this block
	 *
	 * @return block type-id
	 */
	public int getTypeId() {
		return type;
	}

	/**
	 * Gets the light level between 0-15
	 *
	 * @return light level
	 */
	public byte getLightLevel() {
		return light;
	}

	public Block getBlock() {
		return world.getBlockAt(x, y, z);
	}

	public boolean update() {
		return update(false);
	}

	public boolean update(boolean force) {
		Block block = getBlock();

		synchronized (block) {
			if (block.getTypeId() != type) {
				if (force) {
					type = block.getTypeId();
					block.setTypeId(this.getTypeId());
				} else {
					return false;
				}
			}
		}

		return true;
	}

	public FixedLocation getLocation() {
		return location;
	}
}
