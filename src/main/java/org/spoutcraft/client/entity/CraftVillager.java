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
package org.spoutcraft.client.entity;

import net.minecraft.src.EntityVillager;

import org.spoutcraft.api.entity.Villager;

public class CraftVillager extends CraftCreature implements Villager {
	public CraftVillager(EntityVillager entity) {
		super(entity);
	}

	@Override
	public String toString() {
		return "CraftVillager";
	}

	public Occupation getOccupation() {
		switch(((EntityVillager)handle).getProfession()) {
			case 0: return Occupation.FARMER;
			case 1: return Occupation.LIBRARIAN;
			case 2: return Occupation.PRIEST;
			case 3: return Occupation.BLACKSMITH;
			case 4: return Occupation.BUTCHER;
			default: return Occupation.VILLAGER;
		}
	}
}
