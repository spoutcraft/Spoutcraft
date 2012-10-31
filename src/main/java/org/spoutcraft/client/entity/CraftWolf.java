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

import net.minecraft.src.EntityWolf;
import net.minecraft.src.PathEntity;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.entity.AnimalTamer;
import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.entity.Wolf;

public class CraftWolf extends CraftAnimals implements Wolf {
	private AnimalTamer owner;

	public CraftWolf(EntityWolf wolf) {
		super(wolf);
	}

	public boolean isAngry() {
		return getEntityWolf().isAngry();
	}

	public void setAngry(boolean angry) {
		getEntityWolf().setAngry(angry);
	}

	public boolean isSitting() {
		return getEntityWolf().isSitting();
	}

	public void setSitting(boolean sitting) {
		getEntityWolf().setSitting(sitting);
		setPath((PathEntity) null);
	}

	public boolean isTamed() {
		return getEntityWolf().isTamed();
	}

	public void setTamed(boolean tame) {
		getEntityWolf().setTamed(tame);
	}

	public AnimalTamer getOwner() {
		// If the wolf has a previously set owner use that, otherwise try and find the player who owns it
		if (owner == null && !("").equals(getOwnerName())) {
			owner = Spoutcraft.getPlayer(getOwnerName());
		}
		return owner;
	}

	public void setOwner(AnimalTamer tamer) {
		owner = tamer;
		if (owner != null) {
			setTamed(true);
			setPath((PathEntity) null);
			if (owner instanceof Player) {
				setOwnerName(((Player) owner).getName());
			} else {
				setOwnerName("");
			}
		} else {
			setTamed(false);
			setOwnerName("");
		}
	}

	String getOwnerName() {
		return getEntityWolf().getOwnerName();
	}

	void setOwnerName(String ownerName) {
		getEntityWolf().setOwner(ownerName);
	}

	private void setPath(PathEntity pathentity) {
		getEntityWolf().setPathToEntity(pathentity);
	}

	public EntityWolf getEntityWolf() {
		return (EntityWolf) handle;
	}

	@Override
	public String toString() {
		return "CraftWolf[anger=" + isAngry() + ",owner=" + getOwner() + ",tame=" + isTamed() + ",sitting=" + isSitting() + "]";
	}
}
