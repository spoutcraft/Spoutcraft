package org.spoutcraft.spoutcraftapi.animation;

/**
 * This is used to set the new values to the animated object. Implement this interface for your properties.
 * @author tux
 *
 */
public interface PropertyDelegate {
	/**
	 * Sets the value to the object.
	 * Will be called each animation cycle.
	 * @param value to be set to the object.
	 */
	public void set(Animatable value);
}
