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
package org.spoutcraft.client.packet;

import java.io.IOException;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketAirTime implements SpoutPacket {
	public int airTime;
	public int air;

	public PacketAirTime() {
	}

	public PacketAirTime(int maxTime, int time) {
		this.airTime = maxTime;
		this.air = time;
	}

	public void readData(SpoutInputStream input) throws IOException {
		this.airTime = input.readInt();
		this.air = input.readInt();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(this.airTime);
		output.writeInt(this.air);
	}

	public void run(int id) {
		SpoutClient.getInstance().getActivePlayer().setMaximumAir(airTime);
		SpoutClient.getInstance().getActivePlayer().setRemainingAir(air);
	}

	public PacketType getPacketType() {
		return PacketType.PacketAirTime;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
