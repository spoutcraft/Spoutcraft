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
package org.spoutcraft.client.entity;

import org.spoutcraft.api.property.Property;
import org.spoutcraft.api.util.FixedLocation;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.Vector;

public class CraftCameraEntity extends CraftEntity  {
	public CraftCameraEntity(FixedLocation location) {
		super(location);
		handle = new EntityCamera(this);
		teleport(location);
		addProperty("location", new Property() {
			public void set(Object value) {
				teleport((Location)value);
			}
			public Object get() {
				return getLocation();
			}
		});
		addProperty("velocity", new Property() {
			public void set(Object value) {
				setVelocity((Vector)value);
			}
			public Object get() {
				return getVelocity();
			}
		});
	}

	@Override
	public boolean teleport(FixedLocation location) {
		handle.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), (float)location.getYaw(), (float)location.getPitch());
		((EntityCamera)handle).setRotation((float)location.getYaw(), (float)location.getPitch());
		return true;
	}

	public EntityCamera getHandle() {
		return (EntityCamera) handle;
	}
}
