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
package org.spoutcraft.client.packet;

import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.api.gui.Slot;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class PacketSlotClick implements SpoutPacket {
	private UUID screen;
	private UUID slot;
	private int mouseClick;
	private boolean holdingShift;

	public PacketSlotClick() {
	}

	public PacketSlotClick(Slot slot, int mouseClick, boolean holdingShift) {
		screen = slot.getScreen().getId();
		this.slot = slot.getId();
		this.mouseClick = mouseClick;
		this.holdingShift = holdingShift;
	}

	public void readData(SpoutInputStream input) throws IOException {
		long msb = input.readLong();
		long lsb = input.readLong();
		screen = new UUID(msb,lsb);
		msb = input.readLong();
		lsb = input.readLong();
		slot = new UUID(msb,lsb);
		mouseClick = input.read();
		holdingShift = input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits()); // 16
		output.writeLong(slot.getMostSignificantBits());
		output.writeLong(slot.getLeastSignificantBits()); // 32
		output.write(mouseClick); // mouseClick will usually be 0 (left) or 1 (right) - so this is safe unless the mouse has... 257 buttons :P
		output.writeBoolean(holdingShift);//34
	}

	public void run(int playerId) {
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketSlotClick;
	}

	public int getVersion() {
		return 0;
	}
}
