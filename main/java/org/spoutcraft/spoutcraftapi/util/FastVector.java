package org.spoutcraft.spoutcraftapi.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FastVector implements FixedVector{
	public final int x;
	public final int y;
	public final int z;
	public FastVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public int getBlockX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getBlockY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public int getBlockZ() {
		return z;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	public double distance(Vector other) {
		return Math.sqrt(distanceSquared(other));
	}

	public double distanceSquared(Vector other) {
		return (x - other.getX()) * (x - other.getX()) + (y - other.getY()) * (y - other.getY()) + (z - other.getZ()) * (z - other.getZ());
	}

	public float angle(Vector other) {
		double dot = dot(other) / (length() * other.length());
		return (float) Math.acos(dot);
	}

	public double dot(Vector other) {
		return x * other.getX() + y * other.getY() + z * other.getZ();
	}

	public boolean isInAABB(Vector min, Vector max) {
		return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY() && z >= min.getZ() && z <= max.getZ();
	}

	public boolean isInSphere(Vector origin, double radius) {
		return (Math.pow(origin.getX() - x, 2) + Math.pow(origin.getY() - y, 2) + Math.pow(origin.getZ() - z, 2)) <= Math.pow(radius, 2);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FastVector) {
			FastVector other = (FastVector)obj;
			return (new EqualsBuilder()).append(x, other.getX()).append(y, other.getY()).append(z, other.getZ()).isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(x).append(y).append(z).toHashCode();
	}

}
