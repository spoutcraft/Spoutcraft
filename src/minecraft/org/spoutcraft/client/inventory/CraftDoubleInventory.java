/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.inventory;

import net.minecraft.src.IInventory;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockFace;
import org.spoutcraft.spoutcraftapi.inventory.DoubleChestInventory;

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
