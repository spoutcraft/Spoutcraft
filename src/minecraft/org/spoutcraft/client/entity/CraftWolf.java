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

import net.minecraft.src.EntityWolf;
import net.minecraft.src.PathEntity;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.entity.AnimalTamer;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.entity.Wolf;

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
		return getEntityWolf().func_48141_af();
	}

	public void setSitting(boolean sitting) {
		getEntityWolf().func_48140_f(sitting);
		setPath((PathEntity) null);
	}

	public boolean isTamed() {
		return getEntityWolf().func_48139_F_();
	}

	public void setTamed(boolean tame) {
		getEntityWolf().func_48138_b(tame);
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
		return getEntityWolf().func_48145_ag();
	}

	void setOwnerName(String ownerName) {
		getEntityWolf().func_48143_a(ownerName);
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
