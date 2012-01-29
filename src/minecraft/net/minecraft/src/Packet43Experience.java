package net.minecraft.src;

import java.io.*;

public class Packet43Experience extends Packet {
	public float experience;
	public int experienceTotal;
	public int experienceLevel;

	public Packet43Experience() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		experience = datainputstream.readFloat();
		experienceLevel = datainputstream.readShort();
		experienceTotal = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeFloat(experience);
		dataoutputstream.writeShort(experienceLevel);
		dataoutputstream.writeShort(experienceTotal);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleExperience(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
