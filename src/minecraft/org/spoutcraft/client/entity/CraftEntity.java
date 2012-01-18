/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.spoutcraft.client.player.SpoutPlayer;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.entity.Arrow;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.entity.TextEntity;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;
import org.spoutcraft.spoutcraftapi.util.MutableVector;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class CraftEntity extends PropertyObject implements Entity {
	protected net.minecraft.src.Entity handle = null;
	protected static HashMap<Class<? extends Entity>, Class<? extends CraftEntity>> interfacedClasses = new HashMap<Class<? extends Entity>, Class<? extends CraftEntity>>();

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
		return new MutableLocation(getWorld(),handle.posX,handle.posY,handle.posZ,handle.rotationYaw,handle.rotationPitch);
	}

	public void setVelocity(Vector velocity) {
		handle.motionX = velocity.getX();
		handle.motionY = velocity.getY();
		handle.motionZ = velocity.getZ();
	}

	public Vector getVelocity() {
		return new MutableVector(handle.motionX, handle.motionY, handle.motionZ);
	}

	public World getWorld() {
		return handle.worldObj.world;
	}

	public boolean teleport(FixedLocation location) {
		handle.setPosition(location.getX(), location.getY(), location.getZ());
		handle.setAngles((float)location.getYaw(), (float)location.getPitch());
		return true;
	}

	public boolean teleport(Entity destination) {
		return teleport(destination.getLocation());
	}

	public Set<Entity> getNearbyEntities(double x, double y, double z) {
		List<net.minecraft.src.Entity> notchEntityList = handle.worldObj.getEntitiesWithinAABBExcludingEntity(handle, handle.boundingBox.expand(x, y, z));
		Set<Entity> entities = new HashSet<Entity>(notchEntityList.size());

		for (net.minecraft.src.Entity e: notchEntityList) {
			entities.add(e.spoutEntity);
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
		handle.setEntityDead();
	}

	public boolean isDead() {
		return handle.isDead;
	}

	public Entity getPassenger() {
		return handle.riddenByEntity.spoutEntity;
	}

	public boolean setPassenger(Entity passenger) {
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

	@SuppressWarnings("unchecked")
	public static Entity spawn(FixedLocation loc, Class<Entity> clazz) {
		Class<CraftEntity> craftClass = (Class<CraftEntity>) interfacedClasses.get(clazz);
		CraftEntity ret = null;
		try {
			ret = craftClass.getConstructor(FixedLocation.class).newInstance(loc);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public static void registerTypes() {
		interfacedClasses.put(TextEntity.class, CraftTextEntity.class);
		interfacedClasses.put(Player.class, SpoutPlayer.class);
		interfacedClasses.put(Arrow.class, CraftArrow.class);
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
