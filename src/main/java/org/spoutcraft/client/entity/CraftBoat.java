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

import net.minecraft.src.EntityBoat;

import org.spoutcraft.api.entity.Boat;

public class CraftBoat extends CraftVehicle implements Boat {
	protected EntityBoat boat;

	public CraftBoat(EntityBoat entity) {
		super(entity);
		boat = entity;
	}

	public double getMaxSpeed() {
		return boat.maxSpeed;
	}

	public void setMaxSpeed(double speed) {
		if (speed >= 0D) {
			boat.maxSpeed = speed;
		}
	}

	public double getOccupiedDeceleration() {
		return boat.occupiedDeceleration;
	}

	public void setOccupiedDeceleration(double speed) {
		if (speed >= 0D) {
			boat.occupiedDeceleration = speed;
		}
	}

	public double getUnoccupiedDeceleration() {
		return boat.unoccupiedDeceleration;
	}

	public void setUnoccupiedDeceleration(double speed) {
		boat.unoccupiedDeceleration = speed;
	}

	public boolean getWorkOnLand() {
		return boat.landBoats;
	}

	public void setWorkOnLand(boolean workOnLand) {
		boat.landBoats = workOnLand;
	}

	@Override
	public String toString() {
		return "CraftBoat";
	}
}
