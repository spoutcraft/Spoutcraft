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

public class FastLocation extends FastVector implements FixedLocation {
	private final double yaw;
	private final double pitch;

	public FastLocation(int x, int y, int z, double yaw, double pitch) {
		super(x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
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

	public Vector toVector() {
		return new MutableVector(x, y, z);
	}
}
