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

import java.util.Random;

import net.minecraft.src.BlockDispenser;
import net.minecraft.src.TileEntityDispenser;

import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.Dispenser;
import org.spoutcraft.api.inventory.Inventory;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.inventory.CraftInventory;

public class CraftDispenser extends CraftBlockState implements Dispenser {
	private final SpoutcraftWorld world;
	private final TileEntityDispenser dispenser;

	public CraftDispenser(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		dispenser = (TileEntityDispenser) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Inventory getInventory() {
		return new CraftInventory(dispenser);
	}

	public boolean dispense() {
		Block block = getBlock();

		synchronized (block) {
			if (block.getType() == MaterialData.dispenser) {
				BlockDispenser dispense = (BlockDispenser) net.minecraft.src.Block.dispenser;

				dispense.randomDisplayTick(world.getHandle(), getX(), getY(), getZ(), new Random());
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean update(boolean force) {
		boolean result = super.update(force);

		if (result) {
			dispenser.updateEntity();
		}

		return result;
	}
}
