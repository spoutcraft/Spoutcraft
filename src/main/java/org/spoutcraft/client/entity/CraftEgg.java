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

import net.minecraft.src.Entity;
import net.minecraft.src.EntityEgg;

import org.spoutcraft.api.entity.Egg;
import org.spoutcraft.api.entity.LivingEntity;

public class CraftEgg extends AbstractProjectile implements Egg {
	public CraftEgg(Entity entity) {
		super(entity);
	}

	public EntityEgg getEgg() {
		return (EntityEgg)handle;
	}

	public LivingEntity getShooter() {
		return (LivingEntity) getEgg().thrower.spoutEntity;
	}

	public void setShooter(LivingEntity shooter) {
		getEgg().thrower = ((CraftLivingEntity)shooter).getEntityLiving();
	}
}
