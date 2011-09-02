package org.spoutcraft.spoutcraftapi.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.World;

public class MutableLocation extends MutableVector implements Location{
	private double pitch;
	private double yaw;
	private World world;
	
	public MutableLocation() {
		pitch = yaw = 0D;
		world = null;
	}
	
	/**
	 * Constructs a new Location with the given coordinates
	 *
	 * @param world The world in which this location resides
	 * @param x The x-coordinate of this new location
	 * @param y The y-coordinate of this new location
	 * @param z The z-coordinate of this new location
	 */
	public MutableLocation(final World world, final double x, final double y, final double z) {
		this(world, x, y, z, 0, 0);
	}

	/**
	 * Constructs a new Location with the given coordinates and direction
	 *
	 * @param world The world in which this location resides
	 * @param x The x-coordinate of this new location
	 * @param y The y-coordinate of this new location
	 * @param z The z-coordinate of this new location
	 * @param yaw The absolute rotation on the x-plane, in degrees
	 * @param pitch The absolute rotation on the y-plane, in degrees
	 */
	public MutableLocation(final World world, final double x, final double y, final double z, final double yaw, final double pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public World getWorld() {
		return world;
	}

	public Location setYaw(double yaw) {
		this.yaw = yaw;
		return this;
	}

	public Location setPitch(double pitch) {
		this.pitch = pitch;
		return this;
	}

	public Location setWorld(World world) {
		this.world = world;
		return this;
	}
	
	public Vector getDirection() {
		Vector vector = new MutableVector();

		double rotX = this.getYaw();
		double rotY = this.getPitch();

		vector.setY(-Math.sin(Math.toRadians(rotY)));

		double h = Math.cos(Math.toRadians(rotY));

		vector.setX(-h * Math.sin(Math.toRadians(rotX)));
		vector.setZ(h * Math.cos(Math.toRadians(rotX)));

		return vector;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location other = (Location)obj;
			return (new EqualsBuilder()).append(getX(), other.getX()).append(getY(), other.getY()).append(getZ(), other.getZ()).append(getYaw(), other.getYaw()).append(getPitch(), other.getPitch()).append(getWorld(), other.getWorld()).isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(getX()).append(getY()).append(getZ()).append(getYaw()).append(getPitch()).append(getWorld()).toHashCode();
	}
}
