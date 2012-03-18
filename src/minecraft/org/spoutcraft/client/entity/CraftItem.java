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

import net.minecraft.src.EntityItem;

import org.spoutcraft.spoutcraftapi.entity.Item;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

import org.spoutcraft.client.inventory.CraftItemStack;

public class CraftItem extends CraftEntity implements Item {
	private EntityItem item;

	public CraftItem(EntityItem entity) {
		super(entity);
		this.item = entity;
	}

	public ItemStack getItemStack() {
		return new CraftItemStack(item.item);
	}

	public void setItemStack(ItemStack stack) {
		item.item = new net.minecraft.src.ItemStack(stack.getTypeId(), stack.getAmount(), stack.getDurability());
	}

	public int getPickupDelay() {
		return item.delayBeforeCanPickup;
	}

	public void setPickupDelay(int delay) {
		item.delayBeforeCanPickup = delay;
	}

	@Override
	public String toString() {
		return "CraftItem";
	}
}
