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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MutableLocation extends MutableVector implements Location {
	private double pitch;
	private double yaw;

	public MutableLocation() {
		pitch = yaw = 0D;
	}

	/**
	 * Constructs a new Location with the given coordinates
	 *
	 * @param world The world in which this location resides
	 * @param x The x-coordinate of this new location
	 * @param y The y-coordinate of this new location
	 * @param z The z-coordinate of this new location
	 */
	public MutableLocation(final double x, final double y, final double z) {
		this(x, y, z, 0, 0);
	}

	/**
	 * Constructs a new Location with the given coordinates and direction
	 *
	 * @param world The world in which this location resides
	 * @param x The x-coordinate of this new location
	 * @param y The y-coordinate of this new location
	 * @param z The z-coordinate of this new location
	 * @param yaw The absolute rotation on the x-plane, in degrees
	 * @param pitch The absolute rotation on the y-plane, in degrees
	 */
	public MutableLocation(final double x, final double y, final double z, final double yaw, final double pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public Location setYaw(double yaw) {
		this.yaw = yaw;
		return this;
	}

	public Location setPitch(double pitch) {
		this.pitch = pitch;
		return this;
	}

	public Vector getDirection() {
		Vector vector = new MutableVector();

		double rotX = this.getYaw();
		double rotY = this.getPitch();

		vector.setY(-Math.sin(Math.toRadians(rotY)));

		double h = Math.cos(Math.toRadians(rotY));

		vector.setX(-h * Math.sin(Math.toRadians(rotX)));
		vector.setZ(h * Math.cos(Math.toRadians(rotX)));

		return vector;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location other = (Location) obj;
			return (new EqualsBuilder()).append(getX(), other.getX()).append(getY(), other.getY()).append(getZ(), other.getZ()).append(getYaw(), other.getYaw()).append(getPitch(), other.getPitch()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(getX()).append(getY()).append(getZ()).append(getYaw()).append(getPitch()).toHashCode();
	}

	public Vector toVector() {
		return new MutableVector(x, y, z);
	}
}
