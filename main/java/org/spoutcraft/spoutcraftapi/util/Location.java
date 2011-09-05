package org.spoutcraft.spoutcraftapi.util;

import org.spoutcraft.spoutcraftapi.World;

public interface Location extends Vector, FixedLocation{

	/**
     * Sets the yaw of this location
     *
     * @param yaw New yaw
     * @return this location
     */
	public Location setYaw(double yaw);

	/**
     * Sets the pitch of this location
     *
     * @param pitch New pitch
     * @return this location
     */
	public Location setPitch(double pitch);

	/**
	 * Sets the world of this location
	 * 
	 * @param world New world
	 * @return this location
	 */
	public Location setWorld(World world);
}
