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
package org.spoutcraft.api;

import org.spoutcraft.api.animation.Animatable;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.MutableLocation;

public class AnimatableLocation extends MutableLocation implements Animatable, Location {
	public AnimatableLocation(double x, double y, double z) {
		super(x, y, z, 0, 0);
	}

	public AnimatableLocation(double x, double y, double z, double yaw, double pitch) {
		super(x, y, z, yaw, pitch);
	}

	public Animatable getValueAt(double p, Animatable startValue, Animatable endValue) {
		Location p1 = (Location) startValue;
		Location p2 = (Location) endValue;
		double x = p1.getX(), y = p1.getY(), z = p1.getZ();
		double yaw = p1.getYaw(), pitch = p1.getPitch();
		x += (p1.getX() - p2.getX()) * p;
		y += (p1.getY() - p2.getY()) * p;
		z += (p1.getZ() - p2.getZ()) * p;
		yaw += (p1.getYaw() - p2.getYaw()) * p;
		pitch += (p1.getPitch() - p2.getPitch()) * p;

		return new AnimatableLocation(x, y, z, yaw, pitch);
	}
}
