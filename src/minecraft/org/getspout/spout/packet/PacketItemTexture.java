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
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

@SuppressWarnings("deprecation")
public class PacketItemTexture implements SpoutPacket {
	private int id;
	private short data;
	private String pluginName;
	private String name;
	public PacketItemTexture() {
		
	}
	
	public PacketItemTexture(int id, short data, String pluginName, String name) {
		this.id = id;
		this.data = data;
		this.name = name;
		this.pluginName = pluginName;
	}
	
	private String getPluginName() {
		if (pluginName == null) {
			return "";
		} else {
			return pluginName;
		}
	}
	
	private void setPluginName(String pluginName) {
		if (pluginName.equals("")) {
			this.pluginName = null;
		} else {
			this.pluginName = pluginName;
		}
	}

	public int getNumBytes() {
		return 6 + PacketUtil.getNumBytes(name) + PacketUtil.getNumBytes(getPluginName());
	}


	public void readData(DataInputStream input) throws IOException {
		id = input.readInt();
		data = input.readShort();
		name = PacketUtil.readString(input);
		setPluginName(PacketUtil.readString(input));
		
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeShort(data);
		PacketUtil.writeString(output, name);
		PacketUtil.writeString(output, getPluginName());
	}

	public void run(int PlayerId) {
		ItemManager manager = SpoutClient.getInstance().getItemManager();
		if (name.equals("[reset]"))
			name = null;
		manager.setItemTexture(id, data, pluginName, name);
	}

	public PacketType getPacketType() {
		return PacketType.PacketItemTexture;
	}

	public int getVersion() {
		return 1;
	}

	public void failure(int playerId) {
		
	}

}
