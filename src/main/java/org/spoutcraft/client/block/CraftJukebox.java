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

import net.minecraft.src.BlockJukeBox;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityRecordPlayer;

import org.spoutcraft.api.Effect;
import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.Jukebox;
import org.spoutcraft.api.material.Item;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutcraftWorld;

public class CraftJukebox extends CraftBlockState implements Jukebox {
	private final SpoutcraftWorld world;
	private final TileEntityRecordPlayer jukebox;

	public CraftJukebox(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		jukebox = (TileEntityRecordPlayer) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Item getPlaying() {
		return MaterialData.getItem(jukebox.record.itemID);
	}

	public void setPlaying(Item record) {
		int id = 0;
		if (record != null) {
			id = record.getRawId();
		}
		jukebox.record = new ItemStack(id, 1, 0);
		jukebox.updateEntity();
		if (0 == id) {
			getBlock().setData((byte) 0);
		} else {
			getBlock().setData((byte) 1);
		}
		world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getRawId());
	}

	public boolean isPlaying() {
		return getBlock().getData() == 1;
	}

	public boolean eject() {
		boolean result = isPlaying();
		((BlockJukeBox)net.minecraft.src.Block.jukebox).ejectRecord(world.getHandle(), getX(), getY(), getZ());
		return result;
	}
}
