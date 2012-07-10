/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spoutcraft.client.packet;

import java.io.IOException;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.player.accessories.AccessoryHandler;
import org.spoutcraft.client.player.accessories.AccessoryType;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;


public class PacketAccessory implements SpoutPacket{
	private AccessoryType type;
	private String url;
	
	public PacketAccessory() {
	}

	public PacketAccessory(AccessoryType type, String url) {
		this.type = type;
		this.url = url;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		type = AccessoryType.byId(input.readInt());
		url = input.readString();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(type.getId());
		output.writeString(url);
	}

	@Override
	public void run(int playerId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(playerId);
		AccessoryHandler.addAccessoryType(e, type, url);
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketAccessory;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}