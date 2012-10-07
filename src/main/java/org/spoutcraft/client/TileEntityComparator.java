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
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;

public class TileEntityComparator implements Comparator<TileEntity>{
	private int playerX = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posX);
	private int playerY = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posY);
	private int playerZ = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posZ);

	@Override
	public int compare(TileEntity o1, TileEntity o2) {
		int x1 = (o1.xCoord - playerX) * (o1.xCoord - playerX);
		int y1 = (o1.yCoord - playerY) * (o1.yCoord - playerY);
		int z1 = (o1.zCoord - playerZ) * (o1.zCoord - playerZ);

		int x2 = (o2.xCoord - playerX) * (o2.xCoord - playerX);
		int y2 = (o2.yCoord - playerY) * (o2.yCoord - playerY);
		int z2 = (o2.zCoord - playerZ) * (o2.zCoord - playerZ);
		return (x1 + y1 + z1) - (x2 + y2 + z2);
	}
}
