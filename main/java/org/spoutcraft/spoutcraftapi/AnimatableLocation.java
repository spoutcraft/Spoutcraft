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
