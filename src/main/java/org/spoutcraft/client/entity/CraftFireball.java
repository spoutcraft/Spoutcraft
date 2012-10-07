/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.entity;

import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityLiving;

import org.spoutcraft.api.entity.Fireball;
import org.spoutcraft.api.entity.LivingEntity;
import org.spoutcraft.api.util.MutableVector;
import org.spoutcraft.api.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
	public CraftFireball(EntityFireball entity) {
		super(entity);
	}

	@Override
	public String toString() {
		return "CraftFireball";
	}

	public float getYield() {
		return ((EntityFireball) handle).yield;
	}

	public boolean isIncendiary() {
		return ((EntityFireball) handle).incendiary;
	}

	public void setIsIncendiary(boolean incendiary) {
		((EntityFireball) handle).incendiary = incendiary;
	}

	public void setYield(float yield) {
		((EntityFireball) handle).yield = yield;
	}

	public LivingEntity getShooter() {
		if (((EntityFireball) handle).shootingEntity != null) {
			return (LivingEntity) ((EntityFireball) handle).shootingEntity.spoutEntity;
		}
		return null;
	}

	public void setShooter(LivingEntity shooter) {
		if (shooter instanceof CraftLivingEntity) {
			((EntityFireball) handle).shootingEntity = (EntityLiving) ((CraftLivingEntity) shooter).handle;
		}
	}

	public Vector getDirection() {
		return new MutableVector(((EntityFireball) handle).accelerationX, ((EntityFireball) handle).accelerationY, ((EntityFireball) handle).accelerationZ);
	}

	public void setDirection(Vector direction) {
		((EntityFireball) handle).accelerationX = direction.getX();
		((EntityFireball) handle).accelerationY = direction.getY();
		((EntityFireball) handle).accelerationZ = direction.getZ();
	}
}
