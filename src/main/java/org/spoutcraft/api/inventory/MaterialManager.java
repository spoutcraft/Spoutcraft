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
package org.spoutcraft.api.inventory;

public interface MaterialManager {
	public float getFriction(org.spoutcraft.api.material.Block block);

	public void setFriction(org.spoutcraft.api.material.Block block, float friction);

	public void resetFriction(org.spoutcraft.api.material.Block block);

	public float getHardness(org.spoutcraft.api.material.Block block);

	public void setHardness(org.spoutcraft.api.material.Block block, float hardness);

	public void resetHardness(org.spoutcraft.api.material.Block block);

	public boolean isOpaque(org.spoutcraft.api.material.Block block);

	public void setOpaque(org.spoutcraft.api.material.Block block, boolean opacity);

	public void resetOpacity(org.spoutcraft.api.material.Block block);

	public int getLightLevel(org.spoutcraft.api.material.Block block);

	public void setLightLevel(org.spoutcraft.api.material.Block block, int level);

	public void resetLightLevel(org.spoutcraft.api.material.Block block);

	public String getToolTip(org.spoutcraft.api.inventory.ItemStack is);
}
