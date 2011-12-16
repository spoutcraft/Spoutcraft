package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.client.gui.ScreenUtil;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;

public class PacketOpenScreen implements SpoutPacket {
	ScreenType type = null;
	public PacketOpenScreen() {
		
	}

	public int getNumBytes() {
		return 4;
	}

	public void readData(DataInputStream input) throws IOException {
		type = ScreenType.getType(input.readInt());
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(type.getCode());
	}

	public void run(int playerId) {
		ScreenUtil.open(type);
	}

	public void failure(int playerId) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketOpenScreen;
	}

	public int getVersion() {
		return 0;
	}

}
