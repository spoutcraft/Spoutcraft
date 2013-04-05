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
package org.spoutcraft.api.util;

public interface FixedLocation extends FixedVector {
	/**
	 * Gets the yaw of this location
	 *
	 * @return Yaw
	 */
	public double getYaw();

	/**
	 * Gets the pitch of this location
	 *
	 * @return Pitch
	 */
	public double getPitch();

	/**
	 * Gets a Vector pointing in the direction that this Location is facing
	 *
	 * @return Vector
	 */
	public Vector getDirection();

	/**
	 * Creates a vector with the properties of this location
	 * @return vector
	 */
	public Vector toVector();
}
