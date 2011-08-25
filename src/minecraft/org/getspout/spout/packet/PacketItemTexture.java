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
import org.getspout.spout.inventory.ItemManager;

public class PacketItemTexture implements SpoutPacket {
	protected String texture = "";
	private int id;
    private short data;
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(texture) + 6;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		id = input.readInt();
		data = input.readShort();
		texture = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeShort(data);
		PacketUtil.writeString(output, texture);
	}

	@Override
	public void run(int PlayerId) {
		if(texture.equals("[reset]")){
			texture = null;
		}
		ItemManager manager = SpoutClient.getInstance().getItemManager();
		manager.setItemTexture(id,data,texture);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketItemTexture;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
