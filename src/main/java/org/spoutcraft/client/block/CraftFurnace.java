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

import net.minecraft.src.TileEntityFurnace;

import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.Furnace;
import org.spoutcraft.api.inventory.Inventory;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.inventory.CraftInventory;

public class CraftFurnace extends CraftBlockState implements Furnace {
	private final SpoutcraftWorld world;
	private final TileEntityFurnace furnace;

	public CraftFurnace(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		furnace = (TileEntityFurnace) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Inventory getInventory() {
		return new CraftInventory(furnace);
	}

	@Override
	public boolean update(boolean force) {
		boolean result = super.update(force);

		if (result) {
			furnace.updateEntity();
		}

		return result;
	}

	public short getBurnTime() {
		return (short) furnace.furnaceBurnTime;
	}

	public void setBurnTime(short burnTime) {
		furnace.furnaceBurnTime = burnTime;
	}

	public short getCookTime() {
		return (short) furnace.furnaceCookTime;
	}

	public void setCookTime(short cookTime) {
		furnace.furnaceCookTime = cookTime;
	}
}
