package net.minecraft.src;

import java.io.*;

public class Packet200Statistic extends Packet {
	public int statisticId;
	public int amount;

	public Packet200Statistic() {
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleStatistic(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		statisticId = datainputstream.readInt();
		amount = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(statisticId);
		dataoutputstream.writeByte(amount);
	}

	public int getPacketSize() {
		return 6;
	}
}
