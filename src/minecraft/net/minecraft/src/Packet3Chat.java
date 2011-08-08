package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet3Chat extends Packet {

	public String message;


	public Packet3Chat() {}

	public Packet3Chat(String var1) {
		if(var1.length() > 119) {
			var1 = var1.substring(0, 119);
		}

		this.message = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.message = readString(var1, 119);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		writeString(this.message, var1);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleChat(this);
	}

	public int getPacketSize() {
		return this.message.length();
	}
}
