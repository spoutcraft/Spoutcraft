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

import org.spoutcraft.api.gui.ScreenType;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketScreenAction implements SpoutPacket {
	protected byte action = -1;
	protected byte screen = -1; // UnknownScreen

	public PacketScreenAction() {
	}

	public PacketScreenAction(ScreenAction action, ScreenType screen) {
 		this.action = (byte)action.getId();
		this.screen = (byte)screen.getCode();
	}

	public int getNumBytes() {
		return 2;
	}

	public void readData(SpoutInputStream input) throws IOException {
		action = (byte) input.read();
		screen = (byte) input.read();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.write(action);
		output.write(screen);
	}

	public void run(int playerId) {
		switch(ScreenAction.getScreenActionFromId(action)) {
			case Open:
				SpoutClient.getHandle().displayPreviousScreen();
				break;
			case Close:
				SpoutClient.getHandle().displayPreviousScreen();
				break;
			case Force_Close:
				SpoutClient.getHandle().displayGuiScreen(null, false);
				break;
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketScreenAction;
	}

	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
