package org.spoutcraft.api.entity;

import org.spoutcraft.api.util.Vector;

/**
 * Represents a vehicle entity.
 *
 * @author sk89q
 */
public interface Vehicle extends Entity {

    /**
     * Gets the vehicle's velocity.
     *
     * @return velocity vector
     */
    public Vector getVelocity();

    /**
     * Sets the vehicle's velocity.
     *
     * @param vel velocity vector
     */
    public void setVelocity(Vector vel);
}
