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
import org.getspout.spout.player.ChatManager;

public class PacketClipboardText implements SpoutPacket{
	
	public PacketClipboardText() {
		
	}
	
	public PacketClipboardText(String text) {
		this.text = text;
	}
	protected String text;
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(text);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		text = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		if (text.length() > PacketUtil.maxString) {
			text = text.substring(0, PacketUtil.maxString - 1);
		}
		PacketUtil.writeString(output, text);
	}

	@Override
	public void run(int playerId) {
		ChatManager.copy(text);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketClipboardText;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
