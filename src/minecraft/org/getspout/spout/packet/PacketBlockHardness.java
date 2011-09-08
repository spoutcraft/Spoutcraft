package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketBlockHardness implements SpoutPacket{
	private List<Integer> xCoords;
	private List<Integer> yCoords;
	private List<Integer> zCoords;
	private List<Float> hardness;
	
	public PacketBlockHardness() {
		
	}
	
	public int getNumBytes() {
		return 4 + (xCoords.size() * 4 * 4);
	}

	public void readData(DataInputStream input) throws IOException {
		int size = input.readInt();
		xCoords = new ArrayList<Integer>(size);
		yCoords = new ArrayList<Integer>(size);
		zCoords = new ArrayList<Integer>(size);
		hardness = new ArrayList<Float>(size);
		for (int i = 0; i < size; i++) {
			xCoords.add(input.readInt());
		}
		for (int i = 0; i < size; i++) {
			yCoords.add(input.readInt());
		}
		for (int i = 0; i < size; i++) {
			zCoords.add(input.readInt());
		}
		for (int i = 0; i < size; i++) {
			hardness.add(input.readFloat());
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		int size = xCoords.size();
		output.writeInt(size);
		for (int i = 0; i < size; i++) {
			output.writeInt(xCoords.get(i));
		}
		for (int i = 0; i < size; i++) {
			output.writeInt(yCoords.get(i));
		}
		for (int i = 0; i < size; i++) {
			output.writeInt(zCoords.get(i));
		}
		for (int i = 0; i < size; i++) {
			output.writeFloat(hardness.get(i));
		}
	}

	public void run(int playerId) {

	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketBlockHardness;
	}

	public int getVersion() {
		return 0;
	}
}
