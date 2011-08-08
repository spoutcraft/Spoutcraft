package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet4UpdateTime extends Packet {

	public long time;


	public void readPacketData(DataInputStream var1) throws IOException {
		this.time = var1.readLong();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeLong(this.time);
	}

	public void processPacket(NetHandler var1) {
		//Spout Start
		if (Config.isTimeDayOnly() || Config.isTimeNightOnly()) {
			return;
		}
		//Spout End
		var1.handleUpdateTime(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
