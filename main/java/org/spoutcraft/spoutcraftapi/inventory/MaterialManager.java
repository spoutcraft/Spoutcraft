/*
 * This file is part of SpoutcraftAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * SpoutcraftAPI is licensed under the GNU Lesser General Public License.
 *
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.inventory;

public interface MaterialManager {
	public float getFriction(org.spoutcraft.spoutcraftapi.material.Block block);

	public void setFriction(org.spoutcraft.spoutcraftapi.material.Block block, float friction);

	public void resetFriction(org.spoutcraft.spoutcraftapi.material.Block block);

	public float getHardness(org.spoutcraft.spoutcraftapi.material.Block block);

	public void setHardness(org.spoutcraft.spoutcraftapi.material.Block block, float hardness);

	public void resetHardness(org.spoutcraft.spoutcraftapi.material.Block block);

	public boolean isOpaque(org.spoutcraft.spoutcraftapi.material.Block block);

	public void setOpaque(org.spoutcraft.spoutcraftapi.material.Block block, boolean opacity);

	public void resetOpacity(org.spoutcraft.spoutcraftapi.material.Block block);

	public int getLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);

	public void setLightLevel(org.spoutcraft.spoutcraftapi.material.Block block, int level);

	public void resetLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);

	public String getToolTip(org.spoutcraft.spoutcraftapi.inventory.ItemStack is);
}
