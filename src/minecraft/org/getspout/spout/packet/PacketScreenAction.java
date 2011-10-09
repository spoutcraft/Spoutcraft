/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;

public class PacketScreenAction implements SpoutPacket{
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

	public void readData(DataInputStream input) throws IOException {
		action = input.readByte();
		screen = input.readByte();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeByte(action);
		output.writeByte(screen);
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
