package org.spoutcraft.spoutcraftapi.entity;

import java.util.List;
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

/**
 * Represents a base entity in the world
 */
public interface Entity {

	public Location getLocation();

	public void setVelocity(Vector velocity);

	public Vector getVelocity();

	public World getWorld();

	public boolean teleport(Location location);

	public boolean teleport(Entity destination);

	public List<org.spoutcraft.spoutcraftapi.entity.Entity> getNearbyEntities(double x, double y, double z);

	public int getEntityId();

	public int getFireTicks();

	public int getMaxFireTicks();

	public void setFireTicks(int ticks);

	public void remove();

	public boolean isDead();

	public abstract Entity getPassenger();

	public abstract boolean setPassenger(Entity passenger);

	public abstract boolean isEmpty();

	public abstract boolean eject();

	public float getFallDistance();

	public void setFallDistance(float distance);

	public UUID getUniqueId();
}
