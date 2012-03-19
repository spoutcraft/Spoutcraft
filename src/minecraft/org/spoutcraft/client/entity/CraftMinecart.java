/*
 * This file is part of Bukkit (http://bukkit.org/).
 *
 * Bukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.entity;

import net.minecraft.src.EntityMinecart;

import org.spoutcraft.spoutcraftapi.entity.Minecart;
import org.spoutcraft.spoutcraftapi.util.MutableVector;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class CraftMinecart extends CraftVehicle implements Minecart {
	/**
	 * Stores the minecart type id, which is used by Minecraft to differentiate
	 * minecart types. Here we use subclasses.
	 */
	public enum Type {
		Minecart(0),
		StorageMinecart(1),
		PoweredMinecart(2);
		private final int id;

		private Type(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	protected EntityMinecart minecart;

	public CraftMinecart(EntityMinecart entity) {
		super(entity);
		minecart = entity;
	}

	public void setDamage(int damage) {
		minecart.func_41024_b(damage);
	}

	public int getDamage() {
		return minecart.func_41025_i();
	}

	public double getMaxSpeed() {
		return minecart.maxSpeed;
	}

	public void setMaxSpeed(double speed) {
		if (speed >= 0D) {
			minecart.maxSpeed = speed;
		}
	}

	public boolean isSlowWhenEmpty() {
		return minecart.slowWhenEmpty;
	}

	public void setSlowWhenEmpty(boolean slow) {
		minecart.slowWhenEmpty = slow;
	}

	public Vector getFlyingVelocityMod() {
		return new MutableVector(minecart.flyingX, minecart.flyingY, minecart.flyingZ);
	}

	public void setFlyingVelocityMod(Vector flying) {
		minecart.flyingX = flying.getX();
		minecart.flyingY = flying.getY();
		minecart.flyingZ = flying.getZ();
	}

	public Vector getDerailedVelocityMod() {
		return new MutableVector(minecart.derailedX, minecart.derailedY, minecart.derailedZ);
	}

	public void setDerailedVelocityMod(Vector derailed) {
		minecart.derailedX = derailed.getX();
		minecart.derailedY = derailed.getY();
		minecart.derailedZ = derailed.getZ();
	}

	@Override
	public String toString() {
		return "CraftMinecart";
	}
}
