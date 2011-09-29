package org.getspout.spout.entity;

import java.util.List;
import java.util.UUID;

import org.getspout.spout.SpoutcraftWorld;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;
import org.spoutcraft.spoutcraftapi.util.MutableVector;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class CraftEntity extends PropertyObject implements Entity {
	protected net.minecraft.src.Entity handle = null;
	
	public CraftEntity(net.minecraft.src.Entity handle)
	{
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
		return new MutableLocation(getWorld(),handle.posX,handle.posY,handle.posZ);
	}

	public void setVelocity(Vector velocity) {
		handle.motionX = velocity.getX();
		handle.motionY = velocity.getY();
		handle.motionZ = velocity.getZ();
	}

	public Vector getVelocity() {
		return new MutableVector(handle.motionX,handle.motionY,handle.motionZ);
	}

	public World getWorld() {
		return handle.worldObj.world;
	}

	public boolean teleport(Location location) {
		handle.setPosition(location.getX(), location.getY(), location.getZ());
		return true;
	}

	public boolean teleport(Entity destination) {
		return teleport(destination.getLocation());
	}

	public List<Entity> getNearbyEntities(double x, double y, double z) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getEntityId() {
		return handle.entityId;
	}

	public int getFireTicks() {
		return handle.fire;
	}

	public int getMaxFireTicks() {
		// TODO Auto-generated method stub
		return 0;
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
		if(!isEmpty()){
			handle.riddenByEntity.ridingEntity = null;
			handle.riddenByEntity = null;
			return true;
		}
		return false;
	}

	public float getFallDistance() {
		return 0;
	}

	public void setFallDistance(float distance) {
		// TODO Auto-generated method stub

	}

	public UUID getUniqueId() {
		return handle.uniqueId;
	}
}
