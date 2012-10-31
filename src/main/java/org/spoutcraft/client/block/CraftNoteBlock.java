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

import net.minecraft.src.TileEntityNote;

import org.spoutcraft.api.Instrument;
import org.spoutcraft.api.Note;
import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.NoteBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutcraftWorld;

public class CraftNoteBlock extends CraftBlockState implements NoteBlock {
	private final SpoutcraftWorld world;
	private final TileEntityNote note;

	public CraftNoteBlock(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		note = (TileEntityNote) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Note getNote() {
		return new Note(note.note);
	}

	public byte getRawNote() {
		return note.note;
	}

	public void setNote(Note n) {
		note.note = n.getId();
	}

	public void setRawNote(byte n) {
		note.note = n;
	}

	public boolean play() {
		Block block = getBlock();

		synchronized (block) {
			if (block.getType() == MaterialData.noteblock) {
				note.triggerNote(world.getHandle(), getX(), getY(), getZ());
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean play(byte instrument, byte note) {
		Block block = getBlock();

		synchronized (block) {
			if (block.getType() == MaterialData.noteblock) {
				world.getHandle().playAuxSFX(getX(), getY(), getZ(), instrument, note);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean play(Instrument instrument, Note note) {
		Block block = getBlock();

		synchronized (block) {
			if (block.getType() == MaterialData.noteblock) {
				world.getHandle().playAuxSFX(getX(), getY(), getZ(), instrument.getType(), note.getId());
				return true;
			} else {
				return false;
			}
		}
	}
}
