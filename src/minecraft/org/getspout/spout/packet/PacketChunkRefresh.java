package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketChunkRefresh implements SpoutPacket {

	private int cx;
	private int cz;
	
	public PacketChunkRefresh() {
	}

	public PacketChunkRefresh(int cx, int cz) {
		this.cx = cx;
		this.cz = cz;
	}

	public int getNumBytes() {
		return 8;
	}

	public void readData(DataInputStream input) throws IOException {
		this.cx = input.readInt();
		this.cz = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(this.cx);
		output.writeInt(this.cz);
	}

	public void run(int id) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketChunkRefresh;
	}

	@Override
	public int getVersion() {
		return 0;
	}



}
