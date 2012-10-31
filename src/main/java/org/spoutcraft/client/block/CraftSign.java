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

import net.minecraft.src.TileEntitySign;

import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.Sign;
import org.spoutcraft.client.SpoutcraftWorld;

public class CraftSign extends CraftBlockState implements Sign {
	private final SpoutcraftWorld world;
	private final TileEntitySign sign;

	public CraftSign(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		sign = (TileEntitySign) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public String[] getLines() {
		return sign.signText;
	}

	public String getLine(int index) throws IndexOutOfBoundsException {
		return sign.signText[index];
	}

	public void setLine(int index, String line) throws IndexOutOfBoundsException {
		sign.signText[index] = line;
	}

	@Override
	public boolean update(boolean force) {
		boolean result = super.update(force);

		if (result) {
			sign.updateEntity();
		}

		return result;
	}
}
