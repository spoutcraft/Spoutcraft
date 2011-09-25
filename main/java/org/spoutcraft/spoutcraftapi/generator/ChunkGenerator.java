/*
 * This file is part of Bukkit (http://bukkit.org/).
 * 
 * Bukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

/**
 * A chunk generator is responsible for the initial shaping of an entire chunk.
 * For example, the nether chunk generator should shape netherrack and soulsand
 */
public abstract class ChunkGenerator {
	/**
	 * Shapes the chunk for the given coordinates.<br />
	 * <br />
	 * This method should return a byte[32768] in the following format:
	 * <pre>
	 * for (int x = 0; x < 16; x++) {
	 *	 for (int z = 0; z < 16; z++) {
	 *		 for (int y = 0; y < 128; y++) {
	 *			 // result[(x * 16 + z) * 128 + y] = ??;
	 *		 }
	 *	 }
	 * }
	 * </pre>
	 *
	 * Note that this method should <b>never</b> attempt to get the Chunk at
	 * the passed coordinates, as doing so may cause an infinite loop
	 *
	 * @param world The world this chunk will be used for
	 * @param random The random generator to use
	 * @param x The X-coordinate of the chunk
	 * @param z The Z-coordinate of the chunk
	 * @return byte[] containing the types for each block created by this generator
	 */
	public abstract byte[] generate(World world, Random random, int x, int z);

	/**
	 * Tests if the specified location is valid for a natural spawn position
	 *
	 * @param world The world we're testing on
	 * @param x X-coordinate of the block to test
	 * @param z Z-coordinate of the block to test
	 * @return true if the location is valid, otherwise false
	 */
	public boolean canSpawn(World world, int x, int z) {
		Block highest = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);

		switch (world.getEnvironment()) {
			case NETHER:
				return true;
			case SKYLANDS:
				return highest.getType() != MaterialData.air
						&& highest.getType() != MaterialData.water
						&& highest.getType() != MaterialData.lava;
			case NORMAL:
			default:
				return highest.getType() == MaterialData.sand
						|| highest.getType() == MaterialData.gravel;
		}
	}

	/**
	 * Gets a list of default {@link BlockPopulator}s to apply to a given world
	 *
	 * @param world World to apply to
	 * @return List containing any amount of BlockPopulators
	 */
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return new ArrayList<BlockPopulator>();
	}

	/**
	 * Gets a fixed spawn location to use for a given world.
	 *
	 * A null value is returned if a world should not use a fixed spawn point,
	 * and will instead attempt to find one randomly.
	 *
	 * @param world The world to locate a spawn point for
	 * @param random Random generator to use in the calculation
	 * @return Location containing a new spawn point, otherwise null
	 */
	public FixedLocation getFixedSpawnLocation(World world, Random random) {
		return null;
	}
}