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
import java.util.UUID;

import org.spoutcraft.api.gui.GenericComboBox;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketComboBox implements SpoutPacket {
	private GenericComboBox box;
	private UUID uuid;
	private boolean open;
	private int selection;

	public PacketComboBox() {
	}

	public PacketComboBox(GenericComboBox box) {
		this.box = box;
		this.uuid = box.getId();
		this.open = box.isOpen();
		this.selection = box.getSelectedRow();
	}

	public int getNumBytes() {
		return 8 + 8 + 1 + 4;
	}
	public void readData(SpoutInputStream input) throws IOException {
		uuid = new UUID(input.readLong(), input.readLong());
		open = input.readBoolean();
		selection = input.readInt();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeLong(uuid.getMostSignificantBits());
		output.writeLong(uuid.getLeastSignificantBits());
		output.writeBoolean(open);
		output.writeInt(selection);
	}

	public void run(int playerId) {
		if (SpoutClient.getInstance().getActivePlayer().getMainScreen().getActivePopup() != null) {
			Widget w = SpoutClient.getInstance().getActivePlayer().getMainScreen().getActivePopup().getWidget(uuid);
			if (w != null && w instanceof GenericComboBox) {
				box = (GenericComboBox) w;
				box.setOpen(open);
				box.setSelection(selection);
			}
		}
	}

	public void failure(int playerId) {}

	public PacketType getPacketType() {
		return PacketType.PacketComboBox;
	}

	public int getVersion() {
		return 0;
	}
}
