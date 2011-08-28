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
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		versionString = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, versionString);
	}
	
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(versionString);
	}
	
	@Override
	public void run(int playerId) {
	}
	
	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketFullVersion;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
