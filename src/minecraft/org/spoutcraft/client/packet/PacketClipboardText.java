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
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.client.player.ChatManager;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketClipboardText implements SpoutPacket{
	
	public PacketClipboardText() {
		
	}
	
	public PacketClipboardText(String text) {
		this.text = text;
	}
	protected String text;
	public int getNumBytes() {
		return PacketUtil.getNumBytes(text);
	}

	public void readData(DataInputStream input) throws IOException {
		text = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		if (text.length() > PacketUtil.maxString) {
			text = text.substring(0, PacketUtil.maxString - 1);
		}
		PacketUtil.writeString(output, text);
	}


	public void run(int playerId) {
		ChatManager.copy(text);
	}


	public PacketType getPacketType() {
		return PacketType.PacketClipboardText;
	}
	

	public int getVersion() {
		return 0;
	}


	public void failure(int playerId) {
		
	}

}
