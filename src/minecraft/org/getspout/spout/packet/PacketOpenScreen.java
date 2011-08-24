package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.gui.ScreenType;

public class PacketOpenScreen implements SpoutPacket {
	ScreenType type = null;
	
	@Override
	public int getNumBytes() {
		return 4;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		type = ScreenType.getType(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(type.getCode());
	}

	@Override
	public void run(int playerId) {
		type.open();
	}

	@Override
	public void failure(int playerId) {
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketOpenScreen;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
