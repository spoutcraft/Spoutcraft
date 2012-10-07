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
package org.spoutcraft.client.inventory;

import net.minecraft.src.IInventory;

import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.BlockFace;
import org.spoutcraft.api.inventory.DoubleChestInventory;

public class CraftDoubleInventory extends CraftInventory implements DoubleChestInventory {
	protected Block top;
	protected Block bottom;
	public CraftDoubleInventory(IInventory inventory, Block top, Block bottom) {
		super(inventory);
		this.top = top;
		this.bottom = bottom;
	}

	public Block getTopHalf() {
		return top;
	}

	public Block getBottomHalf() {
		return bottom;
	}

	public Block getLeftSide() {
		if ((this.getDirection() == BlockFace.WEST) || (this.getDirection() == BlockFace.NORTH)) {
			return top;
		} else {
			return bottom;
		}
	}

	public Block getRightSide() {
		if (this.getLeftSide().equals(top)) {
			return bottom;
		} else {
			return top;
		}
	}

	public BlockFace getDirection() {
		if (top.getLocation().getBlockX() == bottom.getLocation().getBlockX()) {
			return this.isReversed(BlockFace.SOUTH) ? BlockFace.NORTH : BlockFace.SOUTH;
		} else {
			return this.isReversed(BlockFace.WEST) ? BlockFace.EAST : BlockFace.WEST;
		}
	}

	private boolean isReversed(BlockFace primary) {
		BlockFace secondary = primary.getOppositeFace();
		if (isSolid(top.getRelative(secondary)) || isSolid(bottom.getRelative(secondary))) {
			return false;
		} else {
			return isSolid(top.getRelative(primary)) || isSolid(bottom.getRelative(primary));
		}
	}

	private static boolean isSolid(Block block) {
		// o[]: If block type is completely solid.
		// This should really be part of Spout or Bukkit, but for now it's here.
		return net.minecraft.src.Block.opaqueCubeLookup[block.getTypeId()];
	}
}
