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
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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

package org.getspout.spout.entity;

import net.minecraft.src.EntityTNTPrimed;

import org.spoutcraft.spoutcraftapi.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

	public CraftTNTPrimed(EntityTNTPrimed entity) {
		super(entity);
	}

	@Override
	public String toString() {
		return "CraftTNTPrimed";
	}

	public EntityTNTPrimed getHandle() {
		return (EntityTNTPrimed)handle;
	}

	public float getYield() {
		return getHandle().yield;
	}

	public boolean isIncendiary() {
		return getHandle().incendiary;
	}

	public void setIsIncendiary(boolean isIncendiary) {
		getHandle().incendiary = isIncendiary;
	}

	public void setYield(float yield) {
		getHandle().yield = yield;
	}

	public int getFuseTicks() {
		return getHandle().fuse;
	}

	public void setFuseTicks(int fuseTicks) {
		getHandle().fuse = fuseTicks;
	}

}
