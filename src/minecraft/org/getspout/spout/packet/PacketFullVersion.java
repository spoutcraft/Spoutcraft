package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketFullVersion implements SpoutPacket {

	private String versionString;
	
	public PacketFullVersion() {
	}
	
	public PacketFullVersion(String versionString) {
		this.versionString = versionString;
	}

	public void readData(DataInputStream input) throws IOException {
		versionString = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, versionString);
	}
	
	public int getNumBytes() {
		return PacketUtil.getNumBytes(versionString);
	}
	
	public void run(int playerId) {
	}
	
	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketFullVersion;
	}

	public int getVersion() {
		return 0;
	}
}
