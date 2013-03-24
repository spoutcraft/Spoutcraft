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
package org.spoutcraft.api.material.item;

import java.io.IOException;

import gnu.trove.map.hash.TObjectFloatHashMap;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.material.Block;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.material.Tool;

public class GenericCustomTool extends GenericCustomItem implements Tool{
	private short durability = 1;
	private TObjectFloatHashMap<Block> strengthMods = new TObjectFloatHashMap<Block>();

	public GenericCustomTool() {
		super();
	}

	public GenericCustomTool(String addon, String name, String texture) {
		super(addon, name, texture);
	}

	public short getDurability() {
		return durability;
	}

	public Tool setDurability(short durability) {
		this.durability = durability;
		return this;
	}

	public float getStrengthModifier(Block block) {
		if (strengthMods.contains(block)) {
			return strengthMods.get(block);
		}
		return 1.0F;
	}

	public Tool setStrengthModifier(Block block, float modifier) {
		strengthMods.put(block, modifier);
		return this;
	}

	public Block[] getStrengthModifiedBlocks() {
		return (Block[]) strengthMods.keys();
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setDurability(input.readShort());
		short size = input.readShort();
		for (int i = 0; i < size; i++) {
			int id = input.readInt();
			int data = input.readShort();
			float mod = input.readFloat();
			Block block = MaterialData.getBlock(id, (short) data);
			if (data == -1) {
				block = MaterialData.getCustomBlock(id);
			}
			setStrengthModifier(block, mod);
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeShort(getDurability());
		Block[] mod = getStrengthModifiedBlocks();
		output.writeShort((short) mod.length);
		for (int i = 0; i < mod.length; i++) {
			Block block =  mod[i];
			if (block instanceof CustomBlock) {
				output.writeInt(((CustomBlock)block).getCustomId());
				output.writeShort((short) -1);
			} else {
				output.writeInt(block.getRawId());
				output.writeShort((short) block.getRawData());
			}
			output.writeFloat(getStrengthModifier(block));
		}
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 0;
	}
}
