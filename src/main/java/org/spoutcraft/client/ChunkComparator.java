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
package org.spoutcraft.client;

import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.MathHelper;

public class ChunkComparator implements Comparator<Chunk>{
	private int playerX = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posX / 16.0D);
	private int playerZ = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posZ / 16.0D);

	@Override
	public int compare(Chunk o1, Chunk o2) {
		int x1 = (o1.xPosition - playerX) * (o1.xPosition - playerX);
		int z1 = (o1.zPosition - playerZ) * (o1.zPosition - playerZ);

		int x2 = (o2.xPosition - playerX) * (o2.xPosition - playerX);
		int z2 = (o2.zPosition - playerZ) * (o2.zPosition - playerZ);
		return (x2 + z2) - (x1 + z1);
	}
}
