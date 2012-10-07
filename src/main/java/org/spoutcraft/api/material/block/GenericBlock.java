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
package org.spoutcraft.api.material.block;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.block.design.BlockDesign;
import org.spoutcraft.api.material.Block;

public class GenericBlock implements Block{
	private final int id;
	private final int data;
	private final boolean subtypes;
	private final String name;
	private String customName;
	private BlockDesign design;

	private GenericBlock(String name, int id, int data, boolean subtypes) {
		this.name = name;
		this.id = id;
		this.data = data;
		this.subtypes = subtypes;
	}

	protected GenericBlock(String name, int id, int data) {
		this(name, id, data, true);
	}

	protected GenericBlock(String name, int id) {
		this(name, id, 0, false);
	}

	public int getRawId() {
		return id;
	}

	public int getRawData() {
		return data;
	}

	public boolean hasSubtypes() {
		return subtypes;
	}

	public String getName() {
		if (customName != null) {
			return customName;
		}
		return name;
	}

	public String getNotchianName() {
		return name;
	}

	public void setName(String name) {
		this.customName = name;
	}

	public float getFriction() {
		return Spoutcraft.getClient().getMaterialManager().getFriction(this);
	}

	public Block setFriction(float friction) {
		Spoutcraft.getClient().getMaterialManager().setFriction(this, friction);
		return this;
	}

	public float getHardness() {
		return Spoutcraft.getClient().getMaterialManager().getHardness(this);
	}

	public Block setHardness(float hardness) {
		Spoutcraft.getClient().getMaterialManager().setHardness(this, hardness);
		return this;
	}

	public boolean isOpaque() {
		return Spoutcraft.getClient().getMaterialManager().isOpaque(this);
	}

	public Block setOpaque(boolean opaque) {
		Spoutcraft.getClient().getMaterialManager().setOpaque(this, opaque);
		return this;
	}

	public int getLightLevel() {
		return Spoutcraft.getClient().getMaterialManager().getLightLevel(this);
	}

	public Block setLightLevel(int level) {
		Spoutcraft.getClient().getMaterialManager().setLightLevel(this, level);
		return this;
	}

	public BlockDesign getBlockDesign() {
		return design;
	}

	public Block setBlockDesign(BlockDesign design) {
		this.design = design;
		return this;
	}
}
