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

import net.minecraft.src.EntityPigZombie;

import org.spoutcraft.api.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {
	public CraftPigZombie(EntityPigZombie entity) {
		super(entity);
	}

	public EntityPigZombie getHandle() {
		return (EntityPigZombie)handle;
	}

	@Override
	public String toString() {
		return "CraftPigZombie";
	}

	public int getAnger() {
		return getHandle().angerLevel;
	}

	public void setAnger(int level) {
		getHandle().angerLevel = level;
	}

	public void setAngry(boolean angry) {
		setAnger(angry ? 400 : 0);
	}

	public boolean isAngry() {
		return getAnger() > 0;
	}
}
