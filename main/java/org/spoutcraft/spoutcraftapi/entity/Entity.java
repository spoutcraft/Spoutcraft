/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.entity;

import java.util.List;
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.property.PropertyInterface;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

/**
 * Represents a base entity in the world
 */
public interface Entity extends PropertyInterface {

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
