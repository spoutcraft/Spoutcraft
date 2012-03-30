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

import net.minecraft.src.EntityEnderman;

import org.spoutcraft.spoutcraftapi.entity.Enderman;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class CraftEnderman extends CraftMonster implements Enderman {
	public CraftEnderman(EntityEnderman entity) {
		super(entity);
	}

	public EntityEnderman getEntityEnderman() {
		return (EntityEnderman)handle;
	}

	@Override
	public String toString() {
		return "CraftEnderman";
	}

	public Material getCarriedMaterial() {
		return MaterialData.getMaterial(getEntityEnderman().getCarried(), (short) getEntityEnderman().getCarryingData());
	}

	public void setCarriedMaterial(Material data) {
		getEntityEnderman().setCarried(data.getRawId());
		getEntityEnderman().setCarryingData(data.getRawData());
	}
}
