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
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketControlAction implements SpoutPacket {
	protected UUID screen;
	protected UUID widget;
	protected float state;
	protected String data = "";

	public PacketControlAction() {

	}

	public PacketControlAction(Screen screen, Widget widget, float state) {
		this.screen = screen.getId();
		this.widget = widget.getId();
		this.state = state;
	}

	public PacketControlAction(Screen screen, Widget widget, String data, float position) {
		this.screen = screen.getId();
		this.widget = widget.getId();
		this.state = position;
		this.data = data;
	}

	public int getNumBytes() {
		return 36 + PacketUtil.getNumBytes(data);
	}

	public void readData(DataInputStream input) throws IOException {
		long msb = input.readLong();
		long lsb = input.readLong();
		this.screen = new UUID(msb, lsb);
		msb = input.readLong();
		lsb = input.readLong();
		this.widget = new UUID(msb, lsb);
		this.state = input.readFloat();
		this.data = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits());
		output.writeLong(widget.getMostSignificantBits());
		output.writeLong(widget.getLeastSignificantBits());
		output.writeFloat(state);
		PacketUtil.writeString(output, data);
	}

	public void run(int playerId) {
		// Nothing to do
	}

	public PacketType getPacketType() {
		return PacketType.PacketControlAction;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {

	}
}
