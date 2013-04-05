/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import net.minecraft.src.EntityPlayer;

import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.api.inventory.PlayerInventory;
import org.spoutcraft.client.inventory.CraftInventoryPlayer;

public class CraftHumanEntity extends CraftLivingEntity {
	public CraftHumanEntity(EntityPlayer player) {
		super(player);
	}

	public EntityPlayer getMCPlayer() {
		return (EntityPlayer)handle;
	}

	public String getName() {
		return getMCPlayer().username;
	}

	public PlayerInventory getInventory() {
		return new CraftInventoryPlayer(getMCPlayer().inventory);
	}

	public ItemStack getItemInHand() {
		return getInventory().getItemInHand();
	}

	public void setItemInHand(ItemStack item) {
		getInventory().setItemInHand(item);
	}

	public boolean isSleeping() {
		boolean sleep = getMCPlayer().isPlayerSleeping();
		return sleep;
	}

	public int getSleepTicks() {
		return getMCPlayer().getSleepTimer();
	}
}