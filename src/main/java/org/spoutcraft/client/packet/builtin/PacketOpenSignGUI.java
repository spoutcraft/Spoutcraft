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
package org.spoutcraft.client.packet.builtin;

import java.io.IOException;

import net.minecraft.src.GuiEditSign;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketType;
import org.spoutcraft.client.packet.SpoutPacket;

public class PacketOpenSignGUI extends SpoutPacket {
	int x,y,z;

	public int getNumBytes() {
		return 12; // Never be too lazy to calculate !
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
	}

	@Override
	public void run(int playerId) {
		World world = SpoutClient.getHandle().theWorld;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof TileEntitySign) {
			TileEntitySign sign = (TileEntitySign)te;
			GuiEditSign gui = new GuiEditSign(sign);
			SpoutClient.getHandle().displayGuiScreen(gui);
		}
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketOpenSignGUI;
	}

	@Override
	public int getVersion() {
		return 1;
	}
}
