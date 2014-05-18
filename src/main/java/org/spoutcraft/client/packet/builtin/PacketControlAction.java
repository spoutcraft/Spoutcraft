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
import java.util.UUID;

import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.packet.PacketType;
import org.spoutcraft.client.packet.SpoutPacket;

public class PacketControlAction extends SpoutPacket {
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

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		long msb = input.readLong();
		long lsb = input.readLong();
		this.screen = new UUID(msb, lsb);
		msb = input.readLong();
		lsb = input.readLong();
		this.widget = new UUID(msb, lsb);
		this.state = input.readFloat();
		this.data = input.readString();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits());
		output.writeLong(widget.getMostSignificantBits());
		output.writeLong(widget.getLeastSignificantBits());
		output.writeFloat(state);
		output.writeString(data);
	}

	@Override
	public void run(int playerId) {
		// Nothing to do
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketControlAction;
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void failure(int playerId) {
	}
}
