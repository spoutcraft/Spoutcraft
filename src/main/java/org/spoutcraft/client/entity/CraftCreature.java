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

import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityLiving;

import org.spoutcraft.api.entity.Creature;
import org.spoutcraft.api.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature {
	public CraftCreature(EntityCreature entity) {
		super(entity);
	}

	public void setTarget(LivingEntity target) {
		EntityCreature entity = getEntityCreature();
		if (target == null) {
			entity.entityToAttack = null;
		} else if (target instanceof CraftLivingEntity) {
			EntityLiving victim = ((CraftLivingEntity) target).getEntityLiving();
			entity.entityToAttack = victim;
			entity.pathToEntity = entity.worldObj.getPathEntityToEntity(entity, entity.entityToAttack, 16.0F, true, false, false, true);
		}
	}

	public CraftLivingEntity getTarget() {
		if (getEntityCreature().entityToAttack == null) {
			return null;
		}
		return (CraftLivingEntity) getEntityCreature().entityToAttack.spoutEntity;
	}

	public EntityCreature getEntityCreature() {
		return (EntityCreature) handle;
	}

	@Override
	public String toString() {
		return "CraftCreature";
	}
}
