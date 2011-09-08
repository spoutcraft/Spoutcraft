package org.spoutcraft.spoutcraftapi.util;

import org.spoutcraft.spoutcraftapi.World;

public class FastLocation extends FastVector implements FixedLocation{
	private final double yaw;
	private final double pitch;
	private final World world;

	public FastLocation(int x, int y, int z, double yaw, double pitch, World world) {
		super(x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world;
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

}
