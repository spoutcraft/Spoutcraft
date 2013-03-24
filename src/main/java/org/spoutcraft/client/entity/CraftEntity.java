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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.spoutcraft.api.entity.EntitySkinType;
import org.spoutcraft.api.property.PropertyObject;
import org.spoutcraft.api.property.Property;
import org.spoutcraft.api.util.FixedLocation;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.MutableLocation;
import org.spoutcraft.api.util.MutableVector;
import org.spoutcraft.api.util.Vector;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.player.SpoutPlayer;

public class CraftEntity extends PropertyObject{
	protected net.minecraft.src.Entity handle = null;

	public CraftEntity() {
	}

	public CraftEntity(FixedLocation location) {
	}

	public CraftEntity(net.minecraft.src.Entity handle) {
		this.handle = handle;
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

	public Location getLocation() {
		return new MutableLocation(handle.posX,handle.posY,handle.posZ,handle.rotationYaw,handle.rotationPitch);
	}

	public void setVelocity(Vector velocity) {
		handle.motionX = velocity.getX();
		handle.motionY = velocity.getY();
		handle.motionZ = velocity.getZ();
	}

	public Vector getVelocity() {
		return new MutableVector(handle.motionX, handle.motionY, handle.motionZ);
	}

	public SpoutcraftWorld getWorld() {
		return handle.worldObj.world;
	}

	public boolean teleport(FixedLocation location) {
		handle.setPosition(location.getX(), location.getY(), location.getZ());
		handle.setAngles((float)location.getYaw(), (float)location.getPitch());
		return true;
	}

	public boolean teleport(CraftEntity destination) {
		return teleport(destination.getLocation());
	}

	public Set<CraftEntity> getNearbyEntities(double x, double y, double z) {
		List<net.minecraft.src.Entity> notchEntityList = handle.worldObj.getEntitiesWithinAABBExcludingEntity(handle, handle.boundingBox.expand(x, y, z));
		Set<CraftEntity> entities = new HashSet<CraftEntity>(notchEntityList.size());

		for (net.minecraft.src.Entity e: notchEntityList) {
			entities.add(e.spoutEnty);
		}
		return entities;
	}

	public int getEntityId() {
		return handle.entityId;
	}

	public int getFireTicks() {
		return handle.fire;
	}

	public int getMaxFireTicks() {
		return handle.fire;
	}

	public void setFireTicks(int ticks) {
		handle.fire = ticks;
	}

	public void remove() {
		handle.setDead();
	}

	public boolean isDead() {
		return handle.isDead;
	}

	public CraftEntity getPassenger() {
		return handle.riddenByEntity.spoutEnty;
	}

	public boolean setPassenger(CraftEntity passenger) {
		handle.riddenByEntity = ((CraftEntity)passenger).handle;
		((CraftEntity)passenger).handle.ridingEntity = handle;
		return true;
	}

	public boolean isEmpty() {
		return handle.riddenByEntity == null;
	}

	public boolean eject() {
		if (!isEmpty()) {
			handle.riddenByEntity.ridingEntity = null;
			handle.riddenByEntity = null;
			return true;
		}
		return false;
	}

	public float getFallDistance() {
		return handle.fallDistance;
	}

	public void setFallDistance(float distance) {
		handle.fallDistance = distance;
	}

	public UUID getUniqueId() {
		return handle.uniqueId;
	}

	public int getTicksLived() {
		return handle.ticksExisted;
	}

	public void setTicksLived(int value) {
		handle.ticksExisted = value;
	}

	public void setSkin(String skinURI, EntitySkinType type) {
	}
}
