/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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

import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayerSP;

import org.spoutcraft.client.player.ClientPlayer;

public class CraftEntityFactory {
	public static CraftEntity getCraftEntity(net.minecraft.src.Entity aThis) {
		if (aThis instanceof EntityPlayerSP) {
			return ClientPlayer.getInstance();
		}
		if (aThis instanceof EntityLivingBase) {
			return new CraftLivingEntity((EntityLivingBase)aThis);
		}
		return null; // Should not break, got most of the important stuff
	}
}
