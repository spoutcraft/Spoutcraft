/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.material.item.GenericCustomTool;

public class PacketGenericTool implements SpoutPacket{
	GenericCustomTool item = new GenericCustomTool();

	public void readData(SpoutInputStream input) throws IOException {
		item.readData(input);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		item.writeData(output);
	}

	public void run(int playerId) {
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketGenericTool;
	}

	public int getVersion() {
		return item.getVersion();
	}
}
