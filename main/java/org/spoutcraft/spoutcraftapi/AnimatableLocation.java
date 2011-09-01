package org.spoutcraft.spoutcraftapi;

import org.spoutcraft.spoutcraftapi.animation.Animatable;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;

public class AnimatableLocation extends MutableLocation implements Animatable, Location {

	
	public AnimatableLocation(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0);
	}

	public AnimatableLocation(World world, double x, double y, double z, double yaw, double pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public Animatable getValueAt(double p, Animatable startValue, Animatable endValue) {
		Location p1 = (Location)startValue;
		Location p2 = (Location)endValue;
		if(!p1.getWorld().equals(p2.getWorld())){
			return null;
		}
		double x = p1.getX(), y = p1.getY(), z = p1.getZ();
		double yaw = p1.getYaw(), pitch = p1.getPitch();
		x += (p1.getX() - p2.getX())*p;
		y += (p1.getY() - p2.getY())*p;
		z += (p1.getZ() - p2.getZ())*p;
		yaw += (p1.getYaw() - p2.getYaw())*p;
		pitch += (p1.getPitch() - p2.getPitch())*p;
		
		return new AnimatableLocation(p1.getWorld(), x,y,z,yaw,pitch);
	}
}
