package org.spoutcraft.spoutcraftapi.util;

public interface FixedVector extends Cloneable {

	/**
	 * Gets the X component.
	 * 
	 * @return x
	 */
	public double getX();

	/**
	 * Gets the floored value of the X component, indicating the block that this
	 * vector is contained with.
	 * 
	 * @return block X
	 */
	public int getBlockX();

	/**
	 * Gets the Y component.
	 * 
	 * @return y
	 */
	public double getY();

	/**
	 * Gets the floored value of the Y component, indicating the block that this
	 * vector is contained with.
	 * 
	 * @return block y
	 */
	public int getBlockY();

	/**
	 * Gets the Z component.
	 * 
	 * @return z
	 */
	public double getZ();

	/**
	 * Gets the floored value of the Z component, indicating the block that this
	 * vector is contained with.
	 * 
	 * @return block z
	 */
	public int getBlockZ();

	/**
	 * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2). The value
	 * of this method is not cached and uses a costly square-root function, so
	 * do not repeatedly call this method to get the vector's magnitude. NaN
	 * will be returned if the inner result of the sqrt() function overflows,
	 * which will be caused if the length is too long.
	 * 
	 * @return the magnitude
	 */
	public double length();

	/**
	 * Gets the magnitude of the vector squared.
	 * 
	 * @return the magnitude
	 */
	public double lengthSquared();

	/**
	 * Get the distance between this vector and another. The value of this
	 * method is not cached and uses a costly square-root function, so do not
	 * repeatedly call this method to get the vector's magnitude. NaN will be
	 * returned if the inner result of the sqrt() function overflows, which will
	 * be caused if the distance is too long.
	 * 
	 * @return the distance
	 */
	public double distance(Vector other);

	/**
	 * Get the squared distance between this vector and another.
	 * 
	 * @return the distance
	 */
	public double distanceSquared(Vector other);

	/**
	 * Gets the angle between this vector and another in radians.
	 * 
	 * @param other
	 * @return angle in radians
	 */
	public float angle(Vector other);

	/**
	 * Calculates the dot product of this vector with another. The dot product
	 * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
	 * 
	 * @param other
	 * @return dot product
	 */
	public double dot(Vector other);

	/**
	 * Returns whether this vector is in an axis-aligned bounding box. The
	 * minimum and maximum vectors given must be truly the minimum and maximum
	 * X, Y and Z components.
	 * 
	 * @param min
	 * @param max
	 * @return whether this vector is in the AABB
	 */
	public boolean isInAABB(Vector min, Vector max);

	/**
	 * Returns whether this vector is within a sphere.
	 * 
	 * @param origin
	 * @param radius
	 * @return whether this vector is in the sphere
	 */
	public boolean isInSphere(Vector origin, double radius);
}
