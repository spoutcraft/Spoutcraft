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
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.material.item.GenericCustomFood;

public class PacketGenericFood implements SpoutPacket {
	GenericCustomFood item = new GenericCustomFood();

	public int getNumBytes() {
		return item.getNumBytes();
	}

	public void readData(DataInputStream input) throws IOException {
		item.readData(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		item.writeData(output);
	}

	public void run(int playerId) {
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketGenericFood;
	}

	public int getVersion() {
		return item.getVersion();
	}
}
