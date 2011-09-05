package org.spoutcraft.spoutcraftapi.util;

import org.spoutcraft.spoutcraftapi.World;

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
	 * Gets the world that this location resides in
	 * 
	 * @return World that contains this location
	 */
	public World getWorld();

	/**
	 * Gets a Vector pointing in the direction that this Location is facing
	 * 
	 * @return Vector
	 */
	public Vector getDirection();
}
